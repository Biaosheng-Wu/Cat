package org.example.catcarebusiness.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.catcarebusiness.entity.Cat;
import org.example.catcarebusiness.mapper.CatMapper;
import org.example.catcarebusiness.service.CatService;
import org.springframework.stereotype.Service;

@Service // 告诉 Spring 这是一个业务实现类，交给容器管理
public class CatServiceImpl extends ServiceImpl<CatMapper, Cat> implements CatService {
    // 继承了 ServiceImpl 后，基础的增删改查逻辑 MyBatis-Plus 已经全部帮我们写好了！
}