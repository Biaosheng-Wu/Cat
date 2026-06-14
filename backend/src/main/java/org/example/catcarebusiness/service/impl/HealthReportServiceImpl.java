package org.example.catcarebusiness.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.catcarebusiness.entity.HealthReport;
import org.example.catcarebusiness.mapper.HealthReportMapper;
import org.example.catcarebusiness.service.HealthReportService;
import org.springframework.stereotype.Service;

@Service
public class HealthReportServiceImpl extends ServiceImpl<HealthReportMapper, HealthReport> implements HealthReportService {
}