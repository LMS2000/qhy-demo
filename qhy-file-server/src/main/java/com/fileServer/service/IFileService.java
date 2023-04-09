package com.fileServer.service;

import cn.hutool.core.net.multipart.UploadFile;
import com.fileServer.domain.UpLoadCourseZipFileDto;
import com.fileServer.domain.UpLoadFileDto;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    /**
     * 上传文件
     * @param file
     * @param bucketName
     * @param dirName
     * @return
     */
   UpLoadFileDto uploadFile(MultipartFile file,String bucketName,String dirName);


    /**
     * 删除文件
     * @param bucketName
     * @param fileName
     * @return
     */
   Boolean deleteFile(String bucketName, String fileName);

    /**
     * 上传压缩文件
     * @param file
     * @param bucketName
     * @param dirName
     * @return
     */
   UpLoadCourseZipFileDto uploadZipFile(MultipartFile file,String bucketName ,String dirName);
}
