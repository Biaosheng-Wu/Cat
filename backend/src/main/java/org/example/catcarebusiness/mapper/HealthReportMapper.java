package org.example.catcarebusiness.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.catcarebusiness.entity.HealthReport;

@Mapper // 告诉 Spring 这是一个数据库映射接口
public interface HealthReportMapper extends BaseMapper<HealthReport> {
    // 继承 BaseMapper 后，自动拥有了对 t_health_report 表的增删改查能力
}