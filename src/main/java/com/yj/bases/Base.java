package com.yj.bases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * 基类
 */
public class Base {
    public  static WebDriver driver;

    @BeforeSuite
    @Parameters(value = {"browserType","driverPath"})
    public void init(String browserType,String driverPath){
        if("ie".equalsIgnoreCase(browserType)){
            System.setProperty("webdriver.ie.driver",driverPath);
            //创建对象，保存浏览器的设置信息
            DesiredCapabilities capabilities=new DesiredCapabilities();
            //设置浏览器的安全设置
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
            //设置浏览器的缩放忽略
            capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING,true);
            driver=new InternetExplorerDriver(capabilities);

        }else if("chrome".equalsIgnoreCase(browserType)){
            System.setProperty("webdriver.chrome.driver",driverPath);
            //创建对象，保存浏览器的设置信息
            DesiredCapabilities capabilities=new DesiredCapabilities();
            //设置浏览器的安全设置
            //设置浏览器的缩放忽略
            driver=new ChromeDriver(capabilities);

        }else if("firefox".equalsIgnoreCase(browserType)){
            System.setProperty("webdriver.firefox.driver",driverPath);
            //创建对象，保存浏览器的设置信息
            DesiredCapabilities capabilities=new DesiredCapabilities();
            //设置浏览器的安全设置
            //设置浏览器的缩放忽略
            driver=new FirefoxDriver(capabilities);

        }



    }

    @AfterSuite
    public void tearDown() throws InterruptedException{
//        Thread.sleep(3000);
//        driver.quit();//关闭所有窗口,并关闭驱动
    }

    /**
     * 显示等待（只能等待）
     * @param by
     * @return
     */
    public WebElement getElement(By by){
        WebDriverWait wait =new WebDriverWait(driver,30);
        try{
            WebElement webElement=wait.until(ExpectedConditions.presenceOfElementLocated(by));
            return webElement;
        }catch (Exception e){
            Reporter.log("定位元素超时了");
//            e.printStackTrace();
        }
        return  null;
    }
}
