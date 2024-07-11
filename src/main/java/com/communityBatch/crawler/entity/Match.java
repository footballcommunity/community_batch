package com.communityBatch.crawler.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.relational.core.sql.In;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Builder
@Getter
@ToString
public class Match {
    private Long id;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String address;
    private int price;
    private String info;
    private String status;
    private String link;
    private int sex;
    private String type;
    private int total_cnt;
    private int current_cnt;
}
