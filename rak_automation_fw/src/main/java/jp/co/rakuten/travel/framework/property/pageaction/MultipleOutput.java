package jp.co.rakuten.travel.framework.property.pageaction;

import jp.co.rakuten.travel.framework.parameter.Parameters;
import jp.co.rakuten.travel.framework.property.ServiceOutputParameters;
import jp.co.rakuten.travel.framework.property.pageaction.singularity.SingleOutput;
import jp.co.rakuten.travel.framework.property.pageaction.singularity.WebPageAction;

public interface MultipleOutput< S extends Parameters, T extends ServiceOutputParameters< S > > extends SingleOutput, WebPageAction
{
    /**
     * Used to search for specific item that is designated by the search parameters
     * @param item Target item
     * @return TRUE if successful and FALSE otherwise
     */
    boolean lookupItem( T item );

    /**
     * Checks for presence of multiple pages and returns TRUE if available 
     * <br>and FALSE if otherwise or currently in the last page
     * @return TRUE if available and FALSE if otherwise or currently in the last page
     */
    boolean hasNextPage();

    /**
     * Loads next page
     */
    void nextPage();
}
