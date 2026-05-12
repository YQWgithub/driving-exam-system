package com.drivingexam.controller;

import com.drivingexam.common.Result;
import com.drivingexam.entity.SystemConfig;
import com.drivingexam.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/config")
@CrossOrigin
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @PostMapping("/add")
    public Result<?> addSystemConfig(@RequestBody SystemConfig systemConfig) {
        try {
            systemConfigService.addSystemConfig(systemConfig);
            return Result.success("添加成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> deleteSystemConfig(@PathVariable Integer id) {
        try {
            systemConfigService.deleteSystemConfig(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<?> updateSystemConfig(@RequestBody SystemConfig systemConfig) {
        try {
            systemConfigService.updateSystemConfig(systemConfig);
            return Result.success("更新成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/info/{id}")
    public Result<SystemConfig> getSystemConfig(@PathVariable Integer id) {
        try {
            SystemConfig systemConfig = systemConfigService.getSystemConfigById(id);
            return Result.success(systemConfig);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public Result<List<SystemConfig>> getSystemConfigList() {
        try {
            List<SystemConfig> systemConfigs = systemConfigService.getAllSystemConfigs();
            return Result.success(systemConfigs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/value/{paramKey}")
    public Result<String> getParamValue(@PathVariable String paramKey) {
        try {
            String value = systemConfigService.getParamValue(paramKey);
            return Result.success(value);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/all")
    public Result<Map<String, String>> getAllConfigs() {
        try {
            List<SystemConfig> systemConfigs = systemConfigService.getAllSystemConfigs();
            Map<String, String> configMap = new HashMap<>();
            
            for (SystemConfig config : systemConfigs) {
                configMap.put(config.getParamKey(), config.getParamValue());
            }
            
            return Result.success(configMap);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
