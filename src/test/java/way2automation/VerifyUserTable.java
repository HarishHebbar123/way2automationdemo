package way2automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.BasePage;
import pages.UserTablePage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static io.qameta.allure.Allure.step;

public class VerifyUserTable {
    public WebDriver driver;

    @BeforeMethod
    public void setupTest() {
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            InputStream input = loader.getResourceAsStream("url.properties");
            Properties properties = new Properties();
            properties.load(input);
            String url = properties.get("url").toString();
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.navigate().to(url);
        } catch (IOException e) {
            throw new IllegalArgumentException("properties file not found.");
        }
    }

    @Test
    public void VerifyUserTable() throws Exception {
        UserTablePage userTablePage = new UserTablePage(driver);
        step("Validate User List Table", () -> {
            userTablePage.waitUntilVisible(10, userTablePage.userTable);
            BasePage basePage = new BasePage(driver);
            basePage.takeScreenshot("User List Table");
        });
        step("Add Users to Table with details", () -> {
            userTablePage.addUsers();
        });

    }

    @AfterMethod
    public void teardownTest() {
        driver.quit();
    }
}
