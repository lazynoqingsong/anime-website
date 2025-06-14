package com.xiejinxin.controller;

import com.xiejinxin.common.Result;
import com.xiejinxin.service.FavoriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/favorite")
@CrossOrigin
public class FavoriteController {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteController.class);

    @Autowired
    private FavoriteService favoriteService;

    /**
     * 添加收藏
     */
    @PostMapping
    public Result addFavorite(@RequestParam Integer userId, @RequestParam Integer animeId) {
        try {
            logger.info("添加收藏请求：userId={}, animeId={}", userId, animeId);
            favoriteService.addFavorite(userId, animeId);
            return Result.success("收藏成功");
        } catch (Exception e) {
            logger.error("添加收藏失败", e);
            return Result.error("收藏失败: " + e.getMessage());
        }
    }

    /**
     * 取消收藏
     */
    @DeleteMapping
    public Result removeFavorite(@RequestParam Integer userId, @RequestParam Integer animeId) {
        try {
            logger.info("取消收藏请求：userId={}, animeId={}", userId, animeId);
            favoriteService.removeFavorite(userId, animeId);
            return Result.success("取消收藏成功");
        } catch (Exception e) {
            logger.error("取消收藏失败", e);
            return Result.error("取消收藏失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户收藏列表
     */
    @GetMapping
    public Result getUserFavorites(
        @RequestParam Integer userId,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        try {
            logger.info("获取收藏列表请求：userId={}, page={}, pageSize={}", userId, page, pageSize);
            Map<String, Object> result = favoriteService.getUserFavorites(userId, page, pageSize);
            return Result.success(result);
        } catch (Exception e) {
            logger.error("获取收藏列表失败", e);
            return Result.error("获取收藏列表失败: " + e.getMessage());
        }
    }

    /**
     * 检查是否已收藏
     */
    @GetMapping("/check")
    public Result checkFavorite(@RequestParam Integer userId, @RequestParam Integer animeId) {
        try {
            logger.info("检查收藏状态：userId={}, animeId={}", userId, animeId);
            boolean isFavorited = favoriteService.checkFavorite(userId, animeId);
            return Result.success(isFavorited);
        } catch (Exception e) {
            logger.error("检查收藏状态失败", e);
            return Result.error("检查收藏状态失败: " + e.getMessage());
        }
    }
} 