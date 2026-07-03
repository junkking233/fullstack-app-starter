package com.fullstack.starter.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fullstack.starter.common.Result;
import com.fullstack.starter.dto.UserQuery;
import com.fullstack.starter.entity.User;
import com.fullstack.starter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Result<IPage<User>> list(@Valid UserQuery query) {
        return Result.ok(userService.page(query));
    }

    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Long id) {
        return Result.ok(userService.getById(id));
    }

    @PostMapping
    public Result<User> create(@Valid @RequestBody User user) {
        return Result.ok("创建成功", userService.create(user));
    }

    @PutMapping("/{id}")
    public Result<User> update(@PathVariable Long id, @Valid @RequestBody User user) {
        return Result.ok("更新成功", userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.ok("删除成功", null);
    }

    @PutMapping("/{id}/ban")
    public Result<Void> ban(@PathVariable Long id) {
        userService.banUser(id);
        return Result.ok("封禁成功", null);
    }

    @PutMapping("/{id}/unban")
    public Result<Void> unban(@PathVariable Long id) {
        userService.unbanUser(id);
        return Result.ok("解封成功", null);
    }
}
