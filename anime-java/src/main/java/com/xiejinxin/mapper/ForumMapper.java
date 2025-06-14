package com.xiejinxin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 论坛数据访问接口
 */
@Mapper
public interface ForumMapper {
    
    /**
     * 获取帖子列表
     */
    List<Map<String, Object>> getPostList(Map<String, Object> params);
    
    /**
     * 获取帖子总数
     */
    Integer getPostCount(Map<String, Object> params);
    
    /**
     * 获取帖子详情
     */
    Map<String, Object> getPostById(@Param("id") Number id);
    
    /**
     * 创建新帖子
     */
    void createPost(Map<String, Object> params);
    
    /**
     * 增加帖子浏览次数
     */
    void increaseViewCount(@Param("id") Number id);
    
    /**
     * 获取帖子回复列表
     */
    List<Map<String, Object>> getRepliesByPostId(@Param("postId") Number postId);
    
    /**
     * 创建回复
     */
    void createReply(Map<String, Object> params);
    
    /**
     * 更新帖子回复数
     */
    void updateReplyCount(@Param("postId") Number postId);
} 