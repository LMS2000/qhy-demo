package com.auth.entity.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoleAuthority implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    * id
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
    * 角色id
    */
    private Integer rid;
    /**
    * 权限id
    */
    private Integer aid;
}
