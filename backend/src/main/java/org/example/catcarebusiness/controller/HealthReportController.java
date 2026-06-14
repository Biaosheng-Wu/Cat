package org.example.catcarebusiness.controller;

import org.example.catcarebusiness.entity.HealthReport;
import org.example.catcarebusiness.service.HealthReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/report")
public class HealthReportController {

    @Autowired
    private HealthReportService healthReportService;

    /**
     * 提交流浪猫健康异常上报
     * 前端传入包含文字描述、类型以及刚刚生成的 OSS 图片网址的 JSON 数据
     */
    @PostMapping("/submit")
    public String submitReport(@RequestBody HealthReport report) {
        // 1. 模拟当前上报人是 1 号测试用户（爸爸）
        report.setReporterId(1L);
        // 2. 设置默认的处理状态为 0（待处理）
        report.setStatus(0);
        // 3. 设置当前系统时间为上报时间
        report.setReportTime(LocalDateTime.now());

        boolean saved = healthReportService.save(report);
        return saved ? "校园流浪猫健康异常上报成功！系统已通知管理员处理。" : "上报失败，请检查填写内容。";
    }
}