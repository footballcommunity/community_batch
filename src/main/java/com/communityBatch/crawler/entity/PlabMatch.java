package com.communityBatch.crawler.entity;

import com.communityBatch.crawler.entity.plabEnums.Sex;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlabMatch {
    private static String LINK_URL = "https://www.plabfootball.com/match/";
    private static String TYPE = "PLAB";
    private Long id;
    private String title;
    private String startTime;
    private String hours;
    private String address;
    private String price;
    private String info;
    private String status;
    private String link;
    private String sex;

    public LocalDateTime timeStringToLocalDateTime(){
        // 7월 7일 일요일 14:00
        String [] splitted = startTime.split(" ");
        int year = LocalDateTime.now().getYear();
        int month = Integer.parseInt(splitted[0].substring(0,1));
        int dayOfMonth = Integer.parseInt(splitted[1].substring(0,1));
        int hour = Integer.parseInt(splitted[3].substring(0,2));
        int minute = Integer.parseInt(splitted[3].substring(3,5));
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute);
    }


    public LocalDateTime addHoursTotimeString(){
        LocalDateTime time = timeStringToLocalDateTime();
        time.plusHours(Integer.parseInt(hours.substring(2,3)));
        return time;
    }

    public int parseSex(){
        switch (sex){
            case "남자":
                return 1;
            case "여자":
                return -1;
            case "남녀모두":
                return 0;
            default:
                throw new RuntimeException();
        }
    }

    public String parseStatus(){
        switch (status){
            case "신청가능":
            case "마감임박":
                return "AVAILABLE";
            default:
                return "FULL";
        }
    }


    public Match toMatch(){
        return Match.builder()
                .id(id)
                .title(title)
                .startTime(timeStringToLocalDateTime())
                .endTime(addHoursTotimeString())
                .address(address)
                .price(Integer.parseInt(String.join("",price.substring(0,price.length()-1).split(","))))
                .sex(parseSex())
                .info(info)
                .status(parseStatus())
                .link(LINK_URL+id)
                .type(TYPE)
                .total_cnt(-1)
                .current_cnt(-1)
                .build();
    }
}
