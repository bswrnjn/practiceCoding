package automationFramework;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.thoughtworks.selenium.Wait;

public class FirstTestCase {

    private static WebDriver driver = null;

    public static void main(String[] args) {

        // Create a new instance of the Firefox driver

        driver = new FirefoxDriver();

        //Put a Implicit wait, this means that any search for elements on the page could take the time the implicit wait is set for before throwing exception

       // driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //Launch the demo store

       // driver.get("http://www.store.demoqa.com");
       // driver.get("http://www.google.com");
        driver.get("http://www.gmail.com");
        
        //Get title of the page
        
        String x = driver.getTitle();
        
        System.out.println(x);
        
        /*String a = driver.getPageSource();
        System.out.println(a);*/
        
        //Get curret url of the page
        
        String y = driver.getCurrentUrl();
        
        System.out.println(y);

        // Find the element that's ID attribute is 'account'(My Account) 

       // driver.findElement(By.xpath(".//*[@id='menu-item-55']/a")).click();
        
      //  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
        driver.manage().window().maximize();
        
        String z = driver.getCurrentUrl();
        
        System.out.println(z);

        // Find the element that's ID attribute is 'log' (Username)

        // Enter Username on the element found by above desc.

       // driver.findElement(By.id("log")).sendKeys("testuser_1@test.com"); 

        // Find the element that's ID attribute is 'pwd' (Password)

        // Enter Password on the element found by the above desc.

       // driver.findElement(By.id("pwd")).sendKeys("Test@123");

        // Now submit the form. WebDriver will find the form for us from the element 
        
        /*//Code for google search
        driver.findElement(By.xpath("//input[@name='q']")).sendKeys("selenium");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        
        WebElement table = driver.findElement(By.className("sbdd_b"));
        List<WebElement> rows = driver.findElements(By.tagName("li"));
        for(WebElement results : rows){
        	String res = results.getText();
        	System.out.println(res);
        } */
        
        FirefoxProfile ffpath = new FirefoxProfile();
        ffpath.setAcceptUntrustedCertificates(true);
        //WebDriver driver = new FirefoxDriver(ffpath);
        driver.findElement(By.xpath("//input[@id='Email']")).sendKeys("abc@gmail.com");
        
      //SSL in Chrome
        /*@BeforeClass
        public void setUp() {
    		DesiredCapabilities capability = DesiredCapabilities.chrome();
    		// To Accept SSL certificate
    		capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
    		// setting system property for Chrome browser
    		System.setProperty("webdriver.chrome.driver", "E:/chromedriver.exe");
    		// create Google Chrome instance and maximize it
    		driver = new ChromeDriver(capability);
    		driver.manage().window().maximize();
    	}
        */
        
        //SSL in IE
        /*System.setProperty("webdriver.ie.driver", "D:/IEDriverServer.exe");
        driver = new InternetExplorerDriver();
        driver.manage().window().maximize();
        driver.get("https://cacert.org/");
        driver.get("javascript:document.getElementById('overridelink').click();");*/
        
        /*Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        robot.keyPress(KeyEvent.VK_ENTER);
        */
        
        WebDriverWait wait  = new WebDriverWait(driver, 10);
     //   wait.until(ele.isDisplayed());

       // driver.findElement(By.id("login")).click();

        // Print a Log In message to the screen

      //  System.out.println(" Login Successfully, now it is the time to Log Off buddy.");

        // Find the element that's ID attribute is 'account_logout' (Log Out)

        //driver.findElement (By.xpath(".//*[@id='account_logout']/a")).click();

        // Close the driver

        driver.quit();

            }

    }