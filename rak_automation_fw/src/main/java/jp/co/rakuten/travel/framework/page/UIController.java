package jp.co.rakuten.travel.framework.page;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.SearchContext;

import jp.co.rakuten.travel.framework.configuration.Controller;
import jp.co.rakuten.travel.framework.html.HtmlAttributes;
import jp.co.rakuten.travel.framework.property.elementaction.Writable;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.Clickable;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.Focusable;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.Readable;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.Selectable;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.SubmittableForm;
import jp.co.rakuten.travel.framework.utility.ByType;

/**
 * Wrapped Handler for operations of elements within the page using WebDriver
 *
 */
public interface UIController extends Controller
{
    /* BROWSER and APP */
    /**
     * 
     * @param element WebElement
     * @return TRUE if successful and FALSE otherwise
     */
    public abstract boolean tick( Clickable element );

    public abstract boolean tick( Clickable element, boolean click );

    public abstract boolean click( Clickable element );

    public abstract boolean isClicked( Clickable element );

    public abstract boolean submit( SubmittableForm element );

    public abstract String read( Readable element );

    public abstract boolean write( Writable element, String str );

    public abstract boolean append( Writable element, String str );

    public abstract boolean select( Selectable element, String str );

    public abstract boolean select( Selectable element, int index );

    public abstract String getFirstSelectedOption( Selectable element );

    public abstract boolean selectVisible( Selectable element, String str );

    public abstract List< String > getListOptions( Selectable element );

    public abstract List< String > selectContains( Selectable element, String str );

    public abstract boolean focus( Focusable element );

    /* WEB ELEMENT METHODS */

    abstract SearchContext get( ByType by, String id );

    public abstract SearchContext get( SearchContext context, ByType by, String id );

    public abstract List< SearchContext > list( ByType by, String id );

    public abstract List< SearchContext > list( SearchContext context, ByType by, String id );

    public abstract boolean hasElement( ByType by, String id );

    public abstract boolean hasElement( SearchContext context, ByType by, String id );

    /* WEB ELEMENT ACTION METHODS */

    public abstract SearchContext getElement( ServiceElement element, String id );

    public abstract SearchContext getElement( SearchContext context, ServiceElement element, String id );

    public abstract SearchContext getNextElement( SearchContext element );

    public abstract SearchContext getNextElement( final ServiceElement element, String id, String siblingId );

    public abstract SearchContext getNextElement( final SearchContext context, final ServiceElement element, String id, String siblingId );

    public abstract SearchContext getPreviousElement( SearchContext element );

    public abstract SearchContext getPreviousElement( final ServiceElement element, String id, String siblingId );

    public abstract SearchContext getPreviousElement( final SearchContext context, final ServiceElement element, String id, String siblingId );

    public abstract List< SearchContext > getElements( ServiceElement element, String id );

    public abstract List< SearchContext > getElements( SearchContext context, ServiceElement element, String id );

    public abstract String getElementText( ServiceElement element, String id );

    public abstract String getElementText( SearchContext context, ServiceElement element, String id );

    public abstract String getText( SearchContext context );

    public abstract List< String > getElementTextList( ServiceElement element, String id );

    public abstract List< String > getElementTextList( SearchContext context, ServiceElement element, String id );

    public abstract boolean hasElement( ServiceElement element, String id );

    public abstract boolean hasElement( SearchContext context, ServiceElement element, String id );

    public abstract boolean hasItem( ServiceElement element, String id, String item );

    public abstract boolean hasItem( SearchContext context, ServiceElement element, String id, String item );

    public abstract String getAttribute( SearchContext element, HtmlAttributes attr, boolean isRelative );

    /**
     * If a modal dialogue will be expected as a result of some links or scripts,
     * <br>that dialogue MUST be handled first
     * @param timeout Time in milliseconds to wait for the Modal Dialogue to popup
     */
    public abstract void acceptAlert( long timeout );

    /**
     * If a modal dialogue will be expected as a result of some links or scripts,
     * <br>that dialogue MUST be handled first
     */
    public abstract void acceptAlert();

    /**
     * If a modal dialogue will be expected as a result of some links or scripts,
     * <br>that dialogue MUST be handled first
     */
    public abstract void cancelAlert();

    public abstract void scrollIntoView( SearchContext element );

    /**
     * Moves the cursor and screen to the top left most part of the page
     */
    public abstract void goTop();

    /**
     * Moves the cursor and screen to the bottom right most part of the page
     */
    public abstract void goDown();

    /**
     * 
     * @return list of generated file instances, e.g. screenshot, web log file, etc.
     */
    public abstract List< File > getGeneratedFiles();

    /**
     * Windows / Page switching process
     */
    public abstract void switchToNewWindow( boolean close );

    public abstract void switchToPreviousWindow( boolean close );

    public abstract Set< String > getWindowIds();

    public abstract String getCurrentWindowId();

    public abstract boolean switchWindow( String id );

    public abstract boolean selectFrame( ServiceElement element, String id );

    public abstract String getPageSource();

    /**
     * Implicit Wait for looking for elements
     * @param element
     * @param id
     * @return found: true, not found: false, 
     * @exception TimeOut 
     */
    public abstract boolean wait( ServiceElement element, String id );

    /**
     * Waits tills the service element doesn't display waitForText.
     * @param element : ServiceElement, to watch for.
     * @param id
     * @param waitForText : text for which needs to wait
     * @return
     */
    public abstract boolean waitForText( ServiceElement serviceElement, String id, String waitForText );

    public abstract boolean waitForReference( ServiceElement serviceElement, String id, String referenceText );

    public abstract int totalRetries();

}
