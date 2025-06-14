package com.xiejinxin.service;

import com.xiejinxin.mapper.RecommendationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.*;

/**
 * 推荐服务实现类
 * 基于内容的推荐算法
 */
@Service
public class RecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);
    
    @Autowired
    private RecommendationMapper recommendationMapper;
    
    /**
     * 计算用户对每个标签的偏好权重
     */
    private Map<String, Double> calculateUserPreferences(Integer userId) {
        Map<String, Double> tagWeights = new HashMap<>();
        
        try {
            // 获取用户观看历史和对应的标签
            List<Map<String, Object>> watchedAnimes = recommendationMapper.getUserWatchedAnimeTags(userId);
            
            // 计算每个标签的权重
            for (Map<String, Object> record : watchedAnimes) {
                try {
                    double weight = calculateWatchWeight(record);
                    String tags = (String) record.get("tags");
                    
                    if (tags != null && !tags.isEmpty()) {
                        tagWeights.merge(tags, weight, Double::sum);
                    }
                } catch (Exception e) {
                    logger.error("计算观看权重出错: {}", e.getMessage());
                }
            }
            
            // 归一化权重
            normalizeWeights(tagWeights);
            
            // 更新用户偏好
            updateUserPreferences(userId, tagWeights);
            
        } catch (Exception e) {
            logger.error("计算用户偏好失败: {}", e.getMessage());
        }
        
        return tagWeights;
    }
    
    /**
     * 计算单个观看记录的权重
     */
    private double calculateWatchWeight(Map<String, Object> record) {
        double weight = 1.0;
        
        try {
            // 获取观看进度
            Object progressObj = record.get("watch_progress");
            if (progressObj != null) {
                double progress = Double.parseDouble(progressObj.toString());
                weight *= (progress / 100.0);
            }
            
            // 获取是否看完
            Object finishedObj = record.get("is_finished");
            if (finishedObj != null) {
                int finished = Integer.parseInt(finishedObj.toString());
                if (finished == 1) {
                    weight *= 1.5; // 看完的权重更高
                }
            }
            
            // 考虑观看时间衰减
            Object watchTimeObj = record.get("watch_time");
            if (watchTimeObj != null) {
                // 可以根据观看时间添加时间衰减因子
                weight *= 1.0; // 这里可以根据需要调整衰减系数
            }
            
        } catch (Exception e) {
            logger.error("计算权重出错: {}", e.getMessage());
            return 1.0; // 发生错误时返回默认权重
        }
        
        return weight;
    }
    
    /**
     * 归一化权重
     */
    private void normalizeWeights(Map<String, Double> weights) {
        if (weights.isEmpty()) return;
        
        double sum = weights.values().stream().mapToDouble(Double::doubleValue).sum();
        if (sum > 0) {
            weights.replaceAll((k, v) -> v / sum);
        }
    }
    
    /**
     * 更新用户偏好
     */
    private void updateUserPreferences(Integer userId, Map<String, Double> tagWeights) {
        try {
            for (Map.Entry<String, Double> entry : tagWeights.entrySet()) {
                Map<String, Object> params = new HashMap<>();
                params.put("userId", userId);
                params.put("tagName", entry.getKey());
                params.put("weight", entry.getValue());
                recommendationMapper.updateUserPreference(params);
            }
        } catch (Exception e) {
            logger.error("更新用户偏好失败: {}", e.getMessage());
        }
    }
    
    /**
     * 生成推荐
     */
    public List<Map<String, Object>> generateRecommendations(Integer userId, Integer limit) {
        List<Map<String, Object>> recommendations = new ArrayList<>();
        
        try {
            // 计算用户偏好
            Map<String, Double> userPreferences = calculateUserPreferences(userId);
            
            if (userPreferences.isEmpty()) {
                // 如果没有用户偏好数据，返回热门推荐
                recommendations = recommendationMapper.getPopularAnime();
                saveRecommendations(userId, recommendations, "根据热门度推荐");
                return recommendations;
            }
            
            // 获取所有可推荐的动漫
            List<Map<String, Object>> allAnimes = recommendationMapper.getAllAnimeWithTags();
            
            // 计算每个动漫的推荐分数
            List<Map.Entry<Map<String, Object>, Double>> scoredAnimes = new ArrayList<>();
            
            for (Map<String, Object> anime : allAnimes) {
                double score = calculateAnimeScore(anime, userPreferences);
                scoredAnimes.add(new AbstractMap.SimpleEntry<>(anime, score));
            }
            
            // 按分数排序
            scoredAnimes.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
            
            // 取前N个结果
            for (int i = 0; i < Math.min(limit, scoredAnimes.size()); i++) {
                Map<String, Object> anime = scoredAnimes.get(i).getKey();
                anime.put("score", scoredAnimes.get(i).getValue());
                recommendations.add(anime);
            }
            
            // 保存推荐结果
            if (!recommendations.isEmpty()) {
                saveRecommendations(userId, recommendations, null);
            }
            
        } catch (Exception e) {
            logger.error("生成推荐失败: {}", e.getMessage());
        }
        
        logger.info("用户 {} 的推荐已更新，共 {} 条推荐", userId, recommendations.size());
        return recommendations;
    }
    
    /**
     * 计算动漫推荐分数
     */
    private double calculateAnimeScore(Map<String, Object> anime, Map<String, Double> userPreferences) {
        double score = 0.0;
        
        try {
            String animeTags = (String) anime.get("tags");
            if (animeTags != null && !animeTags.isEmpty()) {
                // 计算标签相似度
                for (Map.Entry<String, Double> pref : userPreferences.entrySet()) {
                    if (animeTags.contains(pref.getKey())) {
                        score += pref.getValue();
                    }
                }
            }
            
            // 考虑热度因素
            Object commentCount = anime.get("comment_count");
            if (commentCount != null) {
                int count = Integer.parseInt(commentCount.toString());
                score *= (1 + Math.log1p(count) * 0.1); // 热度调整因子
            }
            
        } catch (Exception e) {
            logger.error("计算推荐分数出错: {}", e.getMessage());
        }
        
        return score;
    }
    
    /**
     * 保存推荐结果
     */
    private void saveRecommendations(Integer userId, List<Map<String, Object>> recommendations, String defaultReason) {
        try {
            List<Map<String, Object>> toSave = new ArrayList<>();
            
            for (Map<String, Object> rec : recommendations) {
                Map<String, Object> item = new HashMap<>();
                item.put("userId", userId);
                item.put("animeId", rec.get("id"));
                item.put("score", rec.get("score"));
                item.put("reason", defaultReason != null ? defaultReason : generateRecommendationReason(rec, null));
                toSave.add(item);
            }
            
            if (!toSave.isEmpty()) {
                recommendationMapper.saveRecommendations(toSave);
            }
            
        } catch (Exception e) {
            logger.error("保存推荐结果失败: {}", e.getMessage());
        }
    }
    
    /**
     * 生成推荐原因
     */
    private String generateRecommendationReason(Map<String, Object> anime, Map<String, Double> userPreferences) {
        StringBuilder reason = new StringBuilder();
        
        try {
            String tags = (String) anime.get("tags");
            if (tags != null && !tags.isEmpty()) {
                reason.append("包含您感兴趣的标签: ").append(tags);
            }
            
            Object commentCount = anime.get("comment_count");
            if (commentCount != null) {
                int count = Integer.parseInt(commentCount.toString());
                if (count > 0) {
                    if (reason.length() > 0) reason.append("，");
                    reason.append("有").append(count).append("条评论");
                }
            }
            
        } catch (Exception e) {
            logger.error("生成推荐原因出错: {}", e.getMessage());
            return "根据您的观看历史推荐";
        }
        
        return reason.length() > 0 ? reason.toString() : "根据您的观看历史推荐";
    }
    
    /**
     * 获取每日推荐
     */
    public List<Map<String, Object>> getDailyRecommendations(Integer userId, int limit) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("limit", limit);
            
            List<Map<String, Object>> recommendations = recommendationMapper.getDailyRecommendations(params);
            
            if (recommendations.isEmpty()) {
                // 如果没有推荐结果，重新生成
                return generateRecommendations(userId, limit);
            }
            
            return recommendations;
            
        } catch (Exception e) {
            logger.error("获取每日推荐失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取更多推荐
     */
    public Map<String, Object> getMoreRecommendations(Integer userId, int page, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("offset", (page - 1) * pageSize);
            params.put("limit", pageSize);
            
            List<Map<String, Object>> recommendations = recommendationMapper.getMoreRecommendations(params);
            result.put("list", recommendations);
            
        } catch (Exception e) {
            logger.error("获取更多推荐失败: {}", e.getMessage());
            result.put("list", new ArrayList<>());
        }
        
        return result;
    }
    
    /**
     * 定时更新推荐
     */
    @Scheduled(fixedRate = 300000) // 5分钟
    public void scheduleRecommendationsUpdate() {
        logger.info("开始定时更新推荐");
        try {
            generateRecommendations(1, 20); // 为用户ID=1生成推荐
            logger.info("定时更新推荐完成");
        } catch (Exception e) {
            logger.error("定时更新推荐失败: {}", e.getMessage());
        }
    }
} 