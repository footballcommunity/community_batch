package com.communityBatch.crawler.configuration;

import com.communityBatch.crawler.entity.Match;
import com.communityBatch.crawler.entity.PlabMatch;
import org.springframework.batch.item.ItemProcessor;

public class PlabToMatchProcessor implements ItemProcessor<PlabMatch, Match> {

    @Override
    public Match process(PlabMatch item) throws Exception {
        return Match.from(item);
    }
}
