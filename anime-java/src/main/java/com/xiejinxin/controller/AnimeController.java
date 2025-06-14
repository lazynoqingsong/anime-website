package com.xiejinxin.controller;

import com.xiejinxin.common.Result;
import com.xiejinxin.service.AnimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/anime")
@CrossOrigin
public class AnimeController {
    private static final Logger logger = LoggerFactory.getLogger(AnimeController.class);
    
    @Autowired
    private AnimeService animeService;

    @GetMapping("/recent")
    public Result getRecentAnimes() {
        List<Map<String, Object>> animes = animeService.getRecentAnimes();
        return Result.success(animes);
    }

    @GetMapping("/weekly")
    public Result getWeeklySchedule() {
        Map<String, List<Map<String, Object>>> schedule = animeService.getWeeklySchedule();
        return Result.success(schedule);
    }

    @GetMapping("/filter")
    public Result getAnimesByFilters(
        @RequestParam(required = false) String region_id,
        @RequestParam(required = false) String format_id,
        @RequestParam(required = false) String letter_id,
        @RequestParam(required = false) String season_id,
        @RequestParam(required = false) String year,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String genre,
        @RequestParam(defaultValue = "1") Integer currentPage,
        @RequestParam(defaultValue = "12") Integer pageSize
    ) {
        try {
            Map<String, Object> params = new HashMap<>();
            
            if (region_id != null && !region_id.isEmpty()) {
                Integer categoryId = animeService.getCategoryIdByName(region_id, "region");
                params.put("region_id", categoryId);
            }
            
            if (format_id != null && !format_id.isEmpty()) {
                Integer categoryId = animeService.getCategoryIdByName(format_id, "format");
                params.put("format_id", categoryId);
            }
            
            if (letter_id != null && !letter_id.isEmpty()) {
                Integer categoryId = animeService.getCategoryIdByName(letter_id, "letter");
                params.put("letter_id", categoryId);
            }
            
            if (season_id != null && !season_id.isEmpty()) {
                Integer categoryId = animeService.getCategoryIdByName(season_id, "season");
                params.put("season_id", categoryId);
            }
            
            params.put("year", year);
            params.put("status", status);
            params.put("genre", genre);
            params.put("offset", (currentPage - 1) * pageSize);
            params.put("limit", pageSize);

            logger.info("筛选参数: {}", params);
            Map<String, Object> result = animeService.getAnimesByFiltersWithPaging(params);
            return Result.success(result);
        } catch (Exception e) {
            logger.error("获取动漫列表失败", e);
            return Result.error("获取动漫列表失败：" + e.getMessage());
        }
    }

    // 添加一个辅助方法来获取category_id
    private Integer getCategoryIdByName(String name, String type) {
        return animeService.getCategoryIdByName(name, type);
    }

    @GetMapping("/categories/{type}")
    public Result getCategoriesByType(@PathVariable String type) {
        List<Map<String, Object>> categories = animeService.getCategoriesByType(type);
        return Result.success(categories);
    }

    // 获取动漫详情
    @GetMapping("/{id}")
    public Result getAnimeDetail(@PathVariable Integer id) {
        logger.info("获取动漫详情, id: {}", id);
        try {
            Map<String, Object> animeDetail = animeService.getAnimeDetail(id);
            logger.info("获取动漫详情成功: {}", animeDetail);
            return Result.success(animeDetail);
        } catch (Exception e) {
            logger.error("获取动漫详情失败", e);
            return Result.error("获取动漫详情失败: " + e.getMessage());
        }
    }

    // 获取剧集列表
    @GetMapping("/{id}/episodes")
    public Result getAnimeEpisodes(@PathVariable Integer id) {
        logger.info("获取剧集列表, animeId: {}", id);
        try {
            List<Map<String, Object>> episodes = animeService.getAnimeEpisodes(id);
            logger.info("获取剧集列表成功, 数量: {}", episodes.size());
            return Result.success(episodes);
        } catch (Exception e) {
            logger.error("获取剧集列表失败", e);
            return Result.error("获取剧集列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public Result searchAnimes(
        @RequestParam String keyword,
        @RequestParam(defaultValue = "1") Integer currentPage,
        @RequestParam(defaultValue = "12") Integer pageSize
    ) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("keyword", keyword);
            params.put("offset", (currentPage - 1) * pageSize);
            params.put("limit", pageSize);
            
            Map<String, Object> result = animeService.searchAnimes(params);
            return Result.success(result);
        } catch (Exception e) {
            logger.error("搜索动漫失败", e);
            return Result.error("搜索失败：" + e.getMessage());
        }
    }

    /**
     * 获取最新更新的动漫列表(用于最近更新栏)
     */
    @GetMapping("/latest-updates")
    public Result getLatestUpdates() {
        try {
            List<Map<String, Object>> animes = animeService.getLatestUpdatedAnimes();
            return Result.success(animes);
        } catch (Exception e) {
            logger.error("获取最新更新动漫列表失败", e);
            return Result.error("获取最新更新动漫列表失败");
        }
    }

    /**
     * 获取观看历史
     */
    @GetMapping("/watch-history")
    public Result getWatchHistory(@RequestParam Long userId) {
        try {
            List<Map<String, Object>> history = animeService.getWatchHistory(userId);
            return Result.success(history);
        } catch (Exception e) {
            logger.error("获取观看历史失败", e);
            return Result.error("获取观看历史失败");
        }
    }

    /**
     * 添加观看记录
     */
    @PostMapping("/watch-history")
    public Result addWatchHistory(
        @RequestParam Long userId,
        @RequestParam Long animeId,
        @RequestParam Long episodeId,
        @RequestParam Integer episodeNumber
    ) {
        try {
            animeService.addWatchHistory(userId, animeId, episodeId, episodeNumber);
            return Result.success();
        } catch (Exception e) {
            logger.error("添加观看记录失败", e);
            return Result.error("添加观看记录失败");
        }
    }

    /**
     * 清空观看历史
     */
    @DeleteMapping("/watch-history")
    public Result clearWatchHistory(@RequestParam Long userId) {
        try {
            animeService.clearWatchHistory(userId);
            return Result.success();
        } catch (Exception e) {
            logger.error("清空观看历史失败", e);
            return Result.error("清空观看历史失败");
        }
    }
} 