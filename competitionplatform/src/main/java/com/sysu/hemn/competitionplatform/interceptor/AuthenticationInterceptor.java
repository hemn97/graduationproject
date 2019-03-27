package com.sysu.hemn.competitionplatform.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.sysu.hemn.competitionplatform.annotation.AdminToken;
import com.sysu.hemn.competitionplatform.annotation.CheckToken;
import com.sysu.hemn.competitionplatform.annotation.LoginToken;
import com.sysu.hemn.competitionplatform.entity.User;
import com.sysu.hemn.competitionplatform.entity.UserAuth;
import com.sysu.hemn.competitionplatform.service.UserAuthService;
import com.sysu.hemn.competitionplatform.service.UserService;
import com.sysu.hemn.competitionplatform.utils.JwtUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Resource
    private UserAuthService userAuthService;
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有LoginToken注释，有则跳过认证
        if (method.isAnnotationPresent(LoginToken.class)) {
            LoginToken loginToken = method.getAnnotation(LoginToken.class);
            if (loginToken.required()) {
                if (token == null) {
                    httpServletRequest.setAttribute("userId", -1l);
                } else {
                    // 获取 token 中的 id
                    userAuthentication(httpServletRequest, token);
                }
                return true;
            }
        }

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(CheckToken.class)) {
            CheckToken checkToken = method.getAnnotation(CheckToken.class);
            if (checkToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 id
                userAuthentication(httpServletRequest, token);
                return true;
            }
        }

        //检查有没有需要管理员权限的注解
        if (method.isAnnotationPresent(AdminToken.class)) {
            AdminToken adminToken = method.getAnnotation(AdminToken.class);
            if (adminToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 id
                String id = userAuthentication(httpServletRequest, token);
                UserAuth auth = userAuthService.findById(id);
                User admin = userService.findById(auth.getUserId());
                if (admin == null || !admin.getRoleType().equals(2)) {
                    // 非管理员token
                    throw new RuntimeException("非管理员token，请重新登录");
                }
                return true;
            }
        }

        return true;
    }

    private String userAuthentication(HttpServletRequest httpServletRequest, String token) {
        String id;
        try {
            id = JWT.decode(token).getClaim("id").asString();
        } catch (JWTDecodeException j) {
            throw new RuntimeException("访问异常！");
        }
        UserAuth auth = userAuthService.findById(id);
        if (auth == null) {
            throw new RuntimeException("用户不存在，请重新登录");
        }
        httpServletRequest.setAttribute("userId", auth.getUserId());
        Boolean verify = JwtUtil.isVerify(token, auth);
        if (!verify) {
            throw new RuntimeException("非法访问！");
        }
        return id;
    }
}
