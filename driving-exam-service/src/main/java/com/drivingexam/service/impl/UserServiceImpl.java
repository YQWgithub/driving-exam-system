package com.drivingexam.service.impl;

import com.drivingexam.dao.UserMapper;
import com.drivingexam.entity.User;
import com.drivingexam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int register(User user) {
        if (checkUsernameExists(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (user.getPhone() != null && checkPhoneExists(user.getPhone())) {
            throw new RuntimeException("手机号已被注册");
        }
        if (user.getEmail() != null && checkEmailExists(user.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }
        return userMapper.insert(user);
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("账户已被禁用");
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAll();
    }

    @Override
    public List<User> getUsersByRole(Integer role) {
        return userMapper.selectByRole(role);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.update(user);
    }

    @Override
    public int deleteUser(Integer id) {
        return userMapper.deleteById(id);
    }

    @Override
    public int updateUserStatus(Integer id, Integer status) {
        return userMapper.updateStatus(id, status);
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return userMapper.selectByUsername(username) != null;
    }

    @Override
    public boolean checkPhoneExists(String phone) {
        return userMapper.selectByPhone(phone) != null;
    }

    @Override
    public boolean checkEmailExists(String email) {
        return userMapper.selectByEmail(email) != null;
    }
}
