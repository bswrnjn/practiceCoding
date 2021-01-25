package jp.co.rakuten.travel.framework.logger;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;

import jp.co.rakuten.travel.framework.utility.ErrorType;
import jp.co.rakuten.travel.framework.utility.Pair;

public class TestLogger extends Logger
{
    private static final int                               MAX_WARN   = 3;
    private static final TestLoggerFactory                 s_factory  = new TestLoggerFactory();
    private static final List< Pair< String, ErrorType > > s_errors   = Collections.synchronizedList( new ArrayList< Pair< String, ErrorType > >() );
    private static final CircularFifoQueue< String >       s_warnings = new CircularFifoQueue< String >( MAX_WARN );

    protected TestLogger( String name )
    {
        super( name );
    }

    @Override
    public void error( Object message )
    {
        s_errors.add( new Pair< String, ErrorType >( String.valueOf( message ), ErrorType.UNVERIFIED ) );
        super.error( message );
    }

    public void error( Object message, ErrorType type )
    {
        s_errors.add( new Pair< String, ErrorType >( String.valueOf( message ), type ) );
        super.error( message );
    }

    @Override
    public void warn( Object message )
    {
        s_warnings.add( String.class.cast( message ) );
        super.warn( message );
    }

    @Override
    public void debug( Object message )
    {
        super.debug( message );
    }

    @Override
    public void fatal( Object message )
    {
        s_errors.add( new Pair< String, ErrorType >( String.valueOf( message ), ErrorType.UNVERIFIED ) );
        super.fatal( message );
    }

    public void fatal( Object message, ErrorType type )
    {
        s_errors.add( 0, new Pair< String, ErrorType >( String.valueOf( message ), type ) );
        super.error( message );
    }

    @Override
    public void info( Object message )
    {
        super.info( message );
    }

    // warning detected due to very old API static override
    public static Logger getLogger( @SuppressWarnings( "rawtypes" ) Class clazz )
    {
        return Logger.getLogger( clazz.getSimpleName(), s_factory );
    }

    static public Logger getLogger( String name )
    {
        return Logger.getLogger( name, s_factory );
    }

    public static void clearComments()
    {
        s_errors.clear();
        s_warnings.clear();
    }

    public static List< Pair< String, ErrorType > > errors()
    {
        return s_errors;
    }

    public static List< String > warnings()
    {
        return new ArrayList<>( s_warnings );
    }

    public static Logger setupLogger( String logFilepath, Class< ? > clazz )
    {
        URL log4jUrl = null;

        // create log directory
        try
        {
            new File( logFilepath ).mkdirs();
            log4jUrl = TestLogger.class.getClassLoader().getResource( "log4j.properties" );

            if( log4jUrl == null )
            {
                throw new NullPointerException();
            }
        }
        catch( NullPointerException e )
        {
            throw e;
        }

        // initialize log4j from file
        System.setProperty( "LOG_PATH", logFilepath );
        PropertyConfigurator.configure( log4jUrl );

        // initialize global logger
        return TestLogger.getLogger( clazz.getName() );
    }

    public static Logger setupLogger( String name, String filepath, String filename, String filePattern, String fileThreshold )
    {
        // Create Logger
        Logger log = Logger.getLogger( name );

        // Create Logging File Appender
        FileAppender fileApp = new FileAppender();
        fileApp.setName( "LocalFileAppender" );
        fileApp.setFile( filepath + File.pathSeparator + filename );
        fileApp.setLayout( new PatternLayout( filePattern ) );
        fileApp.setThreshold( Level.toLevel( fileThreshold ) );
        fileApp.setAppend( true );
        fileApp.activateOptions();

        log.addAppender( fileApp );

        return log;
    }
}
