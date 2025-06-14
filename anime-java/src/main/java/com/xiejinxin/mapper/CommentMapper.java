package com.xiejinxin.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper {
    void addComment(Map<String, Object> params);
    List<Map<String, Object>> getComments(Map<String, Object> params);
    Integer getCommentCount(Integer animeId);
    
    // 添加获取用户评论列表的方法
    List<Map<String, Object>> getUserComments(Map<String, Object> params);
    Integer getUserCommentCount(Integer userId);
} 