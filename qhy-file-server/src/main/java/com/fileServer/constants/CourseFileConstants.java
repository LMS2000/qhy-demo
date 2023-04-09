package com.fileServer.constants;

/**
 * @author 大忽悠
 * @create 2023/2/27 21:22
 */
public interface CourseFileConstants {
    /**
     * 课程图片目录名
     */
    String IMG_FILES="imgs";
    /**
     * 课程图片分割符
     */
    String IMG_FILE_PATH_SEPARATOR =" ";
    /**
     * 课程文件名
     */
    String COURSE_FILES_PREFIX="01_";
    /**
     * 课程教案名
     */
    String COURSE_TEACH_PREFIX="02_";
    /**
     * 静态资源的请求前缀
     */
    String STATIC_REQUEST_PREFIX_PATTERN="/static/**";
    String STATIC_REQUEST_PREFIX="static";
}
