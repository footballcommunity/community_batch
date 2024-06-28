//package com.communityBatch.crawler.configuration;
//
//import com.communityBatch.crawler.entity.IamGroundMatch;
//import com.communityBatch.crawler.entity.Match;
//import com.communityBatch.crawler.entity.PlabMatch;
//import com.communityBatch.crawler.utils.PlabApi;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobScope;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
//import org.springframework.batch.item.support.ListItemReader;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//@RequiredArgsConstructor
//public class IamGroundCrawlingJobConfig {
//    private final ObjectMapper objectMapper;
//    private final JobRepository jobRepository;
//    private final DataSource mysqlDataSource;
//    private final PlatformTransactionManager platformTransactionManager;
//    private static final int chunkSize = 100;
//
//    @Bean
//    public Job iamGrounCrawl(){
//        return new JobBuilder("iamgroundcrawl", jobRepository)
//                .start(useCrawler())
//                .build();
//    }
//
//    @Bean
//    @JobScope
//    public Step useCrawler(){
//        return new StepBuilder("iamgroundcrawling", jobRepository)
//                .<PlabMatch, Match>chunk(chunkSize, platformTransactionManager)
//                .reader(iamgroundMatchReader())
//                .processor(iamGroundMatchMatchItemProcessor())
//                .writer(jdbcItemWriter())
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public ItemReader<IamGroundMatch> iamgroundMatchReader() {
//        // 크롤링
//
//    }
//
//    @Bean
//    @StepScope
//    public ItemProcessor<IamGroundMatch, Match> iamGroundMatchMatchItemProcessor(){
//        return
//    }
//
//
//    @Bean
//    @StepScope
//    public ItemWriter<Match> jdbcItemWriter() {
//        return new JdbcBatchItemWriterBuilder<Match>()
//                .dataSource(mysqlDataSource)
//                .beanMapped()
//                .sql("INSERT INTO `match` (id, title, time, address, price, info, status, link, sex) " +
//                        "VALUES (:id, :title, :time, :address, :price, :info, :status, :link, :sex) " +
//                        "ON DUPLICATE KEY UPDATE status=VALUES(status), info=VALUES(info)")
//                .build();
//    }
//}
