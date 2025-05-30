package com.demo.daniel.controller;

import com.demo.daniel.model.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/monitor/cache")
public class CacheController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/info")
    @PreAuthorize("hasAuthority('monitor:cache:all')")
    public ApiResponse<Map<String, Object>> getInfo() {
        Map<String, Object> result = new HashMap<>();
        // Step 1: 获取Redis详情
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        result.put("info", info);
        // Step 2: 获取Key的数量
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::dbSize);
        result.put("keyCount", dbSize);
        // Step 3: 获取请求次数
        List<Map<String, Object>> pieList = new ArrayList<>();
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.serverCommands().info("commandStats"));
        if (commandStats != null && !commandStats.isEmpty()) {
            commandStats.stringPropertyNames().forEach(key -> {
                Map<String, Object> data = new HashMap<>();
                String property = commandStats.getProperty(key);
                data.put("name", StringUtils.substring(key, 8));
                data.put("value", StringUtils.substringBetween(property, "calls=", ",use"));
                pieList.add(data);
            });
        }
        result.put("commandStats", pieList);
        return ApiResponse.ok(result);
    }
}
