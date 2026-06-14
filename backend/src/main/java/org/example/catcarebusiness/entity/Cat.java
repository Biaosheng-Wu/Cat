package org.example.catcarebusiness.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_cat") // 严格对应数据库中的流浪猫档案表
public class Cat {
    @TableId(type = IdType.AUTO) // 指定主键自增策略
    private Long id;                  // 猫咪ID [cite: 23]
    private String name;              // 昵称 [cite: 24]
    private String avatarUrl;         // 头像URL [cite: 25]
    private String breed;             // 品种 [cite: 26]
    private String gender;            // 性别:MALE-公,FEMALE-母,UNKNOWN-未知 [cite: 27]
    private String ageEstimate;       // 预估年龄 [cite: 28]
    private String color;             // 毛色 [cite: 29]
    private String appearanceFeatures;// 外貌特征 [cite: 30]
    private Integer tnrStatus;        // TNR状态:0-未绝育,1-已绝育,2-已剪耳标 [cite: 31]
    private String frequentLocations; // 常出没地点描述 [cite: 32]
    private BigDecimal defaultLatitude; // 默认位置纬度 [cite: 33]
    private BigDecimal defaultLongitude;// 默认位置经度 [cite: 34]
    private String healthStatus;      // 健康状态:HEALTHY-健康,SICK-生病等 [cite: 35, 36]
    private Long creatorId;           // 创建者用户ID [cite: 37]
    private LocalDateTime createTime; // 创建时间 [cite: 38]
    private LocalDateTime updateTime; // 更新时间 [cite: 39, 41]
}