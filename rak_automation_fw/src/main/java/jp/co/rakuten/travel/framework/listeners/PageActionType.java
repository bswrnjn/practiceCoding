package jp.co.rakuten.travel.framework.listeners;

import jp.co.rakuten.travel.framework.parameter.Parameters;

/**
 * Events to be used on page action callbacks
 *
 */
public enum PageActionType implements Parameters, EventType
{
    /**
     * everytime the page finished loading, this event will kick
     */
    ON_PAGE_LOAD,

    UNKNOWN;

    @Override
    public String val()
    {
        return name();
    }

    @Override
    public Parameters unknown()
    {
        return UNKNOWN;
    }

}
