package com.communityBatch.crawler.utils;

import com.communityBatch.crawler.entity.PlabMatch;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlabApiTest {

    PlabApi plabApi;

    @BeforeEach
    public void set(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        plabApi = new PlabApi(objectMapper);
    }
    @Test
    public void apiTest(){
        // Given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        List<PlabMatch> matches = new ArrayList<>();
        // When
        // Then
        assertDoesNotThrow(() -> plabApi.getAllMatches(formatter.format(now)));
        assertNotNull(matches);
    }

    @Test
    public void dateTimeDeserializeTest() throws IOException {
        // Given
        class Time{
            private LocalDateTime kst;
            private LocalDateTime utc;

            public Time(LocalDateTime kst, LocalDateTime utc){
                this.kst = kst;
                this.utc = utc;
            }

            public void setKst(LocalDateTime kst){
                this.kst = kst;
            }
            public void setUtc(LocalDateTime utc){
                this.utc = utc;
            }
        }
        // GMT+9 한국 표준시
        // UTC
        String kst = "2024-07-01T09:00:00+09:00";
        String utc = "2024-07-01T00:00:00Z";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // When
        // Then
        assertThrows(InvalidFormatException.class,() -> objectMapper.readValue(objectMapper.writeValueAsString(kst), LocalDateTime.class));
        assertDoesNotThrow(() -> objectMapper.readValue(objectMapper.writeValueAsString(kst), ZonedDateTime.class));
        assertDoesNotThrow(() -> objectMapper.readValue(objectMapper.writeValueAsString(utc), LocalDateTime.class));
    }

}