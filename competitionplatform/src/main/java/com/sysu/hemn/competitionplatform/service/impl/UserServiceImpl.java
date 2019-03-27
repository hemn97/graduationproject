package com.sysu.hemn.competitionplatform.service.impl;

import com.sysu.hemn.competitionplatform.entity.User;
import com.sysu.hemn.competitionplatform.entity.UserAuth;
import com.sysu.hemn.competitionplatform.mapper.UserAuthMapper;
import com.sysu.hemn.competitionplatform.mapper.UserMapper;
import com.sysu.hemn.competitionplatform.service.UserService;
import com.sysu.hemn.competitionplatform.utils.MailUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserAuthMapper userAuthMapper;

    @Override
    public User findById(Long id) {
        return userMapper.select(id, null, null, null);
    }

    @Override
    public void updateUserData(User user) {
        userMapper.update(user);
    }

    @Override
    public Integer register(User user) {
        User hasNickname = userMapper.select(null, user.getNickname(), null, null);
        User hasUsername = userMapper.select(null, null, user.getUsername(), null);
        User hasEmail = userMapper.select(null, null, null, user.getEmail());
        if (hasNickname != null) {
            return -1;  // 昵称被注册
        } else if (hasUsername != null) {
            return -2;  // 用户名被注册
        } else if (hasEmail != null) {
            return -3;  // 邮箱被注册
        }
        userMapper.insert(user);
        UserAuth usernameAuth = new UserAuth();
        usernameAuth.setUserId(user.getId());
        usernameAuth.setIdentifier(user.getUsername());
        usernameAuth.setCredential(user.getCredential());
        usernameAuth.setIdentityType("USERNAME");
        userAuthMapper.insert(usernameAuth);
        UserAuth emailAuth = new UserAuth();
        emailAuth.setUserId(user.getId());
        emailAuth.setIdentifier(user.getEmail());
        emailAuth.setCredential(user.getCredential());
        emailAuth.setIdentityType("EMAIL");
        userAuthMapper.insert(emailAuth);
        return 1;   // 注册成功
    }

    @Override
    public Integer activate(User user) {
        User record = userMapper.select(null, null, user.getUsername(), user.getEmail());
        if (record == null) {
            return -1;  // 用户不存在
        } else if (!record.getRoleType().equals(0)) {
            return -2;  // 已经激活
        } else if (!record.getActivateCode().equals(user.getActivateCode())) {
            return -3;  // 激活码错误
        } else {
            record.setRoleType(1);
            userMapper.update(record);
            return 1;
        }
    }

    @Override
    public Integer resendActivateCode(User user) {
        User record = userMapper.select(null, null, user.getUsername(), user.getEmail());
        if (record == null) {
            return -1;  // 用户不存在
        } else if (!record.getRoleType().equals(0)) {
            return -2;  // 用户已经激活
        } else if (record.getRoleType().equals(0)){
            record.setActivateCode(user.getActivateCode());
            userMapper.update(record);
            // 发送激活邮件
            try {
                MailUtil.sendEmail(record.getEmail(), "[竞赛平台]新用户注册激活码",
                        "您的验证码为:<p style='text-align: center; font-size: 20px; font-weight: bold'>" +
                                record.getActivateCode()+"</p>如非本人操作，请忽略此邮件。<p>提示：请勿回复该邮件!</p>");
            } catch (Exception e) {
                return -3;  // 发送邮件失败
            }
        }
        return 1;
    }

    @Override
    public List<User> listUser(Integer pageNumber, Integer pageSize, String searchKey, Integer roleType,
                               String orderByClause) {
        return userMapper.list((pageNumber-1)*pageSize, pageSize, searchKey, roleType, orderByClause);
    }

    @Override
    public Long maxPage(Integer pageSize, String searchKey, Integer roleType) {
        Long count =  userMapper.count(searchKey, roleType);
        return (count + pageSize - 1) / pageSize;
    }

    @Override
    public void updateUser(User user) {
        userMapper.update(user);
    }
}
