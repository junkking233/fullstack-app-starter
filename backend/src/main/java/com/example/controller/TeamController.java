package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Team;
import com.example.exception.BusinessException;
import com.example.mapper.TeamMapper;
import jakarta.validation.Valid;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamMapper teamMapper;

    public TeamController(TeamMapper teamMapper) {
        this.teamMapper = teamMapper;
    }

    @GetMapping
    public Result<IPage<Team>> list(String keyword, String groupName, String confederation,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        QueryWrapper<Team> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like("name_cn", keyword).or().like("name_en", keyword).or().like("country", keyword));
        }
        if (StringUtils.hasText(groupName)) {
            wrapper.eq("group_name", groupName);
        }
        if (StringUtils.hasText(confederation)) {
            wrapper.eq("confederation", confederation);
        }
        wrapper.orderByAsc("group_name").orderByAsc("id");
        return Result.ok(teamMapper.selectPage(new Page<>(page, pageSize), wrapper));
    }

    @GetMapping("/{id}")
    public Result<Team> get(@PathVariable Long id) {
        Team team = teamMapper.selectById(id);
        if (team == null) {
            throw new BusinessException(404, "球队不存在");
        }
        return Result.ok(team);
    }

    @PostMapping
    public Result<Team> create(@Valid @RequestBody Team team) {
        teamMapper.insert(team);
        return Result.ok("创建成功", team);
    }

    @PutMapping("/{id}")
    public Result<Team> update(@PathVariable Long id, @Valid @RequestBody Team team) {
        team.setId(id);
        if (teamMapper.updateById(team) == 0) {
            throw new BusinessException(404, "球队不存在");
        }
        return Result.ok("更新成功", teamMapper.selectById(id));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (teamMapper.deleteById(id) == 0) {
            throw new BusinessException(404, "球队不存在");
        }
        return Result.ok("删除成功", null);
    }
}
