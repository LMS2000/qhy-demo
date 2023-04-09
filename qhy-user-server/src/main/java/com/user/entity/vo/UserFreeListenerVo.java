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
public class UserFreeListenerVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
        * 电话号码
        */
        @ApiModelProperty("电话号码")
    private String phoneNumber;
    /**
        * 用户信息
        */
        @ApiModelProperty("用户信息")
    private String info;
}
