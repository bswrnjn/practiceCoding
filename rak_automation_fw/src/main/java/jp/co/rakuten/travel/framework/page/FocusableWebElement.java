package jp.co.rakuten.travel.framework.page;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import jp.co.rakuten.travel.framework.property.elementaction.singularity.Focusable;
import jp.co.rakuten.travel.framework.utility.ByType;

public class FocusableWebElement extends WebElementEx implements Focusable
{
    public FocusableWebElement( SearchContext element, ByType type, String id )
    {
        super( (WebElement)element, type, id );
    }

    public FocusableWebElement( SearchContext element )
    {
        super( (WebElement)element );
    }

    @Override
    public void focus( WebDriver driver )
    {
        Actions action = new Actions( driver );
        action.moveToElement( element() ).build().perform();
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
