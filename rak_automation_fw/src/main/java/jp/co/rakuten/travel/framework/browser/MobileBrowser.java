package jp.co.rakuten.travel.framework.browser;

import static org.openqa.selenium.remote.CapabilityType.BROWSER_NAME;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import jp.co.rakuten.travel.framework.utility.Utility;

public final class MobileBrowser extends BrowserImpl
{
    BrowserType m_driverBrowserType;

    protected MobileBrowser( File pacFile )
    {
        super( pacFile );
        switch( Utility.getEnum( TestApiObject.instance().get( TestApiParameters.API_BROWSER ), BrowserType.class ) )
        {
        case MOBILE_FIREFOX:
            m_driverBrowserType = BrowserType.FIREFOX;
            break;
        case MOBILE_CHROME:
            m_driverBrowserType = BrowserType.CHROME;
            break;
        default:
            break;
        }
    }

    @Override
    protected void onSetupProxy( Proxy proxy )
    {
        /**
        * No changes necessary for proxy
        */
    }

    @Override
    protected void onSetupCapabilities( DesiredCapabilities capabilities )
    {

        capabilities.setCapability( BROWSER_NAME, m_driverBrowserType );
    }

    @Override
    protected void onSetupDriver()
    {
        if( Boolean.getBoolean( TestApiObject.instance().get( TestApiParameters.API_SAUCE_LABS_ENABLED ) ) )
        {
            String username = TestApiObject.instance().get( TestApiParameters.API_SAUCE_LABS_USERNAME );
            String password = TestApiObject.instance().get( TestApiParameters.API_SAUCE_LABS_PASSWORD );
            try
            {
                m_driver = new RemoteWebDriver( new URL( "http://" + username + ":" + password + "@ondemand.saucelabs.com:80/wd/hub" ), m_capabilities );
                printSessionId();
            }
            catch( MalformedURLException e )
            {
                throw new WebDriverException( "Original Exception from " + e.getClass().getSimpleName(), e );
            }
        }
        else
        {
            switch( m_driverBrowserType )
            {
            case FIREFOX:
                m_driver = new FirefoxDriver( m_capabilities );
                UserAgentType userAgent = Utility.getEnum( TestApiObject.instance().get( TestApiParameters.API_USER_AGENT ), UserAgentType.class );
                m_driver.manage().window().setSize( new Dimension( userAgent.width(), userAgent.length() ) );
                break;
            case CHROME:
                try
                {
                    m_driver = new ChromeDriver( m_capabilities );
                }
                catch( Exception e )
                {
                    LOG.warn( "Please check Environment Variables for chromedriver" );
                    LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
                    if( m_driver == null )
                    {
                        throw new NullPointerException( "Browser not supported " + m_driverBrowserType );
                    }
                }
                break;
            default:
                break;
            }
        }
    }

    @Override
    protected void setupCapabilities()
    {
        LOG.info( "setupCapabilities" );
        m_capabilities = new DesiredCapabilities();
        UserAgentType userAgent = Utility.getEnum( TestApiObject.instance().get( TestApiParameters.API_USER_AGENT ), UserAgentType.class );

        m_capabilities.setBrowserName( TestApiObject.instance().get( TestApiParameters.API_BROWSER ) );
        m_capabilities.setVersion( TestApiObject.instance().get( TestApiParameters.API_BROWSER_VERSION ) );

        if( Boolean.getBoolean( TestApiObject.instance().get( TestApiParameters.API_SAUCE_LABS_ENABLED ) ) )
        {
            String username = TestApiObject.instance().get( TestApiParameters.API_SAUCE_LABS_USERNAME );
            String version = TestApiObject.instance().get( TestApiParameters.API_BROWSER_VERSION );

            m_capabilities.setCapability( PLATFORM, TestApiObject.instance().get( TestApiParameters.API_SAUCE_LABS_PLATFORM ) );
            m_capabilities.setCapability( "name", username );
            m_capabilities.setCapability( "build", version );
        }
        else
        {
            m_capabilities.setCapability( PLATFORM, Platform.ANY );
            m_capabilities.setCapability( CapabilityType.PROXY, m_proxy );
        }
        switch( m_driverBrowserType )
        {
        case FIREFOX:
            FirefoxProfile customProfile = new FirefoxProfile();
            customProfile.setPreference( "general.useragent.override", userAgent.val() );
            m_capabilities.setCapability( FirefoxDriver.PROFILE, customProfile );
            m_capabilities.setCapability( CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT );
            m_capabilities.setCapability( CapabilityType.ACCEPT_SSL_CERTS, true );
            m_capabilities.setCapability( CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true );
            break;
        case CHROME:
            Map< String, String > mobileEmulation = new HashMap< String, String >();
            mobileEmulation.put( "deviceName", userAgent.val() );
            Map< String, Object > chromeOptions = new HashMap< String, Object >();
            chromeOptions.put( "mobileEmulation", mobileEmulation );
            m_capabilities.setCapability( ChromeOptions.CAPABILITY, chromeOptions );
            break;
        default:
            break;
        }
        onSetupCapabilities( m_capabilities );
    }
}
