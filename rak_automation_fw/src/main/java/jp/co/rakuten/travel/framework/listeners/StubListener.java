package jp.co.rakuten.travel.framework.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.stubs.ContextStub;
import jp.co.rakuten.travel.framework.stubs.ResultStub;
import jp.co.rakuten.travel.framework.stubs.Stub;
import jp.co.rakuten.travel.framework.utility.Utility;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Manages parameters to determine on where to create and execute stubs
 *
 */
public class StubListener implements ITestListener
{
    private Logger       LOG;

    private final String PRE_PROCESS_STUB  = "pre_process";
    private final String POST_PROCESS_STUB = "post_process";
    private final String STUB_CLASS        = "class";

    private Set< Stub >  m_preProcessStubs;
    private Set< Stub >  m_postProcessStubs;

    @SuppressWarnings( "unchecked" )
    @Override
    public void onStart( ITestContext context )
    {
        LOG = TestLogger.getLogger( this.getClass() );
        LOG.info( "onStart ITestContext" );

        m_preProcessStubs = new HashSet< Stub >();
        m_postProcessStubs = new HashSet< Stub >();

        Map< String, Object > raw = new HashMap< String, Object >();
        raw.putAll( context.getCurrentXmlTest().getTestParameters() );

        Set< Map< String, String >> preProcessStubs = new HashSet< Map< String, String >>();
        Set< Map< String, String >> postProcessStubs = new HashSet< Map< String, String >>();

        try
        {
            if( raw.get( PRE_PROCESS_STUB ) != null )
            {
                Set< String > set = LinkedHashSet.class.cast( raw.get( PRE_PROCESS_STUB ) );
                for( Object obj : set )
                {
                    if( obj instanceof LinkedHashMap )
                    {
                        preProcessStubs.add( (LinkedHashMap< String, String >)obj );
                    }
                }
            }
            if( raw.get( POST_PROCESS_STUB ) != null )
            {
                Set< String > set = LinkedHashSet.class.cast( raw.get( POST_PROCESS_STUB ) );
                for( Object obj : set )
                {
                    if( obj instanceof LinkedHashMap )
                    {
                        postProcessStubs.add( (LinkedHashMap< String, String >)obj );
                    }
                }
            }
        }
        catch( ClassCastException e )
        {
            LOG.error( e.getClass().getSimpleName() + " found while parsing Stubs" );
            return;
        }

        if( Utility.isEmpty( preProcessStubs ) )
        {
            LOG.warn( "NO PRE PROCESS Stubs found" );
        }
        if( Utility.isEmpty( postProcessStubs ) )
        {
            LOG.warn( "NO POST PROCESS Stubs found" );
        }

        for( Map< String, String > key : preProcessStubs )
        {
            try
            {
                if( key.get( STUB_CLASS ) == null )
                {
                    LOG.warn( "This entry has no usable class to process" );
                    continue;
                }
                Class< ? > classDef;
                LOG.info( "Parsing PRE_PROCESS for... " + key.get( STUB_CLASS ) );
                classDef = Class.forName( key.get( STUB_CLASS ) );
                Constructor< ? > constructor = classDef.getConstructor( Map.class );

                Stub stub = (Stub)constructor.newInstance( key );
                m_preProcessStubs.add( stub );
            }
            catch( LinkageError | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e )
            {
                LOG.fatal( e.getClass().getSimpleName() + " found while parsing PRE_PROCESS_STUB " + key.get( STUB_CLASS ) );
            }
        }

        for( Map< String, String > key : postProcessStubs )
        {
            try
            {
                if( key.get( STUB_CLASS ) == null )
                {
                    LOG.warn( "This entry has no usable class to process" );
                    continue;
                }
                Class< ? > classDef;
                LOG.info( "Parsing POST_PROCESS for... " + key.get( STUB_CLASS ) );
                classDef = Class.forName( key.get( STUB_CLASS ) );
                Constructor< ? > constructor = classDef.getConstructor( Map.class );

                Stub stub = (Stub)constructor.newInstance( key );
                m_postProcessStubs.add( stub );
            }
            catch( LinkageError | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e )
            {
                LOG.fatal( e.getClass().getSimpleName() + " found while parsing PRE_PROCESS_STUB " + key.get( STUB_CLASS ) );
            }
        }

        for( Stub stub : m_preProcessStubs )
        {
            try
            {
                LOG.info( "Execute call for " + stub.name() );
                if( stub instanceof ContextStub )
                {
                    ContextStub cStub = ContextStub.class.cast( stub );
                    cStub.execute( context );
                }
                else
                {
                    stub.execute();
                }
            }
            catch( ClassCastException e )
            {
                LOG.error( e.getClass().getSimpleName() + " found for " + stub.name() );
            }
        }
    }

    @Override
    public void onTestStart( ITestResult result )
    {
        LOG.info( "onTestStart ITestResult" );

        for( Stub stub : m_preProcessStubs )
        {
            try
            {
                if( stub instanceof ResultStub )
                {
                    ResultStub rStub = ResultStub.class.cast( stub );
                    rStub.execute( result );
                }
                else
                {
                    stub.execute();
                }
            }
            catch( ClassCastException e )
            {
                LOG.error( e.getClass().getSimpleName() + " found for " + stub.name() );
            }
        }
    }

    @Override
    public void onTestSuccess( ITestResult result )
    {
        LOG.info( "onTestSuccess ITestResult" );

        for( Stub stub : m_postProcessStubs )
        {
            try
            {
                if( stub instanceof ResultStub )
                {
                    ResultStub rStub = ResultStub.class.cast( stub );
                    rStub.execute( result );
                }
                else
                {
                    stub.execute();
                }
            }
            catch( ClassCastException e )
            {
                LOG.error( e.getClass().getSimpleName() + " found for " + stub.name() );
            }
        }
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
    public void onTestFailedButWithinSuccessPercentage( ITestResult result )
    {

    }

    @Override
    public void onFinish( ITestContext context )
    {
        LOG.info( "onFinish ITestContext" );

        for( Stub stub : m_postProcessStubs )
        {
            try
            {
                if( stub instanceof ContextStub )
                {
                    ContextStub cStub = ContextStub.class.cast( stub );
                    cStub.execute( context );
                }
                else
                {
                    stub.execute();
                }
            }
            catch( ClassCastException e )
            {
                LOG.error( e.getClass().getSimpleName() + " found for " + stub.name() );
            }
        }
    }

}
