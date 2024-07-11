package com.communityBatch.crawler.utils;

import com.communityBatch.crawler.entity.PlabMatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlabCrwalerTest {

    @Test
    @DisplayName("1개 플랩 경기 크롤링 테스트")
    void crawlSingleMatchTest() throws Exception {
        // Given
        PlabCrwaler plabCrwaler = new PlabCrwaler();
        plabCrwaler.open(new ExecutionContext());
        // When
        PlabMatch match = (PlabMatch) plabCrwaler.read();
        // Then
        assertEquals(plabCrwaler.getCurNum(), 1);
        assertEquals(plabCrwaler.getCurDay(), 0);
    }

    @Test
    @DisplayName("기존 ExecutionContext 부터 크롤링")
    void crawalFromExecutionContext() throws Exception {
        // Given
        PlabCrwaler plabCrwaler = new PlabCrwaler();
        ExecutionContext executionContext = new ExecutionContext();
        LocalDateTime now = LocalDateTime.now();
        // 두번째 날 100개 까지 이미 처리한 상황
        executionContext.put("curDay", 1);
        executionContext.put("curNum", 100);
        executionContext.put("saveState", true);
        plabCrwaler.open(executionContext);
        // When
        PlabMatch match = (PlabMatch) plabCrwaler.read();
        // Then
        assertEquals(1, plabCrwaler.getCurDay());
        assertEquals(100,plabCrwaler.getCurNum());
        assertEquals(now.plusDays(1).getDayOfMonth(),match.toMatch().getStartTime().getDayOfMonth());
    }

    @Test
    @DisplayName("날짜 넘어가기 테스트")
    void crawlNextDayTest() throws Exception {
        // Given
        PlabCrwaler plabCrwaler = new PlabCrwaler();
        ExecutionContext executionContext = new ExecutionContext();
        LocalDateTime now = LocalDateTime.now();
        // 두번째 날 100개 까지 이미 처리한 상황
        executionContext.put("curDay", 0);
        executionContext.put("curNum", 416);
        executionContext.put("saveState", true);
        plabCrwaler.open(executionContext);
        // When
        PlabMatch match = (PlabMatch) plabCrwaler.read();
        // Then
        assertEquals(1, plabCrwaler.getCurDay());
        assertEquals(1,plabCrwaler.getCurNum());
        assertEquals(now.plusDays(1).getDayOfMonth(),match.toMatch().getStartTime().getDayOfMonth());
    }

    @Test
    @DisplayName("마지막 경기 테스트")
    void crawlLastTest() throws Exception {
        // Given
        PlabCrwaler plabCrwaler = new PlabCrwaler();
        ExecutionContext executionContext = new ExecutionContext();
        LocalDateTime now = LocalDateTime.now();
        // 두번째 날 100개 까지 이미 처리한 상황
        executionContext.put("curDay", 13);
        executionContext.put("curNum", 368);
        executionContext.put("saveState", true);
        plabCrwaler.open(executionContext);
        // When
        PlabMatch match = (PlabMatch) plabCrwaler.read();
        // Then
        assertEquals(null,match);
    }
}