package jp.co.rakuten.travel.framework.property.pageaction.singularity;

/**
 * Handles actions of a service like submit a page for query/search,
 * search for element ID, XPath
 *
 */
@FunctionalInterface
public interface Submittable extends WebPageAction
{
    /**
     * Process of clicking something in a web page. Either a button or a link
     * <br>and expecting another page to be handled by another controller
     * @return TRUE if the form or pages submitted is successful and FALSE otherwise
     */
    boolean submit();
}
