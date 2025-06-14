package com.xiejinxin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface RecommendationMapper {
    /**
     * 获取用户观看过的动漫及其标签
     */
    List<Map<String, Object>> getUserWatchedAnimeTags(Integer userId);
    
    /**
     * 获取所有动漫及其标签
     */
    List<Map<String, Object>> getAllAnimeWithTags();
    
    /**
     * 获取热门动漫
     */
    List<Map<String, Object>> getPopularAnime();
    
    /**
     * 更新用户标签偏好
     */
    void updateUserPreference(Map<String, Object> params);
    
    /**
     * 批量保存推荐结果
     */
    void saveRecommendations(List<Map<String, Object>> recommendations);
    
    /**
     * 获取每日推荐
     */
    List<Map<String, Object>> getDailyRecommendations(Map<String, Object> params);
    
    /**
     * 获取更多推荐
     */
    List<Map<String, Object>> getMoreRecommendations(Map<String, Object> params);
} 