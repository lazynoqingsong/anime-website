package com.xiejinxin.entity;

import lombok.Data;

@Data
public class AnimeEpisode {
    private Integer id;
    private Integer animeId;
    private Integer episodeNumber;
    private String title;
    private String videoUrl;
    private String duration;
    private String createdAt;
} 