package jp.co.rakuten.travel.framework.page;

import io.appium.java_client.android.AndroidDriver;
import io.selendroid.standalone.android.KeyEvent;
import jp.co.rakuten.travel.framework.app.android.Android;
import jp.co.rakuten.travel.framework.configuration.Configuration;
import jp.co.rakuten.travel.framework.configuration.Equipment.EquipmentType;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.SearchContext;

/** Android Driver Wrapper */
public final class AndroidControllerImpl extends AppControllerImpl implements AndroidController
{

    /* Android */
    @Override
    public AndroidDriver< ? > driver()
    {
        Android android = (Android)Configuration.instance().equipment( EquipmentType.TRAVEL_ANDROID );
        return android.driver();
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

        // Vertical scrolling down
        driver().swipe( x, starty, x, endy, 0 );
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
        driver().pressKeyCode( KeyEvent.KEYCODE_BACK );
    }
}
