package jp.co.rakuten.travel.framework.app.android;

import static org.openqa.selenium.remote.CapabilityType.BROWSER_NAME;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;
import org.testng.SkipException;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.Connection;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import jp.co.rakuten.travel.framework.browser.ApiErrorInfo;
import jp.co.rakuten.travel.framework.browser.BrowserType;
import jp.co.rakuten.travel.framework.configuration.ConfigurationFactory;
import jp.co.rakuten.travel.framework.configuration.Equipment;
import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.Result;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import jp.co.rakuten.travel.framework.utility.ErrorType;
import jp.co.rakuten.travel.framework.utility.Ftp;
import jp.co.rakuten.travel.framework.utility.Utility;

public class AndroidImpl implements Android, Equipment
{
    protected TestLogger          LOG = (TestLogger)TestLogger.getLogger( this.getClass() );

    protected Proxy               m_proxy;
    protected DesiredCapabilities m_capabilities;

    protected File                m_pacFile;

    protected AndroidDriver< ? >  m_driver;

    public AndroidImpl()
    {
        InputStream pacFileStream = null;
        pacFileStream = ConfigurationFactory.class.getClassLoader().getResourceAsStream( TestApiObject.instance().get( TestApiParameters.API_FILE_PROXY ) );
        try
        {
            pacFileStream = ConfigurationFactory.class.getClassLoader().getResourceAsStream( TestApiObject.instance().get( TestApiParameters.API_FILE_PROXY ) );

            if( pacFileStream == null )
            {
                throw new FileNotFoundException( "Pac file not found : " + TestApiObject.instance().get( TestApiParameters.API_FILE_PROXY ) );
            }
            // [patch] create temporary file.
            // TODO : After upgarding to Selenium 3.0, convert pac file into appropriate data structure and feed it to browser.
            m_pacFile = Utility.createTempFileFromStream( pacFileStream, "pac" );
        }
        catch( IOException exception )
        {
            LOG.error( "Error occurred while creating equipment with cause : " + exception.getMessage() );
        }

    }

    @Override
    public void init()
    {
        LOG.info( "init" );
        setupProxy();
        setupCapabilities();
        onSetupDriver();

    }

    protected void setupProxy()
    {
        LOG.info( "setupProxy" );
    }

    protected void onSetupProxy( Proxy proxy )
    {}

    @Override
    public void onSetupCapabilities( DesiredCapabilities capabilities )
    {
        capabilities.setCapability( BROWSER_NAME, BrowserType.FIREFOX );
    }

    @Override
    public void setupCapabilities()
    {
        LOG.info( "setupCapabilities" );
        m_capabilities = new DesiredCapabilities();

        // FIXME Get the apk file from vm arguments
        String apkFileName = "app-stg.apk";
        if( !Ftp.downloadFile( apkFileName ) )
        {
            LOG.info( "downloadFile: " + apkFileName + " failed" );
        }

        m_capabilities.setCapability( PLATFORM, Platform.ANDROID );
        m_capabilities.setCapability( AndroidMobileCapabilityType.PROXY, m_proxy );
        m_capabilities.setCapability( MobileCapabilityType.DEVICE_NAME, "Android Emulator" );
        m_capabilities.setCapability( MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300 );
        m_capabilities.setCapability( MobileCapabilityType.LOCALE, "ja" );
        m_capabilities.setCapability( MobileCapabilityType.LANGUAGE, "ja" );
        m_capabilities.setCapability( MobileCapabilityType.FULL_RESET, true );
        m_capabilities.setCapability( MobileCapabilityType.APP, TestApiObject.instance().get( TestApiParameters.API_FTP_LOCAL_FILE_PATH ) + File.separator + apkFileName );
        m_capabilities.setCapability( AndroidMobileCapabilityType.APP_PACKAGE, "jp.co.rakuten.travel.andro.stg" );
        m_capabilities.setCapability( AndroidMobileCapabilityType.APP_ACTIVITY, "jp.co.rakuten.travel.andro.activity.Opening" );
        m_capabilities.setCapability( AndroidMobileCapabilityType.RECREATE_CHROME_DRIVER_SESSIONS, true );
        m_capabilities.setCapability( AndroidMobileCapabilityType.AVD, "Nexus" );
        m_capabilities.setCapability( AndroidMobileCapabilityType.SUPPORTS_JAVASCRIPT, true );
        m_capabilities.setCapability( AndroidMobileCapabilityType.UNICODE_KEYBOARD, true );
    }

