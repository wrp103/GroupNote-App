import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class GroupNoteTestV2 {

    public static AndroidDriver driver;
    public static WebDriverWait wait;

    @BeforeMethod
    public void setUp() throws MalformedURLException {

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("deviceName", "Pixel 4 XL API 30");
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("automationName", "UiAutomator2");
        desiredCapabilities.setCapability("platformVersion", "11");
        desiredCapabilities.setCapability("appPackage","com.billpotter.groupnote");
        desiredCapabilities.setCapability("appActivity","com.billpotter.groupnote.MainActivity");
        desiredCapabilities.setCapability("skipUnlock", "false");
        desiredCapabilities.setCapability("ensureWebviewsHavePages", true);
        URL remoteUrl = new URL("http://localhost:4723/wd/hub");
        driver = new AndroidDriver(remoteUrl, desiredCapabilities);
        wait = new WebDriverWait(driver, 10);
    }

    // log the user into the application
    public void login() {

        // enter username
        By username = By.id("com.billpotter.groupnote:id/txtUsernameLogin");
        wait.until(ExpectedConditions.visibilityOfElementLocated(username)).click();
        WebElement el1 = driver.findElementById("com.billpotter.groupnote:id/txtUsernameLogin");
        el1.sendKeys("bill");
        el1.click();

        // enter password
        WebElement el2 = driver.findElementById("com.billpotter.groupnote:id/txtPasswordLogin");
        el2.sendKeys("bill123");

        // click login
        WebElement el3 = driver.findElementById("com.billpotter.groupnote:id/btnLogin");
        el3.click();
    }

    public void logout() {

        driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
        // click menu button
        By menuButton = By.className("android.widget.ImageView");
        wait.until(ExpectedConditions.visibilityOfElementLocated(menuButton)).click();
        // click logout
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        WebElement el5 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView");
        el5.click();
        // close app
        driver.closeApp();

    }


    // test to create a group and add a user to it
    @Test
    public void createGroup() {

        // log the user into the app
       login();

        // click create new group
        By createButton = By.id("com.billpotter.groupnote:id/menuCreateGroup");
        wait.until(ExpectedConditions.visibilityOfElementLocated(createButton)).click();

        // wait for create screen to appear
        By createScreen = By.id("com.billpotter.groupnote:id/txtGroupTitle");
        wait.until(ExpectedConditions.visibilityOfElementLocated(createScreen)).click();

        // enter group name
        WebElement el4 = driver.findElementById("com.billpotter.groupnote:id/txtGroupTitle");
        el4.sendKeys("testgroup7");

        // press create button
        WebElement el5 = driver.findElementById("com.billpotter.groupnote:id/btnCreateGroup");
        el5.click();

        // wait for add user screen to appear
        By addUserScreen = By.id("com.billpotter.groupnote:id/txtUsernameAdd");
        wait.until(ExpectedConditions.visibilityOfElementLocated(addUserScreen)).click();

        // add user to group
        WebElement el6 = driver.findElementById("com.billpotter.groupnote:id/txtUsernameAdd");
        el6.sendKeys("h");

        // click add user button
        WebElement el7 = driver.findElementById("com.billpotter.groupnote:id/btnAddUser");
        el7.click();


        // log the user out of the app
       logout();

    }



    // test to add a user to an existing group
    @Test
    public void addUser() {

        // log the user into the app
        login();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        // click on first group
        WebElement group = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.ListView/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.Button[1]");
        group.click();

        // click on add user
        By userToAdd = By.id("com.billpotter.groupnote:id/txtUsernameAdd");
        wait.until(ExpectedConditions.visibilityOfElementLocated(userToAdd)).click();

        // type in username
        WebElement el1 = driver.findElementById("com.billpotter.groupnote:id/txtUsernameAdd");
        el1.sendKeys("d");

        // add user
        WebElement el2 = driver.findElementById("com.billpotter.groupnote:id/btnAddUser");
        el2.click();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);


        // log the user out of the app
        logout();

    }




        // test to edit an existing note
        @Test
        public void editNote() {

            // log the user into the app
            login();

            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

            // click on manage notes
            WebElement el2 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.ListView/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.Button[2]");
            el2.click();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // click on edit note
            WebElement el3 = driver.findElementByXPath("(//android.widget.ImageView[@content-desc=\"edit button\"])[1]");
            el3.click();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // edit note title
            WebElement el4 = driver.findElementById("com.billpotter.groupnote:id/txtNoteTitle");
            el4.click();
            el4.sendKeys("update title");
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // edit note body
            WebElement el5 = driver.findElementById("com.billpotter.groupnote:id/txtNoteBody");
            el5.click();
            el5.sendKeys("update body");
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // click save
            WebElement el6 = driver.findElementById("com.billpotter.groupnote:id/btnSaveNote");
            el6.click();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);


            // log the user out of the app
            logout();

        }



        // test to delete an existing note
        @Test
        public void deleteNote() {

            // log the user into the app
            login();

            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // click on manage notes
            WebElement el2 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget" +
                    ".FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.ListView/android.widget." +
                    "LinearLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.Button[2]");
            el2.click();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // click on delete note
            WebElement el1 = driver.findElementByXPath("(//android.widget.ImageView[@content-desc=\"delete button\"])[1]");
            el1.click();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);


            // log the user out of the app
            logout();

        }



        // test to create a new user account and fill in the new profile
        @Test
        public void createAccount() {

            // click create account
            WebElement el1 = driver.findElementById("com.billpotter.groupnote:id/btnCreateAcc");
            el1.click();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // enter username
            WebElement el2 = driver.findElementById("com.billpotter.groupnote:id/txtUsernameRegister");
            el2.click();
            el2.sendKeys("testusername6");
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // enter password
            WebElement el3 = driver.findElementById("com.billpotter.groupnote:id/txtPasswordRegister");
            el3.click();
            el3.sendKeys("testpassword");
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // enter email address
            WebElement el4 = driver.findElementById("com.billpotter.groupnote:id/txtEmailRegister");
            el4.click();
            el4.sendKeys("testemail@email.com");
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // click register
            WebElement el5 = driver.findElementById("com.billpotter.groupnote:id/btnRegister");
            el5.click();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // enter firstname
            WebElement el6 = driver.findElementById("com.billpotter.groupnote:id/txtFirstname");
            el6.click();
            el6.sendKeys("testfirstname");
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // enter lastname
            WebElement el7 = driver.findElementById("com.billpotter.groupnote:id/txtLastname");
            el7.click();
            el7.sendKeys("testlastname");
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // enter phone number
            WebElement el8 = driver.findElementById("com.billpotter.groupnote:id/txtTelephone");
            el8.sendKeys("01234567899");
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // save profile
            WebElement el9 = driver.findElementById("com.billpotter.groupnote:id/btnSaveProfile");
            el9.click();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // log the user out of the app
            logout();

        }

        // test to update an existing profile
        @Test
        public void updateProfile() {

            // log the user into the app
            login();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // open menu
            By menuButton = By.className("android.widget.ImageView");
            wait.until(ExpectedConditions.visibilityOfElementLocated(menuButton)).click();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // click on manage profile
            WebElement el2 = (MobileElement) driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout" +
                    "/android.widget.ListView/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView");
            el2.click();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // update firstname
            WebElement el3 = driver.findElementById("com.billpotter.groupnote:id/txtFirstname");
            el3.click();
            el3.sendKeys("firstname");
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // update surname
            WebElement el4 = driver.findElementById("com.billpotter.groupnote:id/txtLastname");
            el4.click();
            el4.sendKeys("surname");
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // update telephone
            WebElement el5 = driver.findElementById("com.billpotter.groupnote:id/txtTelephone");
            el5.click();
            el5.sendKeys("12345678910");
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // click save
            WebElement el6 = driver.findElementById("com.billpotter.groupnote:id/btnSaveProfile");
            el6.click();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            // log the user out of the app
            logout();

        }

    @AfterMethod
    public void tearDown() {
        // close the app after each test
        driver.quit();
    }
}