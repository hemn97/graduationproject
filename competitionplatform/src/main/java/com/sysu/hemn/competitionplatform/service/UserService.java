package com.sysu.hemn.competitionplatform.service;

import com.sysu.hemn.competitionplatform.entity.User;

import java.util.List;

public interface UserService {
    User findById(Long id);
    void updateUserData(User user);
    Integer register(User user);
    Integer activate(User user);
    Integer resendActivateCode(User user);
    List<User> listUser(Integer pageNumber, Integer pageSize, String searchKey, Integer roleType, String orderByClause);
    Long maxPage(Integer pageSize, String searchKey, Integer roleType);
    void updateUser(User user);
}
