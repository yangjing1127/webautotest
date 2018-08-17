package com.yj.cases;

import com.yj.bases.Base;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegisterCase extends Base {


    @Test
    public void failCase(){
        driver.navigate().to("http://39.108.136.60:8085/lmcanon_web_auto/mng/register.html");
        driver.findElement(By.id("signup-button")).click();;
        String errorMsg=driver.findElement(By.className("tips")).getText();
        Assert.assertEquals(errorMsg,"用户名不能为空");
    }

    /**
     * 测试失败的用例
     */
    @Test
    public void failCase1(){
        driver.navigate().to("http://39.108.136.60:8085/lmcanon_web_auto/mng/register.html");
        driver.findElement(By.id("signup-button")).click();;
        String errorMsg=driver.findElement(By.className("tips")).getText();
        Assert.assertEquals(errorMsg,"用户名不能为空");
    }
    @Test
    public void failCase2(){
        driver.navigate().to("http://39.108.136.60:8085/lmcanon_web_auto/mng/register.html");
        driver.findElement(By.id("mobilephone")).sendKeys("aa");
        driver.findElement(By.id("signup-button")).click();;
        String errorMsg=driver.findElement(By.className("tips")).getText();
        Assert.assertEquals(errorMsg,"非法的手机号");
    }
    @Test
    public void failCase3(){
        driver.navigate().to("http://39.108.136.60:8085/lmcanon_web_auto/mng/register.html");
        driver.findElement(By.id("mobilephone")).sendKeys("15868821400");
        driver.findElement(By.id("signup-button")).click();;
        String errorMsg=driver.findElement(By.className("tips")).getText();
        Assert.assertEquals(errorMsg,"密码不能为空");
    }
    @Test
    public void failCase4(){
        driver.navigate().to("http://39.108.136.60:8085/lmcanon_web_auto/mng/register.html");
        driver.findElement(By.id("mobilephone")).sendKeys("15868821400");
        driver.findElement(By.id("password")).sendKeys("123");
        driver.findElement(By.id("signup-button")).click();;
        String errorMsg=driver.findElement(By.className("tips")).getText();
        Assert.assertEquals(errorMsg,"密码长度至少为6位");
    }

    @Test
    public void failCase5(){
        driver.navigate().to("http://39.108.136.60:8085/lmcanon_web_auto/mng/register.html");
        driver.findElement(By.id("mobilephone")).sendKeys("15868821400");
        driver.findElement(By.id("password")).sendKeys("123456");
        driver.findElement(By.id("signup-button")).click();;
        String errorMsg=driver.findElement(By.className("tips")).getText();
        Assert.assertEquals(errorMsg,"重复密码不能为空");
    }
    @Test
    public void failCase6(){
        driver.navigate().to("http://39.108.136.60:8085/lmcanon_web_auto/mng/register.html");
        driver.findElement(By.id("mobilephone")).sendKeys("15868821400");
        driver.findElement(By.id("password")).sendKeys("123456");
        driver.findElement(By.id("pwdconfirm")).sendKeys("1234567");
        driver.findElement(By.id("signup-button")).click();;
        String errorMsg=driver.findElement(By.className("tips")).getText();
        Assert.assertEquals(errorMsg,"密码不一致");
    }
}
