package jp.co.rakuten.travel.framework.utility;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.testng.Reporter;

public class ReporterAppender extends AppenderSkeleton
{

    public ReporterAppender()
    {
        super();
    }

    public ReporterAppender( Layout layout )
    {
        super();
        setLayout( layout );
    }

    @Override
    protected void append( LoggingEvent event )
    {
        Reporter.log( layout.format( event ) );
        ThrowableInformation throwable = event.getThrowableInformation();
        if( throwable != null )
        {
            StringWriter sw = new StringWriter();
            throwable.getThrowable().printStackTrace( new PrintWriter( sw ) );
            Reporter.log( sw.toString() );
        }
    }

    @Override
    public void close()
    {}

    @Override
    public boolean requiresLayout()
    {
        return true;
    }

}
