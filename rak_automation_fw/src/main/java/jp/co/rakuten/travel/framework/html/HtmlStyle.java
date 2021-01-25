package jp.co.rakuten.travel.framework.html;

import htmlflow.HtmlWriterComposite;
import htmlflow.TextNode;
import htmlflow.elements.ElementType;

/**
 * Added missing element tag "&lt;style&gt;" of framework htmlFlow
 * @see <a href="https://github.com/fmcarvalho/HtmlFlow">https://github.com/fmcarvalho/HtmlFlow</a>
 */
public class HtmlStyle< T > extends HtmlWriterComposite< T, HtmlStyle< T > >
{
    /**
     * Adds the style attribute to any html Tag
     * @param css : String containing css which will be setted in the style tag.
     * @return: HtmlStyle
     */
    public HtmlStyle< T > text( String css )
    {
        addChild( new TextNode< T >( css ) );
        return this;
    }

    @Override
    public String getElementName()
    {
        return ElementType.STYLE.toString();
    }

}
