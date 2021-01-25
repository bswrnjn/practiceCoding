package jp.co.rakuten.travel.framework.page;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.rakuten.travel.framework.browser.Browser;
import jp.co.rakuten.travel.framework.configuration.Configuration;
import jp.co.rakuten.travel.framework.configuration.Equipment.EquipmentType;
import jp.co.rakuten.travel.framework.listeners.PageActionType;
import jp.co.rakuten.travel.framework.listeners.WebEventListener;
import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import jp.co.rakuten.travel.framework.property.elementaction.Writable;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.WebElementAction;
import jp.co.rakuten.travel.framework.utility.ByDataLocator;
import jp.co.rakuten.travel.framework.utility.ByType;
import jp.co.rakuten.travel.framework.utility.Time;
import jp.co.rakuten.travel.framework.utility.Utility;
import net.lightbody.bmp.core.har.Har;

/** Web Driver Wrapper */
public final class WebControllerImpl extends UIControllerImpl< WebDriver > implements WebController
{
    protected TestLogger      LOG           = (TestLogger)TestLogger.getLogger( WebController.class );

    private final URL         m_homepage;

    protected Deque< String > m_windowStack = new ArrayDeque< String >();

    /**
     * Number of element lookup retries for the whole suite
     */
    private int               m_retries     = 0;

    /**
     * to be used for webdriver file associations
     */
    List< File >              m_files       = new ArrayList< File >();

    public WebControllerImpl( URL url )
    {
        m_homepage = url;
    }

    /* BROWSER */

    @Override
    public URL url() throws MalformedURLException
    {
        return new URL( driver().getCurrentUrl() );
    }

    @Override
    public void homepage()
    {
        if( !browse( m_homepage.toString() ) )
        {
            LOG.warn( "Browse for " + m_homepage.toString() + " failed" );
        }
    }

    @Override
    public boolean reload()
    {
        try
        {
            return browse( url().toString() );
        }
        catch( MalformedURLException e )
        {
            LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( "Malformed URL" );
            return false;
        }
    }

    @Override
    public boolean browse( String url )
    {
        LOG.info( "Opening... " + url );
        try
        {
            new URL( url );
        }
        catch( MalformedURLException e )
        {
            LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( "Malformed URL " + url );
            return false;
        }

        driver().get( url );
        /**
         * initial use of callback assuming no other actions will be done in the page
         * <br> if any action will be issued, the call will eventually be called twice
         */
        // FIXME - to be removed when a better solution is available
        WebEventListener.instance().callback( PageActionType.ON_PAGE_LOAD );

        return true;
    }

    @Override
    public void isPageLoaded()
    {
        for( int ctr = 0; ctr < Integer.valueOf( TestApiObject.instance().get( TestApiParameters.API_PAGE_TIMEOUT ) ); ctr++ )
        {
            JavascriptExecutor jse = (JavascriptExecutor)driver();
            if( jse.executeScript( "return document.readyState", "" ).equals( "complete" ) )
            {
                LOG.info( "Page alredy loaded!" );
                break;
            }
            LOG.info( "Waiting for page load..." );
            sleep( 1 * Time.SECOND );
        }
    }

