package com.auth.service.impl;

import com.auth.entity.dao.Authority;
import com.auth.entity.dto.AuthorityDto;
import com.auth.entity.vo.AuthorityVo;
import com.auth.mapper.AuthorityMapper;
import com.auth.service.IAuthorityService;
import com.auth.service.IRoleAuthorityService;
import com.auth.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyCode.feature.mybaits.CustomPage;
import org.bouncycastle.cms.PasswordRecipientId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.auth.entity.factory.AuthorityFactory.AUTHORITY_CONVERTER;

@Service
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, Authority> implements IAuthorityService {

    @Resource
    private IRoleAuthorityService roleAuthorityService;


    @Override
    public Integer saveAuthority(AuthorityVo authorityVo) {
        Authority authority = AUTHORITY_CONVERTER.toAuthority(authorityVo);
        save(authority);
        return authority.getId();
    }

    @Override
    public AuthorityDto getAuthorityById(Integer id) {
        Authority byId = getById(id);
       return AUTHORITY_CONVERTER.toAuthorityDto(byId);
    }

    @Override
    public List<AuthorityDto> listAuthority(CustomPage customPage) {
        List<Authority> result = CustomPage.getResult(customPage, new Authority(), this, null);
        return AUTHORITY_CONVERTER.toListAuthorityDto(result);
    }

    @Override
    @Transactional
    public void delAuthorityById(Integer id) {
      removeById(id);
      roleAuthorityService.removeByAuthorityId(id);
    }
}
