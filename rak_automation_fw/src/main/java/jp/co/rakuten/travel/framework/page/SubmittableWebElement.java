package jp.co.rakuten.travel.framework.page;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import jp.co.rakuten.travel.framework.property.elementaction.singularity.SubmittableForm;
import jp.co.rakuten.travel.framework.utility.ByType;

public class SubmittableWebElement extends WebElementEx implements SubmittableForm
{
    public SubmittableWebElement( SearchContext element, ByType type, String id )
    {
        super( (WebElement)element, type, id );
    }

    public SubmittableWebElement( SearchContext element )
    {
        super( (WebElement)element );
    }

    @Override
    public boolean submit()
    {
        try
        {
            element().submit();
        }
        catch( NoSuchElementException e )
        {
            String msg = e.getClass().getSimpleName() + " found with message " + e.getMessage();
            LOG.debug( msg, e );
            LOG.warn( msg );
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
}
