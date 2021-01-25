package jp.co.rakuten.travel.framework.utility;

import java.util.List;

import org.openqa.selenium.WebElement;

public class RemoteWebDriver extends org.openqa.selenium.remote.RemoteWebDriver // implements FindsByDataLocator
{
    private int w3cComplianceLevel = 0;

    public RemoteWebDriver()
    {}

    public int getW3CStandardComplianceLevel()
    {
        return w3cComplianceLevel;
    }

    // @Override
    public WebElement findElementByDataLocator( String using )
    {
        if( getW3CStandardComplianceLevel() == 0 )
        {
            return findElement( "data-locator name", using );
        }
        else
        {
            return findElementByCssSelector( "." + cssEscape( using ) );
        }
    }

    // @Override
    public List< WebElement > findElementsByDataLocator( String using )
    {
        if( getW3CStandardComplianceLevel() == 0 )
        {
            return findElements( "data-locator name", using );
        }
        else
        {
            return findElementsByCssSelector( "." + cssEscape( using ) );
        }
    }

    static String cssEscape( String using )
    {
        using = using.replaceAll( "(['\"\\\\#.:;,!?+<>=~*^$|%&@`{}\\-\\/\\[\\]\\(\\)])", "\\\\$1" );
        if( using.length() > 0 && Character.isDigit( using.charAt( 0 ) ) )
        {
            using = "\\" + Integer.toString( 30 + Integer.parseInt( using.substring( 0, 1 ) ) ) + " " + using.substring( 1 );
        }
        return using;
    }
}
