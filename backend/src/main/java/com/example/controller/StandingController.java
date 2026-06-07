package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.Match;
import com.example.entity.Standing;
import com.example.entity.Team;
import com.example.exception.BusinessException;
import com.example.mapper.MatchMapper;
import com.example.mapper.StandingMapper;
import com.example.mapper.TeamMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/standings")
public class StandingController {
    private final StandingMapper standingMapper;
    private final MatchMapper matchMapper;
    private final TeamMapper teamMapper;

    public StandingController(StandingMapper standingMapper, MatchMapper matchMapper, TeamMapper teamMapper) {
        this.standingMapper = standingMapper;
        this.matchMapper = matchMapper;
        this.teamMapper = teamMapper;
    }

    @GetMapping
    public Result<List<Standing>> list(String groupName) {
        QueryWrapper<Standing> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(groupName)) {
            wrapper.eq("group_name", groupName);
        }
        wrapper.orderByAsc("group_name").orderByAsc("rank_no").orderByDesc("points");
        return Result.ok(standingMapper.selectList(wrapper));
    }

    @PostMapping("/recalculate")
    public Result<List<Standing>> recalculate() {
        standingMapper.delete(new QueryWrapper<Standing>().notInSql("team_id", "SELECT id FROM t_team"));
        standingMapper.delete(new QueryWrapper<Standing>()
                .notInSql("(team_id, group_name)", "SELECT id, group_name FROM t_team WHERE group_name IS NOT NULL"));

        Map<Long, Standing> standings = new LinkedHashMap<>();
        List<Team> teams = teamMapper.selectList(new QueryWrapper<Team>().isNotNull("group_name"));
        for (Team team : teams) {
            Standing standing = standingMapper.selectOne(new QueryWrapper<Standing>()
                    .eq("group_name", team.getGroupName()).eq("team_id", team.getId()));
            if (standing == null) {
                standing = new Standing();
                standing.setGroupName(team.getGroupName());
                standing.setTeamId(team.getId());
                standingMapper.insert(standing);
            }
            reset(standing);
            standings.put(team.getId(), standing);
        }

        List<Match> matches = matchMapper.selectList(new QueryWrapper<Match>()
                .eq("stage", "GROUP").eq("status", "FINISHED")
                .isNotNull("home_score").isNotNull("away_score"));
        for (Match match : matches) {
            Standing home = standings.get(match.getHomeTeamId());
            Standing away = standings.get(match.getAwayTeamId());
            if (home == null || away == null) {
                continue;
            }
            apply(home, match.getHomeScore(), match.getAwayScore());
            apply(away, match.getAwayScore(), match.getHomeScore());
        }

        Map<String, List<Standing>> byGroup = new LinkedHashMap<>();
        for (Standing standing : standings.values()) {
            byGroup.computeIfAbsent(standing.getGroupName(), key -> new ArrayList<>()).add(standing);
        }
        List<Standing> result = new ArrayList<>();
        List<Standing> thirdPlaced = new ArrayList<>();
        for (List<Standing> groupStandings : byGroup.values()) {
            groupStandings.sort(Comparator.comparing(Standing::getPoints, Comparator.nullsLast(Integer::compareTo)).reversed()
                    .thenComparing(Comparator.comparing(Standing::getGoalDiff, Comparator.nullsLast(Integer::compareTo)).reversed())
                    .thenComparing(Comparator.comparing(Standing::getGoalsFor, Comparator.nullsLast(Integer::compareTo)).reversed()));
            for (int i = 0; i < groupStandings.size(); i++) {
                Standing standing = groupStandings.get(i);
                standing.setRankNo(i + 1);
                if (i < 2) {
                    standing.setQualifyStatus("QUALIFIED");
                } else if (i == 2) {
                    standing.setQualifyStatus("THIRD_PENDING");
                    thirdPlaced.add(standing);
                } else {
                    standing.setQualifyStatus("ELIMINATED");
                }
                result.add(standing);
            }
        }
        thirdPlaced.sort(Comparator.comparing(Standing::getPoints, Comparator.nullsLast(Integer::compareTo)).reversed()
                .thenComparing(Comparator.comparing(Standing::getGoalDiff, Comparator.nullsLast(Integer::compareTo)).reversed())
                .thenComparing(Comparator.comparing(Standing::getGoalsFor, Comparator.nullsLast(Integer::compareTo)).reversed()));
        for (int i = 0; i < thirdPlaced.size(); i++) {
            thirdPlaced.get(i).setQualifyStatus(i < 8 ? "BEST_THIRD_QUALIFIED" : "ELIMINATED");
        }
        for (Standing standing : result) {
            standingMapper.updateById(standing);
        }
        return Result.ok("积分榜已重新计算", result);
    }

    @PutMapping("/{id}")
    public Result<Standing> update(@PathVariable Long id, @RequestBody Standing standing) {
        standing.setId(id);
        if (standingMapper.updateById(standing) == 0) {
            throw new BusinessException(404, "积分榜记录不存在");
        }
        return Result.ok("更新成功", standingMapper.selectById(id));
    }

    private void reset(Standing standing) {
        standing.setPlayed(0);
        standing.setWins(0);
        standing.setDraws(0);
        standing.setLosses(0);
        standing.setGoalsFor(0);
        standing.setGoalsAgainst(0);
        standing.setGoalDiff(0);
        standing.setPoints(0);
        standing.setQualifyStatus("PENDING");
    }

    private void apply(Standing standing, int goalsFor, int goalsAgainst) {
        standing.setPlayed(standing.getPlayed() + 1);
        standing.setGoalsFor(standing.getGoalsFor() + goalsFor);
        standing.setGoalsAgainst(standing.getGoalsAgainst() + goalsAgainst);
        standing.setGoalDiff(standing.getGoalsFor() - standing.getGoalsAgainst());
        if (goalsFor > goalsAgainst) {
            standing.setWins(standing.getWins() + 1);
            standing.setPoints(standing.getPoints() + 3);
        } else if (goalsFor == goalsAgainst) {
            standing.setDraws(standing.getDraws() + 1);
            standing.setPoints(standing.getPoints() + 1);
        } else {
            standing.setLosses(standing.getLosses() + 1);
        }
    }
}
