package com.xiejinxin.controller;

import com.xiejinxin.common.Result;
import com.xiejinxin.service.ForumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 论坛控制器
 */
@RestController
@RequestMapping("/forum")
@CrossOrigin
public class ForumController {
    private static final Logger logger = LoggerFactory.getLogger(ForumController.class);

    @Autowired
    private ForumService forumService;

    /**
     * 获取帖子列表
     */
    @GetMapping("/posts")
    public Result getPostList(
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false, defaultValue = "latest") String sortBy
    ) {
        logger.info("获取帖子列表请求，页码: {}, 每页数量: {}, 排序: {}", page, pageSize, sortBy);
        try {
            Map<String, Object> result = forumService.getPostList(page, pageSize, sortBy);
            return Result.success(result);
        } catch (Exception e) {
            logger.error("获取帖子列表失败", e);
            return Result.error("获取帖子列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取帖子详情
     */
    @GetMapping("/posts/{id}")
    public Result getPostDetail(@PathVariable Object id) {
        logger.info("获取帖子详情请求，ID: {}", id);
        try {
            Map<String, Object> post = forumService.getPostDetail(id);
            if (post == null) {
                return Result.error("帖子不存在");
            }
            return Result.success(post);
        } catch (Exception e) {
            logger.error("获取帖子详情失败", e);
            return Result.error("获取帖子详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建新帖子
     */
    @PostMapping("/posts")
    public Result createPost(@RequestBody Map<String, Object> params) {
        logger.info("接收到创建帖子请求，参数：{}", params);
        try {
            Object userId = params.get("userId");
            String title = params.get("title").toString();
            String content = params.get("content").toString();
            
            logger.info("准备创建帖子，用户ID：{}, 标题：{}", userId, title);
            
            forumService.createPost(userId, title, content);
            return Result.success("帖子发布成功");
        } catch (Exception e) {
            logger.error("创建帖子失败", e);
            return Result.error("创建帖子失败: " + e.getMessage());
        }
    }

    /**
     * 获取帖子回复列表
     */
    @GetMapping("/replies/{postId}")
    public Result getReplies(@PathVariable Object postId) {
        logger.info("获取回复列表请求，帖子ID: {}", postId);
        try {
            List<Map<String, Object>> replies = forumService.getReplies(postId);
            return Result.success(replies);
        } catch (Exception e) {
            logger.error("获取回复列表失败", e);
            return Result.error("获取回复列表失败: " + e.getMessage());
        }
    }

    /**
     * 创建回复
     */
    @PostMapping("/replies")
    public Result createReply(@RequestBody Map<String, Object> params) {
        logger.info("接收到创建回复请求，参数：{}", params);
        try {
            Object postId = params.get("postId");
            Object userId = params.get("userId");
            String content = params.get("content").toString();
            
            logger.info("准备创建回复，帖子ID：{}, 用户ID：{}", postId, userId);
            
            forumService.createReply(postId, userId, content);
            return Result.success("回复发布成功");
        } catch (Exception e) {
            logger.error("创建回复失败", e);
            return Result.error("创建回复失败: " + e.getMessage());
        }
    }
} 