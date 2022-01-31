package omi_ri.webApplicationSpecificFunctions;

import com.aventstack.extentreports.ExtentTest;
import omi_ri.utilities.baseTest;
import omi_ri.webPageObject.*;
import org.openqa.selenium.WebDriver;

public class srmAPIWebFuctions extends baseTest {

    public void APIPortal_Login(String EmailAddress, String Password, WebDriver webDriver, ExtentTest test) throws Exception {

        pageObjectEcosystemsAPIWeb ecosystemsAPIObject = new pageObjectEcosystemsAPIWeb(webDriver);
        webAction.waitForPageLoaded(webDriver);
        try {
            if (webAction.elementExists(ecosystemsAPIObject.logoutBtn)) {
                webAction.clickObject(ecosystemsAPIObject.logoutBtn, webDriver, test);
            }

            if (webAction.checkIfObjectIsDisplayed(ecosystemsAPIObject.lblLogin, 10, "Login label successfully displayed", "Login label not displayed", webDriver, test)) {
                webAction.clickObject(ecosystemsAPIObject.lblLogin, "Login label successfully clicked", "Login label not clicked", webDriver, test);
            }
            webAction.waitForPageLoaded(webDriver);
            if (webAction.checkIfObjectIsDisplayed(ecosystemsAPIObject.email, 5, "Email label successfully displayed", "Email label not displayed", webDriver, test)) {
                webAction.enterText(ecosystemsAPIObject.email, EmailAddress);
                webAction.enterText(ecosystemsAPIObject.password, Password);
                repo.ExtentLogPass("Login data captured successfully", webDriver, test);
                webAction.clickObject(ecosystemsAPIObject.loginBtn, webDriver, test);
            }
            webAction.waitForPageLoaded(webDriver);
            if (webAction.waitForElement(ecosystemsAPIObject.logoutBtn, 20, webDriver)) {
                repo.ExtentLogPass("Login Completed", webDriver, test);
            } else {
                repo.ExtentLogFail("Login Failed", webDriver, test);
            }

        } catch (Exception e) {
            repo.ExtentLogFail("Failed to Complete APIPortal_Login Test Exception : " + e.getMessage(), webDriver, test);
        }

    }




    public void APIPortal_UpgradePage(WebDriver webDriver, ExtentTest test) throws Exception {

        pageObjectEcosystemsAPIWeb ecosystemsAPIObject = new pageObjectEcosystemsAPIWeb(webDriver);

        try {
            webAction.scrollUpandDown("Down");
            webAction.scrollUpandDown("Down");
            webAction.scrollUpandDown("Down");
            if (webAction.checkIfObjectIsDisplayed(ecosystemsAPIObject.lblUpgrade, 10, "Upgrade label successfully displayed", "Upgrade label not displayed", webDriver, test)) {
                webAction.clickObject(ecosystemsAPIObject.lblUpgrade, "Upgrade label successfully clicked", "Upgrade label not clicked", webDriver, test);
            }
            webAction.waitForPageLoaded(webDriver);
            webAction.checkIfObjectIsDisplayed(ecosystemsAPIObject.lblHereToUpgrade, 10, "Upgrade Page successfully displayed ", "Upgrade Page not displayed", webDriver, test);
            webAction.scrollUpandDown("Down");
        } catch (Exception e) {
            repo.ExtentLogFail("Failed to Complete APIPortal_UpgradePage Test Exception : " + e.getMessage(), webDriver, test);
        }

    }

}

