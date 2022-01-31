package omi_ri.utilities;

import com.aventstack.extentreports.ExtentTest;
import omi_ri.reporting.reportingClass;
import omi_ri.webApplicationSpecificFunctions.srmAPIWebFuctions;
import omi_ri.webFunctions.webActions;
import omi_ri.webFunctions.webUtilities;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;

public class baseTest extends constants {

    public static ExtentTest test;
    public static ExtentTest parent;

    protected webUtilities utilWeb = new webUtilities();
    protected webActions webAction = new webActions();


    protected dataFunctions data = new dataFunctions();
    protected reportingClass repo = new reportingClass();

    public String dataLocation;

    protected srmAPIWebFuctions srmAPIAppWeb;

    public void setupWebDriver(String browser) throws Exception {
        try {
            switch (browser.toUpperCase()) {
                case "CHROME":
                    utilWeb.setWebDriver(utilWeb.initializeWedriver("Chrome", null));
                    break;
                case "FIREFOX":
                    utilWeb.setWebDriver(utilWeb.initializeWedriver("Firefox", null));
                    break;

                case "IE":
                    utilWeb.setWebDriver(utilWeb.initializeWedriver("InternetExplorer", null));
                    break;
                case "MOBILECHROME":
                    utilWeb.setWebDriver(utilWeb.initializeWedriver("MOBILECHROME", null));
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setupReport(String reportName) throws Exception {
        repo.setExtent(repo.initializeExtentReports(reportName, data.getAppendReport()));
    }

    public void setupData(String Location, String sheetname) throws Exception {
        try {
            count = 1;
            dataLocation = Location;
            utilWeb.WindowsProcess("chromedriver.exe");
            utilWeb.CloseRunningProcess();
            data.GetEnvironmentVariables();
            sDataType = data.getDataType();

            switch (data.getDataType().toUpperCase()) {

                case "EXCEL":
                    sheet = data.ReadExcel(Location, sheetname);
                    iRow = data.rowCount(sheet, record, resultset, sDataType) - 1;
                    break;

                case "FILLO":
                    record = data.ConnectFillo(Location, "Select * from Sheet1");
                    iRow = data.rowCount(sheet, record, resultset, sDataType);
                    break;

                case "SQLSERVER":
                    resultset = data.ConnectAndQuerySQLServer(data.getDBHost(), data.getDBUsername(), data.getDBPassword(), "Select * from  [BookFlights].[dbo].[BookFlights]");
                    iRow = data.rowCount(sheet, record, resultset, sDataType);
                    resultset = data.ConnectAndQuerySQLServer(data.getDBHost(), data.getDBUsername(), data.getDBPassword(), "Select * from  [BookFlights].[dbo].[BookFlights]");
            }

        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }

    protected ExtentTest TestName(String testName, String childName) throws Exception {
        setupReport(JiraTestName);
        addTestToMap(JiraTestName);
        parent = repo.setParent(testName);
        test = repo.setChildNode(childName, parent);
        test.assignCategory("Regression");
        return test;
    }


    protected void initialiseWeb() {
        srmAPIAppWeb = new srmAPIWebFuctions();
    }

    public void addTestToMap(String testName) {
        scenariosMap.put(testName, null);
    }

    public void addReportToMap(String testName, String reportPath) {
        testReportMap.put(testName, reportPath);
    }

    public void addTestResultToMap(String testName, String result) {
        testResults.put(testName, result);
    }

    public HashMap<String, String> buildTestData(Object... data) {
        HashMap<String, String> testData;
        if (testHeaderMap.size() > 0) {
            testData = commonUtilities.buildScenarioTestData(testHeaderMap, data);
        } else {
            testData = commonUtilities.buildTestDataHeader(data);
        }
        return testData;
    }

    public void buildScenarioData(Object... data) {
        if (testHeaderMap.size() == 0) {
            testHeaderMap = buildTestData(data);
        } else {
            testDataMap = buildTestData(data);
        }
    }

    @BeforeSuite
    public void setUpSelenium() throws Exception {
        try {
            if (scenariosMap == null) {
                scenariosMap = new HashMap<>();
                testResults = new HashMap<>();
                testReportMap = new HashMap<>();
            }
            System.out.println("Initializing the Map details....");
            testHeaderMap.clear();
            testDataMap.clear();
        }catch (Exception e){
            System.out.println("Before Suite Error setUpSelenium : " + e.getMessage());
        }
    }

    @AfterTest
    public void tearDown() throws Exception {
        try {
            if (repo.getExtent() != null) {
                repo.getExtent().flush();
            }
            boolean driverHasQuit = utilWeb.driverHasQuit();
            if (!driverHasQuit) {
                utilWeb.getWebdriver().quit();
            }

            addTestResultToMap(JiraTestName,testResult);
            addReportToMap(JiraTestName, String.valueOf(Paths.get(String.valueOf(repo.spark.getFile()))));
            System.out.println("Test Execution for : "+JiraTestName+" - Status is : "+testResult);
            testHeaderMap.clear();
            testDataMap.clear();
            JiraTestName = null;
            testResult = null;
            testHasFailed = false;
        }catch (Exception e){
            System.out.println("After test tear down error : " + e.getMessage());
        }
    }

    @AfterSuite
    public void cleanUp() {
        try {
            if ((scenariosMap.size() > 0)) {
                updateJIRAExecutionStatus();
            }
            //Update the text file after cloning the tests
            String testsCloned = "false";
            data.WriteTextFile(sDefaultPath + "\\JiraFiles\\JiraTestsCloned.txt", testsCloned);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DataProvider(name = "readTestData")
    public Object[][] ReadVariant(Method m) throws IOException {
        try {
            String sheetName = "";
            // iterate over all the groups listed in the annotation
            for (String excelFile : ((Test) m.getAnnotation(Test.class)).groups()) {
                // add each to the list
                sheetName = excelFile;
            }
            System.out.println("Current sheet name is " + sheetName);  // print test sheet name
            return dataFunctions.ReadTestData(sheetName, dataLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateJIRAExecutionStatus() {
        try {

            JTricksRESTClient jTricksRESTClient = new JTricksRESTClient();

            //Check if the tests have already been copied to the new cycle
            jiraTestsCopied = Boolean.parseBoolean(data.ReadTextFile(sDefaultPath + "\\JiraFiles\\JiraTestsCloned.txt"));
            if (!jiraTestsCopied) {
                defaultCycleID = data.ReadTextFile(defaultCycleIDFilePath);
                System.out.println("Cloning tests from cycle ID : " + defaultCycleID);
                jTricksRESTClient.copyTestCasesFromOneCycleToAnother(defaultCycleID, newCycleName);
            } else {
                jTricksRESTClient.cycleExists(newCycleName);
            }

            //Update JIRA Execution Status
            Iterator it = scenariosMap.keySet().iterator();
            System.out.println("JIRA UPDATE WILL BE DONE.");

            while (it.hasNext()) {
                String key = it.next().toString();
                reportPath = testReportMap.get(key);
                finalResult = testResults.get(key);

                //Add the created test to the cycle and update the execution status
                if (finalResult != null && !finalResult.trim().isEmpty()) {
                    jTricksRESTClient.addTestToCycle();
                    System.out.println("Execution status for test : '" + key + "' is " + finalResult);
                    jTricksRESTClient.updateTestExecutionStatus(key, finalResult);

                } else {
                    System.out.println("Execution status update error for test : '" + key);
                }

                if (reportPath != null) {
                    jTricksRESTClient.addAttachmentToJira(reportPath, key);
                } else {
                    System.out.println("Add Attachment Error for Scenario : '" + key);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
