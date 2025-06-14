package com.xiejinxin.service;

import com.xiejinxin.mapper.CommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentMapper commentMapper;

    public void addComment(Integer animeId, Integer userId, String content) {
        logger.info("准备添加评论，参数：animeId={}, userId={}, content={}", animeId, userId, content);
        Map<String, Object> params = new HashMap<>();
        params.put("animeId", animeId);
        params.put("userId", userId);
        params.put("content", content);
        commentMapper.addComment(params);
        logger.info("评论添加成功");
    }

    public Map<String, Object> getComments(Integer animeId, Integer page, Integer pageSize) {
        logger.info("准备获取评论，参数：animeId={}, page={}, pageSize={}", animeId, page, pageSize);
        
        Map<String, Object> params = new HashMap<>();
        params.put("animeId", animeId);
        params.put("offset", (page - 1) * pageSize);
        params.put("limit", pageSize);

        List<Map<String, Object>> comments = commentMapper.getComments(params);
        Integer total = commentMapper.getCommentCount(animeId);

        Map<String, Object> result = new HashMap<>();
        result.put("list", comments);
        result.put("total", total);
        
        logger.info("获取评论成功，总数：{}，当前页评论数：{}", total, comments.size());
        return result;
    }

    /**
     * 获取用户评论列表
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 评论列表和总数
     */
    public Map<String, Object> getUserComments(Integer userId, Integer page, Integer pageSize) {
        logger.info("获取用户评论列表，参数：userId={}, page={}, pageSize={}", userId, page, pageSize);
        
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", (page - 1) * pageSize);
        params.put("limit", pageSize);

        List<Map<String, Object>> comments = commentMapper.getUserComments(params);
        Integer total = commentMapper.getUserCommentCount(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("list", comments);
        result.put("total", total);
        
        logger.info("获取用户评论成功，总数：{}，当前页评论数：{}", total, comments.size());
        return result;
    }
} 