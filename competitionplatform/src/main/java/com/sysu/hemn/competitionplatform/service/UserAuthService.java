package com.sysu.hemn.competitionplatform.service;

import com.sysu.hemn.competitionplatform.entity.UserAuth;

public interface UserAuthService {
    UserAuth findByIdentifier(String identifier);
    UserAuth findById(String id);
}
