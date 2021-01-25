package jp.co.rakuten.travel.framework.browser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;
import org.testng.SkipException;

import jp.co.rakuten.travel.framework.configuration.Configuration;
import jp.co.rakuten.travel.framework.configuration.Equipment;
import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.FrameworkObject;
import jp.co.rakuten.travel.framework.parameter.FrameworkParameters;
import jp.co.rakuten.travel.framework.parameter.Result;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import jp.co.rakuten.travel.framework.utility.ErrorType;
import jp.co.rakuten.travel.framework.utility.Utility;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;

public abstract class BrowserImpl implements Browser, Equipment
{
    protected TestLogger          LOG = (TestLogger)TestLogger.getLogger( this.getClass() );

    protected Proxy               m_proxy;
    protected DesiredCapabilities m_capabilities;

    protected BrowserMobProxy     m_mobProxy;
    protected final File          m_pacFile;

    protected WebDriver           m_driver;

    protected BrowserImpl( File pacFile )
    {
        m_pacFile = pacFile;
    }

    @Override
    public final WebDriver driver()
    {
        return m_driver;
    }

    protected void setupProxy()
    {
        LOG.info( "setupProxy" );

        /**
         * prioritize mob proxy for network monitoring
         */
        if( TestApiObject.instance().bool( TestApiParameters.API_NETWORK_MONITOR ) )
        {
            setupBrowserMobProxy();
            m_proxy = ClientUtil.createSeleniumProxy( m_mobProxy );
        }
        /**
         * overridden proxy
         */
        else if( !StringUtils.isEmpty( FrameworkObject.instance().get( FrameworkParameters.FW_PROXY_HOST ) ) )
        {
            String proxy = FrameworkObject.instance().get( FrameworkParameters.FW_PROXY_HOST );
            LOG.info( "Setting up application proxy with " + proxy );

            String host = FrameworkObject.instance().get( FrameworkParameters.FW_PROXY_HOST );
            m_proxy = new Proxy() //
                    .setHttpProxy( host )
                    .setFtpProxy( host )
                    .setSslProxy( host );
        }
        /**
         * default windows proxy
         */
        else
        {
            LOG.info( "Default proxy to use PAC file from " + m_pacFile.getAbsolutePath() );
            m_proxy = new Proxy() //
                    .setProxyType( Proxy.ProxyType.PAC )
                    .setProxyAutoconfigUrl( m_pacFile.getAbsolutePath() );
        }

        onSetupProxy( m_proxy );
    }

    protected abstract void onSetupProxy( Proxy proxy );

    protected void setupCapabilities()
    {
        LOG.info( "setupCapabilities" );

        m_capabilities = new DesiredCapabilities();

        m_capabilities.setCapability( CapabilityType.PROXY, m_proxy );
        m_capabilities.setCapability( CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT );
        m_capabilities.setCapability( CapabilityType.ACCEPT_SSL_CERTS, true );
        m_capabilities.setCapability( CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true );
        onSetupCapabilities( m_capabilities );
    }

    protected abstract void onSetupCapabilities( DesiredCapabilities capabilities );

    protected void setupDriver()
    {
        onSetupDriver();
    }

    protected abstract void onSetupDriver();

    @Override
    public String name()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public void setPageTimeout( int seconds )
    {
        m_driver.manage().timeouts().pageLoadTimeout( seconds, TimeUnit.SECONDS );
    }

    @Override
    public void setElementTimeout( int seconds )
    {
        m_driver.manage().timeouts().implicitlyWait( seconds, TimeUnit.SECONDS );
    }

    @Override
    public void printSessionId()
    {
        if( Boolean.getBoolean( TestApiObject.instance().get( TestApiParameters.API_SAUCE_LABS_ENABLED ) ) )
        {
            // System out will be used ONLY for sauce labs
            // it is a requirement from sauce labs to have this line of output
            LOG.info( "SauceOnDemandSessionID=" + ((RemoteWebDriver)m_driver).getSessionId().toString() + " job-name=" + Configuration.SUITE_NAME );
        }
    }

    protected void setupBrowserMobProxy()
    {
        LOG.info( "setupBrowserMobProxy" );

        m_mobProxy = new BrowserMobProxyServer();
        String proxyHost = FrameworkObject.instance().get( FrameworkParameters.FW_PROXY_HOST );
        /**
         * Check whether upstream chained proxy is required, e.g. STG/DEV proxy
         * set Chain proxy as (String)host, (int)port if it is required, otherwise, skip
         * 
         */
        LOG.info( "Set up Upstream Chained Proxy : " + proxyHost );
        if( StringUtils.isEmpty( proxyHost ) )
        {
            LOG.info( "Skip Upstream Chained Proxy Setting" );
        }
        else
        {
            String host = FrameworkObject.instance().get( FrameworkParameters.FW_PROXY_HOST ).split( ":" )[ 0 ];
            int port = Integer.parseInt( FrameworkObject.instance().get( FrameworkParameters.FW_PROXY_HOST ).split( ":" )[ 1 ] );
            m_mobProxy.setChainedProxy( new InetSocketAddress( host, port ) );

        }

        m_mobProxy.setTrustAllServers( true );
        m_mobProxy.start();
        m_mobProxy.enableHarCaptureTypes( CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT );
        m_mobProxy.newHar();
    }

