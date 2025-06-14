package com.xiejinxin.service;

import com.xiejinxin.entity.User;
import com.xiejinxin.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public List<User> selectAll(User user) {
       return   userMapper.selectAll();
    }
    public boolean checkUsernameExists(String username) {
        return userMapper.findByUsername(username) != null;
    }

    public void register(User user) {
        log.info("注册用户: {}", user.getUsername());  // 添加日志
        // 可以在这里添加密码加密等逻辑
        userMapper.insert(user);
    }

    /**
     * 验证用户密码
     */
    public boolean verifyPassword(Integer userId, String password) {
        User user = userMapper.findById(userId);
        return user != null && user.getPassword().equals(password);
    }

    /**
     * 更新用户密码
     */
    public void updatePassword(Integer userId, String newPassword) {
        userMapper.updatePassword(userId, newPassword);
        log.info("用户密码更新成功: userId={}", userId);
    }
}
