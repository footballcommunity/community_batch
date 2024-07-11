package com.communityBatch.crawler.utils;

import com.communityBatch.crawler.entity.Match;
import com.communityBatch.crawler.entity.PlabMatch;
import com.communityBatch.crawler.entity.plabEnums.Sex;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.batch.item.*;

import java.sql.Driver;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class PlabCrwaler implements ItemStreamReader {
    private String url;
    private WebDriver driver;
    private WebDriverWait wait;

    private int curDay; // 0 ~ 13
    private int curNum; // 0 ~ 경기 수
    private static Duration DURATION = Duration.ofSeconds(10);


    public PlabCrwaler(){
        url = "https://www.plabfootball.com/";
    }

    public ChromeOptions getOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--blink-settings=imagesEnabled=false");
        options.addArguments("--headless");

        return options;
    }

    // 크롤러 실행
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        driver = new ChromeDriver(getOptions());
        wait = new WebDriverWait(driver,DURATION);

        // 총 14일
        if(executionContext.containsKey("saveState")){
            this.curDay = executionContext.getInt("curDay");
            this.curNum = executionContext.getInt("curNum");
        } else {
            this.curDay = 0;
            this.curNum = 0;
        }
    }
    // Batch 실행 상태 update
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        if(!executionContext.containsKey("saveState")){
            executionContext.put("saveState", true);
        }
        executionContext.put("curNum", this.curNum);
        executionContext.put("curDay", this.curDay);
    }
    //
    @Override
    public void close() throws ItemStreamException {
        driver.close();
    }
    public void clickDate(WebElement date) throws InterruptedException {
        int DATE_NUM = 6;
        try {
            date.click();
        } catch (ElementNotInteractableException e){
            WebElement next = driver.findElement(By.className("slick-next"));
            for(int i = DATE_NUM; i < this.curDay; i++){
                log.info("cur : {}",i);
                next.click();
                wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.id("datewrap")).get(i+1)));
                log.info(driver.findElements(By.id("datewrap")).toString());
            }
            wait.until(ExpectedConditions.elementToBeClickable(next));
            date.click();
        }
    }
    // 한 개의 Match 읽기
    @Override
    public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        driver.get(url);
        // 요일 선택
        // 현재 날짜 부터 2주 뒤 까지 ex) 6/5 ~ 6/18
        // 로딩창 없어질 때 까지 wait
        wait.until(ExpectedConditions.attributeContains(By.id("fullLoader"), "style", "display: none;"));
        // 현재 날짜로 이동
        WebElement date = driver.findElements(By.id("datewrap")).get(this.curDay);
        clickDate(date);

        // List 로딩
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("li")));
        WebElement element = driver.findElement(By.id("list"));
        List<WebElement> matchList = element.findElements(By.tagName("li"));
        log.info("total {} matches detected", matchList.size());
        // 다음 날짜로 이동
        if(matchList.size() == this.curNum){
            this.curDay += 1;
            this.curNum = 0;
            // item exhausted
            if(this.curDay == 14){
                return null;
            }
            WebElement nextDate = driver.findElements(By.id("datewrap")).get(this.curDay);
            clickDate(nextDate);
        }

        log.info("Reading -> curDay : {}, curNum : {}",this.curDay, this.curNum);
        // Read
        try {
            PlabMatch match = getMatch();
            return match;
        } finally {
            this.curNum += 1;
        }
    }

    private PlabMatch getMatch(){
        // List 로딩
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("li")));
        WebElement element = driver.findElement(By.id("list"));
        List<WebElement> matchList = element.findElements(By.tagName("li"));

        // this.curNum 다음 걸 읽어야 함
        WebElement match = matchList.get(this.curNum);
        wait.until(ExpectedConditions.elementToBeClickable(match));
        By findStatus = By.className("list--match-schedule__status");
        By findTitle = By.className("match-list__title");
        By findSex = By.className("match--option");
        wait.until(ExpectedConditions.presenceOfElementLocated(findStatus));
        wait.until(ExpectedConditions.presenceOfElementLocated(findTitle));
        // 밖에 정보 읽기
        String status = match.findElement(findStatus).getText();
        String title = match.findElement(findTitle).getText();
        String sex = match.findElement(findSex).getText();
        // 스크롤 위치로 이동
        Point location = match.getLocation();
        Actions actions = new Actions(driver);
        actions.moveToLocation(location.x,location.y);
        match.click();
        // 티셔트 매치 예외 처리
        PlabMatch curMatch;
        try {
            curMatch = getMatchDetails(status, title,false, sex);
        } catch (NoSuchElementException e) {
            log.info("Tshirt match exception");
            // modal--container에 동일한 display: none 인 태그가 여러개 있음
            // 그 중 display: none이 아닌 태그의 x버튼 클릭
            List<WebElement> matchInfos = driver.findElements(By.className("modal--container"));
            for (WebElement matchInfo : matchInfos) {
                if (!matchInfo.getAttribute("style").equals("display: none;")) {
                    matchInfo.findElement(By.className("modal--close-white")).click();
                    break;
                }
            }
            curMatch = getMatchDetails(status, title,false, sex);
        } catch (TimeoutException e){
            this.curNum += 1;
            throw e;
        }
        log.info(curMatch.toString());
        return curMatch;
    }

    private PlabMatch getMatchDetails(String status, String title, boolean isTshirt, String sex){
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("section-pc")));
        WebElement matchInfo = driver.findElement(By.className("section-pc"));
        String info = "";
        if(isTshirt){
            info = "티셔츠 매치";
        } else {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mnRule")));
            info = driver.findElement(By.id("mnRule")).getText();
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("matchTime")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("stadium-info__address")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("matchFee__money")));
        PlabMatch curMatch = PlabMatch.builder()
                .id(Long.valueOf(driver.getCurrentUrl().split("/")[4]))
                .title(title)
                .startTime(matchInfo.findElement(By.className("matchTime")).getText())
                .hours(matchInfo.findElement(By.className("matchFee")).findElements(By.tagName("span")).get(1).getText())
                .address(matchInfo.findElement(By.className("stadium-info__address")).getText())
                .price(matchInfo.findElement(By.className("matchFee__money")).getText())
                .info(info)
                .status(status)
                .sex(sex)
                .link(driver.getCurrentUrl())
                .build();
        return curMatch;
    }
}
