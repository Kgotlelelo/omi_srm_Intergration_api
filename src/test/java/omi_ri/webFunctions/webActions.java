package omi_ri.webFunctions;

import com.aventstack.extentreports.ExtentTest;
import omi_ri.reporting.reportingClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class webActions {

    protected reportingClass repo = new reportingClass();


    public void clickObject(WebElement element, WebDriver driver, ExtentTest test) throws Exception {
        try {
            if (element.isDisplayed()) {
                Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                        .withTimeout(Duration.ofSeconds(5))
                        .pollingEvery(Duration.ofMillis(1000))
                        .ignoring(WebDriverException.class);
                wait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
            }
        } catch (Exception e) {
            System.out.println(element + " NOT found.");
            repo.ExtentLogFail("Element : " + element + " Not Found Error : " + e.getMessage(), driver, test);
            driver.quit();
            return;
        }
    }

    public void checkBox(WebElement element, WebDriver driver, ExtentTest test) throws Exception {
        try {
            if (element.isDisplayed()) {
                Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                        .withTimeout(Duration.ofSeconds(5))
                        .pollingEvery(Duration.ofMillis(1000))
                        .ignoring(WebDriverException.class);
                wait.until(ExpectedConditions.elementToBeClickable(element));
                boolean isSelected = element.isSelected();
                if(isSelected == false) {
                    element.click();
                }
            }
        } catch (Exception e) {
            System.out.println(element + " NOT found.");
            repo.ExtentLogFail("Element : " + element + " Not Found Error : " + e.getMessage(), driver, test);
            driver.quit();
            return;
        }
    }

    public void clickObject(WebElement element, String pMessage, String fMessage, WebDriver driver, ExtentTest test) throws Exception {
        try {
            if (element.isDisplayed()) {
                Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                        .withTimeout(Duration.ofSeconds(5))
                        .pollingEvery(Duration.ofMillis(1000))
                        .ignoring(WebDriverException.class);
                wait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
            }
        } catch (Exception e) {
            System.out.println(element + " NOT found.");
            repo.ExtentLogFail(fMessage + ": Element : " + element + " Not Found Error : " + e.getMessage(), driver, test);
            driver.quit();
            return;
        }
        repo.ExtentLogPass(pMessage, driver, test);
    }

    public void clickAction(WebElement element, WebDriver driver, ExtentTest test) throws Exception {
        try {
            if (element.isDisplayed()) {
                element.click();
            }
        } catch (Exception e) {
            System.out.println(element + " NOT found.");
            repo.ExtentLogFail("Element : " + element + " Not Found Error : " + e.getMessage(), driver, test);
            driver.quit();
            return;
        }
    }

    public void clickObjectUsingAction(WebElement e, WebDriver driver, ExtentTest test) throws Exception {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(Duration.ofSeconds(5))
                    .pollingEvery(Duration.ofMillis(1000))
                    .ignoring(WebDriverException.class);
            wait.until(ExpectedConditions.elementToBeClickable(e));
            Actions action = new Actions(driver);
            action.moveToElement(e).click().build().perform();
        } catch (Exception ex) {
            System.out.println("Element " + e + " Data Input Error.");
            repo.ExtentLogFail("Element :" + e + " Data Input Error: " + ex.getMessage(), driver, test);
            driver.quit();
            return;
        }
    }

    public void jsClickAction(WebElement element, WebDriver driver, ExtentTest test) throws Exception {
        try {
            if (element.isEnabled()) {
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript("arguments[0].click();", element);
            } else {
                System.out.println("Java script Unable to click on element : " + element);

            }
        } catch (Exception e) {
            repo.ExtentLogFail("Element : " + element + " Not Found Error : " + e.getMessage(), driver, test);
            driver.quit();
            return;
        }
    }

    public void enterText(WebElement element, String text) {
        if (!text.isEmpty()) {
            clearTextField(element);
            element.sendKeys(text);
        }
    }

    public void enterText(WebElement element, String text, WebDriver driver, ExtentTest test) throws Exception {
        try {
            if (!text.isEmpty()) {
                clearTextField(element);
                element.sendKeys(text);
            }
        } catch (Exception e) {
            System.out.println("Element " + element + " Data Input Error.");
            repo.ExtentLogFail("Element :" + element + " Data Input Error: " + e.getMessage(), driver, test);
            driver.quit();
            return;
        }
    }

    public void clearTextField(WebElement element) {
        element.clear();
    }

    public void clearField(WebElement element) {
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
    }

    public boolean witForElement(WebElement e, int timeOut, WebDriver driver) {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(Duration.ofSeconds(timeOut))
                    .pollingEvery(Duration.ofMillis(5000))
                    .ignoring(WebDriverException.class);
            wait.until(ExpectedConditions.visibilityOf(e));
        } catch (TimeoutException exp) {
            exp.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean checkIfObjectIsDisplayed(WebElement elemet, int unit, String pMassage, String fMassage, WebDriver driver, ExtentTest test) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, unit);
            wait.until(ExpectedConditions.visibilityOf(elemet));
        } catch (TimeoutException e) {
            System.out.println("Element " + elemet + " Not Displayed Error.");
            repo.ExtentLogFail(fMassage + "Element :" + elemet + " Not Displayed Error: " + e.getMessage(), driver, test);
            driver.quit();
            return false;
        }
        repo.ExtentLogPass(pMassage, driver, test);
        return true;
    }

    public boolean checkIfObjectIsDisplayed(WebElement elemet, int unit, WebDriver driver) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, unit);
            wait.until(ExpectedConditions.visibilityOf(elemet));
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    public void presEnter(WebElement element, WebDriver driver, ExtentTest test) throws Exception {
        try {
            element.sendKeys(Keys.ENTER);
        } catch (
                Exception e) {
            System.out.println("Element " + element + " NOT found unable to click Object.");
            repo.ExtentLogFail("Element :" + element + " Enter not Pressed Error: " + e.getMessage(), driver, test);
            driver.quit();
            return;
        }

    }


    public boolean elementExists(WebElement element) {
        try {
            element.isDisplayed();
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean verifyMessage(WebElement elmnt, String expValue) {
        boolean isMatch = false;
        try {
            String mesg = elmnt.getText().trim();
            System.out.print("The label text: " + mesg);

            if (mesg.equalsIgnoreCase(expValue)) {
                isMatch = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isMatch;
    }

    public boolean verifyMessageContains(WebElement elmnt, String expValue, WebDriver driver, ExtentTest test) throws Exception {
        boolean isMatch = false;
        try {
            String mesg = elmnt.getText().trim();
            System.out.print("The label text: " + mesg);

            if (mesg.contains(expValue)) {
                isMatch = true;
            }
        } catch (Exception e) {
            repo.ExtentLogFail("Validation For : " + expValue + "Failed " + " : " + e.getMessage(), driver, test);
            driver.quit();
            return isMatch;
        }
        repo.ExtentLogPass("Validation For : " + expValue + "Completed Successfully", driver, test);
        return isMatch;
    }

    public boolean verifyElementTextIgnoreWhiteSpace(WebElement e, String text) {
        boolean isMatching = false;
        if ((e.getAttribute("value").replaceAll("\\s+", "")).equalsIgnoreCase(text.replaceAll("\\s+", ""))) {

            isMatching = true;
        }
        return isMatching;
    }

    public boolean verifyElementContainsTextIgnoreWhiteSpace(WebElement e, String text) {
        boolean isMatching = false;
        System.out.println("Element Text : " + e.getAttribute("value"));
        if ((e.getAttribute("value").replaceAll("\\s+", "")).contains(text.replaceAll("\\s+", ""))) {

            isMatching = true;
        }
        return isMatching;
    }

    public String getElementText(WebElement e) {
        return e.getText();
    }

    public void checkIfObjectTextExist(WebElement element, String strExpected, String strPass, String strFail, WebDriver driver, ExtentTest test) throws Exception {
        String strText = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            element = wait.until(ExpectedConditions.elementToBeClickable(element));
            strText = element.getText();
        } catch (Exception e) {
            System.out.println("Element " + element + " NOT Displayed.");
            repo.ExtentLogFail("Element : " + element + " Not Displayed Error: " + e.getMessage(), driver, test);
            driver.quit();
            return;
        }
        if (strText.contains(strExpected)) {
            repo.ExtentLogPass(strPass, driver, test);
        } else {
            repo.ExtentLogFail(strFail, driver, test);
            driver.quit();
            return;
        }

    }

    public boolean checkObjectTextFromList(List<WebElement> object, String objectToFind, String pMassage, String fMassage, WebDriver driver, ExtentTest test) throws Exception {
        try {

            for (int x = 0; x < object.size(); x++) {
                System.out.println(object.get(x).getText().replaceAll("\\s+", ""));
                if (object.get(x).getText().replaceAll("\\s+", "").equalsIgnoreCase(objectToFind.replaceAll("\\s+", ""))) {
                    System.out.println("Object Text Located: " + object.get(x).getText().replaceAll("\\s+", ""));
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Element " + object + " NOT found.");
            repo.ExtentLogFail(fMassage + " " + "Element :" + object + " Not Displayed Displayed Error: " + e.getMessage(), driver, test);
            return false;
        }
        repo.ExtentLogPass(pMassage, driver, test);
        return true;
    }

    public boolean clickObjectTextFromList(List<WebElement> object, String objectToFind, String pMassage, String fMassage, WebDriver driver, ExtentTest test) throws Exception {
        try {
            for (int x = 0; x < object.size(); x++) {
                System.out.println(object.get(x).getText().replaceAll("\\s+", ""));
                if (object.get(x).getText().replaceAll("\\s+", "").equalsIgnoreCase(objectToFind.replaceAll("\\s+", ""))) {
                    System.out.println("Object Text Located: " + object.get(x).getText().replaceAll("\\s+", ""));
                    object.get(x).click();
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Element " + object + " NOT found.");
            repo.ExtentLogFail(fMassage + " " + "Element :" + object + " Not Displayed Displayed Error: " + e.getMessage(), driver, test);
            driver.quit();
            return false;
        }
        repo.ExtentLogPass(pMassage, driver, test);
        return true;
    }

    public boolean waitForElement(WebElement e, int timeOut, WebDriver driver) {
        try {
            Wait<WebDriver> wait = new FluentWait(driver)
                    .withTimeout(Duration.ofSeconds(timeOut))
                    .pollingEvery(Duration.ofMillis(1000))
                    .ignoring(WebDriverException.class);
            wait.until(ExpectedConditions.visibilityOf(e));
        } catch (TimeoutException exp) {
            exp.printStackTrace();
            return false;
        }
        return true;
    }

    public void waitForPageLoaded(WebDriver driver) {
        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                System.out.println("Current Window State       : "
                        + String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")));
                return String
                        .valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
                        .equals("complete");
            }
        });
     /*   ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                    }
                };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(driver, 40);
            wait.until(expectation);
        } catch (Throwable error) {
            System.out.println("Timeout waiting for Page Load Request to complete.");
        }*/
    }

    public void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }

    public void switchToTab(int tab, WebDriver driver) throws Exception {
        //ArrayList<String> allTabs = new ArrayList<String>(driver.getWindowHandles());
        Thread.sleep(2000);
        ArrayList<String> allTabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(allTabs.get(tab));

    }

    public boolean checkPrevioseElement(WebElement e, WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.stalenessOf(e));
            return true;
        } catch (TimeoutException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean waitForObjectInvisibility(WebElement e, int timeout, WebDriver driver) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.invisibilityOf(e));
            return true;
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public void highlightElement(WebElement element, WebDriver driver) {
        for (int i = 0; i < 10; i++) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
                    "color: blue; border: 6px solid blue;");
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
        }
    }

    public void scrollPageToElement(WebElement element, WebDriver driver) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView();", element);
    }

    public boolean isAtCorrectPage(String title, WebDriver driver) {
        boolean isMatch = false;
        if (driver.getTitle().equalsIgnoreCase(title)) {
            isMatch = true;
            System.out.println("Title : " + title + " matches with the page");

        } else {
            System.out.println("Title : " + title + " does not matches with the page");
        }
        return isMatch;
    }

    public void scrollUpandDown(String strUporDown) {

        Actions action = new Actions(webUtilities.getWebdriver());

        if (strUporDown.toUpperCase().equals("DOWN")) {
            action.sendKeys(Keys.PAGE_DOWN).build().perform();

        } else {
            action.sendKeys(Keys.PAGE_UP).build().perform();
        }
    }

    public void openLinkInNewTab(String link, WebDriver driver) {

        String currentHandle = driver.getWindowHandle();
        ((JavascriptExecutor) driver).executeScript("window.open()");
        //getting all the handles currently available
        Set<String> handles = driver.getWindowHandles();
        for (String actual : handles) {
            if (!actual.equalsIgnoreCase(currentHandle)) {
                //switching to the opened tab
                driver.switchTo().window(actual);
                //opening the URL saved.
                driver.get(link);
            }
        }
    }

    public void switchToNewTab(WebDriver driver) {

        String currentHandle = driver.getWindowHandle();
        //getting all the handles currently available
        Set<String> handles = driver.getWindowHandles();
        for (String actual : handles) {
            if (!actual.equalsIgnoreCase(currentHandle)) {
                //switching to the opened tab
                driver.switchTo().window(actual);
            }
        }
    }

    public String SAidentityNumberGenerator() {

        String StrID = "";
        String StrIDNumber = "";

        try {

            Random rand = new Random();
            int count = 0;
            int total = 0;
            int flag = 0;

            while (flag != 2) {
                String strDays = String.valueOf(10 + rand.nextInt(20));
                String strMonth = String.valueOf(10 + rand.nextInt(2));
                String strYear = String.valueOf(60 + rand.nextInt(40));
                String strGender = String.valueOf(4999 + rand.nextInt(4999));

                StrID = strYear + strMonth + strDays + strGender + "08";

                for (int i = 0; i < StrID.length(); i++) {
                    int multiple = (count % 2) + 1;
                    count++;
                    int temp = multiple * Integer.parseInt(String.valueOf(StrID.charAt(i)));
                    temp = (int) Math.floor(temp / 10) + (temp % 10);
                    total += temp;
                }

                total = (total * 9) % 10;

                if (total == 1 || total == 2) {
                    flag = 2;

                    StrIDNumber = StrID + total;

                }
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return StrIDNumber;
    }

    public static String CheckGenderFromIdNumber(String strIdNumber) {
        try {

            // get the gender
            String strgenderCode = strIdNumber.substring(6, 10);
            String strgender = Integer.valueOf(strgenderCode) < 5000 ? "Female" : "Male";

            return strgender;
        } catch (NoAlertPresentException ex) {
            return "Invalid ID Number";                        //return strgender;
        }

    }

    public String generateRandomString(int length) {

        //int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;
        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
        System.out.println(generatedString);
        return generatedString;
    }

    public String generateRandomValue(int length) {

        //int length = 10;
        boolean useLetters = false;
        boolean useNumbers = true;
        String generatedValue = RandomStringUtils.random(length, useLetters, useNumbers);
        System.out.println(generatedValue);
        return generatedValue;
    }

    public void UploadDocuments(String ProposalFilePath, String ExistingDocumentFilePath, WebDriver webDriver, ExtentTest test) throws Exception {
        try {

            WebElement uploadElement = webDriver.findElement(By.xpath("//*[@id='edit-field-upload-proposal-0-upload']"));
            uploadElement.sendKeys(ProposalFilePath);
            Thread.sleep(3000);
            WebElement uploadElementTwo = webDriver.findElement(By.xpath("//*[@id='edit-field-upload-existing-proposal-0-upload']"));
            uploadElementTwo.sendKeys(ExistingDocumentFilePath);

        } catch (Exception e) {
            repo.ExtentLogFail("Failed to Complete Upload Document Test Exception : " + e.getMessage(), webDriver, test);
        }

    }

}
