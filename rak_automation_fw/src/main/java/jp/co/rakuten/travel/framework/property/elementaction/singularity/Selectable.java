package jp.co.rakuten.travel.framework.property.elementaction.singularity;

/**
 * Handler for window elements like drop down box, combo box, list box, links, etc.
 *
 */
public interface Selectable extends WebElementAction
{
    boolean select( String str );

    boolean selectVisible( String str );

    boolean select( int index );

}
