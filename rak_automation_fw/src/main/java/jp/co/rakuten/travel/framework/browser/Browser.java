package jp.co.rakuten.travel.framework.browser;

import org.openqa.selenium.WebDriver;

import net.lightbody.bmp.core.har.Har;

/**
 * Interface for wrapping WebDriver with specified parameters and configuration for known browsers
 *
 */
public interface Browser
{
    WebDriver driver();

    String name();

    void setPageTimeout( int seconds );

    void setElementTimeout( int seconds );

    Har getHar();

    /**
     * for sauce labs usage
     */
    void printSessionId();

}
