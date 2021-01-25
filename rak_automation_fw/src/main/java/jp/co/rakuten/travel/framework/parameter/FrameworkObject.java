package jp.co.rakuten.travel.framework.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.stubs.Stub;
import jp.co.rakuten.travel.framework.stubs.Stub.StubLocation;
import jp.co.rakuten.travel.framework.stubs.Stub.StubType;
import jp.co.rakuten.travel.framework.stubs.StubFactory;
import jp.co.rakuten.travel.framework.stubs.StubInfo;
import jp.co.rakuten.travel.framework.stubs.StubInfo.StubInfoKey;
import jp.co.rakuten.travel.framework.utility.Utility;

public class FrameworkObject
{
    private Logger                                   LOG   = TestLogger.getLogger( FrameworkObject.class );

    /**
     * recommended as volatile by eclipse findbugs
     */
    private static volatile FrameworkObject          s_instance;

    private final Map< FrameworkParameters, Object > m_map = new ConcurrentHashMap<>();

    private FrameworkObject()
    {}

    public static FrameworkObject instance()
    {
        if( s_instance == null )
        {
            s_instance = new FrameworkObject();
        }

        return s_instance;
    }

    public void clear()
    {
        m_map.clear();
    }

    public int numeric( FrameworkParameters key )
    {
        try
        {
            return Integer.valueOf( get( key ) );
        }
        catch( NumberFormatException e )
        {
            LOG.warn( "NumberFormatException found for " + key );
            return 0;
        }
    }

    public long numericLong( FrameworkParameters key )
    {
        try
        {
            return Long.valueOf( get( key ) );
        }
        catch( NumberFormatException e )
        {
            LOG.warn( "NumberFormatException found for " + key );
            return 0L;
        }
    }

    public boolean bool( FrameworkParameters key )
    {
        return get( key ).matches( "(?i:yes|true)" );
    }

    public String get( FrameworkParameters key )
    {
        try
        {
            return (String)m_map.get( key );
        }
        catch( ClassCastException e )
        {
            LOG.warn( "Key object is NOT a String : " + key );
        }
        return StringUtils.EMPTY;
    }

    public void put( FrameworkParameters key, Object value )
    {
        m_map.put( key, value );
    }

    public List< ? extends Stub > stub( StubLocation location )
    {
        List< Stub > stubs = new ArrayList<>();

        // parse map for stubs

        for( StubInfo info : generateStubsInfo() )
        {
            if( info.location().equals( location ) )
            {
                try
                {
                    stubs.add( StubFactory.stub( info ) );
                }
                catch( InstantiationException | IllegalAccessException e )
                {
                    LOG.warn( e.getClass().getSimpleName() + " found" );
                    LOG.warn( "Stub " + info.clazz().getSimpleName() + " for " + location + " is NOT created " );
                }
            }
        }

        return stubs;
    }

    private Set< StubInfo > generateStubsInfo()
    {
        Set< StubInfo > stubs = new HashSet<>();

        if( m_map.get( FrameworkParameters.FW_STUBS ).toString().isEmpty() )
        {
            return stubs;
        }

        @SuppressWarnings( "unchecked" )
        Set< String > set = LinkedHashSet.class.cast( m_map.get( FrameworkParameters.FW_STUBS ) );
        for( Object obj : set )
        {
            if( obj instanceof LinkedHashMap )
            {
                @SuppressWarnings( "unchecked" )
                Map< String, String > map = HashMap.class.cast( obj );
                if( map == null )
                {
                    continue;
                }

                try
                {
                    StubInfo info = new StubInfo.ContainerBuilder() //
                            .clazz( map.get( StubInfoKey.CLASS.name().toLowerCase() ) )
                            .type( Utility.getEnum( map.get( StubInfoKey.TYPE.name().toLowerCase() ), StubType.class ) )
                            .location( Utility.getEnum( map.get( StubInfoKey.LOCATION.name().toLowerCase() ), StubLocation.class ) )
                            .async( map.get( StubInfoKey.ASYNC.name().toLowerCase() ).contains( "yes" ) )
                            .build();

                    stubs.add( info );

                }
                catch( ClassNotFoundException e )
                {
                    LOG.warn( e.getClass().getSimpleName() + " found. Stub NOT parsed" );
                }
            }
        }

        return stubs;
    }
}
