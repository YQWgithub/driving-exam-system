package com.drivingexam.controller;

import com.drivingexam.common.Result;
import com.drivingexam.entity.User;
import com.drivingexam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        try {
            if (userService.checkUsernameExists(user.getUsername())) {
                return Result.error("用户名已存在");
            }
            user.setRole(0);
            user.setStatus(1);
            user.setCreateTime(new Date());
            int result = userService.register(user);
            if (result > 0) {
                return Result.success("注册成功", null);
            } else {
                return Result.error("注册失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        try {
            String username = loginData.get("username");
            String password = loginData.get("password");

            User user = userService.login(username, password);
            user.setPassword(null);

            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            data.put("token", "token_" + user.getId() + "_" + System.currentTimeMillis());
            return Result.success("登录成功", data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/info/{id}")
    public Result<User> getUserInfo(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }

    @GetMapping("/list")
    public Result<List<User>> getUserList() {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            user.setPassword(null);
        }
        return Result.success(users);
    }

    @GetMapping("/list/role/{role}")
    public Result<List<User>> getUsersByRole(@PathVariable Integer role) {
        List<User> users = userService.getUsersByRole(role);
        for (User user : users) {
            user.setPassword(null);
        }
        return Result.success(users);
    }

    @PutMapping("/update")
    public Result<?> updateUser(@RequestBody User user) {
        int result = userService.updateUser(user);
        if (result > 0) {
            return Result.success("更新成功", null);
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> deleteUser(@PathVariable Integer id) {
        int result = userService.deleteUser(id);
        if (result > 0) {
            return Result.success("删除成功", null);
        }
        return Result.error("删除失败");
    }

    @PutMapping("/status/{id}/{status}")
    public Result<?> updateUserStatus(@PathVariable Integer id, @PathVariable Integer status) {
        int result = userService.updateUserStatus(id, status);
        if (result > 0) {
            return Result.success("状态更新成功", null);
        }
        return Result.error("状态更新失败");
    }

    @GetMapping("/check/username/{username}")
    public Result<Boolean> checkUsername(@PathVariable String username) {
        boolean exists = userService.checkUsernameExists(username);
        return Result.success(!exists);
    }

    @GetMapping("/check/phone/{phone}")
    public Result<Boolean> checkPhone(@PathVariable String phone) {
        boolean exists = userService.checkPhoneExists(phone);
        return Result.success(!exists);
    }

    @GetMapping("/check/email/{email}")
    public Result<Boolean> checkEmail(@PathVariable String email) {
        boolean exists = userService.checkEmailExists(email);
        return Result.success(!exists);
    }
}
