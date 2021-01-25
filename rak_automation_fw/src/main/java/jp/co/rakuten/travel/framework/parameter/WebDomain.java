package jp.co.rakuten.travel.framework.parameter;

public enum WebDomain implements Parameters
{
    //
    COM( "com" ),
    TAIWAN( "com.tw" ),
    KOREA( "co.kr" ),
    CHINA( "cn" ),
    INDONESIA( "co.id" ),
    HONGKONG( "com.hk" ),
    THAILAND( "co.th" ),
    MALAYSIA( "com.my" ),
    FRANCE( "fr" ),
    SINGAPORE( "com.sg" ),
    JAPAN( "co.jp"),

    UNKNOWN( Parameters.UNKNOWN );

    private final String m_val;

    WebDomain( String val )
    {
        m_val = val;
    }

    @Override
    public final String val()
    {
        return m_val;
    }

    @Override
    public Parameters unknown()
    {
        return UNKNOWN;
    }
}
