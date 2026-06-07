package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.Match;
import com.example.mapper.MatchMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bracket")
public class BracketController {
    private final MatchMapper matchMapper;

    public BracketController(MatchMapper matchMapper) {
        this.matchMapper = matchMapper;
    }

    @GetMapping
    public Result<List<Match>> list() {
        return Result.ok(matchMapper.selectList(new QueryWrapper<Match>()
                .ne("stage", "GROUP")
                .orderByAsc("match_no")));
    }

    @PutMapping("/matches/{id}")
    public Result<Match> update(@PathVariable Long id, @RequestBody Match match) {
        match.setId(id);
        matchMapper.updateById(match);
        return Result.ok("更新成功", matchMapper.selectById(id));
    }
}
