package jp.co.rakuten.travel.framework.utility;

import java.util.concurrent.TimeUnit;

public class Time
{
    public final static int            SECOND      = 1000;
    public final static int            MINUTE      = 60 * SECOND;
    public final static int            HOUR        = 60 * MINUTE;
    public final static int            DAY         = 24 * HOUR;
    public final static int            WEEK        = 7 * DAY;

    private static ThreadLocal< Long > s_timeStart = new ThreadLocal< Long >();

    private Time()
    {

    }

    public static void setTimeStart()
    {
        s_timeStart.set( System.currentTimeMillis() );
    }

    public static long getDuration()
    {
        return System.currentTimeMillis() - s_timeStart.get();
    }

    public static String getStrDuration()
    {
        return getDuration( getDuration() );
    }

    public static String getDuration( long milli )
    {
        return String.format( "%02d::%02d::%02d", //
                TimeUnit.MILLISECONDS.toHours( milli ),
                TimeUnit.MILLISECONDS.toMinutes( milli ),
                TimeUnit.MILLISECONDS.toSeconds( milli ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( milli ) ) );
    }
}
