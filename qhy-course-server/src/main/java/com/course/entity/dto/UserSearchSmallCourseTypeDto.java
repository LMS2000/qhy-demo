package com.course.entity.dto;

import com.course.entity.dao.Course;
import com.course.entity.dao.SmallCourseType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 大忽悠
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserSearchSmallCourseTypeDto {
    /**
     * id
     */
    private Integer id;
    /**
     * 小分类名
     */
    private String name;
    /**
     * 小分类课程封面路径
     */
    private String coverPath;
    /**
     * 课程小类下的课程集合
     */
    private List<UserSearchCourseDto> courseDtoList;
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

    public UserSearchSmallCourseTypeDto(SmallCourseType smallCourseType) {
          this.id=smallCourseType.getId();
          this.name=smallCourseType.getName();
          this.coverPath=smallCourseType.getCoverPath();
          this.createTime=smallCourseType.getCreateTime();
          this.updateTime=smallCourseType.getUpdateTime();
    }

    public void setCourseDtoList(List<Course> courseList) {
         this.courseDtoList=courseList.stream().map(UserSearchCourseDto::new).collect(Collectors.toList());
    }
}
