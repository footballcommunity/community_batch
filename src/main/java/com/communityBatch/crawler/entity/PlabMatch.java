package com.communityBatch.crawler.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlabMatch {
    private Long id;
    private ZonedDateTime schedule;
    private String label_title;
    private int sex;
    private int level;
    private int player_cnt;
    private String inout_door;
    private boolean is_add;
    private boolean is_rec;
    private boolean is_new_stadium;
    private String type;
    private String test_type;
    private int confirm_cnt;
    private int max_player_cnt;
    private int min_player_cnt;
    private int fee;
    private int playtime;
    private int playminute;
    private boolean is_manager_free;
    private String intro;
    private String label_stadium;
    private String label_stadium2;
    private ZonedDateTime start_dt;
    private ZonedDateTime end_dt;
    private String stud;
    private String grade;
    private String label_manager_name;
    private boolean is_asd;
    private boolean is_replab;
    private boolean is_earlybird;
    private boolean is_super_sub;
    private boolean is_coupon;
    private boolean is_timesale;
    private boolean is_apply;
    private String available_day;
    private boolean is_parking_apply;
    private int parking_cnt;
    private boolean is_finish;
    private String status;
    private String apply_status;
    private String label_title2;
    private String add_title;
    private String label_schedule9;
    private Long stadium_group_id;
    private String stadium_group_name;
    private String area_group_name;
    private String area_name;
    private Long manager_id;
    private String product_type;

    @Override
    public String toString() {
        return "PlabMatch{" +
                "id=" + id +
                ", schedule=" + schedule +
                ", label_title='" + label_title + '\'' +
                ", sex=" + sex +
                ", level=" + level +
                ", player_cnt=" + player_cnt +
                ", inout_door='" + inout_door + '\'' +
                ", is_add=" + is_add +
                ", is_rec=" + is_rec +
                ", is_new_stadium=" + is_new_stadium +
                ", type='" + type + '\'' +
                ", test_type='" + test_type + '\'' +
                ", confirm_cnt=" + confirm_cnt +
                ", max_player_cnt=" + max_player_cnt +
                ", min_player_cnt=" + min_player_cnt +
                ", fee=" + fee +
                ", playtime=" + playtime +
                ", playminute=" + playminute +
                ", is_manager_free=" + is_manager_free +
                ", intro='" + intro + '\'' +
                ", label_stadium='" + label_stadium + '\'' +
                ", label_stadium2='" + label_stadium2 + '\'' +
                ", start_dt=" + start_dt +
                ", end_dt=" + end_dt +
                ", stud='" + stud + '\'' +
                ", grade='" + grade + '\'' +
                ", label_manager_name='" + label_manager_name + '\'' +
                ", is_asd=" + is_asd +
                ", is_replab=" + is_replab +
                ", is_earlybird=" + is_earlybird +
                ", is_super_sub=" + is_super_sub +
                ", is_coupon=" + is_coupon +
                ", is_timesale=" + is_timesale +
                ", is_apply=" + is_apply +
                ", available_day='" + available_day + '\'' +
                ", is_parking_apply=" + is_parking_apply +
                ", parking_cnt=" + parking_cnt +
                ", is_finish=" + is_finish +
                ", status='" + status + '\'' +
                ", apply_status='" + apply_status + '\'' +
                ", label_title2='" + label_title2 + '\'' +
                ", add_title='" + add_title + '\'' +
                ", label_schedule9='" + label_schedule9 + '\'' +
                ", stadium_group_id=" + stadium_group_id +
                ", stadium_group_name='" + stadium_group_name + '\'' +
                ", area_group_name='" + area_group_name + '\'' +
                ", area_name='" + area_name + '\'' +
                ", manager_id=" + manager_id +
                ", product_type='" + product_type + '\'' +
                '}';
    }
}
