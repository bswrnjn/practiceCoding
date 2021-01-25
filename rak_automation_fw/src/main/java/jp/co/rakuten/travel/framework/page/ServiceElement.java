package jp.co.rakuten.travel.framework.page;

import jp.co.rakuten.travel.framework.property.elementaction.singularity.WebElementAction;
import jp.co.rakuten.travel.framework.utility.ByType;

/**
 * Service Element interface to use locators.
 */
public interface ServiceElement
{
    public ByType byType();

    public Class< ? extends WebElementAction > action();
}
