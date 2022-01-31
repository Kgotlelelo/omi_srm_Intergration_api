package omi_ri.webPageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;


public class pageObjectTermsAndConditions {

    protected WebDriver driver;

    public pageObjectTermsAndConditions(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    @FindBy(xpath = "//*[@id='edit-submit']")
    public WebElement btnSubmit;

    @FindBy(xpath = "//*[@type='checkbox']")
    public WebElement chkTermsAndConditions;

    @FindBy(xpath = "//*[contains(text(),'Please accept our terms')]")
    public WebElement mandatoryAcceptTermsAndConditions;
}
