package com.drivingexam.service;

import com.drivingexam.entity.User;

import java.util.List;

public interface UserService {
    int register(User user);
    
    User login(String username, String password);
    
    User getUserById(Integer id);
    
    List<User> getAllUsers();
    
    List<User> getUsersByRole(Integer role);
    
    int updateUser(User user);
    
    int deleteUser(Integer id);
    
    int updateUserStatus(Integer id, Integer status);
    
    boolean checkUsernameExists(String username);
    
    boolean checkPhoneExists(String phone);
    
    boolean checkEmailExists(String email);
}
