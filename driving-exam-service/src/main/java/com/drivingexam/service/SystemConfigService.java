package com.drivingexam.service;

import com.drivingexam.entity.SystemConfig;

import java.util.List;

public interface SystemConfigService {
    int addSystemConfig(SystemConfig systemConfig);
    
    int deleteSystemConfig(Integer id);
    
    int updateSystemConfig(SystemConfig systemConfig);
    
    SystemConfig getSystemConfigById(Integer id);
    
    SystemConfig getSystemConfigByKey(String paramKey);
    
    List<SystemConfig> getAllSystemConfigs();
    
    String getParamValue(String paramKey);
}
