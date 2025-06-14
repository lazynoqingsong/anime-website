package com.xiejinxin.controller;

import com.xiejinxin.common.Result;
import com.xiejinxin.service.RankingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 排行榜控制器
 */
@RestController
@RequestMapping("/anime/ranking")
@CrossOrigin
public class RankingController {
    private static final Logger logger = LoggerFactory.getLogger(RankingController.class);

    @Autowired
    private RankingService rankingService;

    /**
     * 获取周榜数据
     * @param userId 用户ID
     * @return 排行榜数据
     */
    @GetMapping("/weekly")
    public Result getWeeklyRanking(@RequestParam(required = false) Integer userId) {
        logger.info("接收到获取周榜请求，userId: {}", userId);
        try {
            List<Map<String, Object>> ranking = rankingService.getWeeklyRanking(userId);
            logger.info("周榜获取成功，数据条数: {}", ranking.size());
            return Result.success(ranking);
        } catch (Exception e) {
            logger.error("获取周榜失败", e);
            return Result.error("获取周榜失败: " + e.getMessage());
        }
    }

    /**
     * 获取月榜数据
     * @param userId 用户ID
     * @return 排行榜数据
     */
    @GetMapping("/monthly")
    public Result getMonthlyRanking(@RequestParam(required = false) Integer userId) {
        logger.info("接收到获取月榜请求，userId: {}", userId);
        try {
            List<Map<String, Object>> ranking = rankingService.getMonthlyRanking(userId);
            logger.info("月榜获取成功，数据条数: {}", ranking.size());
            return Result.success(ranking);
        } catch (Exception e) {
            logger.error("获取月榜失败", e);
            return Result.error("获取月榜失败: " + e.getMessage());
        }
    }

    /**
     * 获取总榜数据
     * @param userId 用户ID
     * @return 排行榜数据
     */
    @GetMapping("/total")
    public Result getTotalRanking(@RequestParam(required = false) Integer userId) {
        logger.info("接收到获取总榜请求，userId: {}", userId);
        try {
            List<Map<String, Object>> ranking = rankingService.getTotalRanking(userId);
            logger.info("总榜获取成功，数据条数: {}", ranking.size());
            return Result.success(ranking);
        } catch (Exception e) {
            logger.error("获取总榜失败", e);
            return Result.error("获取总榜失败: " + e.getMessage());
        }
    }
} 