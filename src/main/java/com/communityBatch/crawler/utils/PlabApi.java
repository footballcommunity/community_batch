package com.communityBatch.crawler.utils;

import com.communityBatch.crawler.entity.Match;
import com.communityBatch.crawler.entity.PlabMatch;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PlabApi {
    private static String URL = "https://www.plabfootball.com/api/v2/integrated-matches/?page_size=700&ordering=schedule&sch=";
    @Autowired
    private final ObjectMapper objectMapper;
    private class PlabResponseHandler implements HttpClientResponseHandler<List<PlabMatch>>{

        @Override
        public List<PlabMatch> handleResponse(ClassicHttpResponse response) throws HttpException, IOException {
            int status = response.getCode();
            if (status == 200){
                HttpEntity entity = response.getEntity();
                if(entity == null){
                    return new ArrayList<>();
                }
                return objectMapper.readValue(entity.getContent(), new TypeReference<>(){});
            } else{
                log.error("http failed with status {}", status);
                log.error("msg : {}", response.getEntity().getContent());
                throw new HttpException();
            }
        }
    }

    public List<PlabMatch> getAllMatches(String date){
        //api 연결
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(URL+date);
        PlabResponseHandler plabResponseHandler = new PlabResponseHandler();
        try {
            return client.execute(getRequest, plabResponseHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
