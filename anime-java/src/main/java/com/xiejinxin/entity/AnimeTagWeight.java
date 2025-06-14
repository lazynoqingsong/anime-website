package com.xiejinxin.entity;

import lombok.Data;
//动漫标签权重实体
@Data
public class AnimeTagWeight {
    private Integer id;
    private Integer animeId;
    private String tagName;
    private Double weight;
} 