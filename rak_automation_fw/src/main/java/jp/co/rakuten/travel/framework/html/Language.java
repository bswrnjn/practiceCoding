package jp.co.rakuten.travel.framework.html;

import jp.co.rakuten.travel.framework.parameter.Parameters;

public enum Language implements Parameters
{
    //
    ENGLISH( "en"),
    KOREAN( "ko" ),
    CHINESE( "zh-cn"),
    TAIWAN_CHINESE( "zh-tw" ),
    HONGKONG_CHINESE( "zh-hk" ),
    THAI( "th" ),
    IDONESIAN( "id" ),
    MALAYSIAN_ENGLISH( "en-my" ),
    SINGAPOREAN_ENGLISH( "en-sg" ),
    FRENCH( "fr" ),
    JAPANESE( "ja" ),
    UNKNOWN( Parameters.UNKNOWN );

    private String m_val;

    Language( String val )
    {
        m_val = val;
    }

    @Override
    public String val()
    {
        return m_val;
    }

    @Override
    public Parameters unknown()
    {
        return UNKNOWN;
    }
}
