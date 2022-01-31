package omi_ri.webPageObject;


import omi_ri.webFunctions.webActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class pageObjectEcosystemsAPIWeb extends webActions {

    protected WebDriver driver;

    public pageObjectEcosystemsAPIWeb(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    //Start Email Objects
    @FindBy(css="#identifierId")
    private WebElement emailFld;

    @FindBy(name="password")
    private WebElement passwordFld;

    @FindBy(xpath="//span[text()='Next']")
    private WebElement nextBtn;

    //span[@class='bog']//child::span[text()='Support Account Password Assistance']
    @FindBy(xpath="//span[@class='bog']//child::span[text()='Reset your Vodacom API portal password']")
    private WebElement inboxResertPasswordLink;

    @FindBy(xpath="//a[text()[contains(.,'Reset Password')]]")
    private WebElement resertPasswordLink;

    //Reset my password
    @FindBy(linkText="Reset Password")
    private WebElement resentLink;

    public void enterGmailDetails(String email, String password) throws IOException, ParserConfigurationException, SAXException {

        waitForElement(emailFld,10,driver);
        this.emailFld.sendKeys(email);
        clickNextButton();
        waitForElement(passwordFld,10,driver);
        this.passwordFld.sendKeys(password);

    }

    public void clickNextButton() {

        this.nextBtn.click();
    }

    public void clickResertInBoxlink() throws IOException, ParserConfigurationException, SAXException {

        waitForElement(inboxResertPasswordLink,10,driver);
        this.inboxResertPasswordLink.click();
    }

    public void clickResetLink() throws IOException, ParserConfigurationException, SAXException {

        waitForElement(resentLink,10,driver);
        this.resentLink.click();
    }

    //End Email Objects
    @FindBy(xpath = "(//a[@class='user-ico-space'])[1]")
    public WebElement  profileIcon;

    @FindBy(xpath = "(//a[text()[contains(.,'My Profile')]])[1]")
    public WebElement  myprofile;

    @FindBy(id="edit-pass-pass1")
    public WebElement editPasswordOne;

    @FindBy(id="edit-pass-pass2")
    public WebElement editPasswordTwo;

    @FindBy(xpath = "(//*[contains(text(),'Log in')])[1]")
    public WebElement lblLogin;

    @FindBy(xpath = "//*[@autocorrect='none']")
    public WebElement email;

    @FindBy(xpath = "//*[@name='pass']")
    public WebElement password;
    
    @FindBy(xpath = "(//input[@value='Log in'])[2]")
    public WebElement loginBtn;

    @FindBy(xpath = "(//*[text()[contains(.,'Log out')]])[1]")
    public WebElement logoutBtn;

    @FindBy(xpath = "//*[contains(text(),'Track my order')]")
    public WebElement lblTrackMyOrder;


    @FindBy(xpath = "(//*[contains(text(),'Upgrade')])[1]")
    public WebElement lblUpgrade;

    @FindBy(xpath = "//*[contains(text(),'Let’s see if you can upgrade today')]")
    public WebElement lblHereToUpgrade;


    @FindBy(xpath = "//*[contains(text(),'Vodacom World Mall')]")
    public WebElement lblVocacomWorlMall;

    @FindBy(xpath = "//*[contains(text(),'Frequently asked questions')]")
    public WebElement lblFrequesntlyAskedQuestions;

    @FindBy(xpath = "//*[@class='search-block-form widget block']")
    public WebElement searchIcon;

    @FindBy(xpath = "//*[@class='text-field field']")
    public WebElement searchItem;

    @FindBy(xpath = "//*[@class='btn btn-search']")
    public WebElement btnSearch;


    @FindBy(xpath = "//*[text()[contains(.,'Forgot Password?')]]")
    public WebElement forgotPasswordLnk;

    @FindBy(xpath = "//*[@value='Send Link']")
    public WebElement sendLnk;

    @FindBy(xpath = "//*[@type='email']")
    public WebElement emailTxt;

    @FindBy(xpath = "//*[text()[contains(.,'Status message')]]")
    public WebElement ressertArlet;

    //Contact us
    @FindBy(xpath = "(//a[contains(text(),'Contact Us')])[1]")
    public WebElement lnkContactUs;

    @FindBy(xpath = "//input[@id ='edit-name']")
    public WebElement name;

    @FindBy(xpath = "//span[@class='valid-help-error'][text()[contains(.,'Please enter Name')]]")
    public WebElement menditoryname;

    @FindBy(xpath = "//input[@id ='edit-surname']")
    public WebElement surname;

    @FindBy(xpath = "//span[@class='valid-help-error'][text()[contains(.,'Please enter Surname')]]")
    public WebElement menditorysurname;

    @FindBy(xpath = "//input[@id ='edit-organisation-name']")
    public WebElement organisation;

    @FindBy(xpath = "//span[@class='valid-help-error'][text()[contains(.,'Please enter organisation name')]]")
    public WebElement menditoryorganisation;

    @FindBy(xpath = "//input[@id ='edit-email']")
    public WebElement editemail;

    @FindBy(xpath = "//span[@class='valid-help-error'][text()[contains(.,'Please enter your email')]]")
    public WebElement menditoryeditemail;

    @FindBy(xpath = "//input[@id ='edit-cellphone-number']")
    public WebElement  cellphonenumber;

    @FindBy(xpath = "//span[@class='valid-help-error'][text()[contains(.,'Please enter cellphone number')]]")
    public WebElement menditorycellphonenumber;

    @FindBy(xpath = "//textarea[@id ='edit-leave-us-a-note-']")
    public WebElement  leavenote;

    @FindBy(xpath = "//span[@class='valid-help-error'][text()[contains(.,'Please enter your message')]]")
    public WebElement menditoryleavenote;

    @FindBy(xpath = "(//input[@id ='edit-submit'])[4]")
    public WebElement  submit;



    //PersonalDetails


    @FindBy(xpath = "//input[@id='edit-first-name-0-value']")
    public WebElement personalname;

    @FindBy(xpath = "//input[@id='edit-last-name-0-value']")
    public WebElement personallastname;

    @FindBy(xpath = "//input[@id='edit-field-email-address-0-value']")
    public WebElement personalemail;

    @FindBy(xpath = "//input[@id='edit-field-cellphone-number-0-value']")
    public WebElement personalphone;

    @FindBy(xpath = " //select[@id='edit-field-id-type']/child::option")
    public List<WebElement> personalID;

    @FindBy(xpath = "//input[@id='edit-field-id-number-0-value']")
    public WebElement personalIDNo;

    @FindBy(xpath = "(//input[@id='edit-submit'])[1]")
    public WebElement Save;



    //BusinessDetails


    //ChangePassword
    @FindBy(xpath = "//a[@href='#edit-group-change-']/child::span")
    public WebElement lnkChangePassword;

    @FindBy(xpath = "//input[@id='edit-current-pass']")
    public WebElement currentpassowrd;

    @FindBy(xpath = "//input[@id='edit-pass-pass1']")
    public WebElement newpassowrd;

    @FindBy(xpath = "//input[@id='edit-pass-pass2']")
    public WebElement confirmnewpassowrd;

    @FindBy(xpath = "//span[@class='valid-help-error'][text()[contains(.,'Please enter Current password')]]")
    public WebElement menditorycurrentpassowrd;

    @FindBy(xpath = "//span[@class='valid-help-error'][text()[contains(.,'Please enter New Password')]]")
    public WebElement menditorynewpassowrd;

    @FindBy(xpath = "//span[@class='valid-help-error'][text()[contains(.,'Please enter Confirm New Password')]]")
    public WebElement menditoryconfirmnewpassowrd;


    //searchTab
    @FindBy(xpath = "(//a[text()='FAQs'])[2]")
    public WebElement lnkFAQs;

    @FindBy(xpath = "//label[text()='Type keywords to find answers']/following::input[1]")
    public WebElement searchFld;

    @FindBy(xpath = "(//*[@class='faq-title'])[1]")
    public WebElement lblresult;

    @FindBy(xpath = "//div[@class='col-message']")
    public WebElement lblNotifications;






}
