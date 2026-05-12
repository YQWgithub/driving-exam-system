package com.drivingexam.service.impl;

import com.drivingexam.dao.SystemConfigMapper;
import com.drivingexam.entity.SystemConfig;
import com.drivingexam.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SystemConfigServiceImpl implements SystemConfigService {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public int addSystemConfig(SystemConfig systemConfig) {
        return systemConfigMapper.insert(systemConfig);
    }

    @Override
    public int deleteSystemConfig(Integer id) {
        return systemConfigMapper.deleteById(id);
    }

    @Override
    public int updateSystemConfig(SystemConfig systemConfig) {
        return systemConfigMapper.update(systemConfig);
    }

    @Override
    public SystemConfig getSystemConfigById(Integer id) {
        return systemConfigMapper.selectById(id);
    }

    @Override
    public SystemConfig getSystemConfigByKey(String paramKey) {
        return systemConfigMapper.selectByKey(paramKey);
    }

    @Override
    public List<SystemConfig> getAllSystemConfigs() {
        return systemConfigMapper.selectAll();
    }

    @Override
    public String getParamValue(String paramKey) {
        SystemConfig config = systemConfigMapper.selectByKey(paramKey);
        return config != null ? config.getParamValue() : null;
    }
}
