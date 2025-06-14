package com.xiejinxin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 动漫数据访问接口
 */
@Mapper
public interface AnimeMapper {
    /**
     * 获取动漫详情
     */
    Map<String, Object> getAnimeById(@Param("id") Integer id);

    /**
     * 获取动漫剧集列表
     */
    List<Map<String, Object>> getEpisodesByAnimeId(@Param("animeId") Integer animeId);

    /**
     * 获取最近更新的动漫列表
     */
    List<Map<String, Object>> getRecentAnimes();

    /**
     * 根据播放日期获取动漫列表
     * @param day 播放日期
     */
    List<Map<String, Object>> getAnimesByDay(@Param("day") String day);

    /**
     * 根据筛选条件获取动漫列表
     */
    List<Map<String, Object>> getAnimesByFiltersWithPaging(Map<String, Object> params);

    /**
     * 获取总记录数
     */
    Integer getTotalCount(Map<String, Object> params);

    /**
     * 获取分类列表
     * @param type 分类类型 (status/genre/year/season)
     * @return 分类列表
     */
    List<Map<String, Object>> getCategoriesByType(String type);

    Integer getCategoryIdByName(String name, String type);

    /**
     * 搜索动漫
     */
    List<Map<String, Object>> searchAnimes(Map<String, Object> params);

    /**
     * 获取搜索结果总数
     */
    Integer getSearchTotal(Map<String, Object> params);

    /**
     * 获取最新更新的动漫列表(用于最近更新栏)
     */
    List<Map<String, Object>> getLatestUpdatedAnimes();

    /**
     * 获取用户观看历史
     */
    List<Map<String, Object>> getWatchHistory(@Param("userId") Long userId);

    /**
     * 添加观看记录
     */
    void addWatchHistory(Map<String, Object> params);

    /**
     * 清空用户观看历史
     */
    void clearWatchHistory(@Param("userId") Long userId);
}