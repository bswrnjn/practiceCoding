package jp.co.rakuten.travel.framework.page;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import jp.co.rakuten.travel.framework.property.elementaction.singularity.Clickable;
import jp.co.rakuten.travel.framework.utility.ByType;

public class ClickableWebElement extends WebElementEx implements Clickable
{
    public ClickableWebElement( SearchContext element, ByType type, String id )
    {
        super( (WebElement)element, type, id );
    }

    public ClickableWebElement( SearchContext element )
    {
        super( (WebElement)element );
    }

    @Override
    public void click()
    {
        element().click();
    }

    @Override
    public boolean isClicked()
    {
        return element().isSelected();
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
