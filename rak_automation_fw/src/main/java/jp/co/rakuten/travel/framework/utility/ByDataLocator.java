package jp.co.rakuten.travel.framework.utility;

import java.io.Serializable;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;

public class ByDataLocator extends By implements Serializable
{
    private static final long serialVersionUID = -9109985142701219214L;

    private final String      dataLocator;

    public ByDataLocator( String dataLocator )
    {
        this.dataLocator = dataLocator;
    }

    @Override
    public List< WebElement > findElements( SearchContext context )
    {
        return ((FindsByXPath)context).findElementsByXPath( ".//*[" + containingWord( "data-locator", dataLocator ) + "]" );
    }

    @Override
    public WebElement findElement( SearchContext context )
    {
        return ((FindsByXPath)context).findElementByXPath( ".//*[" + containingWord( "data-locator", dataLocator ) + "]" );
    }

    @Override
    public String toString()
    {
        return "By.dataLocator: " + dataLocator;
    }

    /**
     * Generates a partial xpath expression that matches an element whose specified attribute
     * contains the given CSS word. So to match &lt;div class='foo bar'&gt; you would say "//div[" +
     * containingWord("data-locator", "foo") + "]".
     *
     * @param attribute name
     * @param word name
     * @return XPath fragment
     */
    private String containingWord( String attribute, String word )
    {
        return "contains(concat(' ',normalize-space(@" + attribute + "),' '),' " + word + " ')";
    }
}
