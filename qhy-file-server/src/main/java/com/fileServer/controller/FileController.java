package com.fileServer.controller;

import com.fileServer.result.Result;
import com.fileServer.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Api(tags="文件服务请求")
@Validated
public class FileController {

    private final IFileService fileService;


    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Result uploadFile( @RequestPart("file") MultipartFile file,
                              @RequestParam("bucketName") String bucketName,@RequestParam("dirName")String dirName){
        bucketName=bucketName.replace(",","");
        dirName=dirName.replace(",","");
        return Result.success(fileService.uploadFile(file,bucketName,dirName));
    }

    @PostMapping("/uploadZip")
    @ApiOperation("上传压缩课程文件")
    public Result uploadZipCourseFile(@RequestPart("file") MultipartFile multipartFile
            ,@RequestParam("bucketName")String bucketName,@RequestParam("dirName")String dirName){
        return Result.success(fileService.uploadZipFile(multipartFile,bucketName,dirName));
    }

    @PostMapping("/delete")
    @ApiOperation("删除文件")
    public Result deleteFile(@RequestParam("bucketName")String bucketName,
                             @RequestParam("fileName")String fileName){
        return Result.success(fileService.deleteFile(bucketName,fileName));
    }

}
