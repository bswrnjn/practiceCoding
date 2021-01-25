package jp.co.rakuten.travel.framework.stubs;

import jp.co.rakuten.travel.framework.stubs.Stub.StubLocation;
import jp.co.rakuten.travel.framework.stubs.Stub.StubType;
import jp.co.rakuten.travel.framework.utility.Builder;

/**
 * stub info container that holds the parameters to create a stub
 *
 */
public class StubInfo
{
    private final StubType                m_type;
    private final StubLocation            m_location;
    private final Class< ? extends Stub > m_clazz;
    private final boolean                 m_async;

    private StubInfo( ContainerBuilder builder )
    {
        m_type = builder.m_type;
        m_location = builder.m_location;
        m_clazz = builder.m_clazz;
        m_async = builder.m_async;
    }

    public StubType type()
    {
        return m_type;
    }

    public StubLocation location()
    {
        return m_location;
    }

    public Class< ? extends Stub > clazz()
    {
        return m_clazz;
    }

    public boolean async()
    {
        return m_async;
    }

    public static class ContainerBuilder implements Builder< StubInfo >
    {
        private StubType                m_type;
        private StubLocation            m_location;
        private Class< ? extends Stub > m_clazz;
        private boolean                 m_async;

        public ContainerBuilder type( StubType type )
        {
            m_type = type;
            return this;
        }

        public ContainerBuilder location( StubLocation location )
        {
            m_location = location;
            return this;
        }

        @SuppressWarnings( "unchecked" )
        public ContainerBuilder clazz( String clazz ) throws ClassNotFoundException
        {
            Class< ? > classDef = Class.forName( clazz );
            m_clazz = (Class< ? extends Stub >)classDef;
            return this;
        }

        public ContainerBuilder async( boolean async )
        {
            m_async = async;
            return this;
        }

        @Override
        public StubInfo build()
        {
            return new StubInfo( this );
        }
    }

    public enum StubInfoKey
    {
        TYPE,

        LOCATION,

        CLASS,

        ASYNC,
    }
}
