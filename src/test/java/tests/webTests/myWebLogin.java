package tests.webTests;

import omi_ri.utilities.baseTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

public class myWebLogin extends baseTest {

    @BeforeTest
    public void beforeMethod() throws MalformedURLException, InterruptedException {
        try {
           // setupDataAndReport(MyMuzeWebData, "Login");
            initialiseWeb();
            //initialiseMobileCapabilities();
        } catch (Exception e) {
            System.out.println("Before Test Error : " + e.getMessage());
        }
    }

    @Test(dataProvider = "readTestData", groups = {"Login"})
    public void WebLogin(Object ...data) throws Exception {
        try {
            buildScenarioData(data);
            if(testDataMap.size()>0) {
                System.out.println("Scenario Name Is : " + testDataMap.get("Scenario"));
                TestName(testDataMap.get("Scenario"), testDataMap.get("Scenario"));

                //Launch
            //    mobileDriver = callSMSDriver(serviceUrl, testDataMap.get("DeviceName"));
                setupWebDriver(testDataMap.get("Browser"));
                utilWeb.navigate(strWebTestEnvURL);

                //Login
              //  mymuzeAppWeb.SumsungSMSLogin(testDataMap.get("username"), testDataMap.get("MobileNumber"), utilWeb.getWebdriver(), mobileDriver, test);

            }
        } catch (Exception e) {
            repo.ExtentLogInfoFail("Failed to Complete " + this.getClass().getSimpleName() + " Test Exception : " + e.getMessage(), test);
        }
    }

}
