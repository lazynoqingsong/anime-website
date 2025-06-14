package com.xiejinxin.service;

import com.xiejinxin.mapper.RankingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 排行榜服务类
 */
@Service
public class RankingService {
    private static final Logger logger = LoggerFactory.getLogger(RankingService.class);

    @Autowired
    private RankingMapper rankingMapper;

    /**
     * 获取周榜数据
     * @param userId 用户ID，可为null
     * @return 排行榜数据
     */
    public List<Map<String, Object>> getWeeklyRanking(Integer userId) {
        try {
            userId = userId == null ? 0 : userId;
            logger.info("获取周榜数据，userId: {}", userId);
            List<Map<String, Object>> ranking = rankingMapper.getWeeklyRanking(userId);
            logger.info("周榜数据获取成功，数量: {}", ranking.size());
            return ranking;
        } catch (Exception e) {
            logger.error("获取周榜数据失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取月榜数据
     * @param userId 用户ID，可为null
     * @return 排行榜数据
     */
    public List<Map<String, Object>> getMonthlyRanking(Integer userId) {
        try {
            userId = userId == null ? 0 : userId;
            logger.info("获取月榜数据，userId: {}", userId);
            List<Map<String, Object>> ranking = rankingMapper.getMonthlyRanking(userId);
            logger.info("月榜数据获取成功，数量: {}", ranking.size());
            return ranking;
        } catch (Exception e) {
            logger.error("获取月榜数据失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取总榜数据
     * @param userId 用户ID，可为null
     * @return 排行榜数据
     */
    public List<Map<String, Object>> getTotalRanking(Integer userId) {
        try {
            userId = userId == null ? 0 : userId;
            logger.info("获取总榜数据，userId: {}", userId);
            List<Map<String, Object>> ranking = rankingMapper.getTotalRanking(userId);
            logger.info("总榜数据获取成功，数量: {}", ranking.size());
            return ranking;
        } catch (Exception e) {
            logger.error("获取总榜数据失败", e);
            return new ArrayList<>();
        }
    }
} 