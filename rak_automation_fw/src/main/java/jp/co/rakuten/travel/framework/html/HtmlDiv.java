package jp.co.rakuten.travel.framework.html;

import java.util.Map;

import htmlflow.elements.HtmlScriptBlock;
import htmlflow.elements.HtmlScriptLink;

/**
 * Enhanced Element HtmlDiv of framework htmlFlow 
 * added addChild feature.
 * @see <a href="https://github.com/fmcarvalho/HtmlFlow">https://github.com/fmcarvalho/HtmlFlow</a>
 */
public class HtmlDiv extends htmlflow.elements.HtmlDiv< Map< String, Object > >
{
    /**
     * Feature addChild is added to the custom HtmlDiv class
     * @param src Source of the Div
     * @return HtmlDiv HtmlDiv info
     */
    public HtmlDiv scriptLink( String src )
    {
        addChild( new HtmlScriptLink< Map< String, Object > >( src ) );
        return this;
    }

    public HtmlScriptBlock< Map< String, Object > > scriptBlock()
    {
        return addChild( new HtmlScriptBlock< Map< String, Object > >() );
    }
}
