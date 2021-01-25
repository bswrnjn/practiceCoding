package jp.co.rakuten.travel.framework.property.elementaction.singularity;


/**
 * Handler for window elements like edit box, combo box and editable list box
 * <br>Be careful in handling this Interface since there are lots of Readable interface in Java API
 * <br>Be sure to explicitly use the import package listed above, most IDE will default to Java API instead of this package
 *
 */
public interface Readable extends WebElementAction
{
    String read();
}