    @Override
    public void init()
    {
        LOG.info( "init" );
        setupProxy();
        setupCapabilities();
        setupDriver();
        // opening of this driver should be done in a separate thread to minimize wait time
        setPageTimeout( Integer.valueOf( TestApiObject.instance().get( TestApiParameters.API_PAGE_TIMEOUT ) ) );
        int elementTimeout = Utility.getInt( TestApiObject.instance().get( TestApiParameters.API_ELEMENT_TIMEOUT ) );
        if( elementTimeout > 0 )
        {
            setElementTimeout( Integer.valueOf( TestApiObject.instance().get( TestApiParameters.API_ELEMENT_TIMEOUT ) ) );
        }
        driver().manage().deleteAllCookies();
    }

    @Override
    public void release()
    {
        LOG.info( "release" );
        m_driver.quit();

        if( TestApiObject.instance().bool( TestApiParameters.API_NETWORK_MONITOR ) && m_mobProxy.isStarted() )
        {
            m_mobProxy.stop();
        }
    }

    @Override
    public Har getHar()
    {
        if( TestApiObject.instance().bool( TestApiParameters.API_NETWORK_MONITOR ) )
        {
            return m_mobProxy.newHar();
        }

        throw new NullPointerException( "HAR processing is NOT expected" );
    }

    @Override
    public void recover()
    {
        LOG.info( "recover" );
        release();
        init();
    }

    @Override
    public void refresh()
    {
        LOG.info( "refresh" );

        if( TestApiObject.instance().bool( TestApiParameters.API_NETWORK_MONITOR ) )
        {
            m_mobProxy.newHar();
        }

        driver().manage().deleteAllCookies();
    }

    @Override
    public void errorInfo()
    {
        LOG.info( "errorInfo" );
        printCurrentUrl();
        takeScreenshot();

        /**
         * save suite in a file
         */
        if( TestApiObject.instance().bool( TestApiParameters.API_PRINT_HTML_ON_ERROR ) )
        {
            try
            {
                saveHtml();
            }
            catch( FileNotFoundException e )
            {
                LOG.warn( "Error HTML File not saved for " + Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName(), e );
            }
        }
        serverErrorConfirm();
    }

    private void saveHtml() throws FileNotFoundException
    {
        LOG.info( "saveHtml" );
        String filename = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName() + "_error.html";
        String filepath = Reporter.getCurrentTestResult().getTestContext().getOutputDirectory() + File.separator + filename;
        try( PrintWriter out = new PrintWriter( filepath ) )
        {
            out.println( m_driver.getPageSource() );
            out.close();
            LOG.info( "Error HTML file saved in " + filepath );
        }
        catch( FileNotFoundException e )
        {
            throw e;
        }

    }

    private void printCurrentUrl()
    {
        LOG.warn( "Current URL \"" + m_driver.getCurrentUrl() + "\"" );
    }

    private void takeScreenshot()
    {
        String logdir = Reporter.getCurrentTestResult().getTestContext().getOutputDirectory();
        String filename = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName() + ".jpg";
        try
        {
            if( (new File( logdir + filename )).exists() )
            {
                filename = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName() + "_" + Utility.getCurrentTime( "ddmmss" ) + ".jpg";
            }
            /* Get entire page screenshot */
            File screenshot = ((TakesScreenshot)m_driver).getScreenshotAs( OutputType.FILE );
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
                element = m_driver.findElement( By.xpath( "(.//body/div//*[contains(text(), " + "'" + errorInfo.errorMsg() + "'" + ")])[1]" ) );
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
                case INPUT_CHECK_ERROR:
                    result = Result.FAIL;
                    LOG.fatal( "Found Error Message in Page: " + element.getText(), ErrorType.INPUT_CHECK_ERROR );
                    break;
                case PASSWORD_ERROR:
                    result = Result.FAIL;
                    LOG.fatal( "Found Error Message in Page: " + element.getText(), ErrorType.TEST_PARAMETERS_ERROR );
                    break;
                case UNAUTHORIZED_ERROR:
                    LOG.fatal( "Found Error Message in Page: " + element.getText(), ErrorType.UNAUTHORIZED_ERROR );
                    break;
                case PAGE_LOAD_ERROR:
                case DATA_RETRIEVAL_ERROR:
                    LOG.fatal( "Found Error Message in Page: " + element.getText(), ErrorType.PAGE_LOAD_ERROR );
                    break;
                case NO_TICKET_CAN_BE_BOOKED:
                case TEMPORARY_SERVER_ERROR:
                    LOG.fatal( "Found Error Message in Page: " + element.getText(), ErrorType.SERVER_ERROR );
                    break;
                case SSL_CONNECTION_FAILURE:
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
