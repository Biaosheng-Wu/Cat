package org.example.catcarebusiness.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_health_report") // 严格对应数据库中的健康异常表 [cite: 61, 85]
public class HealthReport {
    @TableId(type = IdType.AUTO) // 指定主键自增策略 [cite: 63]
    private Long id;                  // 上报ID [cite: 63]
    private Long catId;               // 猫咪ID [cite: 64]
    private Long reporterId;          // 上报者ID [cite: 65]
    private String reportType;        // 类型:SICK-生病,INJURED-受伤等 [cite: 66, 67]
    private String description;       // 异常详细描述 [cite: 68]
    private String imageUrls;         // 图片URL列表(逗号分隔) [cite: 69]
    private BigDecimal latitude;       // 发现地点纬度 [cite: 70]
    private BigDecimal longitude;      // 发现地点经度 [cite: 71]
    private Integer status;           // 处理状态:0-待处理,1-处理中等 [cite: 72, 73]
    private Long handlerId;           // 处理人ID [cite: 75]
    private String handleRemark;      // 处理备注 [cite: 76]
    private LocalDateTime reportTime; // 上报时间 [cite: 77]
    private LocalDateTime handleTime; // 处理时间 [cite: 78]
}