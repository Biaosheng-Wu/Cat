package org.example.catcarebusiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.catcarebusiness.entity.User;

public interface UserService extends IService<User> {
    User getByUsername(String username);
}
