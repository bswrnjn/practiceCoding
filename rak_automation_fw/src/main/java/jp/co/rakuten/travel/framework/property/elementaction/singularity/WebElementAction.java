package jp.co.rakuten.travel.framework.property.elementaction.singularity;

import org.openqa.selenium.WebElement;

public interface WebElementAction
{
    WebElement element();

    boolean isEnabled();

    boolean isDisplayed();
}
