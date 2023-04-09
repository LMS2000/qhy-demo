package com.user.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 大忽悠
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SdkUserInfoDto {
    private Integer id;
    private Integer cId;
}
