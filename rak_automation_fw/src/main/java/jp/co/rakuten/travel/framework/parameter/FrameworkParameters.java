package jp.co.rakuten.travel.framework.parameter;

import jp.co.rakuten.travel.framework.BaseTest;

public enum FrameworkParameters implements Parameters
{

    /**
     * Test Logs directory
     * <br>Defaults to 'output'
     */
    FW_LOG_DIR( "output" ),

    /**
     * Proxy flag
     * <br> 
     * <br> Defaults to 'yes'
     */
    FW_PROXY_HOST( "" ),

    /**
     * Test Log filename
     * <br>Defaults to 'console.log' to be saved inside FW_LOG_DIR
     */
    FW_CONSOLE_LOG_FILENAME( "console.log" ),

    /**
     * Stubs to be used as external process that is based on {@link BaseTest} API
     */
    FW_STUBS( "" ),

    FW_UNKNOWN( Parameters.UNKNOWN ); //

    private final String m_val;

    FrameworkParameters( String val )
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
        return FW_UNKNOWN;
    }
}
