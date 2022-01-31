package omi_ri.webFunctions;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LoggingPreferences;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class webUtilities {

    protected static WebDriver driver;
    private String processName;

    public static WebDriver getWebdriver() {
        return driver;
    }

    public void setWebDriver(WebDriver DriverTest) {
        this.driver = DriverTest;
    }

    public WebDriver initializeWedriver(String sdriverName, String strURL) {

        try {
            String operatingSystem = System.getProperty("os.name");
            switch (sdriverName.toUpperCase()) {

                case "CHROME":
                    //WebDriverManager.chromedriver().setup();

                    if (operatingSystem.contains("Win")) {
                        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver.exe");
                        System.setProperty("webdriver.chrome.silentOutput", "true");
                    } else {
                        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver");
                    }

                    ChromeOptions ChromeOption = new ChromeOptions();
                    //DesiredCapabilities capabilities = DesiredCapabilities.chrome();
                    LoggingPreferences logs = new LoggingPreferences();

                    ChromeOption.addArguments("--remote-debugging-port=9222");
                    ChromeOption.addArguments("--disable-notifications");
                    ChromeOption.setPageLoadStrategy(PageLoadStrategy.NONE);
                    ChromeOption.setExperimentalOption("useAutomationExtension", false);
                  /*  ChromeOption.addArguments("no-sandbox");
                    ChromeOption.addArguments("disable-popup-blocking");
                    ChromeOption.addArguments("disable-default-apps");
                    ChromeOption.addArguments("--disable-plugins-discovery");
                    ChromeOption.addArguments("--whitelisted-ips");
                    ChromeOption.addArguments("--disable-browser-side-navigation", "--disable-web-security", "--disable-gpu", "--disable-dev-shm-usage");
                    ChromeOption.addArguments("--ignore-certificate-errors", "--allow-running-insecure-content", "--allow-insecure-localhost", "--disable-infobars");
                    ChromeOption.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));*/
                    ChromeOption.addArguments("user-data-dir=" + System.getProperty("user.dir") + "/chromeprofile");

                    driver = new ChromeDriver(ChromeOption);

                    driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
                    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                    driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);

                    driver.manage().window().maximize();
                    break;

                /*case "FIREFOX":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions FireFoxOption = new FirefoxOptions();
                    driver = new FirefoxDriver();
                    driver.manage().window().maximize();
                    break;

                case "IE":
                    WebDriverManager.iedriver().setup();
                    driver = new InternetExplorerDriver();
                    driver.manage().window().maximize();
                    break;

                case "IOS":
                    driver = (IOSDriver) new IOSDriver(new URL(strURL), capabilities);
                    break;

                case "ANDROID":
                    driver = (AndroidDriver) new AndroidDriver(new URL(strURL), capabilities);
                    break;

                case "KOBITON":
                    driver = new AndroidDriver<WebElement>(new URL(strURL), capabilities);
                    break;*/
                case "MOBILECHROME":
                    if (operatingSystem.contains("Win")) {
                        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver.exe");
                    } else {
                        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver");
                    }
                    Map<String, String> mobileEmulation = new HashMap<>();
                    mobileEmulation.put("deviceName", "Pixel 2");
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
                    driver = new ChromeDriver(chromeOptions);
                    break;

            }

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return driver;
    }

    public void WindowsProcess(String processName) {
        this.processName = processName;
    }

    public void CloseRunningProcess() throws Exception {
        if (isRunning()) {
            getRuntime().exec("taskkill /F /IM " + processName +" /T");
        }
    }

    private boolean isRunning() throws Exception {
        Process listTasksProcess = getRuntime().exec("tasklist");
        BufferedReader tasksListReader = new BufferedReader(
                new InputStreamReader(listTasksProcess.getInputStream()));
        String tasksLine;
        while ((tasksLine = tasksListReader.readLine()) != null) {
            if (tasksLine.contains(processName)) {
                return true;
            }
        }
        return false;
    }

    private Runtime getRuntime() {
        return Runtime.getRuntime();
    }

    public void navigate(WebDriver driver, String URL) {
        driver.get(URL);
    }

    public void navigate(String URL) throws InterruptedException {
        driver.get(URL);
        Thread.sleep(2000);
    }

    public boolean driverHasQuit() {
        try {
            driver.getTitle();
            return false;
        } catch (Exception e) {

            return true;
        }
    }

}
