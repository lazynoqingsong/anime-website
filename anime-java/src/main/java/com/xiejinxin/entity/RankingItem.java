package com.xiejinxin.entity;

import lombok.Data;

/**
 * 排行榜项目实体类
 */
@Data
public class RankingItem {
    private Integer id;           // 动漫ID
    private String title;         // 动漫标题
    private String coverImage;    // 封面图片
    private Integer viewCount;    // 观看次数
    private Integer commentCount; // 评论数
    private Integer favoriteCount; // 收藏数
    private Double heatScore;     // 热度分数
    private Boolean isWatched;    // 用户是否已观看
} 