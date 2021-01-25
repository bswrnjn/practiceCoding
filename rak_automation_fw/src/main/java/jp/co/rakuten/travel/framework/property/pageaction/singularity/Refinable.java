package jp.co.rakuten.travel.framework.property.pageaction.singularity;

import java.util.List;

import jp.co.rakuten.travel.framework.property.OptionElement;

/**
 * Handles the implementation of the page that can do some kind of refining mechanism for some list or tables.
 * <br> Controller should be under the assumption that it is holding the current output window
 */
@FunctionalInterface
public interface Refinable< T extends OptionElement > extends WebPageAction
{
    /**
     *
     * @param refineOptions A list of options that can be used at the same time
     * @return TRUE if operation is successful and FALSE otherwise
     */
    boolean refine( List< T > refineOptions );
}
