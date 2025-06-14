package com.xiejinxin.config;

import com.xiejinxin.service.RecommendationService;
import com.xiejinxin.mapper.UserMapper;
import com.xiejinxin.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    private static final Logger logger = LoggerFactory.getLogger(SchedulingConfig.class);

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 每天凌晨2点更新推荐
     */
    @Scheduled(cron = "0 00 02 * * ?")
    public void updateDailyRecommendations() {
        logger.info("开始执行每日推荐更新任务");
        try {
            // 获取所有用户
            List<User> users = userMapper.selectAll();
            
            // 为每个用户生成推荐
            for (User user : users) {
                try {
                    recommendationService.generateRecommendations(user.getId(), 20);
                    logger.info("用户{}的推荐更新完成", user.getId());
                } catch (Exception e) {
                    logger.error("用户{}的推荐更新失败: {}", user.getId(), e.getMessage());
                }
            }
            
            logger.info("每日推荐更新任务完成");
        } catch (Exception e) {
            logger.error("每日推荐更新任务失败", e);
        }
    }
} 