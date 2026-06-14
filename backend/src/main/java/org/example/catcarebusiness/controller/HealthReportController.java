package org.example.catcarebusiness.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.catcarebusiness.config.JwtUtils;
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

    @Autowired
    private JwtUtils jwtUtils;

    private Long getCurrentUserId(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return jwtUtils.getUserIdFromToken(header.substring(7));
        }
        return null;
    }

    /** 提交流浪猫健康异常上报 */
    @PostMapping("/submit")
    public String submitReport(@RequestBody HealthReport report, HttpServletRequest request) {
        report.setReporterId(getCurrentUserId(request));
        report.setStatus(0);
        report.setReportTime(LocalDateTime.now());

        boolean saved = healthReportService.save(report);
        return saved ? "校园流浪猫健康异常上报成功！系统已通知管理员处理。" : "上报失败，请检查填写内容。";
    }
}
