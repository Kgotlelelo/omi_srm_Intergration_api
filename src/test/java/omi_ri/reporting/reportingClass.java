package omi_ri.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.microsoft.playwright.Page;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;

import static omi_ri.utilities.constants.*;

public class reportingClass {

    public static ExtentReports extent;
    public static ExtentSparkReporter spark;
    public static ExtentTest extentTest;
    public static ExtentTest parentTest;
    public static ExtentTest childTest;
    public static ExtentTest childNodeTest;

    public ExtentReports initializeExtentReports(String sReportName, String sAppend) {

        //if (extent == null) {
        String sDefaultPath = System.getProperty("user.dir");

        if (sAppend.toUpperCase().equals("TRUE")) {
            spark = new ExtentSparkReporter(System.getProperty("user.dir") + "/report/" + sReportName + ".html");
        } else {
            spark = new ExtentSparkReporter(System.getProperty("user.dir") + "/report/" + sReportName + getCurrentTimeStamp() + ".html");

        }

        spark.config().setReportName("<img src=" + sDefaultPath + "/logo/logo.jpg");
        spark.config().setCss(".report-name { padding-right: 10px; } .report-name > img { height: 80%;margin-left: 0px;margin-bottom: -10px;width: auto; }");
        extent = new ExtentReports();
        extent.setSystemInfo("HTML Report", "GUI and Mobile Regression");
        extent.setSystemInfo("Automation Engineer", "Kg Ratshoshi");
        extent.attachReporter(spark);
        //}
        return extent;
    }

