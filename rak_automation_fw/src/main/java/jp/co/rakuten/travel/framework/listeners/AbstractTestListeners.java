package jp.co.rakuten.travel.framework.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.log4j.Logger;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestRunner;

import jp.co.rakuten.travel.framework.configuration.Configuration;
import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import jp.co.rakuten.travel.framework.testng.LocalCommandLineArgs;
import jp.co.rakuten.travel.framework.utility.Utility;

/**
 * Provides a way to handle multiple listeners of {@link ITestListeners}
 *
 */
public abstract class AbstractTestListeners implements ITestListeners
{
    protected Logger                     LOG         = null;

    protected String                     m_logPath;
    /**
     * Listener to handle HTML output
     */
    private final List< ITestListeners > m_listeners = new ArrayList< ITestListeners >();

    @Override
    public void onExecutionStart()
    {}

    @Override
    public void onStart( ISuite suite )
    {
        /**
         * LOG4J setup
         * if native logger
         */
        if( LocalCommandLineArgs.instance().isNative )
        {
            String username = StringUtils.isEmpty( LocalCommandLineArgs.instance().username ) ? "" : "_" + LocalCommandLineArgs.instance().username;
            m_logPath = suite.getOutputDirectory() + File.separator + Utility.getTime( "yyyyMM" + File.separator + "yyyyMMddHHmmss" + username ) + File.separator;
        }
        else
        {
            m_logPath = LocalCommandLineArgs.instance().logPath;
        }

        LOG = TestLogger.setupLogger( m_logPath, this.getClass() );
        LOG.info( "TestNG executed with log directory " + m_logPath );

        LOG.info( "onStart ISuite " + suite.getName() );
        Configuration.SUITE_NAME = suite.getName();

        /**
         * member listeners initialization
         */
        addListeners( m_listeners );
        LOG.info( "Executing : " + suite.getXmlSuite().getTests().size() + " test cases" );
        /**
         * call backs
         */
        for( ITestListeners listener : m_listeners )
        {
            listener.onStart( suite );
        }
        if( TestApiObject.instance().bool( TestApiParameters.API_PROFILE_LISTENER ) )
        {
            m_listeners.add( new ProfilerListener() );
            new ProfilerListener().onStart( suite );
        }
    }

    public abstract void addListeners( List< ITestListeners > listeners );

    @Override
    public void onStart( ITestContext context )
    {
        // test case log starts here
        Reporter.clear();

        LOG.info( "onStart ITestContext " + context.getName() );

        // this is only required by getting snapshot from base test
        // when configuration has been implemented
        // there won't be any use of this code block
        LOG.info( "context output directory " + context.getOutputDirectory() );
        TestRunner runner = (TestRunner)context;
        runner.setOutputDirectory( m_logPath );
        LOG.info( "context changed output directory " + context.getOutputDirectory() );

        // reset test case time
        Utility.resetReferenceTime();

        /**
         * call backs
         */
        for( ITestListeners listener : m_listeners )
        {
            listener.onStart( context );
        }
    }

    @Override
    public void onTestStart( ITestResult result )
    {
        LOG.debug( "onTestStart " + result.getTestContext().getName() );
        LOG.info( "TEST NAME : " + result.getTestContext().getName() );

        // initialize result container
        TestLogger.clearComments();

        /**
         * call backs
         */
        for( ITestListeners listener : m_listeners )
        {
            listener.onTestStart( result );
        }
    }

    @Override
    public void onTestSuccess( ITestResult result )
    {
        LOG.info( "onTestSuccess " + result.getTestContext().getName() );

        String className = result.getMethod().getTestClass().getName();
        className = className.substring( className.lastIndexOf( '.' ) + 1 );
        LOG.info( "<<" + result.getTestContext().getCurrentXmlTest().getName() + ">> <<PASSED>> <<" + className + ">> << * >>" );

        /**
         * call backs
         */
        for( ITestListeners listener : m_listeners )
        {
            listener.onTestSuccess( result );
        }
    }

    @Override
    public void onTestFailure( ITestResult result )
    {
        LOG.info( "onTestFailure " + result.getTestContext().getName() );

        String className = result.getMethod().getTestClass().getName();
        className = className.substring( className.lastIndexOf( '.' ) + 1 );
        LOG.info( "<<" + result.getTestContext().getCurrentXmlTest().getName() + ">> <<FAILED>> <<" + className + ">> <<" + Utility.getErrorType() + ">>" );

        /**
         * call backs
         */
        for( ITestListeners listener : m_listeners )
        {
            listener.onTestFailure( result );
        }
    }

    @Override
    public void onTestSkipped( ITestResult result )
    {
        LOG.info( "onTestSkipped " + result.getTestContext().getName() );

        String className = result.getMethod().getTestClass().getName();
        className = className.substring( className.lastIndexOf( '.' ) + 1 );
        LOG.info( "<<" + result.getTestContext().getCurrentXmlTest().getName() + ">> <<SKIPPED>> <<" + className + ">> <<" + Utility.getErrorType() + ">>" );

        /**
         * call backs
         */
        for( ITestListeners listener : m_listeners )
        {
            listener.onTestSkipped( result );
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage( ITestResult result )
    {}

    @Override
    public void onFinish( ITestContext context )
    {
        LOG.info( "onFinish ITestContext " + context.getName() );

        /**
         * call backs
         */
        for( ITestListeners listener : m_listeners )
        {
            listener.onFinish( context );
        }
    }

    @Override
    public void onFinish( ISuite suite )
    {
        LOG.info( "onFinish ISuite " + suite.getName() );
        LOG.info( "output directory " + m_logPath );

        /**
         * call backs
         */
        for( ITestListeners listener : m_listeners )
        {
            listener.onFinish( suite );
        }
    }

    @Override
    public void onExecutionFinish()
    {
        LOG.debug( "onExecutionFinish" );
        LOG.info( "TestNG Finished : " + Utility.getCurrentTime( "yyyyMMddhhmmss" ) );
        LOG.info( "TestNG Duration : " + DurationFormatUtils.formatDuration( Utility.getDuration(), "HH:mm:ss:SS" ) );

        LOG.info( "Please check console log file at " + m_logPath + LocalCommandLineArgs.instance().consoleFile );

        /**
         * call backs
         */
        for( ITestListeners listener : m_listeners )
        {
            listener.onExecutionFinish();
        }
    }
}
