package jp.co.rakuten.travel.framework.property.elementaction.singularity;

import org.openqa.selenium.WebDriver;

/**
 * Handler for window elements like button, radio button, check box, tick box, etc.
 *
 */
public interface Focusable extends WebElementAction
{
    void focus( WebDriver driver );
}
