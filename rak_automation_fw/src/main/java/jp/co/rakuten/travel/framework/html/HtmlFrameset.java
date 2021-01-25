package jp.co.rakuten.travel.framework.html;

import htmlflow.HtmlWriterComposite;

/**
 * Added missing element tag "&lt;frameset&gt;" of framework htmlFlow
 * @see <a href="https://github.com/fmcarvalho/HtmlFlow">https://github.com/fmcarvalho/HtmlFlow</a>
 */
public class HtmlFrameset< T > extends HtmlWriterComposite< T, HtmlFrameset< T > >
{

    @Override
    public String getElementName()
    {
        return "frameset";
    }

}
