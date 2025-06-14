package com.xiejinxin.service;

import com.xiejinxin.mapper.ForumMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 论坛服务类
 */
@Service
public class ForumService {
    private static final Logger logger = LoggerFactory.getLogger(ForumService.class);

    @Autowired
    private ForumMapper forumMapper;

    /**
     * 获取帖子列表
     */
    public Map<String, Object> getPostList(int page, int pageSize, String sortBy) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", (page - 1) * pageSize);
        params.put("limit", pageSize);
        params.put("sortBy", sortBy);
        
        List<Map<String, Object>> posts = forumMapper.getPostList(params);
        Integer total = forumMapper.getPostCount(params);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", posts);
        result.put("total", total);
        
        logger.info("获取帖子列表成功，总数: {}, 当前页数量: {}", total, posts.size());
        return result;
    }

    /**
     * 获取帖子详情
     */
    @Transactional
    public Map<String, Object> getPostDetail(Object id) {
        // 安全地处理id
        Integer safeId;
        if (id instanceof Integer) {
            safeId = (Integer) id;
        } else if (id instanceof Long) {
            safeId = ((Long) id).intValue();
        } else if (id instanceof BigInteger) {
            safeId = ((BigInteger) id).intValue();
        } else if (id instanceof String) {
            safeId = Integer.parseInt((String) id);
        } else {
            safeId = Integer.parseInt(id.toString());
        }
        
        // 增加浏览次数
        forumMapper.increaseViewCount(safeId);
        
        // 获取帖子详情
        Map<String, Object> post = forumMapper.getPostById(safeId);
        
        if (post == null) {
            logger.warn("帖子不存在，ID: {}", safeId);
            return null;
        }
        
        logger.info("获取帖子详情成功，ID: {}", safeId);
        return post;
    }

    /**
     * 创建新帖子
     */
    public void createPost(Object userId, String title, String content) {
        // 安全地处理userId，避免类型转换问题
        Long safeUserId = null;
        if (userId instanceof Integer) {
            safeUserId = ((Integer) userId).longValue();
        } else if (userId instanceof Long) {
            safeUserId = (Long) userId;
        } else if (userId instanceof BigInteger) {
            safeUserId = ((BigInteger) userId).longValue();
        } else if (userId instanceof String) {
            safeUserId = Long.parseLong((String) userId);
        } else {
            safeUserId = Long.parseLong(userId.toString());
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("userId", safeUserId);
        params.put("title", title);
        params.put("content", content);
        
        forumMapper.createPost(params);
        
        logger.info("创建帖子成功，用户ID: {}", safeUserId);
    }

    /**
     * 获取帖子回复列表
     */
    public List<Map<String, Object>> getReplies(Object postId) {
        // 安全地处理postId
        Integer safePostId;
        if (postId instanceof Integer) {
            safePostId = (Integer) postId;
        } else if (postId instanceof Long) {
            safePostId = ((Long) postId).intValue();
        } else if (postId instanceof BigInteger) {
            safePostId = ((BigInteger) postId).intValue();
        } else if (postId instanceof String) {
            safePostId = Integer.parseInt((String) postId);
        } else {
            safePostId = Integer.parseInt(postId.toString());
        }
        
        List<Map<String, Object>> replies = forumMapper.getRepliesByPostId(safePostId);
        logger.info("获取帖子回复列表成功，帖子ID: {}, 回复数量: {}", safePostId, replies.size());
        return replies;
    }

    /**
     * 创建回复
     */
    @Transactional
    public void createReply(Object postId, Object userId, String content) {
        // 安全地处理postId
        Long safePostId = null;
        if (postId instanceof Integer) {
            safePostId = ((Integer) postId).longValue();
        } else if (postId instanceof Long) {
            safePostId = (Long) postId;
        } else if (postId instanceof BigInteger) {
            safePostId = ((BigInteger) postId).longValue();
        } else if (postId instanceof String) {
            safePostId = Long.parseLong((String) postId);
        } else {
            safePostId = Long.parseLong(postId.toString());
        }
        
        // 安全地处理userId
        Long safeUserId = null;
        if (userId instanceof Integer) {
            safeUserId = ((Integer) userId).longValue();
        } else if (userId instanceof Long) {
            safeUserId = (Long) userId;
        } else if (userId instanceof BigInteger) {
            safeUserId = ((BigInteger) userId).longValue();
        } else if (userId instanceof String) {
            safeUserId = Long.parseLong((String) userId);
        } else {
            safeUserId = Long.parseLong(userId.toString());
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("postId", safePostId);
        params.put("userId", safeUserId);
        params.put("content", content);
        
        forumMapper.createReply(params);
        forumMapper.updateReplyCount(safePostId.intValue());
        
        logger.info("创建回复成功，帖子ID: {}, 用户ID: {}", safePostId, safeUserId);
    }
} 