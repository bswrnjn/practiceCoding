package jp.co.rakuten.travel.framework.utility;

import jp.co.rakuten.travel.framework.html.HtmlTag;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class ReporterLayout extends PatternLayout
{
    @Override
    public String format( LoggingEvent event )
    {
        String newMsg = StringEscapeUtils.escapeHtml4( event.getMessage().toString() );
        Throwable throwable = event.getThrowableInformation() == null ? null : event.getThrowableInformation().getThrowable();

        LoggingEvent encodedEvent = new LoggingEvent( event.fqnOfCategoryClass, Logger.getLogger( event.getLoggerName() ), event.timeStamp, event.getLevel(), newMsg, throwable );
        String baseFmt = super.format( encodedEvent ).trim();

        if( baseFmt.contains( " ERROR [" ) )
        {
            baseFmt = HtmlTag.FONT.open( "color=\"red\"" ) + baseFmt + HtmlTag.FONT.close();
        }
        if( baseFmt.contains( " FATAL [" ) )
        {
            baseFmt = "<b>" + HtmlTag.FONT.open( "color=\"red\"" ) + baseFmt + HtmlTag.FONT.close() + "</b>";
        }

        return baseFmt;
    }
}
