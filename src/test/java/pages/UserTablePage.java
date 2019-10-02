package pages;

import com.opencsv.CSVReaderHeaderAware;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.qameta.allure.Allure.step;

public class UserTablePage extends BasePage {

    @FindBy(css = "[table-title='Smart Table example']")
    public WebElement userTable;

    @FindBy(css = "button[type='add']")
    public WebElement addUser;

    @FindBy(css = "[name='FirstName']")
    public WebElement firstName;

    @FindBy(css = "[name='LastName']")
    public WebElement lastName;

    @FindBy(css = "[name='UserName']")
    public WebElement userName;

    @FindBy(css = "[name='Password']")
    public WebElement password;

    @FindBy(css = "input[name='optionsRadios'][value='15']")
    public WebElement companyAAA;

    @FindBy(css = "input[name='optionsRadios'][value='16']")
    public WebElement companyBBB;

    @FindBy(css = "[name='RoleId']")
    public WebElement role;

    @FindBy(css = "[name='Email']")
    public WebElement email;

    @FindBy(css = "[name='Mobilephone']")
    public WebElement cellPhone;

    @FindBy(xpath = "//button[text()='Save']")
    public WebElement save;

    @FindBys({
            @FindBy(css = "[table-title='Smart Table example'] tbody tr:nth-child(1) td:not(.ng-hide)")
    })
    public List<WebElement> row1;


    public UserTablePage(WebDriver driver) {
        super(driver);
    }

    public void addUsers() throws Exception {
        FileReader reader = new FileReader(UserTablePage.class.getClassLoader().getResource("testdata/tabledata.csv").getFile());
        CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(reader);

        Map<String, String> values = csvReader.readMap();

        SoftAssert softAssert = new SoftAssert();
        while (values != null) {
            addUser.click();
            waitUntilVisible(10, firstName);
            firstName.clear();
            firstName.sendKeys(values.get("First Name"));
            lastName.clear();
            lastName.sendKeys(values.get("Last Name"));
            String userNameValue = values.get("User Name") + System.currentTimeMillis();
            userName.clear();
            userName.sendKeys(userNameValue);
            password.clear();
            password.sendKeys(values.get("Password"));
            switch (values.get("Customer")) {
                case "Company AAA":
                    companyAAA.click();
                    break;
                case "Company BBB":
                    companyBBB.click();
                    break;
            }
            selectByVisibleText(role, values.get("Role"));
            email.clear();
            email.sendKeys(values.get("E-mail"));
            cellPhone.clear();
            cellPhone.sendKeys(values.get("Cell Phone"));
            step("Added User details \"" + values + "\"", () -> {
                takeScreenshot("User");
            });
            save.click();

            Map<String, String> finalValues = values;
            step("Verify user added", () -> {
                softAssert.assertEquals(row1.get(0).getText(), finalValues.get("First Name"));
                softAssert.assertEquals(row1.get(1).getText(), finalValues.get("Last Name"));
                softAssert.assertEquals(row1.get(2).getText(), userNameValue);
                softAssert.assertEquals(row1.get(3).getText(), finalValues.get("Customer"));
                softAssert.assertEquals(row1.get(4).getText(), finalValues.get("Role"));
                softAssert.assertEquals(row1.get(5).getText(), finalValues.get("E-mail"));
                softAssert.assertEquals(row1.get(6).getText(), finalValues.get("Cell Phone"));
                takeScreenshot("Tablw");
            });

            new WebDriverWait(driver, 5)
                    .until((ExpectedCondition<Boolean>) driver -> {
                        try {
                            return !firstName.isDisplayed();
                        } catch (NoSuchElementException e) {
                            return true;
                        }
                    });

            values = csvReader.readMap();
        }
        softAssert.assertAll();
    }
}
