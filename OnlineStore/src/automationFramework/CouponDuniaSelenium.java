package automationFramework;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


public class CouponDuniaSelenium {
	
	private static WebDriver driver = null;

    public static void main(String[] args) {
    	
    	driver = new FirefoxDriver();
    	driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    	
    	driver.get("http://selenium.couponapitest.com");
    	System.out.println("Page title is :" + driver.getTitle());
    	
    	//Crawling all the links on page
    	List<WebElement> linkList = driver.findElements(By.tagName("a"));
    	
    	System.out.println("Total no. of links are " + linkList.size());
    	
    	
    	for(WebElement ele : linkList){
    		System.out.println(ele.getText());
    	}
    	
    	//page1 link verification
    	WebElement page1 = driver.findElement(By.linkText("Page 1"));
    	page1.click();
    	try{
    	Assert.assertTrue("Page title not displayed", !driver.getTitle().isEmpty());
    	}
    	catch(AssertionError e)
        {
            System.out.println("Assertion error: Page title not displayed ");
        }
    	
    	
    	driver.navigate().back();
    	
    	//page2 link verification
    	WebElement page2 = driver.findElement(By.linkText("Page 2"));
    	page2.click();
    	try{
    	Assert.assertTrue("php error exists on page", !driver.getPageSource().contains("error") && !driver.getPageSource().contains("php"));
    	}
    	catch(AssertionError e)
        {
            //System.out.println("Assertion error: php error ");
    		System.out.println(e.getMessage());
        }
    	
    	driver.navigate().back();
    	
    	//page3 link verification
    	WebElement page3 = driver.findElement(By.linkText("Page 3"));
    	page3.click();
    	try{
    	Assert.assertFalse("alternate text not found", driver.findElement(By.tagName("img")).getAttribute("alt")!=null);
    		//System.out.println("attribute value :"+ driver.findElement(By.tagName("img")).getAttribute("alt"));
    	}
    	catch(AssertionError e)
        {
            System.out.println("Assertion error: alternate text not found ");
        }
    	
    	    	
    	driver.navigate().back();
    	
    	//page4 link verification
    	WebElement page4 = driver.findElement(By.linkText("Page 4"));
    	page4.click();
    	try{
    	Assert.assertTrue("404 error message", !driver.getPageSource().contains("404") || !driver.getPageSource().contains("Not Found"));
    		//System.out.println("attribute value :"+ driver.findElement(By.tagName("img")).getAttribute("alt"));
    	}
    	catch(AssertionError e)
        {
            System.out.println(e.getMessage());
        }
    	
    	driver.navigate().back();
    	
    	driver.quit();
    	
    	
    }
   
   
}

