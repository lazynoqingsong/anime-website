package com.xiejinxin.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 论坛回复实体类
 */
@Data
public class ForumReply {
    private Integer id;           // 回复ID
    private Integer postId;       // 关联的帖子ID
    private Integer userId;       // 回复用户ID
    private String content;       // 回复内容
    private LocalDateTime createdTime; // 回复时间
} 