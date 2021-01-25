package jp.co.rakuten.travel.framework.app.ios;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.ios.IOSDriver;

/**
 * Interface for wrapping WebDriver with specified parameters and configuration for known browsers
 *
 */
public interface Ios
{
    IOSDriver< ? > driver();

    String name();

    void onSetupCapabilities( DesiredCapabilities capabilities );

    void setupCapabilities();

    void onSetupDriver();
}
