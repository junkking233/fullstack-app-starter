package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.City;
import com.example.entity.Match;
import com.example.entity.Stadium;
import com.example.exception.BusinessException;
import com.example.mapper.CityMapper;
import com.example.mapper.MatchMapper;
import com.example.mapper.StadiumMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    private final CityMapper cityMapper;
    private final StadiumMapper stadiumMapper;
    private final MatchMapper matchMapper;

    public CityController(CityMapper cityMapper, StadiumMapper stadiumMapper, MatchMapper matchMapper) {
        this.cityMapper = cityMapper;
        this.stadiumMapper = stadiumMapper;
        this.matchMapper = matchMapper;
    }

    @GetMapping
    public Result<List<City>> list(String keyword, String country) {
        QueryWrapper<City> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like("name_cn", keyword).or().like("name_en", keyword));
        }
        if (StringUtils.hasText(country)) {
            wrapper.eq("country", country);
        }
        wrapper.orderByAsc("country").orderByAsc("id");
        return Result.ok(cityMapper.selectList(wrapper));
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> get(@PathVariable Long id) {
        City city = cityMapper.selectById(id);
        if (city == null) {
            throw new BusinessException(404, "城市不存在");
        }
        List<Stadium> stadiums = stadiumMapper.selectList(new QueryWrapper<Stadium>().eq("city_id", id));
        List<Match> matches = matchMapper.selectList(new QueryWrapper<Match>().eq("city_id", id).orderByAsc("match_no"));
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("city", city);
        data.put("stadiums", stadiums);
        data.put("matches", matches);
        data.put("matchCount", matches.size());
        return Result.ok(data);
    }
}
