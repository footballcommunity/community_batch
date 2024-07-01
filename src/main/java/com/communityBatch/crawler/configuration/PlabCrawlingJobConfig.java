package com.communityBatch.crawler.configuration;

import com.communityBatch.crawler.entity.Match;
import com.communityBatch.crawler.entity.PlabMatch;
import com.communityBatch.crawler.utils.PlabApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class PlabCrawlingJobConfig {
    private final ObjectMapper objectMapper;
    private final JobRepository jobRepository;
    private final DataSource mysqlDataSource;
    private final PlatformTransactionManager platformTransactionManager;
    private static final int chunkSize = 100;

    @Bean
    public Job plabCrawl(){
        return new JobBuilder("plabcrwal", jobRepository)
                .start(useApi())
                .build();
    }

    @Bean
    @JobScope
    public Step useApi(){
        return new StepBuilder("plabapi", jobRepository)
                .<PlabMatch, Match>chunk(chunkSize, platformTransactionManager)
                .reader(plabMatchReaderFromApi())
                .processor(plabMatchToMatchProcessor())
                .writer(jdbcItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<PlabMatch> plabMatchReaderFromApi() {
        PlabApi plabApi = new PlabApi(objectMapper);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        List<PlabMatch> matches = new ArrayList<>();
        for(int i = 0; i < 14; i++){
            LocalDate next = now.plusDays(i);
            plabApi.getAllMatches(formatter.format(next)).stream().forEach(match -> {
                if(match.getLabel_stadium() != null) {
                    matches.add(match);
                }
            });
        }
        return new ListItemReader<>(matches);
    }

    @Bean
    @StepScope
    public ItemProcessor<PlabMatch, Match> plabMatchToMatchProcessor(){
        ItemProcessor<PlabMatch, Match> itemProcessor = new ItemProcessor() {
            @Override
            public Object process(Object item) throws Exception {
                return Match.from((PlabMatch) item);
            }
        };
        return itemProcessor;
    }


    @Bean
    @StepScope
    public ItemWriter<Match> jdbcItemWriter() {
        return new JdbcBatchItemWriterBuilder<Match>()
                .dataSource(mysqlDataSource)
                .beanMapped()
                .sql("INSERT INTO `match` (title, time, address, price, info, status, link, sex, type) " +
                        "VALUES (:title, :time, :address, :price, :info, :status, :link, :sex, :type) " +
                        "ON DUPLICATE KEY UPDATE status=VALUES(status), info=VALUES(info)")
                .build();
    }

//    @Bean
//    @JobScope
//    public Step crawling(){
//        return new StepBuilder("plabcrawl", jobRepository)
//                .<Match, Match>chunk(chunkSize, platformTransactionManager)
//                .reader(plabMatchReader())
//                .writer(jdbcItemWriter())
//                .build();
//    }

//    @Bean
//    @StepScope
//    public ItemReader<Match> plabMatchReader() {
//        PlabCrwaler plabCrwaler = new PlabCrwaler();
//        plabCrwaler.activate();
//        List<Match> matches = plabCrwaler.getMatches();
//        return new ListItemReader<>(matches);
//    }


}
