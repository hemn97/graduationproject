package com.sysu.hemn.competitionplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.sysu.hemn.competitionplatform.annotation.AdminToken;
import com.sysu.hemn.competitionplatform.annotation.CheckToken;
import com.sysu.hemn.competitionplatform.annotation.LoginToken;
import com.sysu.hemn.competitionplatform.entity.User;
import com.sysu.hemn.competitionplatform.entity.UserAuth;
import com.sysu.hemn.competitionplatform.service.UserAuthService;
import com.sysu.hemn.competitionplatform.service.UserService;
import com.sysu.hemn.competitionplatform.utils.EncryptUtil;
import com.sysu.hemn.competitionplatform.utils.IpUtil;
import com.sysu.hemn.competitionplatform.utils.JwtUtil;
import com.sysu.hemn.competitionplatform.utils.MailUtil;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.lang.RandomStringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/competitionplatform/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Resource
    private UserAuthService userAuthService;
    @Resource
    private UserService userService;

    @LoginToken
    @RequestMapping(value="/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String login(HttpServletRequest request) {
        String identifier = request.getParameter("identifier");
        String credential = EncryptUtil.md5Password(request.getParameter("credential"));
        UserAuth userAuth = userAuthService.findByIdentifier(identifier);
        JSONObject result = handleUserToken(userAuth, credential);
        return result.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/get", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String get(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("userId");
        User user = userService.findById(id);
        JSONObject result = new JSONObject();
        if (user == null) {
            result.put("code", -1);
            result.put("message", "用户不存在");
        } else {
            result.put("code", 1);
            result.put("data", user);
        }
        return result.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String update(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("userId");
        String school = request.getParameter("school");
        String telNumber = request.getParameter("telNumber");
        String gender = request.getParameter("gender");
        String skills = request.getParameter("skills");
        User user = userService.findById(id);
        user.setSchool(school);
        user.setTelNumber(telNumber);
        user.setGender(gender);
        user.setSkills(skills);
        userService.updateUserData(user);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        return result.toJSONString();
    }

    @LoginToken
    @RequestMapping(value="/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String register(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String nickname = request.getParameter("nickname");
        String username = request.getParameter("username");
        String realname = request.getParameter("realname");
        String credential = EncryptUtil.md5Password(request.getParameter("credential"));
        String email = request.getParameter("email");
        String school = request.getParameter("school");
        String telNumber = request.getParameter("telNumber");
        String gender = request.getParameter("gender");
        String skills = request.getParameter("skills");
        User user = new User();
        user.setNickname(nickname);
        user.setUsername(username);
        user.setCredential(credential);
        user.setRealname(realname);
        user.setEmail(email);
        user.setSchool(school);
        user.setTelNumber(telNumber);
        user.setGender(gender);
        user.setSkills(skills);
        user.setRegisterIp(IpUtil.ipAddress(request));
        user.setRoleType(0);
        user.setActivateCode(RandomStringUtils.randomAlphanumeric(6));
        Integer registerRes = userService.register(user);
        if (registerRes.equals(1)) {
            // 发送激活邮件
            try {
                MailUtil.sendEmail(user.getEmail(), "[竞赛平台]新用户注册激活码",
                        "您的验证码为:<p style='text-align: center; font-size: 20px; font-weight: bold'>" +
                                user.getActivateCode()+"</p>" +
                                "如非本人操作，请忽略此邮件。<p>提示：请勿回复该邮件!</p>");
            } catch (Exception e) {
                result.put("code", -1);
                return result.toJSONString();
            }
        }
        result.put("code", 1);
        result.put("status", registerRes);
        return result.toJSONString();
    }

    @LoginToken
    @RequestMapping(value="/activate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String activate(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String identifier = request.getParameter("identifier");
        String activateCode = request.getParameter("activateCode");
        User user = new User();
        if (identifier.matches("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$")) {
            user.setEmail(identifier);
        } else {
            user.setUsername(identifier);
        }
        user.setActivateCode(activateCode);
        Integer activateRes = userService.activate(user);
        if (activateRes.equals(1)) {
            // 激活成功，自动登录
            UserAuth userAuth = userAuthService.findByIdentifier(identifier);
            String token = JwtUtil.createJWT(6000000, userAuth);
            result.put("token", token);
        }
        result.put("code", 1);
        result.put("status", activateRes);
        return result.toJSONString();
    }

    @LoginToken
    @RequestMapping(value="/activate/resend", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String recendCode(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String identifier = request.getParameter("identifier");
        String activateCode = RandomStringUtils.randomAlphanumeric(6);
        User user = new User();
        if (identifier.matches("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$")) {
            user.setEmail(identifier);
        } else {
            user.setUsername(identifier);
        }
        user.setActivateCode(activateCode);
        Integer resendRes = userService.resendActivateCode(user);
        result.put("code", 1);
        result.put("status", resendRes);
        return result.toJSONString();
    }

    @AdminToken
    @RequestMapping(value="/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String list(HttpServletRequest request) {
        Integer pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String searchKey = request.getParameter("searchKey");
        Integer roleType = convertRoleType(request.getParameter("roleType"));
        String orderByClause = request.getParameter("orderByClause");
        List<User> list = userService.listUser(pageNumber, pageSize, searchKey, roleType, orderByClause);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("list", list);
        return result.toJSONString();
    }

    @AdminToken
    @RequestMapping(value="/maxpage", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String maxpage(HttpServletRequest request) {
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String searchKey = request.getParameter("searchKey");
        Integer roleType = convertRoleType(request.getParameter("roleType"));
        Long maxPage = userService.maxPage(pageSize, searchKey, roleType);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("maxPage", maxPage);
        return result.toJSONString();
    }

    @AdminToken
    @RequestMapping(value="/role/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String roleUpdate(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        Integer roleType = Integer.parseInt(request.getParameter("roleType"));
        User user = userService.findById(id);
        user.setRoleType(roleType);
        userService.updateUser(user);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        return result.toJSONString();
    }

    private JSONObject handleUserToken(UserAuth userAuth, String credential) {
        JSONObject result = new JSONObject();
        User user = userService.findById(userAuth.getUserId());
        if (userAuth == null) {
            result.put("code", -1);
            result.put("message", "用户不存在");
        } else if (user.getRoleType().equals(0)){
            result.put("code", -1);
            result.put("message", "用户未激活");
        } else if (!userAuth.getCredential().equals(credential)) {
            result.put("code", -1);
            result.put("message", "登录失败，密码错误");
        } else {
            String token = JwtUtil.createJWT(6000000, userAuth);
            result.put("code", 1);
            result.put("token", token);
            result.put("roleType", user.getRoleType());
        }
        return result;
    }

    private static Integer convertRoleType(String roleType) {
        if (roleType == null) return null;
        if (roleType.equals("未激活")) {
            return 0;
        } else if (roleType.equals("普通用户")) {
            return 1;
        } else if (roleType.equals("管理员")) {
            return 2;
        } else if (roleType.equals("封禁中")) {
            return 3;
        } else {
            return null;
        }
    }
}
