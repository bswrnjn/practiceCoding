package automationFramework;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.internal.WrapsDriver;

public class Makemytrip {
	
	private static WebDriver driver = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.get("http://www.makemytrip.com");
        long startTime = System.currentTimeMillis();
        System.out.println(startTime);
        driver.findElement(By.xpath("//li[@class='chf_login_link']//span//span[3]")).click();
       // driver.findElement(By.xpath("//div[@id='loginWrapper']//form[@id='login_form_0']//p[1]//span[1]//input")).sendKeys("bsw.095@gmail.com");
       // driver.findElement(By.xpath("//div[@id='loginWrapper']//form[@id='login_form_0']//p[2]//span[1]//input[2]")).sendKeys("12340000");
        
        
       // JavascriptExecutor jse = (JavascriptExecutor) driver;
       // jse.executeScript("document.getElementById('loginWrapper').value = 'sunilrathore77@gmail.com';");
        
        /*public static void setAttribute(WebElement element, String attributeName, String value)
        { 
        	WrapsDriver wrappedElement = (WrapsDriver) element;
        JavascriptExecutor driver = (JavascriptExecutor)wrappedElement.getWrappedDriver();
        driver.executeScript("arguments[0].setAttribute(arguments[1],arguments[2])", element, attributeName, value); }*/

    /*    public static void setAttribute(WebElement element, String attributeName, String value)

        {

        WrapsDriver wrappedElement = (WrapsDriver) element;

        JavascriptExecutor driver = (JavascriptExecutor)wrappedElement.getWrappedDriver();

        driver.executeScript("arguments[0].setAttribute(arguments[1],arguments[2])", element, attributeName, value);

        }*/
	}

}
