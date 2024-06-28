package com.communityBatch.crawler.utils;

import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;

import static org.junit.jupiter.api.Assertions.*;

class IamGroundMatchCrawlerTest {

    @Test void crawl(){
        IamGroundMatchCrawler iamGroundMatchCrawler = new IamGroundMatchCrawler();
        iamGroundMatchCrawler.open(new ExecutionContext());

    }
}