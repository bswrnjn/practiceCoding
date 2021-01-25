package jp.co.rakuten.travel.framework.property.pageaction.singularity;

import jp.co.rakuten.travel.framework.property.OptionElement;

/**
 * to be used in conjunction with {@link OptionElement} in order to distinguish button actions of a specific page
 * <br>pages such as in Extranet wherein a page can have multiple routes depending on which button to press
 *
 * @param <T> ENUM that extends {@link OptionElement}
 */
@FunctionalInterface
public interface Optionable< T extends OptionElement > extends WebPageAction
{
    /**
     *
     * @param option Submit option for different method
     * @return TRUE if operation is successful and FALSE otherwise
     */
    boolean submit( T option );
}
