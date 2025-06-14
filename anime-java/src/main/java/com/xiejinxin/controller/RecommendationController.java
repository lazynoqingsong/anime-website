package com.xiejinxin.controller;

import com.xiejinxin.common.Result;
import com.xiejinxin.service.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recommendations")
@CrossOrigin
public class RecommendationController {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    @Autowired
    private RecommendationService recommendationService;

    /**
     * 获取每日推荐
     */
    @GetMapping("/daily")
    public Result getDailyRecommendations(
        @RequestParam Integer userId,
        @RequestParam(defaultValue = "10") Integer limit
    ) {
        logger.info("开始获取每日推荐，用户ID: {}, 限制数量: {}", userId, limit);
        try {
            List<Map<String, Object>> recommendations = recommendationService.getDailyRecommendations(userId, limit);
            
            if (recommendations.isEmpty()) {
                logger.info("用户 {} 没有获取到推荐", userId);
                return Result.success(recommendations);
            }
            
            logger.info("成功获取每日推荐，用户ID: {}, 推荐数量: {}", userId, recommendations.size());
            return Result.success(recommendations);
            
        } catch (Exception e) {
            logger.error("获取每日推荐失败，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
            return Result.error("获取推荐失败: " + e.getMessage());
        }
    }

    /**
     * 获取更多推荐
     */
    @GetMapping("/more")
    public Result getMoreRecommendations(
        @RequestParam Integer userId,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "15") Integer pageSize
    ) {
        logger.info("开始获取更多推荐，用户ID: {}, 页码: {}, 每页数量: {}", userId, page, pageSize);
        try {
            Map<String, Object> result = recommendationService.getMoreRecommendations(userId, page, pageSize);
            List<?> recommendations = (List<?>) result.get("list");
            
            if (recommendations.isEmpty()) {
                logger.info("用户 {} 没有更多推荐", userId);
                return Result.success(result);
            }
            
            logger.info("成功获取更多推荐，用户ID: {}, 本页推荐数量: {}", userId, recommendations.size());
            return Result.success(result);
            
        } catch (Exception e) {
            logger.error("获取更多推荐失败，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
            return Result.error("获取更多推荐失败: " + e.getMessage());
        }
    }

    /**
     * 手动触发推荐生成
     */
    @PostMapping("/generate")
    public Result generateRecommendations(
        @RequestParam Integer userId,
        @RequestParam(defaultValue = "15") Integer limit
    ) {
        logger.info("开始手动生成推荐，用户ID: {}, 限制数量: {}", userId, limit);
        try {
            List<Map<String, Object>> recommendations = recommendationService.generateRecommendations(userId, limit);
            
            if (recommendations.isEmpty()) {
                logger.info("用户 {} 没有生成推荐结果", userId);
                return Result.success(recommendations);
            }
            
            logger.info("成功生成推荐，用户ID: {}, 推荐数量: {}", userId, recommendations.size());
            return Result.success(recommendations);
            
        } catch (Exception e) {
            logger.error("生成推荐失败，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
            return Result.error("生成推荐失败: " + e.getMessage());
        }
    }
} 