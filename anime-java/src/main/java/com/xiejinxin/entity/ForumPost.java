package com.xiejinxin.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 论坛帖子实体类
 */
@Data
public class ForumPost {
    private Integer id;           // 帖子ID
    private Integer userId;       // 发帖用户ID
    private String title;         // 帖子标题
    private String content;       // 帖子内容
    private LocalDateTime createdTime; // 发布时间
    private Integer viewCount;    // 浏览次数
    private Integer replyCount;   // 回复数量
} 