package com.course.entity.dao;import com.baomidou.mybatisplus.annotation.IdType;import com.baomidou.mybatisplus.annotation.TableId;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import lombok.NoArgsConstructor;import java.io.Serializable;import java.time.LocalDateTime;@AllArgsConstructor@NoArgsConstructor@Data@Builderpublic class CourserUploadLog implements Serializable {    private static final long serialVersionUID = 1L;    /**    * id    */    @TableId(value = "id", type = IdType.AUTO)    private Integer id;    /**    * 桶名    */    private String bucketName;    /**    * 文件名    */    private String fileName;    /**    * 文件md5值    */    private String fileMd5;    /**    * 创建时间    */    private LocalDateTime createTime;    /**    * 更新时间    */    private LocalDateTime updateTime;}