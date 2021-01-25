package jp.co.rakuten.travel.framework.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;

import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.utility.Pair;
import jp.co.rakuten.travel.framework.utility.Utility;

public class WebEventListener implements ITestListeners, EventListener
{
    protected Logger                                 LOG      = TestLogger.getLogger( this.getClass() );

    private static WebEventListener                  s_instance;

    private List< Pair< EventType, EventListener > > m_events = new ArrayList<>();

    public static WebEventListener instance()
    {
        if( s_instance == null )
        {
            s_instance = new WebEventListener();
        }

        return s_instance;
    }

    protected void addEvent( EventType type, EventListener obj )
    {
        m_events.add( new Pair<>( type, obj ) );
    }

    protected EventListener [] events( EventType type )
    {
        List< EventListener > listeners = new ArrayList<>();
        for( Pair< EventType, EventListener > pair : m_events )
        {
            if( pair.first().equals( type ) )
            {
                listeners.add( pair.second() );
            }
        }
        return (EventListener [])listeners.toArray( new EventListener [ 0 ] );
    }

    @Override
    public void onExecutionStart()
    {
        s_instance = this;
    }

    @Override
    public void onStart( ISuite suite )
    {
        LOG.info( "onStart ISuite" );

        LOG.info( "*** SUITE CALLBACK ***" );

        // parse the callbacks key
        if( suite.getXmlSuite().getParameters().get( EventListener.CALLBACKS ) != null )
        {
            @SuppressWarnings( "unchecked" )
            Set< String > set = LinkedHashSet.class.cast( suite.getXmlSuite().getParameters().get( EventListener.CALLBACKS ) );
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

                    // type
                    PageActionType type = Utility.getEnum( map.get( EventListener.CALLBACK_TYPE ), PageActionType.class );

                    // class
                    try
                    {
                        Class< ? > classDef = Class.forName( map.get( EventListener.CALLBACK_CLASS ) );

                        EventListener listener = (EventListener)classDef.newInstance();

                        m_events.add( new Pair<>( type, listener ) );
                    }
                    catch( ClassNotFoundException | InstantiationException | IllegalAccessException e )
                    {
                        LOG.fatal( e.getClass().getSimpleName() + " found while parsing callback info " + type + " " + map.get( EventListener.CALLBACK_CLASS ) );
                    }
                    LOG.info( "Suite level callback for " + type + " with " + map.get( EventListener.CALLBACK_CLASS ) );
                }
            }
        }

    }

    @Override
    public void onStart( ITestContext context )
    {

    }

    @Override
    public void onFinish( ITestContext context )
    {
        if( !validate() )
        {
            // FIXME - need to tell the actual test that it failed
            // SoftAssert softAssert = new SoftAssert();
            // softAssert.fail( "Validation failed for " + this.getClass().getSimpleName() );
            // softAssert.assertAll();
        }
    }

    @Override
    public void onTestSuccess( ITestResult result )
    {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage( ITestResult result )
    {

    }

    @Override
    public void onTestStart( ITestResult result )
    {

    }

    @Override
    public void onTestFailure( ITestResult result )
    {

    }

    @Override
    public void onTestSkipped( ITestResult result )
    {

    }

    @Override
    public void onFinish( ISuite suite )
    {

    }

    @Override
    public void onExecutionFinish()
    {

    }

    @Override
    public void callback( EventType type )
    {
        for( Pair< EventType, EventListener > pair : m_events )
        {
            if( pair.first().equals( type ) )
            {
                pair.second().callback( type );
            }
        }
    }

    @Override
    public void callback()
    {
        for( Pair< EventType, EventListener > pair : m_events )
        {
            pair.second().callback();
        }
    }

    @Override
    public void callback( Object ... objs )
    {
        for( Pair< EventType, EventListener > pair : m_events )
        {
            pair.second().callback( objs );
        }
    }

    @Override
    public boolean validate()
    {
        boolean ret = true;
        for( Pair< EventType, EventListener > pair : m_events )
        {
            if( !pair.second().validate() )
            {
                ret = false;
            }
        }
        return ret;
    }
}
