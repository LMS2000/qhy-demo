package com.course.entity.vo;import com.validator.MultipartFileNotEmptyCheck;import com.validator.NotEmptyCheck;import io.swagger.annotations.ApiModelProperty;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import lombok.NoArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.web.multipart.MultipartFile;import javax.validation.constraints.Positive;import java.io.Serializable;@AllArgsConstructor@NoArgsConstructor@Data@Builder@Slf4jpublic class SmallCourseTypeVo implements Serializable {    private static final long serialVersionUID = 1L;    /**     * 小分类名     */    @ApiModelProperty("小分类名")    @NotEmptyCheck(message = "课程小类名不能为空")    private String name;    /**     * 所属大分类id     */    @ApiModelProperty("所属大分类id")    @Positive(message = "参数不合法")    private String parentId;    /**     * 小分类课程封面路径     */    @ApiModelProperty("小分类课程封面文件")    @MultipartFileNotEmptyCheck(message = "课程封面文件缺失")    private MultipartFile coverPath;}