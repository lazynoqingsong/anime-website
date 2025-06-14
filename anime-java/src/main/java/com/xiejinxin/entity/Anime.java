package com.xiejinxin.entity;

import lombok.Data;

@Data
public class Anime {
    private Integer id;
    private String title;
    private String coverImage;
    private String releaseDate;
    private String status;
    private String description;
    private String studio;
    private String broadcastDay;
    private String broadcastTime;
} 