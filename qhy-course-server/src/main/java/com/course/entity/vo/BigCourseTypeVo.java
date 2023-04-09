package com.course.entity.vo;


import com.validator.NotEmptyCheck;
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
public class BigCourseTypeVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 大类名
     */
    @ApiModelProperty("大类名")
    @NotEmptyCheck(message = "课程大类名不能为空")
    private String name;
}
