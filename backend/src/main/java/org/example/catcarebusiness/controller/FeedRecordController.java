package org.example.catcarebusiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    /**
     * 1. 提交投喂打卡
     * 前端传入猫咪ID、经纬度、备注等信息
     */
    @PostMapping("/submit")
    public String submitFeed(@RequestBody FeedRecord record) {
        record.setUserId(1L);
        record.setFeedTime(LocalDateTime.now());

        boolean saved = feedRecordService.save(record);
        return saved ? "投喂打卡成功！" : "打卡失败，请检查填写内容。";
    }

    /**
     * 2. 查询某猫咪今日已被投喂次数（防过量投喂）
     */
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

    /**
     * 3. 获取当前用户（模拟 1 号用户）的个人投喂记录
     * 返回最近 20 条
     */
    @GetMapping("/my")
    public List<FeedRecord> getMyFeedList() {
        return feedRecordService.list(
            new LambdaQueryWrapper<FeedRecord>()
                .eq(FeedRecord::getUserId, 1L)
                .orderByDesc(FeedRecord::getFeedTime)
                .last("LIMIT 20")
        );
    }
}
