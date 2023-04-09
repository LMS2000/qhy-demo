package com.course.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 大忽悠
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserSearchDto {
    private List<UserSearchBigCourseTypeDto> bigCourseTypeDtoList;
}
