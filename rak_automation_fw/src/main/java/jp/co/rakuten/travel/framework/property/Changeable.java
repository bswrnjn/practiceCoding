package jp.co.rakuten.travel.framework.property;

/**
 * To be used in conjunction with {@link OptionElement} in order to distinguish actions
 * 
 * @param <T> ENUM that extends {@link OptionElement}
 */
@FunctionalInterface
public interface Changeable< T extends OptionElement >
{
    /**
     * For use in changing
     * @param option : Change option for different method
     * @return TRUE if successful and FALSE otherwise
     */
    boolean change( T option );
}
