package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.*;
import com.example.mapper.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/charts")
public class ChartController {
    private final UserMapper userMapper;
    private final TeamMapper teamMapper;
    private final MatchMapper matchMapper;
    private final CityMapper cityMapper;
    private final CommentMapper commentMapper;
    private final FavoriteMapper favoriteMapper;
    private final StandingMapper standingMapper;

    public ChartController(UserMapper userMapper, TeamMapper teamMapper, MatchMapper matchMapper,
                           CityMapper cityMapper, CommentMapper commentMapper, FavoriteMapper favoriteMapper,
                           StandingMapper standingMapper) {
        this.userMapper = userMapper;
        this.teamMapper = teamMapper;
        this.matchMapper = matchMapper;
        this.cityMapper = cityMapper;
        this.commentMapper = commentMapper;
        this.favoriteMapper = favoriteMapper;
        this.standingMapper = standingMapper;
    }

    @GetMapping("/dashboard-stats")
    public Result<Map<String, Object>> dashboardStats() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalUsers", userMapper.selectCount(new QueryWrapper<>()));
        data.put("totalTeams", teamMapper.selectCount(new QueryWrapper<>()));
        data.put("totalMatches", matchMapper.selectCount(new QueryWrapper<>()));
        data.put("totalCities", cityMapper.selectCount(new QueryWrapper<>()));
        data.put("pendingComments", commentMapper.selectCount(new QueryWrapper<Comment>().eq("audit_status", "PENDING")));
        data.put("favorites", favoriteMapper.selectCount(new QueryWrapper<>()));
        return Result.ok(data);
    }

    @GetMapping("/match-stage-distribution")
    public Result<List<Map<String, Object>>> matchStageDistribution() {
        return countByMatchField("stage");
    }

    @GetMapping("/city-match-count")
    public Result<List<Map<String, Object>>> cityMatchCount() {
        List<Map<String, Object>> data = new ArrayList<>();
        List<City> cities = cityMapper.selectList(new QueryWrapper<City>().orderByAsc("id"));
        for (City city : cities) {
            long count = matchMapper.selectCount(new QueryWrapper<Match>().eq("city_id", city.getId()));
            data.add(Map.of("name", city.getNameCn(), "value", count));
        }
        return Result.ok(data);
    }

    @GetMapping("/group-points")
    public Result<Map<String, Object>> groupPoints() {
        List<Standing> standings = standingMapper.selectList(new QueryWrapper<Standing>().orderByAsc("group_name").orderByAsc("rank_no"));
        List<String> xData = new ArrayList<>();
        List<Integer> points = new ArrayList<>();
        Map<Long, Team> teams = new HashMap<>();
        for (Team team : teamMapper.selectList(new QueryWrapper<>())) {
            teams.put(team.getId(), team);
        }
        for (Standing standing : standings) {
            Team team = teams.get(standing.getTeamId());
            xData.add((team == null ? "球队" + standing.getTeamId() : team.getNameCn()) + "(" + standing.getGroupName() + ")");
            points.add(standing.getPoints());
        }
        return Result.ok(Map.of("xData", xData, "series", List.of(Map.of("name", "积分", "data", points, "color", "#0d9488"))));
    }

    @GetMapping("/top-favorite-teams")
    public Result<List<Map<String, Object>>> topFavoriteTeams() {
        return favoriteRanking("TEAM");
    }

    @GetMapping("/top-favorite-matches")
    public Result<List<Map<String, Object>>> topFavoriteMatches() {
        return favoriteRanking("MATCH");
    }

    @GetMapping("/public-summary")
    public Result<Map<String, Object>> publicSummary() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalTeams", teamMapper.selectCount(new QueryWrapper<>()));
        data.put("totalMatches", matchMapper.selectCount(new QueryWrapper<>()));
        data.put("totalCities", cityMapper.selectCount(new QueryWrapper<>()));
        data.put("finishedMatches", matchMapper.selectCount(new QueryWrapper<Match>().eq("status", "FINISHED")));
        data.put("upcomingMatches", matchMapper.selectCount(new QueryWrapper<Match>().eq("status", "NOT_STARTED")));
        data.put("topFavoriteTeams", favoriteRankingData("TEAM", 6));
        data.put("topFavoriteMatches", favoriteRankingData("MATCH", 6));
        return Result.ok(data);
    }

    private Result<List<Map<String, Object>>> countByMatchField(String field) {
        List<Map<String, Object>> data = new ArrayList<>();
        List<String> values = matchMapper.selectList(new QueryWrapper<Match>().select(field).groupBy(field))
                .stream().map(match -> "stage".equals(field) ? match.getStage() : match.getStatus()).filter(Objects::nonNull).toList();
        for (String value : values) {
            data.add(Map.of("name", value, "value", matchMapper.selectCount(new QueryWrapper<Match>().eq(field, value))));
        }
        return Result.ok(data);
    }

    private Result<List<Map<String, Object>>> favoriteRanking(String type) {
        return Result.ok(favoriteRankingData(type, 10));
    }

    private List<Map<String, Object>> favoriteRankingData(String type, int limit) {
        Map<Long, Long> counts = new LinkedHashMap<>();
        List<Favorite> favorites = favoriteMapper.selectList(new QueryWrapper<Favorite>().eq("object_type", type));
        for (Favorite favorite : favorites) {
            counts.put(favorite.getObjectId(), counts.getOrDefault(favorite.getObjectId(), 0L) + 1);
        }
        List<Map<String, Object>> data = new ArrayList<>();
        counts.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(limit)
                .forEach(entry -> data.add(Map.of("name", label(type, entry.getKey()), "value", entry.getValue())));
        return data;
    }

    private String label(String type, Long id) {
        if ("TEAM".equals(type)) {
            Team team = teamMapper.selectById(id);
            return team == null ? "球队" + id : team.getNameCn();
        }
        Match match = matchMapper.selectById(id);
        return match == null ? "比赛" + id : "Match " + match.getMatchNo();
    }
}
