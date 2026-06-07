package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.DataMaintenance;
import com.example.exception.BusinessException;
import com.example.mapper.DataMaintenanceMapper;
import com.example.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data-maintenance")
public class DataMaintenanceController {
    private final DataMaintenanceMapper dataMaintenanceMapper;

    public DataMaintenanceController(DataMaintenanceMapper dataMaintenanceMapper) {
        this.dataMaintenanceMapper = dataMaintenanceMapper;
    }

    @GetMapping
    public Result<IPage<DataMaintenance>> list(String dataType, @RequestParam(defaultValue = "1") Integer page,
                                               @RequestParam(defaultValue = "10") Integer pageSize) {
        QueryWrapper<DataMaintenance> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(dataType)) {
            wrapper.eq("data_type", dataType);
        }
        wrapper.orderByDesc("create_time");
        return Result.ok(dataMaintenanceMapper.selectPage(new Page<>(page, pageSize), wrapper));
    }

    @PostMapping
    public Result<DataMaintenance> create(@RequestBody DataMaintenance record, HttpServletRequest request) {
        if (!StringUtils.hasText(record.getDataType()) || !StringUtils.hasText(record.getActionType())) {
            throw new BusinessException(400, "数据类型和维护动作不能为空");
        }
        record.setOperatorId(currentUser(request).getUserId());
        dataMaintenanceMapper.insert(record);
        return Result.ok("记录成功", record);
    }

    private TokenSubject currentUser(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject tokenSubject) {
            return tokenSubject;
        }
        throw new BusinessException(401, "请先登录");
    }
}
