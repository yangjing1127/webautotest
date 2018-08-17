package com.yj.cases;

import com.yj.bases.Base;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RegisterCaseV2 extends Base {


    @Test(dataProvider = "failCaseDatas")
    public void failCase(String mobilePhone,String password,String pwdConfirm,String expectedResult){
        caseMethod(mobilePhone, password, pwdConfirm, expectedResult);
    }


    @DataProvider
    public Object[][] failCaseDatas(){
        Object[][] datas={
                {"","","","用户名不能为空"},
                {"158688","","","非法的手机号"},
                {"15868821400","","","密码不能为空"},
                {"15868821400","123","","密码长度至少为6位"},
                {"15868821400","123456","","重复密码不能为空"},
                {"15868821400","123456","1234567","密码不一致"},
        };
        return datas;
    }


    @Test(dataProvider = "successCaseDatas")
    public void successCase(String mobilePhone,String password,String pwdConfirm,String expectedResult){
        caseMethod(mobilePhone, password, pwdConfirm, expectedResult);
    }

    private void caseMethod(String mobilePhone, String password, String pwdConfirm, String expectedResult) {
        driver.navigate().to("http://39.108.136.60:8085/lmcanon_web_auto/mng/register.html");
        driver.findElement(By.id("mobilephone")).sendKeys(mobilePhone);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("pwdconfirm")).sendKeys(pwdConfirm);
        driver.findElement(By.id("signup-button")).click();
        ;
        String errorMsg = driver.findElement(By.className("tips")).getText();
        Assert.assertEquals(errorMsg, expectedResult);
    }


    @DataProvider
    public Object[][] successCaseDatas(){
        Object[][] datas={
                {"15868821400","123456","123456","此手机号已被注册"},
        };
        return datas;
    }
}
