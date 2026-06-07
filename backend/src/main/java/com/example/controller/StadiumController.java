package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.Match;
import com.example.entity.Stadium;
import com.example.exception.BusinessException;
import com.example.mapper.MatchMapper;
import com.example.mapper.StadiumMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stadiums")
public class StadiumController {
    private final StadiumMapper stadiumMapper;
    private final MatchMapper matchMapper;

    public StadiumController(StadiumMapper stadiumMapper, MatchMapper matchMapper) {
        this.stadiumMapper = stadiumMapper;
        this.matchMapper = matchMapper;
    }

    @GetMapping
    public Result<List<Stadium>> list(Long cityId, String keyword) {
        QueryWrapper<Stadium> wrapper = new QueryWrapper<>();
        if (cityId != null) {
            wrapper.eq("city_id", cityId);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like("name_cn", keyword).or().like("name_en", keyword));
        }
        wrapper.orderByAsc("city_id").orderByAsc("id");
        return Result.ok(stadiumMapper.selectList(wrapper));
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> get(@PathVariable Long id) {
        Stadium stadium = stadiumMapper.selectById(id);
        if (stadium == null) {
            throw new BusinessException(404, "场馆不存在");
        }
        List<Match> matches = matchMapper.selectList(new QueryWrapper<Match>().eq("stadium_id", id).orderByAsc("match_no"));
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("stadium", stadium);
        data.put("matches", matches);
        data.put("matchCount", matches.size());
        return Result.ok(data);
    }
}
