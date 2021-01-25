package jp.co.rakuten.travel.framework.tools.utility;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;

import jp.co.rakuten.travel.framework.utility.Utility;

/**
 * Utility class to initialize logger
 * @version 1.0.0
 * @since 1.0.0  
 */
public class LoggerUtil
{
    /**
     * Set custom log path for logger
     * @param logFileName Log file name
     */
    public static void init( String logFileName )
    {
        String m_logpath = System.getProperty( "user.dir" ) + File.separator + "utilitylog" + File.separator + Utility.getTime( "yyyyMM" + File.separator + "yyyyMMddHHmmss" ) + File.separator;

        // create log directory
        try
        {
            new File( m_logpath ).mkdirs();
        }
        catch( NullPointerException e )
        {
            throw e;
        }

        // initialize log4j
        System.setProperty( "LOG_PATH", m_logpath + logFileName );
        PropertyConfigurator.configure( "src/main/resources/log4j.properties" );
    }

    /**
     * Set custom log path for logger
     * @param logDir Log file path
     * @param logFileName Log file name
     */
    public static void init( String logDir, String logFileName )
    {
        String m_logpath = logDir + File.separator + Utility.getTime( "yyyyMM" + File.separator + "yyyyMMddHHmmss" ) + File.separator;

        // create log directory
        try
        {
            new File( m_logpath ).mkdirs();
        }
        catch( NullPointerException e )
        {
            throw e;
        }

        // initialize log4j
        System.setProperty( "LOG_PATH", m_logpath + logFileName );
        PropertyConfigurator.configure( "src/main/resources/log4j.properties" );
    }
}
