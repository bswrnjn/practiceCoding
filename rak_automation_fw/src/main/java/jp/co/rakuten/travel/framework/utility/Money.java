package jp.co.rakuten.travel.framework.utility;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Money
{

    public static int getAmountFromString( final String str )
    {
        return Utility.getInt( str.replaceAll( "[^\\d.]+", "" ) );
    }

    public static String getStringAmount( final String str )
    {
        return str.replaceAll( "[^\\d.]+", "" );
    }

    public static String commaSeparatedNumber( final int val )
    {
        return NumberFormat.getNumberInstance( Locale.US ).format( val );
    }

    public static float getFloat( final String val )
    {
        try
        {
            return NumberFormat.getNumberInstance( Locale.US ).parse( val ).floatValue();
        }
        catch( ParseException ex )
        {
            return 0.0f;
        }

    }

}
