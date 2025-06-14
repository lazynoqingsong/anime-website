package com.xiejinxin.controller;

import com.xiejinxin.common.Result;
import com.xiejinxin.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/comments")
@CrossOrigin
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Result addComment(@RequestBody Map<String, Object> params) {
        logger.info("接收到添加评论请求，参数：{}", params);
        try {
            Integer animeId = Integer.parseInt(params.get("animeId").toString());
            Integer userId = Integer.parseInt(params.get("userId").toString());
            String content = params.get("content").toString();
            
            logger.info("解析后的参数：animeId={}, userId={}, content={}", animeId, userId, content);
            
            commentService.addComment(animeId, userId, content);
            logger.info("评论添加成功");
            return Result.success();
        } catch (Exception e) {
            logger.error("发表评论失败", e);
            return Result.error("发表评论失败: " + e.getMessage());
        }
    }

    @GetMapping
    public Result getComments(
        @RequestParam Integer animeId,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        logger.info("接收到获取评论请求，参数：animeId={}, page={}, pageSize={}", animeId, page, pageSize);
        try {
            Map<String, Object> result = commentService.getComments(animeId, page, pageSize);
            logger.info("获取评论成功，总数：{}", result.get("total"));
            return Result.success(result);
        } catch (Exception e) {
            logger.error("获取评论失败", e);
            return Result.error("获取评论失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户评论列表
     */
    @GetMapping("/user")
    public Result getUserComments(
        @RequestParam Integer userId,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        logger.info("接收到获取用户评论请求，参数：userId={}, page={}, pageSize={}", userId, page, pageSize);
        try {
            Map<String, Object> result = commentService.getUserComments(userId, page, pageSize);
            logger.info("获取用户评论成功，总数：{}", result.get("total"));
            return Result.success(result);
        } catch (Exception e) {
            logger.error("获取用户评论失败", e);
            return Result.error("获取用户评论失败: " + e.getMessage());
        }
    }
} 