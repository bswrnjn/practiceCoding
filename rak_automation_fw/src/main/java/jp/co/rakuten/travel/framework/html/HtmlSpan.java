package jp.co.rakuten.travel.framework.html;

import htmlflow.HtmlWriterComposite;
import htmlflow.TextNode;
import htmlflow.elements.ElementType;

/**
 * Added missing element tag "&lt;span&gt;" of framework htmlFlow
 * @see <a href="https://github.com/fmcarvalho/HtmlFlow">https://github.com/fmcarvalho/HtmlFlow</a>
 */
public class HtmlSpan< T > extends HtmlWriterComposite< T, HtmlSpan< T > >
{
    /**
     * Adds text message to the <span> element
     * @param msg : String, text which needs to be set in the span tag.
     * @return : HtmlSpan 
     */
    public HtmlSpan< T > text( String msg )
    {
        addChild( new TextNode< T >( msg ) );
        return this;
    }

    @Override
    public String getElementName()
    {
        return ElementType.SPAN.toString();
    }

}
