package omi_ri.webPageObject;

import omi_ri.webFunctions.webActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class pageObjectMyApps extends webActions {

    protected WebDriver driver;

    public pageObjectMyApps(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    @FindBy(xpath = "(//*[contains(text(),'Go Live')])[2]")
    public WebElement lblGoLive;

    @FindBy(xpath = "(//*[contains(text(),'My Apps')])[2]")
    public WebElement lblMyApps;

    @FindBy(xpath = "//*[contains(text(),'Add a New App')]")
    public WebElement btnAddNewApp;


    @FindBy(xpath = "//*[@class='js-text-full text-full form-text required machine-name-source form-control has-error']")
    public WebElement lblSandBoxAppName;

    @FindBy(xpath = "(//*[@type='checkbox'])[2]")
    public WebElement productNameCheckbox;

    @FindBy(xpath = "//*[contains(text(),'Submit')]")
    public WebElement btnSubmit;

    @FindBy(xpath = "//*[contains(text(),'Please enter app name')]")
    public WebElement mandatoryPleaseEnterAppName;

    @FindBy(xpath = "//*[contains(text(),'Please select at least 1 product')]")
    public WebElement mandatoryPleaseSelectProduct;

    @FindBy(xpath = "(//*[contains(text(),'Test App')])[2]")
    public WebElement lblSandBoxApp;

    @FindBy(xpath = "//*[@class='accordion-button row block']")
    public WebElement newAppDropDownArrow;

    @FindBy(xpath = "//*[contains(text(),'Keys')]")
    public WebElement lblKeys;

    @FindBy(xpath = "//*[contains(text(),'Secret')]")
    public WebElement lblSecret;

    @FindBy(xpath = "//*[@class='secret__toggle']")
    public WebElement viewIcon;

    @FindBy(xpath = "//*[@class='secret__value']")
    public WebElement lblEncryptedKey;

    @FindBy(xpath = "//*[contains(text(),'Created')]")
    public WebElement lblCreated;

    @FindBy(xpath = "(//*[contains(text(),'Products')])[4]")
    public WebElement lblProductsTwo;

    @FindBy(xpath = "//*[contains(text(),'Test In Sandbox')]")
    public WebElement lblTestInSandbox;

    @FindBy(xpath = "//*[@href='http://10.132.255.122/api/26#/documentation']")
    public WebElement lblProductlink;

    @FindBy(xpath = "//*[contains(text(),'Documentation')]")
    public WebElement lblDocumentation;

    @FindBy(xpath = "//*[@value='Go Live']")
    public WebElement btnGoLive;

    @FindBy(xpath = "//*[contains(text(),'Please choose a product')]")
    public WebElement mandatoryPleaseChooseAProduct;

    @FindBy(xpath = "//*[@type='radio']")
    public WebElement radProduct;



}
