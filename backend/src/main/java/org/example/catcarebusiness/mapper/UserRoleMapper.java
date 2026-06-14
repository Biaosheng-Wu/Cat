package org.example.catcarebusiness.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.example.catcarebusiness.entity.UserRole;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Insert("INSERT INTO t_user_role (user_id, role_id) VALUES (#{userId}, #{roleId}) " +
            "ON DUPLICATE KEY UPDATE id = id")
    void insertUserRole(Long userId, Long roleId);
}