    @Override
    public String currentUrl()
    {
        String url = "";
        try
        {
            Browser browser = (Browser)Configuration.instance().equipment( EquipmentType.BROWSER );
            url = URLDecoder.decode( browser.driver().getCurrentUrl(), "UTF-8" );
        }
        catch( UnsupportedEncodingException e )
        {
            LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage() + " in WebController" );
        }
        return url;
    }

    /* ACTIONS */

    /** Click by JavaScript
     * @param element WebElement
     * @return TRUE if action is successful and FALSE otherwise */
    @Override
    public boolean clickByJs( SearchContext element )
    {
        try
        {
            JavascriptExecutor executor = (JavascriptExecutor)driver();
            executor.executeScript( "arguments[0].click();", element );
            return true;
        }
        catch( Exception e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " click failed" );
            return false;
        }

    }

    @Override
    public boolean write( Writable element, String str )
    {
        try
        {
            WritableWebElement e = WritableWebElement.class.cast( element );
            e.write( str );
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
        catch( ClassCastException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " is not Writable" );
            return false;
        }
        catch( InvalidElementStateException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage() + " in WebController" );
        }
        catch( NoSuchElementException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " NOT Found" );
        }

        return true;
    }

    /* WEB ELEMENT METHODS */

    @Override
    protected SearchContext get( SearchContext context, ByType by, String id, int retry )
    {
        SearchContext element = null;
        try
        {
            switch( by )
            {
            case BY_CLASS_NAME:
                element = context.findElement( new ByClassName( id ) );
                break;
            case BY_CSS_SELECTOR:
                element = context.findElement( By.cssSelector( id ) );
                break;
            case BY_ID:
                element = context.findElement( By.id( id ) );
                break;
            case BY_ID_OR_NAME:
                element = context.findElement( new ByIdOrName( id ) );
                break;
            case BY_LINK_TEXT:
                element = context.findElement( By.linkText( id ) );
                break;
            case BY_NAME:
                element = context.findElement( new ByIdOrName( id ) );
                break;
            case BY_PARTIAL_LINK_TEXT:
                element = context.findElement( By.partialLinkText( id ) );
                break;
            case BY_TAG_NAME:
                element = context.findElement( By.tagName( id ) );
                break;
            case BY_X_PATH:
                element = context.findElement( By.xpath( id ) );
                break;
            case BY_DATA_LOCATOR:
                element = context.findElement( new ByDataLocator( id ) );
                break;
            case UNKNOWN:
            default:
                LOG.warn( "UNKNOWN type " + by );
                LOG.warn( "Returning null for " + id );
                break;
            }
        }
        catch( StaleElementReferenceException | ElementNotVisibleException | NoSuchElementException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found in findElement  with message " + e.getMessage(), e );
            if( retry > 0 )
            {
                ++m_retries;
                // give chances to comply
                sleep( TestApiObject.instance().numeric( TestApiParameters.API_ELEMENT_RETRY_WAIT ) );
                LOG.debug( "(" + retry + ") " + "Retrying to find " + id );
                return get( context, by, id, --retry );
            }
        }

        return element;
    }

    @Override
    protected List< SearchContext > list( SearchContext context, ByType by, String id, int retry )
    {
        List< WebElement > elements = new ArrayList< WebElement >();
        try
        {
            switch( by )
            {
            case BY_CLASS_NAME:
                elements = context.findElements( new ByClassName( id ) );
                break;
            case BY_CSS_SELECTOR:
                elements = context.findElements( By.cssSelector( id ) );
                break;
            case BY_ID:
                elements = context.findElements( ById.id( id ) );
                break;
            case BY_ID_OR_NAME:
                elements = context.findElements( new ByIdOrName( id ) );
                break;
            case BY_LINK_TEXT:
                elements = context.findElements( By.linkText( id ) );
                break;
            case BY_NAME:
                elements = context.findElements( new ByIdOrName( id ) );
                break;
            case BY_PARTIAL_LINK_TEXT:
                elements = context.findElements( By.partialLinkText( id ) );
                break;
            case BY_TAG_NAME:
                elements = context.findElements( By.tagName( id ) );
                break;
            case BY_X_PATH:
                elements = context.findElements( By.xpath( id ) );
                break;
            case BY_DATA_LOCATOR:
                elements = context.findElements( new ByDataLocator( id ) );
                break;
            case UNKNOWN:
            default:
                LOG.warn( "UNKNOWN type " );
                LOG.warn( "Returning empty list for " + id );
                break;
            }
        }
        catch( ElementNotVisibleException | NoSuchElementException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found in findElement  with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Returning null for " + id + " in WebController GET" );
        }

        if( Utility.isEmpty( elements ) && retry > 0 )
        {
            ++m_retries;
            // give chances to comply
            sleep( TestApiObject.instance().numeric( TestApiParameters.API_ELEMENT_RETRY_WAIT ) );
            LOG.warn( "Retrying to find " + id );
            return list( context, by, id, --retry );
        }
        List< SearchContext > contexts = new ArrayList< SearchContext >();
        contexts.addAll( elements );

        return contexts;
    }

    @Override
    public SearchContext getNextElement( final SearchContext context, final ServiceElement element, String id, String siblingId )
    {
        switch( element.byType() )
        {
        case BY_X_PATH:
            return getElement( context, element, "following-sibling::" + siblingId );

        case UNKNOWN:
            break;

        default:
            return getElement( context, new ServiceElement()
            {

                @Override
                public ByType byType()
                {
                    return ByType.BY_X_PATH;
                }

                @Override
                public Class< ? extends WebElementAction > action()
                {
                    return element.action();
                }
            }, "following-sibling::" + siblingId );
        }

        throw new IllegalArgumentException( "Element is NOT supported " + element + " with " + element.byType() );
    }

    @Override
    public SearchContext getPreviousElement( final SearchContext context, final ServiceElement element, String id, String siblingId )
    {
        switch( element.byType() )
        {
        case BY_X_PATH:
            return getElement( context, element, id + "/preceding-sibling::" + siblingId );

        case UNKNOWN:
            break;

        default:
            return getElement( context, new ServiceElement()
            {
                @Override
                public ByType byType()
                {
                    return ByType.BY_X_PATH;
                }

                @Override
                public Class< ? extends WebElementAction > action()
                {
                    return element.action();
                }
            }, "/preceding-sibling::" + siblingId );
        }

        throw new IllegalArgumentException( "Element is NOT supported " + element + " with " + element.byType() );
    }

    @Override
    public void scrollUp()
    {
        JavascriptExecutor jse = (JavascriptExecutor)driver();
        jse.executeScript( "window.scrollBy(0,-250)", "" );
    }

    @Override
    public void scrollDown()
    {
        JavascriptExecutor jse = (JavascriptExecutor)driver();
        jse.executeScript( "window.scrollBy(0,250)", "" );
    }

    @Override
    public Set< String > getWindowIds()
    {
        return driver().getWindowHandles();
    }

    @Override
    public String getCurrentWindowId()
    {
        return driver().getWindowHandle();
    }

    @Override
    public boolean switchWindow( String id )
    {
        try
        {
            LOG.info( "Switching window to id " + id );
            driver().switchTo().window( id );
            return true;
        }
        catch( NoSuchWindowException e )
        {
            LOG.warn( "Window NOT found for " + id + ". " + e.getMessage() );
            return false;
        }
    }

    /**
     * Execute javascript directly on a page.
     * @param script Javascript to execute on a page. */
    @Override
    public String javaScript( String script, Object ... args )
    {
        String scriptText = "";
        try
        {
            JavascriptExecutor jse = (JavascriptExecutor)driver();
            Object res = jse.executeScript( script, args );
            if( res != null )
            {
                scriptText = res.toString();
            }
        }
        catch( NullPointerException e )
        {
            LOG.debug( getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( "JavascriptExecutor excute error" );
        }
        return scriptText;
    }

    /**
     * Switch to a new frame window.
     **/
    @Override
    public boolean selectFrame( ServiceElement element, String id )
    {
        try
        {
            LOG.info( "Select frame " + id );
            driver().switchTo().defaultContent();
            driver().switchTo().frame( (WebElement)getElement( element, id ) );
            return true;
        }
        catch( NoSuchElementException | StaleElementReferenceException | ClassCastException e )
        {
            LOG.warn( "Frame NOT found for " + id + ". " + e.getMessage() );
            return false;
        }
    }

    @Override
    public String getPageSource()
    {
        return driver().getPageSource();
    }

    @Override
    public Cookie getCookie( String name )
    {
        return driver().manage().getCookieNamed( name );
    }

    @Override
    public Set< Cookie > getCookies()
    {
        return driver().manage().getCookies();
    }

    @Override
    public int totalRetries()
    {
        return m_retries;
    }

    private By locator( ByType locatorType, String id )
    {
        By locator = null;

        switch( locatorType )
        {
        case BY_CLASS_NAME:
            locator = By.className( id );
            break;
        case BY_CSS_SELECTOR:
            locator = By.cssSelector( id );
            break;
        case BY_ID:
            locator = By.id( id );
            break;
        case BY_LINK_TEXT:
            locator = By.linkText( id );
            break;
        case BY_NAME:
            locator = By.name( id );
            break;
        case BY_PARTIAL_LINK_TEXT:
            locator = By.partialLinkText( id );
            break;
        case BY_TAG_NAME:
            locator = By.tagName( id );
            break;
        case BY_X_PATH:
            locator = By.xpath( id );
            break;
        case BY_DATA_LOCATOR:
        case BY_ID_OR_NAME:
        default:
            LOG.warn( "Locator BOT supported type " + locatorType );
            break;
        }

        if( locator == null )
        {
            throw new NullPointerException( "Locator type NOT found" );
        }

        return locator;
    }

    @Override
    public boolean waitForStyle( ServiceElement element, String id, String attr, String value, boolean exact )
    {

        // default time out is set to wait for 15 seconds
        WebDriverWait wait = new WebDriverWait( driver(), TestApiObject.instance().numeric( TestApiParameters.API_ELEMENT_FLUENT_WAIT_TIME_OUT ) );
        By locator = locator( element.byType(), id );

        if( locator == null )
        {
            LOG.warn( "Locator NOT found for " + element );
            return false;
        }

        return wait.until( exact ? ExpectedConditions.attributeToBe( locator, attr, value ) : ExpectedConditions.attributeContains( locator, attr, value ) );
    }

    @Override
    public boolean waitForStyle( SearchContext context, ServiceElement element, String id, String attr, String value, boolean exact )
    {
        WebDriver driver = ((Browser)Configuration.instance().equipment( EquipmentType.BROWSER )).driver();

        // default time out is set to wait for 15 seconds
        WebDriverWait wait = new WebDriverWait( driver, TestApiObject.instance().numeric( TestApiParameters.API_ELEMENT_FLUENT_WAIT_TIME_OUT ) );

        SearchContext webElement = getElement( context, element, id );
        if( webElement == null )
        {
            LOG.warn( "Element NOT found" );
            return false;
        }
        return wait.until( exact ? ExpectedConditions.attributeToBe( (WebElement)webElement, attr, value ) : ExpectedConditions.attributeContains( (WebElement)webElement, attr, value ) );
    }

    @Override
    public Har getHar()
    {
        Browser browser = (Browser)Configuration.instance().equipment( EquipmentType.BROWSER );
        return browser.getHar();
    }
}
