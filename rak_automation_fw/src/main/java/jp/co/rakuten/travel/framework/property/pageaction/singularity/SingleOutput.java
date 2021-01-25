package jp.co.rakuten.travel.framework.property.pageaction.singularity;

public interface SingleOutput extends WebPageAction
{
    /**
     * Used to search for specific shop that is designated by the search parameters
     * @return TRUE if successful and FALSE otherwise
     */
    boolean lookupTarget();
}
