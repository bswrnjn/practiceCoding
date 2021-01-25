package jp.co.rakuten.travel.framework.page;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import jp.co.rakuten.travel.framework.property.elementaction.singularity.Readable;
import jp.co.rakuten.travel.framework.utility.ByType;

public class ReadableWebElement extends WebElementEx implements Readable
{

    public ReadableWebElement( SearchContext element, ByType type, String id )
    {
        super( (WebElement)element, type, id );
    }

    public ReadableWebElement( SearchContext element )
    {
        super( (WebElement)element );
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
