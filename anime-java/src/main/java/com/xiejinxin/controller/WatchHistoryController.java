package com.xiejinxin.controller;

import com.xiejinxin.common.Result;
import com.xiejinxin.service.WatchHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/watch-history")
@CrossOrigin
public class WatchHistoryController {
    private static final Logger logger = LoggerFactory.getLogger(WatchHistoryController.class);

    @Autowired
    private WatchHistoryService watchHistoryService;

    @PostMapping
    public Result addHistory(
        @RequestParam Long userId,
        @RequestParam Long animeId,
        @RequestParam Long episodeId,
        @RequestParam Integer episodeNumber
    ) {
        try {
            logger.info("添加观看历史，参数：userId={}, animeId={}, episodeId={}, episodeNumber={}", 
                       userId, animeId, episodeId, episodeNumber);
            
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("animeId", animeId);
            params.put("episodeId", episodeId);
            params.put("episodeNumber", episodeNumber);
            
            watchHistoryService.addWatchHistory(params);
            return Result.success("添加观看历史成功");
        } catch (Exception e) {
            logger.error("添加观看历史失败", e);
            return Result.error("添加观看历史失败: " + e.getMessage());
        }
    }

    @GetMapping
    public Result getHistory(@RequestParam Long userId) {
        try {
            logger.info("获取用户观看历史，userId: {}", userId);
            List<Map<String, Object>> history = watchHistoryService.getWatchHistory(userId);
            return Result.success(history);
        } catch (Exception e) {
            logger.error("获取观看历史失败", e);
            return Result.error("获取观看历史失败: " + e.getMessage());
        }
    }

    @DeleteMapping
    public Result clearHistory(@RequestParam Long userId) {
        try {
            logger.info("清空用户观看历史，userId: {}", userId);
            watchHistoryService.clearWatchHistory(userId);
            return Result.success("清空观看历史成功");
        } catch (Exception e) {
            logger.error("清空观看历史失败", e);
            return Result.error("清空观看历史失败: " + e.getMessage());
        }
    }
} 