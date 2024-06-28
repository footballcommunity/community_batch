package com.communityBatch.crawler.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.batch.item.*;

import java.time.Duration;

public class IamGroundMatchCrawler implements ItemStreamReader {

    private WebDriver driver;
    private String url;
    private WebDriverWait wait;

    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static String WEB_DRIVER_PATH = "C:/Users/99san/Downloads/chromedriver-win64 (1)/chromedriver-win64/chromedriver.exe";
    public static Duration DURATION = Duration.ofSeconds(10);

    public IamGroundMatchCrawler(){
        url = "https://m.iamground.kr/futsal/s_match/search";
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        WebDriverManager.chromedriver().capabilities(options).setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver,DURATION);
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("match-item")));
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        ItemStreamReader.super.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        driver.close();
    }

    @Override
    public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        return null;
    }
}
