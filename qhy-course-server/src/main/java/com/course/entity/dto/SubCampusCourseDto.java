package com.course.entity.dto;import com.fasterxml.jackson.annotation.JsonFormat;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import lombok.NoArgsConstructor;import lombok.extern.slf4j.Slf4j;import java.io.Serializable;import java.time.LocalDateTime;@AllArgsConstructor@NoArgsConstructor@Data@Builder@Slf4jpublic class SubCampusCourseDto implements Serializable {    private static final long serialVersionUID = 1L;    /**     * id     */    private Integer id;    /**     * 分校区ID     */    private Integer subCampusId;    /**     * 课程ID     */    private Integer courseId;    /**     * 创建时间     */    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")    private LocalDateTime createTime;    /**     * 更新时间     */    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")    private LocalDateTime updateTime;}