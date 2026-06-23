package com.zhiqu.community.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.common.Result;
import com.zhiqu.community.dto.QuestionVO;
import com.zhiqu.community.service.HotlistService;
import org.springframework.web.bind.annotation.*;

@RestController
public class HotlistController {

    private final HotlistService hotlistService;

    public HotlistController(HotlistService hotlistService) {
        this.hotlistService = hotlistService;
    }

    @GetMapping("/api/hotlist")
    public Result<IPage<QuestionVO>> getHotQuestions(@RequestParam(defaultValue = "weekly") String period,
                                                      @RequestParam(defaultValue = "1") Integer page,
                                                      @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.ok(hotlistService.getHotQuestions(period, page, pageSize));
    }
}
