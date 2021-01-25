package jp.co.rakuten.travel.framework.html;

import htmlflow.HtmlWriterComposite;
import htmlflow.elements.HtmlTd;
import htmlflow.elements.HtmlTh;
import htmlflow.elements.HtmlTr;

/**
 * Added missing element tag "&lt;tbody&gt;" of framework htmlFlow
 * @see <a href="https://github.com/fmcarvalho/HtmlFlow">https://github.com/fmcarvalho/HtmlFlow</a>
 */
public class HtmlTBody< T > extends HtmlWriterComposite< T, HtmlTBody< T > >
{
    /**
     * Add the <tr> tag to <tbody> tag. 
     * @return : HtmlTr
     */
    public HtmlTr< T > tr()
    {
        return addChild( new HtmlTr< T >() );
    }

    /**
     * Add the <td> tag to <tbody> tag.
     * @return : HtmlTd
     */
    public HtmlTd< T > td()
    {
        return addChild( new HtmlTd< T >() );
    }

    /**
     * Add the <th> tag to <tbody> tag.
     * @return : HtmlTh
     */
    public HtmlTh< T > th()
    {
        return addChild( new HtmlTh< T >() );
    }

    @Override
    public String getElementName()
    {
        return "tbody";
    }

}
