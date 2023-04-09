package com.course.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Slf4j
public class CampusCourseVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
        * 校区ID
        */
        @ApiModelProperty("校区ID")
    private Integer campusId;
    /**
        * 课程ID
        */
        @ApiModelProperty("课程ID")
    private Integer courseId;
}
