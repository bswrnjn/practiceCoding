package jp.co.rakuten.travel.framework.browser;

import jp.co.rakuten.travel.framework.parameter.Parameters;

/**
 *
 */
public enum UserAgentType implements Parameters
{
    //
    IPHONE5( "Apple-iPhone7C1/1202.440", 320, 568 ),
    IPHONE6( "Apple-iPhone7C2/1202.466", 375, 667 ),
    IPAD3( "Apple-iPad3C3/1001.523", 1024, 768 ),
    ANDROID_PHONE_5_1_1( "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36", 360, 640 ),
    ANDROID_TABLET_4_0_4( "Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 Mobile Safari/535.19", 360, 640 ),
    FEATURE_PHONE( "DoCoMo/2.0 P903i(c100;TB;W24H12)", 270, 480 ),
    IPHONE5_CHROME( "Apple iPhone 5", 320, 568 ),
    NEXUS5_CHROME( "Google Nexus 5", 360, 640 ),
    UNKNOWN( Parameters.UNKNOWN, 0, 0 );//

    private final String m_val;
    private final int    m_width;
    private final int    m_length;

    UserAgentType( String val, int width, int length )
    {
        m_val = val;
        m_width = width;
        m_length = length;
    }

    @Override
    public String val()
    {
        return m_val;
    }

    public int width()
    {
        return m_width;
    }

    public int length()
    {
        return m_length;
    }

    @Override
    public Parameters unknown()
    {
        return UNKNOWN;
    }
}
