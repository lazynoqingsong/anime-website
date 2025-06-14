package com.xiejinxin.entity;

import lombok.Data;
import java.time.LocalDateTime;
//用户偏好实体
@Data
public class UserPreference {
    private Integer id;
    private Integer userId;
    private String tagName;
    private Double weight;
    private LocalDateTime updateTime;
} 