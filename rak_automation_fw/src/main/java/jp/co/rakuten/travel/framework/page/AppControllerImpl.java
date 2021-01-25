package jp.co.rakuten.travel.framework.page;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ByIdOrName;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import jp.co.rakuten.travel.framework.property.elementaction.Writable;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.Clickable;
import jp.co.rakuten.travel.framework.utility.ByDataLocator;
import jp.co.rakuten.travel.framework.utility.ByType;

/** App Driver Wrapper */
public class AppControllerImpl extends UIControllerImpl< AppiumDriver< ? > > implements AppController
{
    List< File > m_files = new ArrayList< File >();

    /* App */
    @Override
    public AppiumDriver< ? > driver()
    {
        return driver();
    }

    @Override
    public boolean write( Writable element, String str )
    {
        try
        {
            WritableWebElement e = WritableWebElement.class.cast( element );
            e.append( str );
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

    @Override
    protected SearchContext get( SearchContext context, ByType by, String id, int retry )
    {
        SearchContext element = null;
        try
        {
            switch( by )
            {
            case BY_ACCESSIBILITY_ID:
                element = context.findElement( MobileBy.AccessibilityId( id ) );
                break;
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
            case BY_DATA_LOCATOR:
                element = context.findElement( new ByDataLocator( id ) );
                break;
            case UNKNOWN:
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
    public void isPageLoaded()
    {
        // TODO Auto-generated method stub

    }

    /**
     * Send key from keyboard
     */
    @Override
    public boolean sendKeys( Keys keys )
    {
        try
        {
            driver().getKeyboard().sendKeys( keys );
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
        catch( InvalidElementStateException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage() + " in WebController" );
        }

        return false;
    }

    @Override
    public void swipeLeft( ServiceElement element )
    {
        // TODO Swipe 100% along the element from right to left
    }

    @Override
    public void swipeLeft()
    {
        Dimension size = driver().manage().window().getSize();

        int startx = (int) (size.width * 0.80);
        int endx = (int) (size.width * 0.20);
        int y = size.height / 2;

        // e.g. swipe( start x, start y, end x, end y, duration )
        driver().swipe( startx, y, endx, y, 0 );

    }

    @Override
    public void swipeRight( ServiceElement element )
    {
        // TODO Swipe 100% along the element from left to right
    }

    @Override
    public void swipeRight()
    {
        Dimension size = driver().manage().window().getSize();

        int startx = (int) (size.width * 0.20);
        int endx = (int) (size.width * 0.80);
        int y = size.height / 2;

        // e.g. swipe( start x, start y, end x, end y, duration )
        driver().swipe( startx, y, endx, y, 0 );
    }

    @Override
    public void swipeUp( ServiceElement element )
    {
        // TODO Swipe 100% along the element from down to up
    }

    @Override
    public void swipeUp()
    {
        Dimension size = driver().manage().window().getSize();

        int starty = (int) (size.height * 0.20);
        int endy = (int) (size.height * 0.80);
        int x = size.width / 2;

        // Vertical scrolling down
        driver().swipe( x, starty, x, endy, 0 );
    }

    @Override
    public void swipeDown( ServiceElement element )
    {
        // TODO Swipe 100% along the element from up to down
    }

    @Override
    public void swipeDown()
    {
        Dimension size = driver().manage().window().getSize();

        int starty = (int) (size.height * 0.80);
        int endy = (int) (size.height * 0.20);
        int x = size.width / 2;

        // Vertical scrolling down
        driver().swipe( x, starty, x, endy, 0 );
    }

    @Override
    public void swipeLateral( int start_x, int end_x, int y )
    {
        // TODO From a pixel to either left (negative) or right (positive)
    }

    @Override
    public void swipeVertical( int x, int y, int to )
    {
        // TODO From a pixel to either down (negative) or up (positive)
    }

    @Override
    public boolean tap()
    {
        // TODO Tap on the screen
        return true;
    }

    @Override
    public boolean tap( Clickable element )
    {
        try
        {
            driver().tap( 1, element.element(), 0 );
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean tap( int x, int y )
    {
        try
        {
            driver().tap( 1, x, y, 0 );
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
        catch( InvalidElementStateException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage() + " in WebController" );
        }

        return false;
    }

    @Override
    public boolean zoom( int x, int y )
    {
        try
        {
            driver().zoom( x, y );;
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean zoom( SearchContext element )
    {
        try
        {
            driver().zoom( (WebElement)element );
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean pinch( int x, int y )
    {
        try
        {
            driver().pinch( x, y );;
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean pinch( SearchContext element )
    {
        try
        {
            driver().pinch( (WebElement)element );;
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean moveElement( int x, int y, int xTarget, int yTarget )
    {
        try
        {
            TouchAction touchAction = new TouchAction( (AppiumDriver< ? >)driver() );
            touchAction.press( x, y ).moveTo( xTarget, yTarget ).release().perform();
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean moveElement( SearchContext element, int x, int y )
    {
        try
        {
            TouchAction touchAction = new TouchAction( (AppiumDriver< ? >)driver() );
            touchAction.press( (WebElement)element ).moveTo( x, y ).release().perform();
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean moveElement( SearchContext element, int x, int y, int duration )
    {
        try
        {
            TouchAction touchAction = new TouchAction( (AppiumDriver< ? >)driver() );
            touchAction.press( (WebElement)element ).waitAction( duration ).moveTo( x, y ).release().perform();
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean moveElement( SearchContext element, SearchContext targetElement )
    {
        try
        {
            TouchAction touchAction = new TouchAction( (AppiumDriver< ? >)driver() );
            touchAction.press( (WebElement)element ).moveTo( (WebElement)targetElement ).release().perform();
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean doubleClick( Clickable element )
    {
        try
        {
            MultiTouchAction multiTouch = new MultiTouchAction( (AppiumDriver< ? >)driver() );
            TouchAction action0 = new TouchAction( (AppiumDriver< ? >)driver() ).tap( element.element() ).release();
            TouchAction action1 = new TouchAction( (AppiumDriver< ? >)driver() ).tap( element.element() ).release();
            multiTouch.add( action0 ).add( action1 ).perform();
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean longPress( Clickable element )
    {
        try
        {
            TouchAction touchAction = new TouchAction( (AppiumDriver< ? >)driver() );
            touchAction.longPress( element.element() ).release().perform();
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean longPress( int x, int y )
    {
        try
        {
            TouchAction touchAction = new TouchAction( (AppiumDriver< ? >)driver() );
            touchAction.longPress( x, y ).release().perform();
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean longPress( Clickable element, int duration )
    {
        try
        {
            TouchAction touchAction = new TouchAction( (AppiumDriver< ? >)driver() );
            touchAction.longPress( element.element(), duration ).release().perform();
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean longPress( int x, int y, int duration )
    {
        try
        {
            TouchAction touchAction = new TouchAction( (AppiumDriver< ? >)driver() );
            touchAction.longPress( x, y, duration ).release().perform();
            return true;
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    /**
     * Switching to WEB_VIEW;
     */
    @Override
    public void switchToWebView()
    {
        Set< String > context = driver().getContextHandles();
        for( String contextName : context )
        {
            if( contextName.contains( "WEBVIEW" ) )
            {
                driver().context( contextName );
                break;
            }

        }
    }

    /**
     * Switching to NATIVE_APP;
     */
    @Override
    public void switchToAppView()
    {
        Set< String > context = driver().getContextHandles();
        for( String contextName : context )
        {
            if( contextName.contains( "NATIVE_APP" ) )
            {
                driver().context( contextName );
                break;
            }

        }
    }

    @Override
    public Dimension getSize( SearchContext element )
    {
        Dimension dimension = new Dimension( 0, 0 );
        try
        {
            if( element != null )
            {
                dimension = ((WebElement)element).getSize();
            }
        }
        catch( ClassCastException e )
        {
            LOG.warn( "No Size available for the element" );
        }

        return dimension;
    }

    @Override
    public Point getLocation( SearchContext element )
    {
        Point point = new Point( 0, 0 );
        try
        {
            if( element != null )
            {
                point = ((WebElement)element).getLocation();
            }
        }
        catch( ClassCastException e )
        {
            LOG.warn( "No Location available for the element" );
        }

        return point;
    }

    @Override
    public void swipeVertical( Float startPercent, Float endPercent )
    {
        Dimension size = driver().manage().window().getSize();
        int starty = (int) (size.height * startPercent);
        int endy = (int) (size.height * endPercent);
        int x = size.width / 2;
        // e.g. swipe( start x, start y, end x, end y, duration )
        driver().swipe( x, starty, x, endy, 0 );
    }

    @Override
    public void slide( SlidableAppElement element, int actualMinValue, int actualMaxValue, int currentMinValue, int currentMaxValue, int targetMinValue, int targetMaxValue )

    {
        try
        {
            SlidableAppElement e = SlidableAppElement.class.cast( element );
            e.slide( driver(), actualMinValue, actualMaxValue, currentMinValue, currentMaxValue, targetMinValue, targetMaxValue );
        }
        catch( NullPointerException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );

        }
        catch( ClassCastException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element " + element + " is not Writable" );

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
    }

    @Override
    public boolean scrollUntilVisible( SearchContext element, boolean visible )
    {
        // TODO Scroll Until Visible
        return false;
    }

    @Override
    public boolean scrollToReference( String reference )
    {
        // TODO Scroll To Reference
        return false;
    }

    @Override
    public boolean selectPicker( SearchContext element, String order, float offset )
    {
        // TODO Select Picker
        return false;
    }

    @Override
    public boolean doubleTap( SearchContext element )
    {
        // TODO Double Tap
        return false;
    }

    @Override
    public void back()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean swipeUntilFound( String reference )
    {
        // TODO Swipe till found
        return false;
    }

}
