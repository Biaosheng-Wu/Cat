package org.example.catcarebusiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.example.catcarebusiness.config.JwtUtils;
import org.example.catcarebusiness.entity.FeedRecord;
import org.example.catcarebusiness.service.FeedRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/feed")
public class FeedRecordController {

    @Autowired
    private FeedRecordService feedRecordService;

    @Autowired
    private JwtUtils jwtUtils;

    private Long getCurrentUserId(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return jwtUtils.getUserIdFromToken(header.substring(7));
        }
        return null;
    }

    /** 提交投喂打卡 */
    @PostMapping("/submit")
    public String submitFeed(@RequestBody FeedRecord record, HttpServletRequest request) {
        record.setUserId(getCurrentUserId(request));
        record.setFeedTime(LocalDateTime.now());

        boolean saved = feedRecordService.save(record);
        return saved ? "投喂打卡成功！" : "打卡失败，请检查填写内容。";
    }

    /** 查询某猫咪今日已被投喂次数（防过量投喂） */
    @GetMapping("/todayCount/{catId}")
    public long getTodayCount(@PathVariable Long catId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        return feedRecordService.count(
            new LambdaQueryWrapper<FeedRecord>()
                .eq(FeedRecord::getCatId, catId)
                .between(FeedRecord::getFeedTime, startOfDay, endOfDay)
        );
    }

    /** 获取当前用户的个人投喂记录（最近20条） */
    @GetMapping("/my")
    public List<FeedRecord> getMyFeedList(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return feedRecordService.list(
            new LambdaQueryWrapper<FeedRecord>()
                .eq(FeedRecord::getUserId, userId)
                .orderByDesc(FeedRecord::getFeedTime)
                .last("LIMIT 20")
        );
    }
}
