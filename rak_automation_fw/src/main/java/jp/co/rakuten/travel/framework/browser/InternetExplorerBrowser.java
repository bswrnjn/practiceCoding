package jp.co.rakuten.travel.framework.browser;

import static org.openqa.selenium.remote.CapabilityType.BROWSER_NAME;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public final class InternetExplorerBrowser extends BrowserImpl
{

    protected InternetExplorerBrowser( File pacFile )
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
        capabilities.setCapability( InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true );
        capabilities.setCapability( InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true );

        capabilities.setCapability( BROWSER_NAME, BrowserType.INTERNET_EXPLORER );
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
            m_driver = new InternetExplorerDriver( m_capabilities );
        }
    }
}
