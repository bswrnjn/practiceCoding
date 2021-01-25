package jp.co.rakuten.travel.framework.app.android;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import jp.co.rakuten.travel.framework.configuration.Controller;
import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.Parameters;

/**
 * Controller to handle system console
 */
public class SystemConsole implements Controller
{
    protected TestLogger LOG = (TestLogger)TestLogger.getLogger( this.getClass() );

    public void execute( ConsoleCommand consoleCommand )
    {
        if( !consoleCommand.equals( ConsoleCommand.UNKNOWN ) )
        {
            LOG.info( "Processing " + consoleCommand + "..." );
        }
        try
        {
            Process process = Runtime.getRuntime().exec( consoleCommand.val() );
            try
            {
                if( !process.waitFor( 30, TimeUnit.SECONDS ) )
                {
                    process.destroy();
                }
            }
            catch( InterruptedException exception )
            {
                LOG.debug( exception.getClass().getSimpleName() + " found with message " + exception.getMessage() );
                LOG.warn( "Command execution: " + consoleCommand + " failed" );
            }
        }
        catch( IOException exception )
        {
            LOG.debug( exception.getClass().getSimpleName() + " found with message " + exception.getMessage() );
            LOG.warn( "Command execution: " + consoleCommand + " failed" );
        }
    }

    public enum ConsoleCommand implements Parameters
    {
        //
        /**
         * Start the emulator
         */
        EMULATOR_START( "emulator -avd Nexus -port 5554" ),
        /**
         * Shutdown the emulator
         */
        EMULATOR_SHUTDOWN( "adb -s emulator-5554 emu kill" ),
        /**
         * Open Airplane Mode in emulator
         */
        AIRPLANE_MODE_OPEN( "adb shell settings put global airplane_mode_on 1" ),
        /**
         * Close Airplane Mode in emulator
         */
        AIRPLANE_MODE_CLOSE( "adb shell settings put global airplane_mode_on 0" ),
        /**
         * After opening or closing Airplane mode, execute it 
         */
        AIRPLANE_MODE_EXECUTION( "adb shell am broadcast -a android.intent.action.AIRPLANE_MODE" ),
        /**
         * Clean up APNs
         */
        APN_DELETE( "adb shell sqlite3 /data/data/com.android.providers.telephony/databases/telephony.db 'DELETE FROM carriers;'" ),
        /**
         * Create empty APN
         */
        APN_CREATE( "adb shell sqlite3 /data/data/com.android.providers.telephony/databases/telephony.db 'INSERT INTO carriers(name, numeric, mcc, mnc, apn,current,sub_id, edited)   VALUES(\\\"STG\\\",\\\"310260\\\",\\\"310\\\",\\\"260\\\",\\\"epc.tmobile.com\\\",1,1,1);'" ),
        /**
         * SET APN PROXY
         */
        APN_PROXY_SET( "adb shell sqlite3 /data/data/com.android.providers.telephony/databases/telephony.db 'UPDATE carriers SET proxy=\\\"stg-proxy.intra-tool.rakuten.co.jp\\\", port=\\\"9502\\\" WHERE name=\\\"STG\\\";'" ),

        /**
         * REMOVE APN PROXY
         */
        APN_PROXY_REMOVE( "adb shell sqlite3 /data/data/com.android.providers.telephony/databases/telephony.db 'UPDATE carriers SET proxy=\\\"\\\", port=\\\"\\\" WHERE name=\\\"STG\\\"'" ),
        /**
         * UNKNOWN
         */
        UNKNOWN( Parameters.UNKNOWN );

        private final String m_val;

        ConsoleCommand( String val )
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
}
