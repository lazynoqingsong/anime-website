package com.xiejinxin.controller;

import com.xiejinxin.common.Result;
import com.xiejinxin.entity.User;
import com.xiejinxin.service.UserService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    
    @Resource
    private UserService userService;
    
    @GetMapping("/selectAll")
    public Result selectAll(User user) {
        List<User> list = userService.selectAll(user);
        return Result.success(list);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {

        try {
            if (userService.checkUsernameExists(user.getUsername())) {
                return Result.error("用户名已存在");
            }
            userService.register(user);
            return Result.success("注册成功");
        } catch (Exception e) {

            return Result.error("注册失败: " + e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public Result changePassword(@RequestBody Map<String, Object> params) {
        try {
            Integer userId = Integer.parseInt(params.get("userId").toString());
            String oldPassword = params.get("oldPassword").toString();
            String newPassword = params.get("newPassword").toString();
            
            // 验证原密码是否正确
            if (!userService.verifyPassword(userId, oldPassword)) {
                return Result.error("原密码不正确");
            }
            
            // 更新密码
            userService.updatePassword(userId, newPassword);
            return Result.success("密码修改成功");
        } catch (Exception e) {
            log.error("修改密码失败", e);
            return Result.error("修改密码失败: " + e.getMessage());
        }
    }
}
