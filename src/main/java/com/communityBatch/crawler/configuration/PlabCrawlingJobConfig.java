package com.communityBatch.crawler.configuration;

import com.communityBatch.crawler.entity.Match;
import com.communityBatch.crawler.entity.PlabMatch;
import com.communityBatch.crawler.utils.PlabCrwaler;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class PlabCrawlingJobConfig {
    private final JobRepository jobRepository;
    private final DataSource mysqlDataSource;
    private final PlatformTransactionManager platformTransactionManager;
    private static final int CHUNK_SIZE = 10;

    @Bean
    public Job plabCrawl(){
        return new JobBuilder("plabcrwal", jobRepository)
                .start(crawling())
                .build();
    }

    @Bean
    @StepScope
    public PlabCrwaler plabCrwalingReader(){
        return new PlabCrwaler();
    }

    @Bean
    @StepScope
    public ItemProcessor<PlabMatch, Match> plabMatchToMatchProcessor(){
        ItemProcessor<PlabMatch, Match> itemProcessor = new ItemProcessor() {
            @Override
            public Object process(Object item) throws Exception {
                return ((PlabMatch) item).toMatch();
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
                .sql("INSERT INTO `match` (id, title, time, address, price, info, status, link, sex, type, total_cnt, current_cnt) " +
                        "VALUES (:id, :title, :startTime, :address, :price, :info, :status, :link, :sex, :type, :total_cnt, :current_cnt) " +
                        "ON DUPLICATE KEY UPDATE status=VALUES(status);")
                .build();
    }

    @Bean
    @JobScope
    public Step crawling(){
        return new StepBuilder("plabcrawl", jobRepository)
                .<Match, Match>chunk(CHUNK_SIZE, platformTransactionManager)
                .faultTolerant()
                .skipLimit(100)
                .skip(NoSuchElementException.class)
                .skip(ExhaustedRetryException.class)
                .skip(TimeoutException.class)
                .skip(NumberFormatException.class)
                .skip(Exception.class)
                .reader(plabCrwalingReader())
                .processor(plabMatchToMatchProcessor())
                .writer(jdbcItemWriter())
                .build();
    }
}
