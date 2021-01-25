package jp.co.rakuten.travel.framework.page;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

import io.appium.java_client.MobileBy;
import jp.co.rakuten.travel.framework.app.android.Android;
import jp.co.rakuten.travel.framework.app.ios.Ios;
import jp.co.rakuten.travel.framework.browser.Browser;
import jp.co.rakuten.travel.framework.configuration.Configuration;
import jp.co.rakuten.travel.framework.configuration.Equipment.EquipmentType;
import jp.co.rakuten.travel.framework.html.HtmlAttributes;
import jp.co.rakuten.travel.framework.listeners.PageActionType;
import jp.co.rakuten.travel.framework.listeners.WebEventListener;
import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import jp.co.rakuten.travel.framework.property.elementaction.Writable;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.Clickable;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.Focusable;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.Readable;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.Selectable;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.SubmittableForm;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.WebElementAction;
import jp.co.rakuten.travel.framework.utility.ByDataLocator;
import jp.co.rakuten.travel.framework.utility.ByType;
import jp.co.rakuten.travel.framework.utility.Time;
import jp.co.rakuten.travel.framework.utility.Utility;

/** Web Driver Wrapper */
public abstract class UIControllerImpl< T extends WebDriver > implements UIController
{
    protected TestLogger                     LOG           = (TestLogger)TestLogger.getLogger( WebController.class );

    protected Deque< String >                m_windowStack = new ArrayDeque< String >();

    /**
     * Number of element lookup retries for the whole suite
     */
    protected int                            m_retries     = 0;

    /**
     * to be used for tagging page loads
     */
    private static ThreadLocal< WebElement > s_html        = new ThreadLocal<>();

    private final String                     HTML          = "html";

    /**
     * to be used for webdriver file associations
     */
    List< File >                             m_files       = new ArrayList< File >();

    protected Configuration                  m_config      = Configuration.instance();

    /* BROWSER and APP */
    protected WebDriver driver()
    {
        Android android = (Android)m_config.equipment( EquipmentType.TRAVEL_ANDROID );
        if( android != null )
        {
            return android.driver();
        }

        Ios ios = (Ios)m_config.equipment( EquipmentType.TRAVEL_IOS );
        if( ios != null )
        {
            return ios.driver();
        }

        Browser browser = (Browser)m_config.equipment( EquipmentType.BROWSER );
        // Check if page finished loading already
        try
        {
            for( int ctr = 0; ctr < Integer.valueOf( TestApiObject.instance().get( TestApiParameters.API_PAGE_TIMEOUT ) ); ctr++ )
            {
                JavascriptExecutor jse = (JavascriptExecutor)browser.driver();
                if( jse.executeScript( "return document.readyState", "" ).equals( "complete" ) )
                {

                    if( s_html.get() != null )
                    {
                        s_html.get().getTagName();
                    }
                    else
                    {
                        s_html.set( browser.driver().findElement( By.tagName( HTML ) ) );
                    }

                    // Page already loaded!
                    break;
                }
                LOG.info( "Waiting for page load..." );
                sleep( 1 * Time.SECOND );
            }
            return browser.driver();
        }
        catch( StaleElementReferenceException e )
        {
            s_html.set( browser.driver().findElement( By.tagName( HTML ) ) );
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            WebEventListener.instance().callback( PageActionType.ON_PAGE_LOAD );
            return browser.driver();
        }
        catch( Exception e )
        {
            LOG.warn( e.getClass().getSimpleName() + " found. Page state might NOT be complete" );
            return browser.driver();
        }
    }

    public abstract void isPageLoaded();

    /* ACTIONS */

    @Override
    public boolean tick( Clickable element )
    {
        return tick( element, true );
    }

    /** Makes sure that the element is set as either checked, selected or simply clicked or NOT
     * @param element WebElement
     * @return TRUE if action is successful and FALSE otherwise
     */
    @Override
    public boolean tick( Clickable element, boolean click )
    {
        ClickableWebElement e = ClickableWebElement.class.cast( element );
        if( click && e.isClicked() )
        {
            LOG.info( e.id() + " already selected" );
            return true;
        }

        if( !click && !e.isClicked() )
        {
            LOG.info( e.id() + " already UN-selected" );
            return true;
        }

        return click( element );
    }

