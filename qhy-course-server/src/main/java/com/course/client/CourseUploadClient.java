package com.course.client;

import com.easyCode.feature.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 大忽悠
 * @create 2023/2/13 18:27
 */
@FeignClient("qhy-file")
public interface CourseUploadClient {
    @PostMapping(value = "/file/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result uploadFile(@RequestPart(value = "file") MultipartFile file, @RequestParam("bucketName")String bucketName, @RequestParam("dirName")String dirName);

    @PostMapping(value = "/file/delete")
    Result deleteFile(@RequestParam("bucketName")String bucketName,@RequestParam("fileName")String fileName);

    @PostMapping(value = "/file/uploadZip",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result uploadZipFile(@RequestPart(value = "file") MultipartFile file, @RequestParam("bucketName")String bucketName, @RequestParam("dirName")String dirName);
}
