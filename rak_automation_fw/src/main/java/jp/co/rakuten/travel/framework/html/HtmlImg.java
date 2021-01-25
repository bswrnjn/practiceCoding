package jp.co.rakuten.travel.framework.html;

import htmlflow.HtmlWriterComposite;
import htmlflow.elements.ElementType;

/**
 * Added missing element tag "&lt;img&gt;" of framework htmlFlow
 * @see <a href="https://github.com/fmcarvalho/HtmlFlow">https://github.com/fmcarvalho/HtmlFlow</a>
 */
public class HtmlImg< T > extends HtmlWriterComposite< T, HtmlImg< T > >
{
    /**
     * Adds source <img> tag in html.
     * @param src : source of the image file
     */
    public HtmlImg( String src )
    {
        this.addAttr( "src", src );
    }

    @Override
    public String getElementName()
    {
        return ElementType.IMG.toString();
    }

}
