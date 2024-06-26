package com.communityBatch.crawler.entity;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Builder
@Getter
public class Match {
    private Long id;
    private String title;
    private LocalDateTime time;
    private String address;
    private int price;
    private String info;
    private String status;
    private String link;
    private int sex;
    private String type;
    private int total_cnt;
    private int current_cnt;
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
    public static Match from(PlabMatch plabMatch){
        String LINK_URL = "https://www.plabfootball.com/match/";
        String info = "인원 : " + (plabMatch.getMax_player_cnt() - plabMatch.getConfirm_cnt()) + "\n" +
                       "경기 : " + plabMatch.getPlayer_cnt() + "vs" + plabMatch.getPlayer_cnt() + "\n" +
                        plabMatch.getType() + "파전" + "\n" +
                        plabMatch.getGrade();
        return Match.builder()
                .id(plabMatch.getId())
                .title(plabMatch.getLabel_title())
                .time(plabMatch.getStart_dt())
                .address(plabMatch.getLabel_stadium())
                .price(plabMatch.getFee())
                .sex(plabMatch.getSex())
                .info(info)
                .status(plabMatch.getApply_status().toUpperCase())
                .link(LINK_URL+plabMatch.getId())
                .type("PLAB")
                .total_cnt(plabMatch.getMax_player_cnt())
                .current_cnt(plabMatch.getConfirm_cnt())
                .build();
    }
//    public static Match from(IamGroundMatch iamGroundMatch){
//        String LINK_URL = "https://m.iamground.kr/futsal/s_match/detail/";
//        return Match.builder()
//                .link(LINK_URL)
//                .build();
//    }
}
