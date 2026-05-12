package com.drivingexam.dao;

import com.drivingexam.entity.SystemConfig;

import java.util.List;

public interface SystemConfigMapper {
    int insert(SystemConfig systemConfig);
    
    int deleteById(Integer id);
    
    int update(SystemConfig systemConfig);
    
    SystemConfig selectById(Integer id);
    
    SystemConfig selectByKey(String paramKey);
    
    List<SystemConfig> selectAll();
}
