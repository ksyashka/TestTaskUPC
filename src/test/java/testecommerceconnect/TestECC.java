package testecommerceconnect;


import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.Properties;

public class TestECC {

    private static WebDriver driver;

    private static String getProperty(String nameProperty) throws IOException {

        Properties prop = new Properties();
        prop.load(new FileInputStream(new File("src/config.properties")));
        return prop.getProperty(nameProperty);
    }

    @Before
    public void setup() throws IOException {
        System.setProperty("webdriver.chrome.driver", getProperty("chromedriverpath"));
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://ecconnect.com.ua/");
        WebElement demoButton = driver.findElement(By.className("demo-btn"));
        demoButton.click();
        WebElement buyButton = driver.findElement(By.className("submit"));
        buyButton.click();

    }

    @Test
    public void successfulPayment() {
        Assert.assertEquals("Оплата товара прошла успешно", payByCard("4999999999990011", "12", "2050", "999",
                "//*[@id=\"middle-col\"]/h1"));

    }

    @Test
    public void rejectedPayment() {
        Assert.assertEquals("Відхилено банком емітентом", payByCard("4999999999990029", "12", "2050", "999",
                "//*[@id=\"message\"]/table/tbody/tr[1]/td[2]/span"));
    }

    @After
    public void tearDown() {
        driver.quit();
    }


    private static String payByCard(String cardNumber, String month, String year, String cvc, String resultPath) {
        List<String> browserTabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(browserTabs.get(1));
        WebElement paymentType = driver.findElement(By.xpath("//*[@id=\"paymentTypeCard\"]"));
        paymentType.click();
        driver.findElement(By.id("CardNumber")).sendKeys(cardNumber);
        driver.findElement(By.id("ExpMonth")).sendKeys(month);
        driver.findElement(By.id("ExpYear")).sendKeys(year);
        driver.findElement(By.id("Email")).sendKeys("test@gmail.com");
        driver.findElement(By.id("cardDataSubmit")).click();
        driver.findElement(By.id("Cvc")).sendKeys(cvc);
        driver.findElement(By.id("CVCSubmit")).click();
        return driver.findElement(By.xpath(resultPath)).getText();
    }


}
