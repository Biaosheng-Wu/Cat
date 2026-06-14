package org.example.catcarebusiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.catcarebusiness.entity.Cat;

public interface CatService extends IService<Cat> {
    // 继承 MyBatis-Plus 的 IService，自动获得海量单表高级操作方法
}