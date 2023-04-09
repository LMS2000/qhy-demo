package com.auth.entity.vo;import io.swagger.annotations.ApiModelProperty;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import lombok.NoArgsConstructor;import lombok.extern.slf4j.Slf4j;import javax.validation.constraints.NotBlank;import javax.validation.constraints.NotNull;import java.io.Serializable;@AllArgsConstructor@NoArgsConstructor@Data@Builder@Slf4jpublic class UserVo implements Serializable {    private static final long serialVersionUID = 1L;    /**     * 启用     */    @ApiModelProperty("启用")    private Integer enabled=0;    /**     * 用户名     */    @ApiModelProperty("用户名")    @NotBlank(message = "用户名不能为空")    @NotNull(message = "用户名不能为空")    private String username;    /**     * 业务名(认证中心存储多种用户类型时,防止出现用户名重复情况,可以通过用户所属业务名加以区分)     */    @ApiModelProperty("业务名(认证中心存储多种用户类型时,防止出现用户名重复情况,可以通过用户所属业务名加以区分)")    private String serviceName;    public Integer getEnabled() {        return enabled==null?0:enabled;    }    @Override    public String toString() {        return "用户名: "+username+" ,业务名: "+(serviceName==null?"没有设置业务名":serviceName)+" ,启用角色: "+((enabled==1)?"是":"否");    }}