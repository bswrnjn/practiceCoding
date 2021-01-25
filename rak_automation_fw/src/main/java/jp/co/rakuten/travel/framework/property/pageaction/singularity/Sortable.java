package jp.co.rakuten.travel.framework.property.pageaction.singularity;

import jp.co.rakuten.travel.framework.property.OptionElement;

/**
 * Handles the implementation of the page that can do some kind of sorting mechanism for some list or tables.
 * <br> Controller should be under the assumption that it is holding the current output window
 */
public interface Sortable< T extends OptionElement > extends WebPageAction
{
    /**
     *
     * @param option type of sorting mechanism
     * @return TRUE if operation is successful and FALSE otherwise
     */
    boolean sort( T option );

    /**
     * 
     * @param option Sort option for different method
     * @return TRUE if type is applied and FALSE otherwise
     */
    boolean checkSort( T option );
}
