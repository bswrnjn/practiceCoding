package jp.co.rakuten.travel.framework.page;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.Slidable;
import jp.co.rakuten.travel.framework.utility.ByType;
import jp.co.rakuten.travel.framework.utility.Time;

/**
 * Slidable App Element
 */
public class SlidableAppElement extends WebElementEx implements Slidable
{
    public SlidableAppElement( SearchContext element, ByType type, String id )
    {
        super( (WebElement)element, type, id );
    }

    public SlidableAppElement( SearchContext element )
    {
        super( (WebElement)element );
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

    @Override
    public void slide( WebDriver driver, int actualMinValue, int actualMaxValue, int currentMinValue, int currentMaxValue, int targetMinValue, int targetMaxValue )
    {

        Dimension dimensions = element().getSize();
        Point upperLeft = element().getLocation();

        int height = upperLeft.y + dimensions.height / 2;
        int minStart = upperLeft.x + (int) ( (dimensions.width * (currentMinValue - actualMinValue) / (actualMaxValue - actualMinValue)));
        int minEnd = upperLeft.x + (int) ( (dimensions.width * (targetMinValue - actualMinValue) / (actualMaxValue - actualMinValue)));
        int maxStart = upperLeft.x + (int) ( (dimensions.width * (currentMaxValue - actualMinValue) / (actualMaxValue - actualMinValue)));
        int maxEnd = upperLeft.x + (int) ( (dimensions.width * (targetMaxValue - actualMinValue) / (actualMaxValue - actualMinValue)));

        ((AppiumDriver< ? >)driver).swipe( minStart, height, minEnd, height, Time.SECOND );
        ((AppiumDriver< ? >)driver).swipe( maxStart, height, maxEnd, height, Time.SECOND );
    }
}
