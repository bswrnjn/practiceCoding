package jp.co.rakuten.travel.framework.property;

/**
 * Promotes that the window will change or update any of the of the parameters within the page
 *
 */
@FunctionalInterface
public interface Updatable
{
    /**
     * For use in changing the Test Parameters or Service Test Parameters with respect to some external
     * <br>explicit source
     * @return TRUE if successful and FALSE otherwise 
     */
    boolean update();
}
