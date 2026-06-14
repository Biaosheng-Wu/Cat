package org.example.catcarebusiness.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.catcarebusiness.entity.Cat;

@Mapper // 告诉 Spring 这是一个数据库映射接口
public interface CatMapper extends BaseMapper<Cat> {
    // 继承 BaseMapper 后，自动拥有了对 t_cat 表的增删改查能力
}