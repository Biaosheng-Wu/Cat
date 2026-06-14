package org.example.catcarebusiness.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.catcarebusiness.entity.User;
import org.example.catcarebusiness.entity.UserRole;
import org.example.catcarebusiness.mapper.UserMapper;
import org.example.catcarebusiness.mapper.UserRoleMapper;
import org.example.catcarebusiness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            // 确保角色数据存在
            initRoleData();

            // 创建测试账号
            initTestUser("admin",  "系统管理员", "ROLE_ADMIN");
            initTestUser("volun",  "爱心志愿者", "ROLE_VOLUNTEER");

            log.info("RBAC 测试账号初始化完成：admin(管理员) / volun(志愿者)，密码均为 123456");
        } catch (Exception e) {
            log.warn("RBAC 表尚未创建，跳过初始化。请先执行 init.sql 中的 RBAC 建表语句。错误: {}", e.getMessage());
        }
    }

    private void initRoleData() {
        Long count = userRoleMapper.selectCount(null);
        if (count == 0) {
            UserRole adminRole = new UserRole();
            adminRole.setRoleCode("ROLE_ADMIN");
            adminRole.setRoleName("管理员");
            adminRole.setDescription("拥有全部权限，包括删除和管理");
            userRoleMapper.insert(adminRole);

            UserRole volunRole = new UserRole();
            volunRole.setRoleCode("ROLE_VOLUNTEER");
            volunRole.setRoleName("志愿者");
            volunRole.setDescription("普通用户，可投喂、上报、查看猫咪");
            userRoleMapper.insert(volunRole);
        }
    }

    private void initTestUser(String username, String nickname, String roleCode) {
        User exist = userMapper.selectOne(
            new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (exist == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setNickname(nickname);
            user.setEnabled(1);
            user.setCreateTime(LocalDateTime.now());
            userMapper.insert(user);

            UserRole role = userRoleMapper.selectOne(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleCode, roleCode));
            if (role != null) {
                userRoleMapper.insertUserRole(user.getId(), role.getId());
            }
        } else {
            // 用户已存在，确保角色已分配
            List<String> roles = userMapper.selectRoleCodesByUserId(exist.getId());
            if (roles == null || !roles.contains(roleCode)) {
                UserRole role = userRoleMapper.selectOne(
                    new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleCode, roleCode));
                if (role != null) {
                    userRoleMapper.insertUserRole(exist.getId(), role.getId());
                    log.info("补充分配角色: {} -> {}", username, roleCode);
                }
            }
        }
    }
}
