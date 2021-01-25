package jp.co.rakuten.travel.framework.page;

import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import jp.co.rakuten.travel.framework.property.elementaction.Writable;
import jp.co.rakuten.travel.framework.utility.ByType;

public class WritableWebElement extends WebElementEx implements Writable
{

    public WritableWebElement( SearchContext element, ByType type, String id )
    {
        super( (WebElement)element, type, id );
    }

    public WritableWebElement( SearchContext element )
    {
        super( (WebElement)element );
    }

    @Override
    public void write( String str )
    {
        String osName = System.getProperty( "os.name" );

        if( osName.toLowerCase().contains( "mac" ) )
        {
            element().clear();
        }
        else
        {
            element().sendKeys( Keys.chord( Keys.CONTROL, "a" ) );
        }

        element().sendKeys( Keys.DELETE );

        element().sendKeys( str );

        element().sendKeys( Keys.ESCAPE );
    }

    @Override
    public void append( String str )
    {
        element().sendKeys( str );
    }

    @Override
    public String read()
    {
        return element().getText();
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

}
