package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Match;
import com.example.exception.BusinessException;
import com.example.mapper.MatchMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    private final MatchMapper matchMapper;

    public MatchController(MatchMapper matchMapper) {
        this.matchMapper = matchMapper;
    }

    @GetMapping
    public Result<IPage<Match>> list(String keyword, Long teamId, Long cityId, String groupName, String stage,
                                     String status, String date,
                                     @RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        QueryWrapper<Match> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like("match_no", keyword).or().like("stage", keyword).or().like("group_name", keyword)
                    .or().apply("EXISTS (SELECT 1 FROM t_team tt WHERE (tt.id = home_team_id OR tt.id = away_team_id) AND (tt.name_cn LIKE CONCAT('%', {0}, '%') OR tt.name_en LIKE CONCAT('%', {0}, '%')))", keyword));
        }
        if (teamId != null) {
            wrapper.and(w -> w.eq("home_team_id", teamId).or().eq("away_team_id", teamId));
        }
        if (cityId != null) {
            wrapper.eq("city_id", cityId);
        }
        if (StringUtils.hasText(groupName)) {
            wrapper.eq("group_name", groupName);
        }
        if (StringUtils.hasText(stage)) {
            wrapper.eq("stage", stage);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq("status", status);
        }
        if (StringUtils.hasText(date)) {
            wrapper.apply("DATE(match_time) = {0}", date);
        }
        wrapper.orderByAsc("match_no");
        return Result.ok(matchMapper.selectPage(new Page<>(page, pageSize), wrapper));
    }

    @GetMapping("/{id}")
    public Result<Match> get(@PathVariable Long id) {
        Match match = matchMapper.selectById(id);
        if (match == null) {
            throw new BusinessException(404, "比赛不存在");
        }
        return Result.ok(match);
    }

    @PostMapping
    public Result<Match> create(@RequestBody Match match) {
        if (match.getStatus() == null) {
            match.setStatus("NOT_STARTED");
        }
        matchMapper.insert(match);
        return Result.ok("创建成功", match);
    }

    @PutMapping("/{id}")
    public Result<Match> update(@PathVariable Long id, @RequestBody Match match) {
        validateScore(match);
        match.setId(id);
        if ("FINISHED".equals(match.getStatus()) && match.getWinnerTeamId() == null
                && match.getHomeScore() != null && match.getAwayScore() != null) {
            if (match.getHomeScore() > match.getAwayScore()) {
                match.setWinnerTeamId(match.getHomeTeamId());
            } else if (match.getAwayScore() > match.getHomeScore()) {
                match.setWinnerTeamId(match.getAwayTeamId());
            }
        }
        if (matchMapper.updateById(match) == 0) {
            throw new BusinessException(404, "比赛不存在");
        }
        return Result.ok("更新成功", matchMapper.selectById(id));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (matchMapper.deleteById(id) == 0) {
            throw new BusinessException(404, "比赛不存在");
        }
        return Result.ok("删除成功", null);
    }

    private void validateScore(Match match) {
        if ((match.getHomeScore() != null && match.getHomeScore() < 0)
                || (match.getAwayScore() != null && match.getAwayScore() < 0)) {
            throw new BusinessException(400, "比分不能为负数");
        }
    }

}
