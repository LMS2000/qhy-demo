package com.auth.service;

import com.auth.entity.dao.Authority;
import com.auth.entity.dto.AuthorityDto;
import com.auth.entity.vo.AuthorityVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easyCode.feature.mybaits.CustomPage;

import java.util.List;

public interface IAuthorityService extends IService<Authority> {
    Integer saveAuthority(AuthorityVo authorityVo);

    AuthorityDto getAuthorityById(Integer id);

    List<AuthorityDto> listAuthority(CustomPage customPage);

    void delAuthorityById(Integer id);
}
