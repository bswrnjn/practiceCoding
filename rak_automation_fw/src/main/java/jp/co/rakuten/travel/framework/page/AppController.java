package jp.co.rakuten.travel.framework.page;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;

import jp.co.rakuten.travel.framework.property.elementaction.singularity.Clickable;

/**
 * Wrapped Handler for operations of elements within the page using WebDriver
 *
 */
public interface AppController extends UIController
{
    WebDriver driver();

    boolean sendKeys( Keys keys );

    /**
     * Swipe 100% along the element from right to left
     * @param element ServiceElement which along to swipe
     */
    void swipeLeft( ServiceElement element );

    /**
     * Swipe 80% from right to left
     */
    void swipeLeft();

    /**
     * Swipe 100% along the element from left to right
     * @param element ServiceElement which along to swipe
     */
    void swipeRight( ServiceElement element );

    /**
     * Swipe 80% from left to right
     */
    void swipeRight();

    /**
     * Swipe 100% along the element from down to up
     * @param element ServiceElement which along to swipe
     */
    void swipeUp( ServiceElement element );

    /**
     * Swipe 80% from down to up
     */
    void swipeUp();

    /**
     * Swipe 100% along the element from up to down
     * @param element ServiceElement which along to swipe
     */
    void swipeDown( ServiceElement element );

    /**
     * Swipe some percentage along the element from up to down or down to up
     * @param startPercent start percent
     * @param endPercent   end percent
     */
    void swipeVertical( Float startPercent, Float endPercent );

    /**
     * Swipe 80% from up to down
     */
    void swipeDown();

    /**
     * From a pixel to either left (negative) or right (positive)
     * @param start_x : start coordinate by x axis
     * @param end_x   : end coordinate by x axis
     * @param y       : coordinate by y axis
     */
    void swipeLateral( int start_x, int end_x, int y );

    /**
     * From a pixel to either down (negative) or up (positive)
     * @param x : coordinate by x axis
     * @param y : coordinate by y axis
     * @param to
     */
    void swipeVertical( int x, int y, int to );

    /**
     * Tap on the screen
     */
    boolean tap();

    /**
     * Tap along the element
     * @param element ServiceElement which along to swipe
     * @return boolean true : passed false : failed 
     */
    boolean tap( Clickable element );

    /**
     * Tap by coordinate to index
     * @param x : coordinate by x axis
     * @param y : coordinate by y axis
     * @return boolean true : passed false : failed 
     */
    boolean tap( int x, int y );

    /**
     * Convenience method for "zooming in" on an element on the screen.
    * @param x x coordinate to start zoom on.
    * @param y y coordinate to start zoom on.
    */
    boolean zoom( int x, int y );

    /**
     * Convenience method for "zooming in" on an element on the screen.
     * @param element The element to pinch.
     */
    boolean zoom( SearchContext element );

    /**
     * Convenience method for pinching an element on the screen.
     * @param x x coordinate to terminate the pinch on.
     * @param y y coordinate to terminate the pinch on.
     */
    boolean pinch( int x, int y );

    /**
     * Convenience method for pinching an element on the screen.
     * @param el The element to pinch.
     */
    boolean pinch( SearchContext element );

    /**
     * Pressing down on an element, sliding their finger to another position, and removing their finger from the screen.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param xTarget xTarget coordinate.
     * @param yTarget yTarget coordinate.
     */
    boolean moveElement( int x, int y, int xTarget, int yTarget );

    /**
     * Pressing down on an element, sliding their finger to another position, and removing their finger from the screen.
     * @param element the element to be moved.
     * @param x x coordinate.
     * @param y y coordinate.
     */
    boolean moveElement( SearchContext element, int x, int y );

    /**
     * Pressing down on an element, sliding their finger to another position, and removing their finger from the screen.
     * @param element the element to be moved.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param ms time in milliseconds to wait.
     */
    boolean moveElement( SearchContext element, int x, int y, int duration );

    /**
     * Pressing down on an element, sliding their finger to another position, and removing their finger from the screen.
     * @param element the element to be moved.
     * @param targetElement the target position.
     */
    boolean moveElement( SearchContext element, SearchContext targetElement );

    /**
     * Double click the element.
     * @param element the element to be clicked.
     */
    boolean doubleClick( Clickable element );

    /**
     * Double tap the element.
     * @param element the element to be taped.
     */
    boolean doubleTap( SearchContext element );

    /**
     * Press and hold the at an absolute position on the screen
     * until the contextmenu event has fired.
     * @param element element to long-press.
     */
    boolean longPress( Clickable element );

    /**
     * Press and hold the at an absolute position on the screen
     * until the contextmenu event has fired.
     *
     * @param x x coordinate.
     * @param y y coordinate.
     */
    boolean longPress( int x, int y );

    /**
     * Press and hold the at an absolute position on the screen
     * until the contextmenu event has fired.
     * @param element element to long-press.
     * @param duration of the long-press, in milliseconds.
     */
    boolean longPress( Clickable element, int duration );

    /**
     * Press and hold the at an absolute position on the screen
     * until the contextmenu event has fired.
     *
     * @param x x coordinate.
     * @param y y coordinate.
     * @param duration of the long-press, in milliseconds.
     */
    boolean longPress( int x, int y, int duration );

    /**
     * Switching to web view;
     */
    void switchToWebView();

    /**
     * Switching to NATIVE_APP;
     */
    void switchToAppView();

    /**
    * Get the size of  search context
    * @param SearchContext SearchContext to get Dimension
    * @return Dimension*/

    Dimension getSize( SearchContext context );

    /**
     * Get the location of search context
     * @param SearchContext SearchContext to get location
     * @return Point
     */
    Point getLocation( SearchContext context );

    /**
     * Slide the price
     * @param actualMinValue Actual Min price from UI
     * @param actualMaxValue Actual Max price from UI
     * @param currentMinValue Current Min price from UI
     * @param currentMaxValue Current Max price from UI
     * @param targetMinValue Target Min price from test objects
     * @param targetMaxValue Target Max price from test objects
     */
    void slide( SlidableAppElement element, int actualMinValue, int actualMaxValue, int currentMinValue, int currentMaxValue, int targetMinValue, int targetMaxValue );

    /**
     * Scroll to the first visible element
     * @param element the element to be Scroll
     * @param visible Boolean parameter. If set to true then asks to scroll to the first visible element in the parent container.
     */
    boolean scrollUntilVisible( SearchContext element, boolean visible );

    /**
     * Scroll to the reference string
     * @param reference The accessibility id of the child element, to which scrolling is performed.
     */
    boolean scrollToReference( String reference );

    /**
     * Select Picker Wheel Value
     * @param element Picker Wheel Element.
     * @param order Either next to select the value next to the current one from the target picker wheel or previous to select the previous one.
     * @param offset The value in range [0.01, 0.5]. It defines how far from picker wheel's center the click should happen.
     * <br> The actual distance is culculated by multiplying this value to the actual picker wheel height. 
     * <br> Too small offset value may not change the picker wheel value and too high value may cause the wheel to switch two or more values at once.
     * <br> Usually the optimal value is located in range [0.15, 0.3]. 0.2 by default.
     */
    boolean selectPicker( SearchContext element, String order, float offset );

    /**
     * clicks on system interface back button
     */
    void back();

    /**
     * This is a general purpose method which will scroll down and look for the text on the screen.
     * @param reference
     * @return boolean, true if the scroll was successful false other wise.
     */
    boolean swipeUntilFound( String reference );
}
