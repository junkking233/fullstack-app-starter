package com.zhiqu.community.config;

import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.mapper.UserMapper;
import com.zhiqu.community.util.TokenSubject;
import com.zhiqu.community.util.TokenUtils;
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

        String path = request.getRequestURI();

        // Public GET paths: allow anonymous access with optional token
        if ("GET".equalsIgnoreCase(request.getMethod()) && isPublicReadPath(path)) {
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {
                try {
                    TokenSubject ts = tokenUtils.parse(authorization.substring(7));
                    User user = userMapper.selectById(ts.getUserId());
                    if (user != null && !"banned".equals(user.getStatus())) {
                        request.setAttribute("currentUser",
                            new TokenSubject(user.getId(), user.getUsername(), user.getRole(), ts.getExpiresAt()));
                    }
                } catch (Exception ignored) {
                    // Token invalid, proceed as anonymous
                }
            }
            return true;
        }

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BusinessException(401, "请先登录");
        }

        TokenSubject tokenSubject = tokenUtils.parse(authorization.substring(7));
        User user = userMapper.selectById(tokenSubject.getUserId());
        if (user == null || "banned".equals(user.getStatus())) {
            throw new BusinessException(401, "登录已失效，请重新登录");
        }

        TokenSubject subject = new TokenSubject(user.getId(), user.getUsername(), user.getRole(), tokenSubject.getExpiresAt());
        request.setAttribute("currentUser", subject);
        checkPermission(request.getRequestURI(), subject);
        return true;
    }

    // GET requests allowed for anonymous access
    private boolean isPublicReadPath(String path) {
        return path.startsWith("/api/charts")
            || path.startsWith("/api/categories")
            || path.startsWith("/api/tags");
    }

    private void checkPermission(String path, TokenSubject subject) {
        if (path.startsWith("/api/users") && !"ADMIN".equals(subject.getRole())
                || path.startsWith("/api/admin") && !"ADMIN".equals(subject.getRole())) {
            throw new BusinessException(403, "无权访问管理接口");
        }
    }
}
