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

        // 公开内容接口：GET 请求无需登录
        String path = request.getRequestURI();
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
                    // Token 无效时以匿名身份访问公开内容
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

    // GET 请求允许匿名访问的公开内容路径
    private boolean isPublicReadPath(String path) {
        return path.startsWith("/api/questions")
            || path.startsWith("/api/answers")
            || path.startsWith("/api/comments")
            || path.startsWith("/api/categories")
            || path.startsWith("/api/tags")
            || path.startsWith("/api/hotlist")
            || path.startsWith("/api/achievements/leaderboard");
    }

    private void checkPermission(String path, TokenSubject subject) {
        if (path.startsWith("/api/users") && !"ADMIN".equals(subject.getRole())) {
            throw new BusinessException(403, "无权访问用户管理接口");
        }
        if (path.startsWith("/api/admin") && !"ADMIN".equals(subject.getRole())) {
            throw new BusinessException(403, "无权访问管理接口");
        }
    }
}
