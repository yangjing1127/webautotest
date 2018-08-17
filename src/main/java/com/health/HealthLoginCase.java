package com.health;

import com.health.util.OCRUtil;
import com.yj.bases.Base;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HealthLoginCase extends Base {

    @Test(dataProvider = "failDatas")
    public void getVerifyCode(String mobilePhone,String password){
        driver.navigate().to("");
        String code=OCRUtil.getVerificationCode("C:\\Users\\Administrator\\Desktop\\test.png",driver);
        driver.findElement(By.id("mngName")).sendKeys(mobilePhone);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("vcode")).sendKeys(code);
        driver.findElement(By.className("submit")).click();
    }

    @DataProvider
    public Object[][] failDatas(){
        Object[][] datas={
                {"username","password"}
        };
        return datas;
    }
}
