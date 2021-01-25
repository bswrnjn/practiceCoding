package jp.co.rakuten.travel.framework.logger;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

/**
 * Custom test logger factory to create hook for controlling the create instance.  
 */
public class TestLoggerFactory implements LoggerFactory
{
    @Override
    public Logger makeNewLoggerInstance( String name )
    {
        return new TestLogger( name );
    }
}
