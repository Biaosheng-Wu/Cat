package org.example.catcarebusiness.controller;

import org.example.catcarebusiness.entity.FeedRecord;
import org.example.catcarebusiness.service.FeedRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/feed")
public class FeedRecordController {

    @Autowired
    private FeedRecordService feedRecordService;

    /**
     * 提交投喂打卡
     * 前端传入猫咪ID、经纬度、备注等信息
     */
    @PostMapping("/submit")
    public String submitFeed(@RequestBody FeedRecord record) {
        record.setUserId(1L);
        record.setFeedTime(LocalDateTime.now());

        boolean saved = feedRecordService.save(record);
        return saved ? "投喂打卡成功！" : "打卡失败，请检查填写内容。";
    }
}
