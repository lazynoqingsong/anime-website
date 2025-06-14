package com.xiejinxin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 排行榜数据访问接口
 */
@Mapper
public interface RankingMapper {

    /**
     * 获取周榜数据
     * @param userId 用户ID
     * @return 排行榜列表
     */
    List<Map<String, Object>> getWeeklyRanking(@Param("userId") Integer userId);
    
    /**
     * 获取月榜数据
     * @param userId 用户ID
     * @return 排行榜列表
     */
    List<Map<String, Object>> getMonthlyRanking(@Param("userId") Integer userId);
    
    /**
     * 获取总榜数据
     * @param userId 用户ID
     * @return 排行榜列表
     */
    List<Map<String, Object>> getTotalRanking(@Param("userId") Integer userId);
} 