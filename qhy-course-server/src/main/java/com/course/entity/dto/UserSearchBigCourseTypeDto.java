package com.course.entity.dto;

import com.course.entity.dao.BigCourseType;
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
public class UserSearchBigCourseTypeDto {
   /**
    * id
    */
   private Integer id;
   /**
    * 大类名
    */
   private String name;
   /**
    * 课程大类下的课程小类
    */
   private List<UserSearchSmallCourseTypeDto> smallCourseTypeDtos;
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

   public UserSearchBigCourseTypeDto(BigCourseType bigCourseType) {
        this.id=bigCourseType.getId();
        this.name=bigCourseType.getName();
        this.createTime=bigCourseType.getCreateTime();
        this.updateTime=bigCourseType.getUpdateTime();
   }
}
