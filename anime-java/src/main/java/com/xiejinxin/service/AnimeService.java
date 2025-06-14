package com.xiejinxin.service;

import com.xiejinxin.mapper.AnimeMapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AnimeService {
    private static final Logger logger = LoggerFactory.getLogger(AnimeService.class);
    
    @Resource
    private AnimeMapper animeMapper;
    
    /**
     * 获取动漫详情
     * @param id 动漫ID
     * @return 动漫详情信息
     */
    public Map<String, Object> getAnimeDetail(Integer id) {
        return animeMapper.getAnimeById(id);
    }
    
    /**
     * 获取动漫剧集列表
     * @param id 动漫ID
     * @return 剧集列表
     */
    public List<Map<String, Object>> getAnimeEpisodes(Integer id) {
        return animeMapper.getEpisodesByAnimeId(id);
    }
    
    /**
     * 获取最近更新的动漫
     * @return 动漫列表
     */
    public List<Map<String, Object>> getRecentAnimes() {
        return animeMapper.getRecentAnimes();
    }
    
    /**
     * 根据播放日期获取动漫列表
     * @param day 星期几
     * @return 动漫列表
     */
    public List<Map<String, Object>> getAnimesByDay(String day) {
        return animeMapper.getAnimesByDay(day);
    }

    /**
     * 获取每周更新时间表
     * @return 按星期分组的动漫列表
     */
    public Map<String, List<Map<String, Object>>> getWeeklySchedule() {
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        Map<String, List<Map<String, Object>>> schedule = new LinkedHashMap<>();
        
        // 初始化每天的列表
        for (String day : days) {
            List<Map<String, Object>> animes = animeMapper.getAnimesByDay(day);
            schedule.put(day, animes);
        }
        
        return schedule;
    }

    /**
     * 根据筛选条件分页获取动漫列表
     * @param params 筛选参数
     * @return 分页结果
     */
    public Map<String, Object> getAnimesByFiltersWithPaging(Map<String, Object> params) {
        try {
            List<Map<String, Object>> animes = animeMapper.getAnimesByFiltersWithPaging(params);
            Integer total = animeMapper.getTotalCount(params);
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", animes != null ? animes : new ArrayList<>());
            result.put("total", total != null ? total : 0);
            
            return result;
        } catch (Exception e) {
            logger.error("获取动漫列表失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("list", new ArrayList<>());
            result.put("total", 0);
            return result;
        }
    }

    /**
     * 获取分类列表
     * @param type 分类类型
     * @return 分类列表
     */
    public List<Map<String, Object>> getCategoriesByType(String type) {
        return animeMapper.getCategoriesByType(type);
    }

    // 添加新方法
    public Integer getCategoryIdByName(String name, String type) {
        try {
            return animeMapper.getCategoryIdByName(name, type);
        } catch (Exception e) {
            logger.error("获取分类ID失败: name={}, type={}", name, type, e);
            return null;
        }
    }

    // 移除此方法
    public List<Map<String, Object>> getAnimesByCriteria(
        Integer regionId,
        Integer formatId,
        Integer letterId,
        Integer seasonId,
        String year,
        String status,
        String genre
    ) {
        return null;
    }

    public Map<String, Object> searchAnimes(Map<String, Object> params) {
        try {
            List<Map<String, Object>> animes = animeMapper.searchAnimes(params);
            Integer total = animeMapper.getSearchTotal(params);
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", animes != null ? animes : new ArrayList<>());
            result.put("total", total != null ? total : 0);
            
            logger.info("搜索结果: total={}, list.size={}", total, animes != null ? animes.size() : 0);
            return result;
        } catch (Exception e) {
            logger.error("搜索动漫失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("list", new ArrayList<>());
            result.put("total", 0);
            return result;
        }
    }

    /**
     * 获取最新更新的动漫(用于最近更新栏)
     */
    public List<Map<String, Object>> getLatestUpdatedAnimes() {
        return animeMapper.getLatestUpdatedAnimes();
    }

    /**
     * 获取用户观看历史
     */
    public List<Map<String, Object>> getWatchHistory(Long userId) {
        return animeMapper.getWatchHistory(userId);
    }

    /**
     * 添加观看记录
     */
    public void addWatchHistory(Long userId, Long animeId, Long episodeId, Integer episodeNumber) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("animeId", animeId);
        params.put("episodeId", episodeId);
        params.put("episodeNumber", episodeNumber);
        animeMapper.addWatchHistory(params);
    }

    /**
     * 清空用户观看历史
     */
    public void clearWatchHistory(Long userId) {
        animeMapper.clearWatchHistory(userId);
    }
}