    @Override
    public void onSetupDriver()
    {

        try
        {
            m_driver = new AndroidDriver<>( new URL( TestApiObject.instance().get( TestApiParameters.DRIVER_SERVER_URL ) ), m_capabilities );
            m_driver.setConnection( Connection.AIRPLANE );
            m_driver.setConnection( Connection.DATA );
        }
        catch( MalformedURLException e )
        {
            m_driver = new AndroidDriver<>( m_capabilities );
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
        }
    }

    @Override
    public void release()
    {
        LOG.info( "release" );
        m_driver.quit();

    }

    @Override
    public void recover()
    {
        LOG.info( "recover" );
        m_driver.resetApp();
    }

    @Override
    public void refresh()
    {
        LOG.info( "refresh" );
        m_driver.resetApp();
    }

    @Override
    public void errorInfo()
    {
        LOG.info( "errorInfo" );
        takeScreenshot();
        serverErrorConfirm();
    }

    @Override
    public AndroidDriver< ? > driver()
    {
        return m_driver;
    }

    @Override
    public String name()
    {
        return this.getClass().getSimpleName();
    }

    private void takeScreenshot()
    {
        m_driver.context( "NATIVE_APP" );

        String logdir = Reporter.getCurrentTestResult().getTestContext().getOutputDirectory();
        String filename = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName() + ".jpg";
        try
        {
            if( (new File( logdir + filename )).exists() )
            {
                filename = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName() + "_" + Utility.getCurrentTime( "ddmmss" ) + ".jpg";
            }
            /* Get entire page screenshot */
            File screenshot = m_driver.getScreenshotAs( OutputType.FILE );
            /* Copy the element screenshot to disk */
            File savedFile = new File( logdir + filename );
            FileUtils.copyFile( screenshot, savedFile );
            if( !savedFile.exists() )
            {
                LOG.warn( "Screenshot created but NOT saved " + logdir + filename );
            }
            else
            {
                LOG.info( filename + " screen shot saved in " + logdir );
            }
        }
        catch( WebDriverException | IOException | NullPointerException e )
        {
            LOG.warn( logdir + filename + " screen shot was unsuccessful." );
            LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
        }
    }

    private void serverErrorConfirm()
    {
        if( !TestApiObject.instance().bool( TestApiParameters.API_SERVER_ERROR_CHECKING ) )
        {
            return;
        }

        LOG.info( "serverErrorConfirm" );

        for( ApiErrorInfo errorInfo : ApiErrorInfo.values() )
        {
            WebElement element = null;

            try
            {
                element = m_driver.findElement( By.id( "message" ) );
            }
            catch( NoSuchElementException e )
            {
                continue;
            }

            LOG.warn( "Error found " + errorInfo );

            if( element != null )
            {
                Result result = Result.SKIP;
                switch( errorInfo )
                {
                case PROXY_DOWN:
                case PROXY_REFUSING_CONNECTIONS:
                    LOG.fatal( "Found Error Message in Page: " + element.getText(), ErrorType.PROXY_DOWN );
                    break;

                default:
                    LOG.fatal( "Found Error Message in Page: " + element.getText(), ErrorType.SERVER_ERROR );
                    break;
                }

                if( result.equals( Result.SKIP ) )
                {
                    throw new SkipException( "SKIPPED due to " + errorInfo.name().toLowerCase() );
                }
            }
        }
    }

}
