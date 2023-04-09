package com.course.filter;

import com.security.domain.BaseUserDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

import static com.course.constants.ServiceConstants.MANAGER_SERVICE_NAME;
import static com.course.constants.ServiceConstants.SUPER_MANAGER;

/**
 * 访问者信息
 *
 * @author 大忽悠
 * @create 2023/2/15 12:25
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VisitDto extends BaseUserDetails {
    /**
     * 服务名
     */
    private String serviceName;
    /**
     * 访问者名字
     */
    private String visitName;
    /**
     * 访问者id
     */
    private Integer id;
    /**
     * 如果是普通管理员此处为主校区ID,普通用户为分校区ID,超级管理员为null
     */
    private Integer cId;
    /**
     * 额外信息: 如果是管理员访问,此处的额外信息记录管理员是超级管理员还是普通管理员 </br>
     * 超级管理员值为1,普通管理员值为0
     */
    private String extra;

    public void copyRpcInfo(VisitDto visitId) {
        id = visitId.id;
        extra = visitId.extra;
        cId=visitId.cId;
    }

    public static VisitDto getCurrentVisit() {
        return ((VisitDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    public boolean isManagerVisit() {
        return Objects.equals(serviceName, MANAGER_SERVICE_NAME);
    }

    public boolean isSuperManager() {
        return isManagerVisit() && extra.equals(SUPER_MANAGER);
    }

    public static <T> T handle(Handle<T> superMangerHandle
            , Handle<T> commonMangerHandle,
                        Handle<T> commonUserHandle) {
        VisitDto visitDto = VisitDto.getCurrentVisit();
        //管理员访问
        if (visitDto.isManagerVisit()) {
            //超级管理员访问
            if (visitDto.isSuperManager()) {
                if(superMangerHandle==null){
                    return null;
                }
                return superMangerHandle.doHandle(visitDto);
            } else {
                //普通管理员访问
                if(commonMangerHandle==null){
                    return null;
                }
                return commonMangerHandle.doHandle(visitDto);
            }
        } else {
            //普通用户访问
            if(commonUserHandle==null){
                return null;
            }
            return commonUserHandle.doHandle(visitDto);
        }
    }


    public interface Handle<T> {
        T doHandle(VisitDto visitDto);
    }
}
