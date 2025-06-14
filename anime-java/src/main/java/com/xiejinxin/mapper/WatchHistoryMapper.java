package com.xiejinxin.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface WatchHistoryMapper {
    void addWatchHistory(Map<String, Object> params);
    List<Map<String, Object>> getWatchHistory(Long userId);
    void clearWatchHistory(Long userId);
} 