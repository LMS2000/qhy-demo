package com.course.entity.dto;

import com.course.constants.ServiceConstants;
import com.course.entity.dao.Course;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 大忽悠
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserSearchCourseDto {
    /**
     * id
     */
    private Integer id;
    /**
     * 课程名字
     */
    private String courseName;
    /**
     * 课程封面路径
     */
    private List<String> courseCoverPath;
    /**
     * 课程文件路径
     */
    private String courseFilePath;
    /**
     * 课程是否启用
     */
    private Integer enable;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    public UserSearchCourseDto(Course course) {
        this.id=course.getId();
        this.courseName=course.getCourseName();
        this.courseCoverPath= List.of(course.getCourseCoverPath().split(ServiceConstants.IMG_FILE_PATH_SEPARATOR));
        this.courseFilePath=course.getCourseFilePath();
        this.enable=course.getEnable();
        this.createTime=course.getCreateTime();
        this.updateTime=course.getUpdateTime();
    }
}
