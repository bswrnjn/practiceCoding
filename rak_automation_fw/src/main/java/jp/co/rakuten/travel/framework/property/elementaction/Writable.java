package jp.co.rakuten.travel.framework.property.elementaction;

import jp.co.rakuten.travel.framework.property.elementaction.singularity.Readable;
import jp.co.rakuten.travel.framework.property.elementaction.singularity.WebElementAction;

/**
 * Handler for window elements like edit box, combo box and editable list box
 *
 */
public interface Writable extends Readable, WebElementAction
{
    void write( String str );

    void append( String str );
}
