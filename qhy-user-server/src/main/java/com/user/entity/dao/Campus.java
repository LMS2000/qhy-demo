package com.user.entity.dao;import com.baomidou.mybatisplus.annotation.IdType;import com.baomidou.mybatisplus.annotation.TableField;import com.baomidou.mybatisplus.annotation.TableId;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import lombok.NoArgsConstructor;import java.io.Serializable;import java.time.LocalDateTime;@AllArgsConstructor@NoArgsConstructor@Data@Builderpublic class Campus implements Serializable {    private static final long serialVersionUID = 1L;    /**    * id    */    @TableId(value = "id", type = IdType.AUTO)    private Integer id;    /**    * 校区名    */    private String name;    /**    * 校区描述信息    */    @TableField("`desc`")    private String desc;    /**    * 创建时间    */    private LocalDateTime createTime;    /**    * 更新时间    */    private LocalDateTime updateTime;}