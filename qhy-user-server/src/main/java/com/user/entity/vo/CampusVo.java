package com.user.entity.vo;

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
public class CampusVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 校区名
     */
    @ApiModelProperty("校区名")
    private String name;
    /**
     * 校区描述信息
     */
    @ApiModelProperty("校区描述信息")
    private String desc;
}
