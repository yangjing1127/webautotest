package com.health.util;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class OCRUtil {


    public static ITesseract instance;

    private final static String datapath = ".";
    private final String testResourcesDataPath = "test/resources/testdata";


    private static byte[] takeScreenshot(WebDriver driver) throws IOException {
        byte[] screenshot = null;
        screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);//得到截图
        return screenshot;
    }

    //得到整个屏幕的截图，处理为对图片进行截取，保留验证码的部分
    private static BufferedImage createElementImage(WebDriver driver, WebElement element, int x, int y, int width, int height) throws Exception {
        Dimension dimension = element.getSize();
        BufferedImage srcImg = ImageIO.read(new ByteArrayInputStream(takeScreenshot(driver)));
        BufferedImage saveImg = srcImg.getSubimage(x, y, dimension.getWidth() + width, dimension.getHeight() + height);
        return saveImg;
    }

    //读取图片获得验证码，默认英文需要中文时，加上instance.setLanguage("chi_sim");
    //path：C:/Users/yangjing15268/Desktop/Testpics/test.png
    public static String getVerificationCode(String path, WebDriver driver) {
        String result =null;
        try {
            File imageFile = new File(path);
            imageFile.createNewFile();
            WebElement element = driver.findElement(By.cssSelector("img[id='vcodeImage']"));
            BufferedImage image = createElementImage(driver, element, 900, 300, 78, 83);//得到剪裁的图片
            ImageIO.write(image, "png", imageFile);
         instance=new Tesseract();
            URL url = ClassLoader.getSystemResource("tessdata");//获得Tesseract的文字库
            String tesspath = url.getPath().substring(1);
            instance.setDatapath(tesspath);//进行读取，默认是英文，如果要使用中文包，加上instance.setLanguage("chi_sim");
            result = instance.doOCR(imageFile);
            System.out.println(result);
            result = result.replaceAll("[^a-z^A-Z^0-9]", "");
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
