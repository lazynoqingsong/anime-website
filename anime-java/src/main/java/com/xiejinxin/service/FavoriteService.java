package com.xiejinxin.service;

import com.xiejinxin.mapper.FavoriteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FavoriteService {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);

    @Autowired
    private FavoriteMapper favoriteMapper;

    /**
     * 添加收藏
     */
    public void addFavorite(Integer userId, Integer animeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("animeId", animeId);
        favoriteMapper.addFavorite(params);
        logger.info("用户{}收藏了动漫{}", userId, animeId);
    }

    /**
     * 取消收藏
     */
    public void removeFavorite(Integer userId, Integer animeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("animeId", animeId);
        favoriteMapper.removeFavorite(params);
        logger.info("用户{}取消收藏了动漫{}", userId, animeId);
    }

    /**
     * 获取用户收藏列表
     */
    public Map<String, Object> getUserFavorites(Integer userId, Integer page, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", (page - 1) * pageSize);
        params.put("limit", pageSize);

        List<Map<String, Object>> favorites = favoriteMapper.getUserFavorites(params);
        Integer total = favoriteMapper.getUserFavoriteCount(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("list", favorites);
        result.put("total", total);

        logger.info("获取用户{}的收藏列表，总数：{}", userId, total);
        return result;
    }

    /**
     * 检查是否已收藏
     */
    public boolean checkFavorite(Integer userId, Integer animeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("animeId", animeId);
        return favoriteMapper.checkFavorite(params) > 0;
    }
} 