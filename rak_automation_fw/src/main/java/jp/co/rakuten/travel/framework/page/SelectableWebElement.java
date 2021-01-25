package jp.co.rakuten.travel.framework.page;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import jp.co.rakuten.travel.framework.property.elementaction.singularity.Selectable;
import jp.co.rakuten.travel.framework.utility.ByType;

public class SelectableWebElement extends WebElementEx implements Selectable
{

    public SelectableWebElement( SearchContext element, ByType type, String id )
    {
        super( (WebElement)element, type, id );
    }

    public SelectableWebElement( SearchContext element )
    {
        super( (WebElement)element );
    }

    @Override
    public boolean select( String str )
    {
        return select( SelectBy.BY_VALUE, str );
    }

    @Override
    public boolean selectVisible( String str )
    {
        return select( SelectBy.BY_TEXT, str );
    }

    @Override
    public boolean select( int index )
    {
        return select( SelectBy.BY_INDEX, String.valueOf( index ) );
    }

    private boolean select( SelectBy by, String val )
    {
        try
        {
            Select select = new Select( element() );
            switch( by )
            {
            case BY_INDEX:
                select.selectByIndex( Integer.valueOf( val ) );
                break;
            case BY_TEXT:
                List< WebElement > selection = select.getAllSelectedOptions();
                for( WebElement selected : selection )
                {
                    if( !StringUtils.isEmpty( selected.getText() ) && selected.getText().equalsIgnoreCase( val ) )
                    {
                        LOG.info( "Already Selected " + val );
                        return true;
                    }
                }

                select.selectByVisibleText( val );
                break;
            case BY_VALUE:
                select.selectByValue( val );
                break;
            }
        }
        catch( NumberFormatException | UnexpectedTagNameException | NoSuchElementException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage() );
            return false;
        }

        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return element().isEnabled();
    }

    @Override
    public boolean isDisplayed()
    {
        return element().isDisplayed();
    }

    private enum SelectBy
    {
        BY_VALUE, //
        BY_INDEX,
        BY_TEXT,
    }
}
