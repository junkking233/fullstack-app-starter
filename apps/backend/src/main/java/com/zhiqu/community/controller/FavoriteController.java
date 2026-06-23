package com.zhiqu.community.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.common.Result;
import com.zhiqu.community.dto.QuestionVO;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.service.FavoriteService;
import com.zhiqu.community.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/api/favorites/toggle")
    public Result<Boolean> toggleFavorite(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        Long questionId = Long.valueOf(body.get("questionId").toString());
        boolean favorited = favoriteService.toggleFavorite(currentUser.getUserId(), questionId);
        return Result.ok(favorited);
    }

    @GetMapping("/api/favorites/check")
    public Result<Boolean> checkFavorite(@RequestParam Long questionId, HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        boolean favorited = favoriteService.hasFavorited(currentUser.getUserId(), questionId);
        return Result.ok(favorited);
    }

    @GetMapping("/api/favorites/my")
    public Result<IPage<QuestionVO>> listMyFavorites(@RequestParam(defaultValue = "1") Integer page,
                                                      @RequestParam(defaultValue = "20") Integer pageSize,
                                                      HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        return Result.ok(favoriteService.listMyFavorites(currentUser.getUserId(), page, pageSize));
    }

    private TokenSubject requireLogin(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject ts) return ts;
        throw new BusinessException(401, "请先登录");
    }
}
