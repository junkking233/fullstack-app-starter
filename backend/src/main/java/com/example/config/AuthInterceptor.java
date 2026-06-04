package com.example.config;

import com.example.exception.BusinessException;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.util.TokenSubject;
import com.example.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenUtils tokenUtils;

    private final UserMapper userMapper;

    public AuthInterceptor(TokenUtils tokenUtils, UserMapper userMapper) {
        this.tokenUtils = tokenUtils;
        this.userMapper = userMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BusinessException(401, "请先登录");
        }

        TokenSubject tokenSubject = tokenUtils.parse(authorization.substring(7));
        User user = userMapper.selectById(tokenSubject.getUserId());
        if (user == null || user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(401, "登录已失效，请重新登录");
        }

        TokenSubject subject = new TokenSubject(user.getId(), user.getUsername(), user.getRole(), tokenSubject.getExpiresAt());
        request.setAttribute("currentUser", subject);
        checkPermission(request.getRequestURI(), subject);
        return true;
    }

    private void checkPermission(String path, TokenSubject subject) {
        if (path.startsWith("/api/users") && !"ADMIN".equals(subject.getRole())) {
            throw new BusinessException(403, "无权访问用户管理接口");
        }
        if (path.startsWith("/api/charts") && !("ADMIN".equals(subject.getRole()) || "PARTNER".equals(subject.getRole()))) {
            throw new BusinessException(403, "无权访问图表接口");
        }
    }
}
