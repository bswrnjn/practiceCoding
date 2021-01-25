package jp.co.rakuten.travel.framework.page;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.remote.RemoteWebElement;

import io.appium.java_client.ios.IOSDriver;
import jp.co.rakuten.travel.framework.app.ios.Ios;
import jp.co.rakuten.travel.framework.configuration.Configuration;
import jp.co.rakuten.travel.framework.configuration.Equipment.EquipmentType;

/** iOS Driver Wrapper */
public final class IosControllerImpl extends AppControllerImpl implements IosController
{

    /* iOS */
    @Override
    public IOSDriver< ? > driver()
    {
        Ios ios = (Ios)Configuration.instance().equipment( EquipmentType.TRAVEL_IOS );
        return ios.driver();
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
    public void swipeUp()
    {
        Dimension size = driver().manage().window().getSize();
        int starty = (int) (size.height * 0.20);
        int endy = (int) (size.height * 0.80);
        int x = size.width / 2;
        // e.g. swipe( start x, start y, end x, end y, duration )
        driver().swipe( x, starty, x, endy, 0 );
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

    public void swipeLateral( int start_x, int end_x, int y )
    {
        driver().swipe( start_x, y, end_x, y, 0 );
    }

    @Override
    public boolean zoom( int x, int y )
    {
        try
        {
            JavascriptExecutor js = (JavascriptExecutor)driver();
            Map< String, Object > params = new HashMap<>();
            params.put( "scale", "0.5" );
            params.put( "velocity", "-1" );
            params.put( "x", x );
            params.put( "y", y );
            js.executeScript( "mobile: pinch", params );
            return true;
        }
        catch( Exception e )
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
            JavascriptExecutor js = (JavascriptExecutor)driver();
            Map< String, Object > params = new HashMap<>();
            params.put( "scale", "0.5" );
            params.put( "velocity", "-1" );
            params.put( "element", ((RemoteWebElement)element).getId() );
            js.executeScript( "mobile: pinch", params );
            return true;
        }
        catch( Exception e )
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
            JavascriptExecutor js = (JavascriptExecutor)driver();
            Map< String, Object > params = new HashMap<>();
            params.put( "scale", "2" );
            params.put( "velocity", "1" );
            params.put( "x", x );
            params.put( "y", y );
            js.executeScript( "mobile: pinch", params );
            return true;
        }
        catch( Exception e )
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
            JavascriptExecutor js = (JavascriptExecutor)driver();
            Map< String, Object > params = new HashMap<>();
            params.put( "scale", "2" );
            params.put( "velocity", "1" );
            params.put( "element", ((RemoteWebElement)element).getId() );
            js.executeScript( "mobile: pinch", params );
            return true;
        }
        catch( Exception e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean scrollToReference( String reference )
    {
        try
        {
            JavascriptExecutor js = (JavascriptExecutor)driver();
            Map< String, Object > params = new HashMap<>();
            params.put( "name", reference );
            js.executeScript( "mobile: scroll", params );
            return true;
        }
        catch( Exception e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }

    }

    @Override
    public boolean scrollUntilVisible( SearchContext element, boolean visible )
    {
        try
        {
            JavascriptExecutor js = (JavascriptExecutor)driver();
            Map< String, Object > params = new HashMap<>();
            params.put( "element", ((RemoteWebElement)element).getId() );
            params.put( "toVisible", visible );
            js.executeScript( "mobile: scroll", params );
            return true;
        }
        catch( Exception e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean selectPicker( SearchContext element, String order, float offset )
    {
        if( offset < 0.01f || offset > 0.5f )
        {
            LOG.warn( "The offset value should in range [0.01, 0.5]" );
            offset = 0.2f;
        }
        try
        {
            JavascriptExecutor js = (JavascriptExecutor)driver();
            Map< String, Object > params = new HashMap<>();
            params.put( "element", ((RemoteWebElement)element).getId() );
            params.put( "order", order );
            params.put( "offset", offset );
            js.executeScript( "mobile: selectPickerWheelValue", params );
            return true;
        }
        catch( Exception e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    @Override
    public boolean doubleTap( SearchContext element )
    {
        try
        {
            JavascriptExecutor js = (JavascriptExecutor)driver();
            Map< String, Object > params = new HashMap<>();
            params.put( "element", ((RemoteWebElement)element).getId() );
            js.executeScript( "mobile: doubleTap", params );
            return true;
        }
        catch( Exception e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
    }

    /**
     * Swipes down until the item can be found
     * @param searchText The text of search keyword
     * @return boolean true:success, false:failed
     */
    public boolean swipeUntilFound( String searchText )
    {
        if( StringUtils.isEmpty( StringUtils.strip( searchText ) ) )
        {
            LOG.warn( "search text not provided" );
            return false;
        }
        searchText = StringUtils.strip( searchText );
        String source = StringUtils.EMPTY;
        String tag = StringUtils.EMPTY;
        try
        {
            boolean isAvailable = false;
            do
            {
                if( StringUtils.isNotEmpty( source ) )
                {
                    String newPageSource = driver().getPageSource();
                    if( StringUtils.equals( source, newPageSource ) )
                    {
                        LOG.warn( "Reached end of the screen, text :" + searchText + " is not available." );
                        return false;
                    }
                    else
                    {
                        source = newPageSource;
                    }
                }
                else
                {
                    source = driver().getPageSource();
                }

                if( StringUtils.isEmpty( source ) )
                {
                    LOG.warn( "No text found on the screen" );
                    return false;
                }

                if( source.contains( searchText ) )
                {
                    isAvailable = true;
                }
                else
                {
                    LOG.warn( "Not found in current screen, scrolling down to next page." );
                    swipeDown();
                }

            }
            while( !isAvailable );

            int index = source.indexOf( searchText );
            if( source.charAt( index + searchText.length() ) != '"' )
            {
                LOG.warn( "Node attribute not found" );
                return false;
            }
            StringBuffer attribute = new StringBuffer();
            do
            {
                index--;
                char ch = source.charAt( index );
                if( ch != '"' && ch != '=' )
                {
                    attribute.append( ch );
                }
            }
            while( source.charAt( index ) != ' ' );
            tag = attribute.reverse().toString().trim();

            if( StringUtils.isNotEmpty( tag ) )
            {
                JavascriptExecutor js = (JavascriptExecutor)driver();
                Map< String, String > scrollObject = new HashMap< String, String >();

                // Mobile scroll supports the following strategies: name, direction, predicateString, and toVisible. Specify one of these
                switch( tag )
                {
                case "value":
                    scrollObject.put( "predicateString", "value == '" + searchText + "'" );
                    break;

                case "label":
                    scrollObject.put( "predicateString", "label == '" + searchText + "'" );
                    break;

                default:
                    scrollObject.put( tag, searchText );
                    break;
                }
                js.executeScript( "mobile: scroll", scrollObject );
            }

        }
        catch( Exception e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found. Element NULL or not found" );
            return false;
        }
        return true;
    }
}
