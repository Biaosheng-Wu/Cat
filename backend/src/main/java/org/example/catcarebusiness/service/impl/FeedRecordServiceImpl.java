package org.example.catcarebusiness.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.catcarebusiness.entity.FeedRecord;
import org.example.catcarebusiness.mapper.FeedRecordMapper;
import org.example.catcarebusiness.service.FeedRecordService;
import org.springframework.stereotype.Service;

@Service
public class FeedRecordServiceImpl extends ServiceImpl<FeedRecordMapper, FeedRecord> implements FeedRecordService {
}
