package jp.co.rakuten.travel.framework.html;

import htmlflow.HtmlWriterComposite;

/**
 * Added missing element HtmlFrame of framework htmlFlow
 * @see <a href="https://github.com/fmcarvalho/HtmlFlow">https://github.com/fmcarvalho/HtmlFlow</a>
 */
public class HtmlFrame< T > extends HtmlWriterComposite< T, HtmlFrame< T > >
{

    /**
     * Adds src and name to frame tag in html
     * @param src : src html file
     * @param name : name of html file
     */
    public HtmlFrame( String src, String name )
    {
        this.addAttr( "src", src );
        this.addAttr( "name", name );
    }

    @Override
    public String getElementName()
    {
        return "frame";
    }

}
