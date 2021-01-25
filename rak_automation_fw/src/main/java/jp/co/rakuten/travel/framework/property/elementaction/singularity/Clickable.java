package jp.co.rakuten.travel.framework.property.elementaction.singularity;

/**
 * Handler for window elements like button, radio button, check box, tick box, etc.
 *
 */
public interface Clickable extends WebElementAction
{
    void click();

    boolean isClicked();
}
