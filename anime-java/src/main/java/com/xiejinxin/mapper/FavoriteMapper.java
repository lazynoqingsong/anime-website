package com.xiejinxin.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface FavoriteMapper {
    // 添加收藏
    void addFavorite(Map<String, Object> params);
    
    // 取消收藏
    void removeFavorite(Map<String, Object> params);
    
    // 获取用户收藏列表
    List<Map<String, Object>> getUserFavorites(Map<String, Object> params);
    
    // 获取用户收藏总数
    Integer getUserFavoriteCount(Integer userId);
    
    // 检查是否已收藏
    Integer checkFavorite(Map<String, Object> params);
} 