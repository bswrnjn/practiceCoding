package jp.co.rakuten.travel.framework.property.pageaction.singularity;

/**
 * Promotes that the window is in process of setting up input parameters
 */
public interface Parameterized extends WebPageAction
{
    /**
     * For use in setting up parameters in a web page.
     * <br>Inputting string values in edit boxes, clicking buttons, selecting choices; etc.
     * <br>Sometimes, the process extends to another page and thus, all succeeding pages are recommended to be
     * <br>a part of this process
     * @return TRUE if all parameters setup are successful and FALSE otherwise
     */
    boolean setParameters();
}
