package com.auth.entity.vo;

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
public class UserRoleVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Integer uid;
    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    private Integer rid;
}
