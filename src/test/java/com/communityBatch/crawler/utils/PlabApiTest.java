package com.communityBatch.crawler.utils;

import com.communityBatch.crawler.entity.Match;
import com.communityBatch.crawler.entity.PlabMatch;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlabApiTest {

    @Test
    public void apiTest(){

        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        PlabApi plabApi = new PlabApi(objectMapper);
        String date = "2024-06-18";
        // When
        List<PlabMatch> matchList = plabApi.getAllMatches(date);

        // Then
        assertNotNull(matchList);
    }

}