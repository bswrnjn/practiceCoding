package jp.co.rakuten.travel.framework.app.android;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;

/**
 * Interface for wrapping WebDriver with specified parameters and configuration for known browsers
 *
 */
public interface Android
{
    AndroidDriver< ? > driver();

    String name();

    void onSetupCapabilities( DesiredCapabilities capabilities );

    void setupCapabilities();

    void onSetupDriver();
}
