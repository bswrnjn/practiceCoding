package jp.co.rakuten.travel.framework.parameter;

import java.util.Map;

import jp.co.rakuten.travel.framework.logger.TestLogger;

public abstract class TestObjectsImpl implements TestObjects
{
    protected TestLogger                      LOG = (TestLogger)TestLogger.getLogger( this.getClass() );

    /** TEST CASE related parameters */
    protected final Map< Parameters, String > m_params;

    public TestObjectsImpl( Map< Parameters, String > params )
    {
        m_params = params;
    }

    @Override
    public void add( Map< Parameters, String > map )
    {
        m_params.putAll( map );
    }

}