    public String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf.format(timestamp);

    }

    public void setExtent(ExtentReports extentTest) {
        reportingClass.extent = extentTest;
    }

    public void setExtentTest(ExtentTest extentTest) {
        reportingClass.extentTest = extentTest;
    }

    public ExtentTest setParent(String parentName) {
        parentTest = extent.createTest(parentName);
        return parentTest;
    }

    public ExtentTest setChild(String childName, ExtentTest parentName) {
        childTest = parentName.createNode(childName);
        return childTest;
    }

    public ExtentTest setChildNode(String childNodeName, ExtentTest parentName) {
        childNodeTest = parentName.createNode(childNodeName);
        return childNodeTest;
    }

    public ExtentReports getExtent() {
        return extent;
    }

    public ExtentTest getExtentTest() {
        return extentTest;
    }

    public void childLogPass(String sStepName, String sExpected, String sActual, WebDriver driver, ExtentTest logger) throws Exception {

        logger.pass("<b>Step Name : </b><br/>" + sStepName + "</b><br/><b>Expected : </b><br/>" + sExpected + "<br/><b>Actual Result :</b><br/>" + sActual, MediaEntityBuilder.createScreenCaptureFromBase64String(CaptureScreenWin(driver)).build());
        if (!testHasFailed) {
            testResult = logger.getStatus().toString();
        }
    }

    public void childLogFail(String sStepName, String sExpected, String sActual, WebDriver driver, ExtentTest logger) throws Exception {

        logger.fail("<b>Step Name : </b><br/>" + sStepName + "</b><br/><b>Expected : </b><br/>" + sExpected + "<br/><b>Actual Result :</b><br/>" + sActual, MediaEntityBuilder.createScreenCaptureFromBase64String(CaptureScreenWin(driver)).build());
        testResult = logger.getStatus().toString();
        testHasFailed = true;
    }

    public void childLogPlayWrightPass(String sStepName, String sExpected, String sActual, Page page, ExtentTest logger) throws Exception {

        logger.pass("<b>Step Name : </b><br/>" + sStepName + "</b><br/><b>Expected : </b><br/>" + sExpected + "<br/><b>Actual Result :</b><br/>" + sActual, MediaEntityBuilder.createScreenCaptureFromBase64String(PlayWrightScreen(page)).build());
        if (!testHasFailed) {
            testResult = logger.getStatus().toString();
        }
    }

    public void childLogPlayWrightFail(String sStepName, String sExpected, String sActual, Page page, ExtentTest logger) throws Exception {

        logger.fail("<b>Step Name : </b><br/>" + sStepName + "</b><br/><b>Expected : </b><br/>" + sExpected + "<br/><b>Actual Result :</b><br/>" + sActual, MediaEntityBuilder.createScreenCaptureFromBase64String(PlayWrightScreen(page)).build());
        testResults.put(JiraTestName, logger.getStatus().toString());
        testHasFailed = true;
    }

    public void ExtentLogPlayWrightPass(String sMessageFail, Page page, ExtentTest test) throws Exception {
        String fileName = PlayWrightScreen(page);
        test.pass(sMessageFail, MediaEntityBuilder.createScreenCaptureFromBase64String(fileName).build());
        if (!testHasFailed) {
            testResult = test.getStatus().toString();
        }
    }

    public void ExtentLogPlayWrightFail(String sMessageFail, Page page, ExtentTest test) throws Exception {
        String fileName = PlayWrightScreen(page);
        test.fail(sMessageFail, MediaEntityBuilder.createScreenCaptureFromBase64String(fileName).build());
        testResult = test.getStatus().toString();
        testHasFailed = true;
    }

    public void ExtentLogPlayrightInfo(String sMessageInfo, ExtentTest test) throws Exception {
        test.info(sMessageInfo);
    }

   /* public void ExtentLogPassFail(String sObject, String sExpected, String sMessagePass, String sMessageFail, Boolean Screenshot, String xmlpath, UtilityFunctions utils, ExtentTest test) throws Exception {
        if (utils.checkIfObjectIsDisplayed(sObject, xmlpath, test)) {
            ExtentLogPass("<b> Expected :</b><br/>" + sExpected + "</b><br/><b> Actual Results</b><br/>" + sMessagePass, Screenshot, utils, test);
            if(!testHasFailed) {
             testResult = test.getStatus().toString();
            }
        } else {
            ExtentLogFail("<b> Expected :</b><br/>" + sExpected + "</b><br/><b> Actual Results</b><br/>" + sMessageFail, Screenshot, utils, test);
         testResult = test.getStatus().toString();
             testHasFailed = true;
        }
    }*/

    public void ExtentLogPass(String sMessagePass, WebDriver driver, ExtentTest test) throws Exception {
        String fileName = CaptureScreenWin(driver);
        test.pass(sMessagePass, MediaEntityBuilder.createScreenCaptureFromBase64String(fileName).build());
        if (!testHasFailed) {
            testResult = test.getStatus().toString();
        }
    }

    public void ExtentLogFail(String sMessageFail, WebDriver driver, ExtentTest test) throws Exception {
        String fileName = CaptureScreenWin(driver);
        test.fail(sMessageFail, MediaEntityBuilder.createScreenCaptureFromBase64String(fileName).build());
        testResult = test.getStatus().toString();
        testHasFailed = true;
        driver.quit();
    }

    public void ExtentLogInfo(String sMessageInfo, ExtentTest test) throws Exception {
        test.info(sMessageInfo);
        testResult = test.getStatus().toString();
        testHasFailed = true;
    }

    public void ExtentLogInfoFail(String sMessageInfo, ExtentTest test) throws Exception {
        test.fail(sMessageInfo, MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenShotTaskBar()).build());
        testResult = test.getStatus().toString();
        testHasFailed = true;
    }


    public String CaptureScreenWin(WebDriver driver) throws IOException {

        File scrImage = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        byte[] fileContent = FileUtils.readFileToByteArray(new File(String.valueOf(scrImage)));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return encodedString;
    }

    public String CaptureScreenMob(AndroidDriver driver) throws IOException {

        File scrImage = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        byte[] fileContent = FileUtils.readFileToByteArray(new File(String.valueOf(scrImage)));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return encodedString;
    }

    public String PlayWrightScreen(Page page) throws IOException {
        byte[] buffer = page.screenshot(new Page.ScreenshotOptions());
        String encodedString = Base64.getEncoder().encodeToString(buffer);
        return encodedString;
    }

    public static String takeScreenShotTaskBar() throws Exception {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = new Robot().createScreenCapture(screenRect);
            ImageIO.write(screenFullImage, "png", os);
            String base64 = Base64.getEncoder().encodeToString(os.toByteArray());
            return base64;
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }
}

