package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.Favorite;
import com.example.exception.BusinessException;
import com.example.mapper.FavoriteMapper;
import com.example.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteMapper favoriteMapper;

    public FavoriteController(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    @GetMapping("/my")
    public Result<List<Favorite>> my(HttpServletRequest request) {
        return Result.ok(favoriteMapper.selectList(new QueryWrapper<Favorite>()
                .eq("user_id", currentUser(request).getUserId()).orderByDesc("create_time")));
    }

    @GetMapping("/status")
    public Result<Map<String, Boolean>> status(String objectType, Long objectId, HttpServletRequest request) {
        long count = favoriteMapper.selectCount(new QueryWrapper<Favorite>()
                .eq("user_id", currentUser(request).getUserId())
                .eq("object_type", objectType).eq("object_id", objectId));
        return Result.ok(Map.of("favorited", count > 0));
    }

    @PostMapping
    public Result<Favorite> create(@RequestBody Favorite favorite, HttpServletRequest request) {
        validate(favorite);
        favorite.setUserId(currentUser(request).getUserId());
        Favorite exists = favoriteMapper.selectOne(new QueryWrapper<Favorite>()
                .eq("user_id", favorite.getUserId())
                .eq("object_type", favorite.getObjectType())
                .eq("object_id", favorite.getObjectId()));
        if (exists != null) {
            return Result.ok("已收藏", exists);
        }
        favoriteMapper.insert(favorite);
        return Result.ok("收藏成功", favorite);
    }

    @DeleteMapping
    public Result<Void> delete(String objectType, Long objectId, HttpServletRequest request) {
        favoriteMapper.delete(new QueryWrapper<Favorite>()
                .eq("user_id", currentUser(request).getUserId())
                .eq("object_type", objectType).eq("object_id", objectId));
        return Result.ok("取消收藏成功", null);
    }

    private void validate(Favorite favorite) {
        if (!"TEAM".equals(favorite.getObjectType()) && !"MATCH".equals(favorite.getObjectType())) {
            throw new BusinessException(400, "收藏类型不合法");
        }
        if (favorite.getObjectId() == null) {
            throw new BusinessException(400, "收藏对象不能为空");
        }
    }

    private TokenSubject currentUser(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject tokenSubject) {
            return tokenSubject;
        }
        throw new BusinessException(401, "请先登录");
    }
}
