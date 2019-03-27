package com.sysu.hemn.competitionplatform.service.impl;

import com.sysu.hemn.competitionplatform.entity.UserAuth;
import com.sysu.hemn.competitionplatform.mapper.UserAuthMapper;
import com.sysu.hemn.competitionplatform.service.UserAuthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userAuthService")
public class UserAuthServiceImpl implements UserAuthService {

    @Resource
    private UserAuthMapper userAuthMapper;

    @Override
    public UserAuth findByIdentifier(String identifier) {
        String identityType;
        if (identifier.matches(
                "^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$")) {
            identityType = "EMAIL";
        } else {
            identityType = "USERNAME";
        }
        UserAuth userAuth = userAuthMapper.select(identifier, identityType, null);
        return userAuth;
    }

    @Override
    public UserAuth findById(String id) {
        return userAuthMapper.select(null, null, id);
    }
}
