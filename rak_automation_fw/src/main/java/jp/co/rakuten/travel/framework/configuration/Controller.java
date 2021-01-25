package jp.co.rakuten.travel.framework.configuration;

import jp.co.rakuten.travel.framework.parameter.Parameters;

/**
 * Used to handle a given equipment. Relationship with equipment should not be concrete, thus no implementation is strictly given.
 *
 */
public interface Controller
{
    public enum ControllerType implements Parameters
    {
        WEB,
        REST,
        REST_RCONNECT,
        ANDROID,
        IOS,
        // DB types
        SLM_DB,
        RESERVATION_DB,
        DICT_DB,

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
}
