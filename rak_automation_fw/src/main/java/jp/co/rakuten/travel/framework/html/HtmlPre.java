package jp.co.rakuten.travel.framework.html;

import htmlflow.HtmlWriterComposite;
import htmlflow.elements.ElementType;

/**
 * Added missing element tag "&lt;pre&gt;" of framework htmlFlow
 * @see <a href="https://github.com/fmcarvalho/HtmlFlow">https://github.com/fmcarvalho/HtmlFlow</a>
 */
public class HtmlPre< T > extends HtmlWriterComposite< T, HtmlPre< T > >
{

    @Override
    public String getElementName()
    {
        return ElementType.PRE.toString();
    }
}
