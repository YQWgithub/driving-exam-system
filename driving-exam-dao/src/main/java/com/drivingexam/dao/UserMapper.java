package com.drivingexam.dao;

import com.drivingexam.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int insert(User user);
    
    int deleteById(Integer id);
    
    int update(User user);
    
    User selectById(Integer id);
    
    User selectByUsername(String username);
    
    User selectByPhone(String phone);
    
    User selectByEmail(String email);
    
    List<User> selectAll();
    
    List<User> selectByRole(Integer role);
    
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
}