    /** Blindly clicks the element. e.g If the check box is checked, it will be unchecked.
     * <br> Good for buttons, radio buttons, links,
     * @param element WebElement
     * @return TRUE if action is successful and FALSE otherwise */
    @Override
    public boolean click( Clickable element )
    {
        try
        {
            Clickable e = Clickable.class.cast( element );
            // banner cover will still treat the element as visible
            // if( !e.element().isDisplayed() )
            // {
            // Point location = e.element().getLocation();
            // }
            // Point location = e.element().getLocation();
            // Dimension winSize = driver().manage().window().getSize();
            // Point winlocation = driver().manage().window().getPosition();
            // if the window relative y location of element is greater than half of the display,
            // it can be safe to assume that the element is below the screen
            // in this sense, scroll down MIGHT help to make it visible, and scroll up otherwise
            // if( (location.getY() + winlocation.getY()) > (winSize.getHeight() / 2) )
            // {
            // scrollDown();
            // }
            // else
            // {
            // scrollUp();
            // }
            e.click();
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
        }
        catch( StaleElementReferenceException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " is stale" );
        }
        catch( ClassCastException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " is not Clickable" );
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

        return false;
    }

    @Override
    public boolean isClicked( Clickable element )
    {
        boolean ret = false;
        try
        {
            Clickable e = Clickable.class.cast( element );
            ret = e.isClicked();
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
        }
        catch( StaleElementReferenceException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " is stale" );
        }
        catch( ClassCastException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " is not Clickable" );
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

        return ret;
    }

    @Override
    public boolean submit( SubmittableForm element )
    {
        try
        {
            SubmittableForm e = SubmittableForm.class.cast( element );
            return e.submit();
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
        }
        catch( StaleElementReferenceException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " is stale" );
        }
        catch( ClassCastException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " is not Submittable" );
        }
        catch( InvalidElementStateException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage() + " in WebController" );
        }

        return false;
    }

    @Override
    public String read( Readable element )
    {
        String str = "";
        try
        {
            Readable e = Readable.class.cast( element );
            return e.read();
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
        }
        catch( ClassCastException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " is not Readable" );
        }
        catch( InvalidElementStateException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage() + " in WebController" );
        }
        return str;
    }

    @Override
    public boolean append( Writable element, String str )
    {
        try
        {
            WritableWebElement e = WritableWebElement.class.cast( element );
            e.append( str );
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
        }
        catch( ClassCastException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " is not Appendable" );
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
        return false;
    }

    @Override
    public boolean select( Selectable element, String str )
    {
        try
        {
            SelectableWebElement e = SelectableWebElement.class.cast( element );
            return e.select( str );
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
        }
        catch( ClassCastException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " is not Selectable" );
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
        return false;
    }

    @Override
    public boolean select( Selectable element, int index )
    {
        try
        {
            SelectableWebElement e = SelectableWebElement.class.cast( element );
            return e.select( index );
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( "Element NULL or not found" );
        }
        catch( ClassCastException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " is not Selectable" );
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
        return false;
    }

    @Override
    public boolean selectVisible( Selectable element, String str )
    {
        try
        {
            SelectableWebElement e = SelectableWebElement.class.cast( element );
            return e.selectVisible( str );
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
        }
        catch( ClassCastException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " is not Selectable" );
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
        return false;
    }

    @Override
    public String getFirstSelectedOption( Selectable element )
    {
        try
        {
            Select e = new Select( element.element() );
            return e.getFirstSelectedOption().getText();
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
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
        return "";
    }

    @Override
    public List< String > getListOptions( Selectable element )
    {
        List< WebElement > elements = new ArrayList< WebElement >();
        List< String > list = new ArrayList< String >();

        try
        {
            Select e = new Select( element.element() );
            elements = e.getOptions();
            // String [] list = new String[elements.size()];

            for( WebElement elem : elements )
            {
                list.add( elem.getText() );
            }

        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
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
        return list;
    }

    @Override
    public List< String > selectContains( Selectable element, String str )
    {
        List< SearchContext > list = list( element.element(), ByType.BY_X_PATH, "option[contains(text(),'" + str + "')]" );
        List< String > strList = new ArrayList< String >();

        if( Utility.isEmpty( list ) )
        {
            LOG.warn( "No items can be selected with " + str );
        }
        else
        {
            for( SearchContext e : list )
            {
                strList.add( getText( e ) );
            }
        }

        return strList;
    }

    @Override
    public boolean focus( Focusable element )
    {
        try
        {
            element.focus( driver() );
            sleep( 1 * Time.SECOND );
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
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
        return false;
    }

    /* WEB ELEMENT METHODS */

    /**
     * @param by Different type to find element
     * @param id Element id
     * @return WebElement, NULL if no element is found 
     */
    @Override
    public SearchContext get( ByType by, String id )
    {
        return get( driver(), by, id );
    }

    /**
     *
     * @param context SearchContext
     * @param by Different type to find element
     * @param id Element id
     * @return WebElement, NULL if no element is found 
     */
    @Override
    public SearchContext get( SearchContext context, ByType by, String id )
    {
        return get( context, by, id, TestApiObject.instance().numeric( TestApiParameters.API_ELEMENT_RETRY_COUNT ) );
    }

    protected abstract SearchContext get( SearchContext context, ByType by, String id, int retry );

    @Override
    public List< SearchContext > list( ByType by, String id )
    {
        return list( driver(), by, id );
    }

    @Override
    public List< SearchContext > list( SearchContext context, ByType by, String id )
    {
        return list( context, by, id, TestApiObject.instance().numeric( TestApiParameters.API_ELEMENT_RETRY_COUNT ) );
    }

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
    public boolean hasElement( ByType by, String id )
    {
        return hasElement( driver(), by, id );
    }

    @Override
    public boolean hasElement( SearchContext context, ByType by, String id )
    {
        return get( context, by, id ) != null;
    }

    @Override
    public SearchContext getElement( ServiceElement element, String id )
    {
        return get( element.byType(), id );
    }

    @Override
    public SearchContext getElement( SearchContext context, ServiceElement element, String id )
    {
        return get( context, element.byType(), id );
    }

    @Override
    public SearchContext getNextElement( SearchContext element )
    {
        return get( element, ByType.BY_X_PATH, "following-sibling::*" );
    }

    @Override
    public SearchContext getNextElement( final ServiceElement element, String id, String siblingId )
    {
        return getNextElement( driver(), element, id, siblingId );
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
    public SearchContext getPreviousElement( SearchContext element )
    {
        return get( element, ByType.BY_X_PATH, "preceding-sibling::*" );
    }

    @Override
    public SearchContext getPreviousElement( ServiceElement element, String id, String siblingId )
    {
        return getPreviousElement( driver(), element, id, siblingId );
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
    public List< SearchContext > getElements( ServiceElement element, String id )
    {
        return list( element.byType(), id );
    }

    @Override
    public List< SearchContext > getElements( SearchContext context, ServiceElement element, String id )
    {
        return list( context, element.byType(), id );
    }

    @Override
    public String getElementText( ServiceElement element, String id )
    {
        String ret = "";
        try
        {
            SearchContext el = getElement( element, id );
            if( el != null )
            {
                ret = getText( el );
            }
        }
        catch( NullPointerException e )
        {
            LOG.warn( "NO Element found for " + element + " " + id );
        }

        return ret;
    }

    @Override
    public String getText( SearchContext element )
    {
        String ret = "";
        try
        {
            if( element != null )
            {
                ret = ((WebElement)element).getText();
            }
        }
        catch( ClassCastException e )
        {
            LOG.warn( "Cast type failed for " + element );
        }

        return ret;
    }

    @Override
    public String getElementText( SearchContext context, ServiceElement element, String id )
    {
        String ret = "";
        try
        {
            SearchContext el = getElement( context, element, id );
            if( el != null )
            {
                ret = getText( el );
            }
        }
        catch( NullPointerException e )
        {
            LOG.warn( "NO Element found for " + element + " " + id );
        }

        return ret;
    }

    @Override
    public List< String > getElementTextList( ServiceElement element, String id )
    {
        return getElementTextList( driver(), element, id );
    }

    @Override
    public List< String > getElementTextList( SearchContext context, ServiceElement element, String id )
    {
        List< SearchContext > elements = getElements( context, element, id );
        List< String > list = new ArrayList< String >();
        if( Utility.isEmpty( elements ) )
        {
            return list;
        }
        for( SearchContext e : elements )
        {
            list.add( getText( e ) );
        }
        return list;
    }

    @Override
    public boolean hasElement( ServiceElement element, String id )
    {
        return getElement( element, id ) != null;
    }

    @Override
    public boolean hasElement( SearchContext context, ServiceElement element, String id )
    {
        return getElement( context, element, id ) != null;
    }

    @Override
    public boolean hasItem( ServiceElement element, String id, String item )
    {
        return hasItem( driver(), element, id, item );
    }

    @Override
    public boolean hasItem( SearchContext context, ServiceElement element, String id, String item )
    {
        Select select = new Select( (WebElement)get( context, element.byType(), id ) );

        List< WebElement > selection = select.getAllSelectedOptions();
        for( WebElement selected : selection )
        {
            if( selected.getText().toLowerCase().contains( item ) )
            {
                LOG.info( "Item found " + item );
                return true;
            }
        }

        LOG.warn( "Item NOT found " + item );
        return false;
    }

    @Override
    public String getAttribute( SearchContext element, HtmlAttributes attr, boolean isRelative )
    {
        String ret = "";
        try
        {
            if( isRelative )
            {
                ret = ((WebElement)element).getAttribute( attr.name().toLowerCase() );
                if( StringUtils.isEmpty( ret ) )
                {
                    return "";
                }
            }
            else
            {

                JavascriptExecutor jse = (JavascriptExecutor)driver();
                ret = jse.executeScript( "return arguments[0].getAttribute('" + attr.name().toLowerCase() + "');", element ).toString();
            }
            return ret;
        }
        catch( WebDriverException | NullPointerException | ClassCastException e )
        {
            return "";
        }
    }

    /**
     * If a modal dialogue will be expected as a result of some links or scripts,
     * <br>that dialogue MUST be handled first
     * @param timeout Time in milliseconds to wait for the Modal Dialogue to popup */
    @Override
    public void acceptAlert( long timeout )
    {
        long waitForAlert = System.currentTimeMillis() + timeout;
        boolean boolFound = false;
        do
        {
            try
            {
                Alert alert = driver().switchTo().alert();
                if( alert != null )
                {
                    alert.accept();
                    boolFound = true;
                }
            }
            catch( NoAlertPresentException ex )
            {}
        }
        while( (System.currentTimeMillis() < waitForAlert) && (!boolFound) );
    }

    /** If a modal dialogue will be expected as a result of some links or scripts,
     * <br>that dialogue MUST be handled first */
    @Override
    public void acceptAlert()
    {
        try
        {
            Alert alert = driver().switchTo().alert();
            alert.accept();
        }
        catch( NoAlertPresentException ex )
        {
            LOG.warn( ex.getClass().getSimpleName() + " found " + ex.getMessage() );
        }
    }

    @Override
    public void cancelAlert()
    {
        try
        {
            Alert alert = driver().switchTo().alert();
            alert.dismiss();
        }
        catch( NoAlertPresentException ex )
        {
            LOG.warn( ex.getClass().getSimpleName() + " found " + ex.getMessage() );
        }
    }

    public void scrollIntoView( SearchContext element )
    {
        ((JavascriptExecutor)driver()).executeScript( "arguments[0].scrollIntoView(true);", element );
    }

    @Override
    public void goTop()
    {}

    @Override
    public void goDown()
    {}

    protected void sleep( long milli )
    {
        try
        {
            Thread.sleep( milli );
        }
        catch( InterruptedException e )
        {
            LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
        }
    }

    @Override
    public List< File > getGeneratedFiles()
    {
        return m_files;
    }

    /**
     * Automatically switch to the new unregistered pop up window
     * <br>Current window can be called by 'public void switchToPreviousWindow()'
     * <br>If multiple new windows are expected, use 'public boolean switchWindow( String id )' instead
     * <br>or else, a random new window will be active
     */
    @Override
    public void switchToNewWindow( boolean close )
    {
        if( StringUtils.isEmpty( m_windowStack.peek() ) || !m_windowStack.peek().equals( getCurrentWindowId() ) )
        {
            LOG.info( "Current window " + getCurrentWindowId() );
            m_windowStack.push( getCurrentWindowId() );
        }
        if( close )
        {
            driver().close();
        }
        boolean switched = false;
        for( String id : getWindowIds() )
        {
            if( !m_windowStack.contains( id ) )
            {
                switched = true;
                m_windowStack.push( id );
                switchWindow( id );
                LOG.info( "Switched to " + id );
                break;
            }
        }
        if( !switched )
        {
            LOG.warn( "No new windows found" );
        }
    }

    /**
     * Automatically switch to the unregistered pop up window
     * <br>If multiple new windows are expected, use 'public boolean switchWindow( String id )' instead
     */
    @Override
    public void switchToPreviousWindow( boolean close )
    {
        if( close )
        {
            driver().close();
        }
        String from = m_windowStack.pop();
        if( StringUtils.isEmpty( m_windowStack.peek() ) )
        {
            LOG.warn( "No previous window registered. " );
            m_windowStack.push( from );
            return;
        }
        switchWindow( m_windowStack.peek() );
        LOG.info( "Switched to " + m_windowStack.peek() );
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
    public boolean wait( ServiceElement serviceElement, String id )
    {
        LOG.info( "wait for " + serviceElement );
        boolean hasWebElement = false;
        FluentWait< WebDriver > fluentWait = new FluentWait< WebDriver >( driver() ) //
                .withTimeout( TestApiObject.instance().numeric( TestApiParameters.API_ELEMENT_FLUENT_WAIT_TIME_OUT ), TimeUnit.SECONDS )
                //
                .pollingEvery( 1, TimeUnit.MILLISECONDS );
        Function< WebDriver, Boolean > function = new Function< WebDriver, Boolean >()
        {
            @Override
            public Boolean apply( WebDriver driver )
            {
                return get( driver(), serviceElement.byType(), id, 0 ) != null;
            }
        };
        try
        {
            hasWebElement = fluentWait.until( function );
        }
        catch( TimeoutException e )
        {
            LOG.warn( "Time Out for " + serviceElement );
        }

        return hasWebElement;
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
        case BY_ACCESSIBILITY_ID:
            locator = MobileBy.AccessibilityId( id );
            break;
        case BY_DATA_LOCATOR:
        case BY_ID_OR_NAME:
        default:
            LOG.warn( "Locator NOT supported type " + locatorType );
            break;
        }

        if( locator == null )
        {
            throw new NullPointerException( "Locator type NOT found" );
        }

        return locator;
    }

    @Override
    public boolean waitForText( ServiceElement element, String id, String waitForText )
    {
        // default time out is set to wait for 15 seconds
        WebDriverWait wait = new WebDriverWait( driver(), TestApiObject.instance().numeric( TestApiParameters.API_ELEMENT_WAIT_TIMEOUT ) );
        By locator = locator( element.byType(), id );

        if( locator == null )
        {
            LOG.warn( "Locator NOT found for " + element );
            return false;
        }

        return wait.until( ExpectedConditions.textToBe( locator, waitForText ) );
    }

    @Override
    public boolean waitForReference( ServiceElement element, String id, String referenceText )
    {
        // default time out is set to wait for 15 seconds
        WebDriverWait wait = new WebDriverWait( driver(), TestApiObject.instance().numeric( TestApiParameters.API_ELEMENT_WAIT_TIMEOUT ) );
        By locator = locator( element.byType(), id );

        if( locator == null )
        {
            LOG.warn( "Locator NOT found for " + element );
            return false;
        }
        return wait.until( ExpectedConditions.textToBePresentInElementLocated( locator, referenceText ) );
    }
}
