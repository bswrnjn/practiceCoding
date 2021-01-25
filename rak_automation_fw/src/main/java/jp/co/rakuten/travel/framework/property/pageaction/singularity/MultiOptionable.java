package jp.co.rakuten.travel.framework.property.pageaction.singularity;

import java.util.Set;

import jp.co.rakuten.travel.framework.property.OptionElement;

/**
 * to be used in conjunction with {@link OptionElement} in order to distinguish selections from a specific page
 * <br>pages such as in Extranet and filter pages wherein a page can have multiple selections  depending on which button to press
 *
 * @param <T> ENUM that extends {@link OptionElement}
 */
@FunctionalInterface
public interface MultiOptionable< T extends OptionElement > extends WebPageAction
{
    /**
    *
    * @param options Submit option for different method
    * @return TRUE if operation is successful and FALSE otherwise
    */
    boolean submit( Set< T > options );

}
