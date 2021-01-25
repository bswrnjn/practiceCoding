package jp.co.rakuten.travel.framework.property.elementaction.singularity;

import org.openqa.selenium.WebDriver;

/**
 * Handles the implementation of the price slider.
 */
public interface Slidable extends WebElementAction
{
    /**
    * Slide the price
    * @param actualMinValue Actual Min price from UI
    * @param actualMaxValue Actual Max price from UI
    * @param currentMinValue Current Min price from UI
    * @param currentMaxValue Current Max price from UI
    * @param targetMinValue Target Min price from test objects
    * @param targetMaxValue Target Max price from test objects
    */
    void slide( WebDriver driver, int actualMinValue, int actualMaxValue, int currentMinValue, int currentMaxValue, int targetMinValue, int targetMaxValue );
}
