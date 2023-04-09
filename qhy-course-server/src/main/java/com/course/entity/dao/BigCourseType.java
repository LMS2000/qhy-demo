package com.course.entity.dao;import com.baomidou.mybatisplus.annotation.FieldFill;import com.baomidou.mybatisplus.annotation.IdType;import com.baomidou.mybatisplus.annotation.TableField;import com.baomidou.mybatisplus.annotation.TableId;import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;import com.course.exception.CourseException;import com.course.service.Impl.BigCourseTypeServiceImpl;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import lombok.NoArgsConstructor;import java.io.Serializable;import java.time.LocalDateTime;@AllArgsConstructor@NoArgsConstructor@Data@Builderpublic class BigCourseType implements Serializable {    private static final long serialVersionUID = 1L;    /**    * id    */    @TableId(value = "id", type = IdType.AUTO)    private Integer id;    /**    * 大类名    */    private String name;    /**     * 创建时间     */    @TableField(fill = FieldFill.INSERT)    private LocalDateTime createTime;    /**     * 更新时间     */    @TableField(fill = FieldFill.INSERT_UPDATE)    private LocalDateTime updateTime;    public static void repeatNameCheck(BigCourseType bigcoursetype, BigCourseTypeServiceImpl bigCourseTypeService) {        long count = bigCourseTypeService.count(new QueryWrapper<BigCourseType>()                .eq("name", bigcoursetype.getName()));        if(count>0){            throw new CourseException("课程大类名重复");        }    }}