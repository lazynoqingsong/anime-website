package com.xiejinxin.service;

import com.xiejinxin.mapper.WatchHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class WatchHistoryService {

    @Autowired
    private WatchHistoryMapper watchHistoryMapper;

    public void addWatchHistory(Map<String, Object> params) {
        watchHistoryMapper.addWatchHistory(params);
    }

    public List<Map<String, Object>> getWatchHistory(Long userId) {
        return watchHistoryMapper.getWatchHistory(userId);
    }

    public void clearWatchHistory(Long userId) {
        watchHistoryMapper.clearWatchHistory(userId);
    }
} 