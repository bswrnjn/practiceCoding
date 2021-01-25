package jp.co.rakuten.travel.framework.parameter;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.utility.Utility;

import org.apache.log4j.Logger;

public class TestApiObject
{
    private static Logger                                     LOG = TestLogger.getLogger( TestApiObject.class );

    /**
     * recommended as volatile by eclipse findbugs
     */
    private static volatile TestApiObject                     s_instance;

    private final Map< Enum< ? extends Parameters >, String > m_map;

    private TestApiObject()
    {
        m_map = new ConcurrentHashMap<>();
    }

    public static TestApiObject instance()
    {
        if( s_instance == null )
        {
            s_instance = new TestApiObject();
        }

        return s_instance;
    }

    public void clear()
    {
        m_map.clear();
    }

    public int numeric( TestApiParameters key )
    {
        try
        {
            return Integer.valueOf( m_map.get( key ) );
        }
        catch( NumberFormatException e )
        {
            LOG.warn( "NumberFormatException found for " + key );
            return 0;
        }
    }

    public long numericLong( TestApiParameters key )
    {
        try
        {
            return Long.valueOf( m_map.get( key ) );
        }
        catch( NumberFormatException e )
        {
            LOG.warn( "NumberFormatException found for " + key );
            return 0L;
        }
    }

    public boolean bool( TestApiParameters key )
    {
        return m_map.get( key ) == null ? false : m_map.get( key ).matches( "(?i:yes|true)" );
    }

    public String get( TestApiParameters key )
    {
        return m_map.get( key );
    }

    public String put( TestApiParameters key, String value )
    {
        return m_map.put( key, value );
    }

    public boolean isPriceCheckDisabled( PriceCheck check )
    {
        Set< PriceCheck > set = EnumSet.noneOf( PriceCheck.class );
        for( String price : m_map.get( TestApiParameters.API_DISABLE_CHECK ).split( "," ) )
        {
            set.add( Utility.getEnum( price, PriceCheck.class ) );
        }
        try
        {
            return set.contains( PriceCheck.ALL ) || set.contains( check );
        }
        catch( ClassCastException | NullPointerException e )
        {
            LOG.warn( e.getClass().getSimpleName() + " found" );
        }

        return false;
    }

    public boolean isPointCheckDisabled( PriceCheck check )
    {
        Set< PriceCheck > set = EnumSet.noneOf( PriceCheck.class );
        for( String price : m_map.get( TestApiParameters.API_DISABLE_CHECK ).split( "," ) )
        {
            set.add( Utility.getEnum( price, PriceCheck.class ) );
        }
        try
        {
            return set.contains( PriceCheck.ALL ) || set.contains( check );
        }
        catch( ClassCastException | NullPointerException e )
        {
            LOG.warn( e.getClass().getSimpleName() + " found" );
        }

        return false;
    }

    public enum PriceCheck implements Parameters
    {
        /**
         * to be used for initial price at search results
         * 
         */
        SEARCH,

        /**
         * to be used for sub total where all default options have been accounted
         */
        SUB_TOTAL,

        /**
         * to be used as the value of final total where tax have not been accounted
         */
        PRE_TAX,

        /**
         * to be used as the value of final total where tax have not been accounted
         */
        PRE_DISCOUNT,

        /**
         * to be used as a total where all options, taxes and discounts have been accounted
         */
        FINAL_TOTAL,

        /**
         * to be used for all point related prices, discounts and campaigns
         */
        POINTS,

        /**
         * to be used for all coupon related prices
         */
        COUPONS,

        /**
         * to be used for disabling all price checks
         */
        ALL,

        UNKNOWN;

        @Override
        public String val()
        {
            return name();
        }

        @Override
        public Parameters unknown()
        {
            return UNKNOWN;
        }
    }
}
