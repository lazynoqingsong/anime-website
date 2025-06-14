package com.xiejinxin.entity;

import lombok.Data;
import java.time.LocalDateTime;

//推荐记录实体
@Data
public class RecommendationEntity {
    private Integer id;
    private Integer userId;
    private Integer animeId;
    private Double score;        // 推荐分数
    private String reason;       // 推荐原因
    private LocalDateTime createTime;
    private Boolean isShown;     // 是否已展示
} 