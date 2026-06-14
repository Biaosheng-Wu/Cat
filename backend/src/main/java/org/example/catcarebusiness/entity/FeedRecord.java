package org.example.catcarebusiness.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_feed_record")
public class FeedRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long catId;
    private Long userId;
    private String remark;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String locationName;
    private LocalDateTime feedTime;
}
