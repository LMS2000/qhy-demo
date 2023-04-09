package com.fileServer.service.impl;

import cn.hutool.core.util.ZipUtil;
import com.alibaba.nacos.api.config.filter.IConfigFilter;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.FakeIOException;
import com.fileServer.client.OssClient;
import com.fileServer.config.OssProperties;
import com.fileServer.constants.CourseFileConstants;
import com.fileServer.domain.UpLoadCourseZipFileDto;
import com.fileServer.domain.UpLoadFileDto;
import com.fileServer.exception.FileException;
import com.fileServer.service.IFileService;
import com.fileServer.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Optional;

import static com.fileServer.constants.CourseFileConstants.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileServiceImpl implements IFileService {

    private final OssClient ossClient;
    private final OssProperties ossProperties;

    @Override
    public UpLoadFileDto uploadFile(MultipartFile file, String bucketName, String dirName) {

        String fileName;
        try{
            String randomPath=
                    FileUtil.generatorFileName(file.getOriginalFilename()==null?file.getName():file.getOriginalFilename());
           fileName=StringUtils.isEmpty(dirName)?randomPath:dirName+"/"+randomPath;
           log.info("正在执行上传文件，文件为{}",fileName);
           ossClient.putObject(bucketName,fileName,file.getInputStream());
        }catch (Exception e){
             log.error("文件上传失败，{}",e);
            throw new FileException("文件上传失败");
        }
        return new UpLoadFileDto(bucketName,fileName,
                FileUtil.getFileUrl(ossProperties.getEndpoint(),STATIC_REQUEST_PREFIX,bucketName,fileName),
                null);
    }

    @Override
    public Boolean deleteFile(String bucketName, String fileName) {
        ossClient.deleteObject(bucketName,fileName);
        return true;
    }


    @Override
    public UpLoadCourseZipFileDto uploadZipFile( MultipartFile multipartFile,String bucketName, String dirName) {
        String dirPath="";
        String imgPathUrl = "";
        String courseFileUrl = "";
        try {
            //1.创建目录--dirName就是压缩文件解压目录名
            dirPath = FileUtil.pathMerge(ossProperties.getRootPath(), bucketName, dirName);
            FileUtil.createDir(dirPath);
            //2.压缩文件解压到目录下
            ZipUtil.unzip(multipartFile.getInputStream(), new File(dirPath), Charset.forName("gbk"));
            //3.定位课程封面目录--imgs
            imgPathUrl = getImgsPath(dirPath, bucketName,dirName);
            //4.定位课程文件--01_开头
            courseFileUrl = getCourseFilePath(dirPath, bucketName,dirName);
        } catch (Exception e) {
            log.error("课程文件上传出现异常: ", e);
            FileUtil.deleteFile(dirPath);
            throw new FileException("课程文件上传失败");
        }
        return new UpLoadCourseZipFileDto(dirName,bucketName,courseFileUrl,imgPathUrl);
    }

    private String getCourseFilePath(String dirPath, String bucketName, String path) {
        File dir = new File(dirPath);
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile() && file.getName().startsWith(CourseFileConstants.COURSE_FILES_PREFIX)) {
                return FileUtil.getFileUrl(ossProperties.getEndpoint(),STATIC_REQUEST_PREFIX, bucketName,path, file.getName());
            }
        }
        throw new FileException("课程文件格式有误,缺少课程文件信息");
    }

    private String getImgsPath(String dirPath, String bucketName, String path){
        File dir = new File(dirPath);
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory() && file.getName().equals(CourseFileConstants.IMG_FILES)) {
                return buildImgsUrl(file, bucketName,path);
            }
        }
        throw new FileException("课程文件格式有误,缺少课程封面imgs目录");
    }


    private String buildImgsUrl(File file, String bucketName, String path) {
        StringBuilder imgUrl = new StringBuilder();
        for (File imgFile : Objects.requireNonNull(file.listFiles())) {
            String imgFileUrl = FileUtil.getFileUrl(ossProperties.getEndpoint(),STATIC_REQUEST_PREFIX, bucketName,path,IMG_FILES, imgFile.getName());
            imgUrl.append(imgFileUrl).append(CourseFileConstants.IMG_FILE_PATH_SEPARATOR);
        }
        return imgUrl.toString();
    }
}
