package jp.co.rakuten.travel.framework.browser;

import static org.openqa.selenium.remote.CapabilityType.BROWSER_NAME;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;

public final class ChromeBrowser extends BrowserImpl
{
    private final String CHROME_HOME   = "CHROME_HOME";
    private final String CHROME_DRIVER = "webdriver.chrome.driver";

    protected ChromeBrowser( File pacFile )
    {
        super( pacFile );
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
        String driverLocation = System.getenv().get( CHROME_HOME );
        if( StringUtils.isEmpty( driverLocation ) )
        {
            LOG.fatal( "Set System property of " + CHROME_HOME + " to actual location of chromedriver" );
        }
        System.setProperty( CHROME_DRIVER, driverLocation );
        capabilities.setCapability( BROWSER_NAME, BrowserType.CHROME );
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
            m_driver = new ChromeDriver( m_capabilities );
        }
    }
}
