//package com.communityBatch.crawler.utils;
//
//import com.communityBatch.crawler.entity.Match;
//import lombok.extern.slf4j.Slf4j;
//import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import io.github.bonigarcia.wdm.WebDriverManager;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//public class PlabCrwaler {
//    private WebDriver driver;
//    private String url;
//    private WebDriverWait wait;
//    private List<Match> matches = new ArrayList<>();
//
//    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
//    public static String WEB_DRIVER_PATH = "C:/Users/99san/Downloads/chromedriver-win64 (1)/chromedriver-win64/chromedriver.exe";
//    public static Duration DURATION = Duration.ofSeconds(10);
//
//    public PlabCrwaler(){
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--start-maximized");
//        options.addArguments("--disable-popup-blocking");
////        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
//        WebDriverManager.chromedriver().capabilities(options).setup();
//        driver = new ChromeDriver();
//        wait = new WebDriverWait(driver,DURATION);
//        url = "https://www.plabfootball.com/";
//    }
//
//    public List<Match> getMatches() {
//        return matches;
//    }
//
//    public void activate() {
//        try {
//            log.info(url);
//            driver.get(url);
//            // 요일 선택
//            // 현재 날짜 부터 2주 뒤 까지 ex) 6/5 ~ 6/18
//            // 로딩창 없어질 때 까지 wait
//            wait.until(ExpectedConditions.attributeContains(By.id("fullLoader"), "style", "display: none;"));
//            List<WebElement> dates = driver.findElements(By.id("datewrap"));
//            for(int i = 0; i < dates.size(); i++){
//                WebElement date = driver.findElements(By.id("datewrap")).get(i);
//                date.click();
//                matches = getMatches(matches);
//            }
//            log.info("total {} matches crawled", matches.size());
//        }catch (Exception e) {
//            log.error(e.getMessage());
//            e.printStackTrace();
//        } finally {
//            driver.close();
//        }
//    }
//
//    private List<Match> getMatches(List<Match> matches) {
//        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("li")));
//
//        WebElement element = driver.findElement(By.id("list"));
//        List<WebElement> matchList = element.findElements(By.tagName("li"));
//
//        log.info("total {} matches detected", matchList.size());
//        for(int i = 0; i < matchList.size(); i++){
//            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("list--match-schedule--item")));
//            List<WebElement> elements = driver.findElement(By.id("list")).findElements(By.tagName("li"));
//
//            log.info("total {} matches was detected", matchList.size());
//            log.info("total {} matches redetected", elements.size());
//            WebElement match = elements.get(i);
//            wait.until(ExpectedConditions.elementToBeClickable(match));
//            By findStatus = By.className("list--match-schedule__status");
//            By findTitle = By.className("match-list__title");
//            wait.until(ExpectedConditions.presenceOfElementLocated(findStatus));
//            wait.until(ExpectedConditions.presenceOfElementLocated(findTitle));
//            String status = match.findElement(findStatus).getText();
//            String title = match.findElement(findTitle).getText();
//            log.info("{} detected",title);
//            Point location = match.getLocation();
//            WebElement body = driver.findElement(By.tagName("body"));
//            ((JavascriptExecutor)driver).executeScript("window.scrollTo(0,"+ location.x + ")",body);
//            match.click();
//            // 티셔트 매치 예외 처리
//            try {
//                crawlMatch(matches, status, title,false);
//            } catch (NoSuchElementException e){
//                log.info("Tshirt match exception");
//                // modal--container에 동일한 display: none 인 태그가 여러개 있음
//                // 그 중 display: none이 아닌 태그의 x버튼 클릭
//                List<WebElement> matchInfos = driver.findElements(By.className("modal--container"));
//                for(WebElement matchInfo : matchInfos){
//                    if(!matchInfo.getAttribute("style").equals("display: none;")){
//                        matchInfo.findElement(By.className("modal--close-white")).click();
//                        break;
//                    }
//                }
//                crawlMatch(matches, status, title, true);
//            } catch (TimeoutException e){
//                e.printStackTrace();
//                log.info("Retry");
//                driver.navigate().refresh();
//                crawlMatch(matches, status, title,true);
//            }
//        }
//        return matches;
//    }
//
//    private void crawlMatch(List<Match> matches, String status, String title, boolean isTshirt) {
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("section-pc")));
//        WebElement matchInfo = driver.findElement(By.className("section-pc"));
//        String info = "";
//        if(isTshirt){
//            info = "티셔츠 매치";
//        } else {
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mnRule")));
//            info = driver.findElement(By.id("mnRule")).getText();
//        }
//
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("matchTime")));
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("stadium-info__address")));
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("matchFee__money")));
//        Match curMatch = new Match.MatchBuilder()
//                .id(Long.valueOf(driver.getCurrentUrl().split("/")[4]))
//                .title(title)
//                .time(matchInfo.findElement(By.className("matchTime")).getText())
//                .address(matchInfo.findElement(By.className("stadium-info__address")).getText())
//                .price(matchInfo.findElement(By.className("matchFee__money")).getText())
//                .info(info)
//                .status(status)
//                .link(driver.getCurrentUrl())
//                .build();
//        matches.add(curMatch);
//        log.info("saved {}", curMatch.toString());
//
//        driver.navigate().back();
//    }
//}
