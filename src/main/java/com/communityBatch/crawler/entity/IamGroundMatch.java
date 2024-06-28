package com.communityBatch.crawler.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Builder
@Getter
public class IamGroundMatch {
    private Long id;
    private String title;
    private ZonedDateTime time;
    private String address;
    private int price;
    private String info;
    private String status;
    private String link;
    private int sex;

    @Override
    public String toString() {
        return  "id : " + id + "\n" +
                "title : " + title + "\n" +
                "time : " + time + "\n" +
                "address : " + address + "\n" +
                "price : " + price + "\n" +
                "info : " + info + "\n" +
                "status : " + status + "\n" +
                "link : " + link + "\n";

    }
}
