package jp.co.rakuten.travel.framework.page;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;

import net.lightbody.bmp.core.har.Har;

/**
 * Wrapped Handler for operations of elements within the page using WebDriver
 *
 */
public interface WebController extends UIController
{
    /* BROWSER */

    /**
     *
     * @return URL current URL of the controller
     * @throws MalformedURLException URL protocol/format/path issue
     */
    URL url() throws MalformedURLException;

    /**
     * Loads the homepage. As per {@link WebDriver}, this is a blocking method
     * <br>Reverts the current URL into home URL
     */
    void homepage();

    /**
     * Reloads the current URL. . As per {@link WebDriver}, this is a blocking method.
     * @return TRUE if successful and FALSE otherwise
     */
    boolean reload();

    /**
     * Loads the specified URL. As per {@link WebDriver}, this is a blocking method.
     * <br>Sets the current URL into specified URL
     * @param url Url string
     * @return TRUE if successful and FALSE otherwise
     */
    boolean browse( String url );

    /**
     * Checks if the current URL returns HTTP 200 response
     * @return TRUE if successful and FALSE otherwise
     */
    void isPageLoaded();

    /**
     * 
     * @return The current URL of the working page
     */
    String currentUrl();

    String javaScript( String script, Object ... args );

    Cookie getCookie( String name );

    Set< Cookie > getCookies();

    boolean waitForStyle( ServiceElement element, String id, String attr, String value, boolean exact );

    boolean waitForStyle( SearchContext context, ServiceElement element, String id, String attr, String value, boolean exact );

    Har getHar();

    /**
     * Click element by JavaScript
     * @param element WebElement
     * @return TRUE if successful and FALSE otherwise
     */
    boolean clickByJs( SearchContext element );

    /**
     * uses small java script to move the screen by 250px (default) up
     */
    abstract void scrollUp();

    /**
     * uses small java script to move the screen by 250px (default) down
     */
    abstract void scrollDown();
}
