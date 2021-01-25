package jp.co.rakuten.travel.framework.bug;

import jp.co.rakuten.travel.framework.parameter.Parameters;

/**
 * Collection of known bug
 *
 */
public enum BugId implements Parameters
{
    RTRV_4740,
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
