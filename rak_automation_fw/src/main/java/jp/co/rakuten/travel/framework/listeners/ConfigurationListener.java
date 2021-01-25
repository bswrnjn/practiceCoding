package jp.co.rakuten.travel.framework.listeners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import jp.co.rakuten.travel.framework.configuration.Configuration;
import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.FrameworkObject;
import jp.co.rakuten.travel.framework.parameter.FrameworkParameters;
import jp.co.rakuten.travel.framework.parameter.ParameterType;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import jp.co.rakuten.travel.framework.utility.Utility;

public class ConfigurationListener implements ITestListeners
{
    protected Logger       LOG = TestLogger.getLogger( this.getClass() );

    protected final String m_logpath;

    public ConfigurationListener( String logpath )
    {
        m_logpath = logpath;
    }

    @Override
    public void onExecutionStart()
    {
        /**
         * not implemented
         */
    }

    @Override
    public void onStart( ISuite suite )
    {
        LOG.info( "onStart ISuite" );

        LOG.info( "PARAMETER SETTINGS" );

        for( FrameworkParameters param : FrameworkParameters.values() )
        {
            FrameworkObject.instance().put( param, param.val() );
            if( suite.getXmlSuite().getParameters().containsKey( param.name().toLowerCase() ) )
            {
                Object value = suite.getXmlSuite().getParameters().get( param.name().toLowerCase() );
                LOG.info( param + " : " + value );
                FrameworkObject.instance().put( param, value );
            }
        }

        for( TestApiParameters param : TestApiParameters.values() )
        {
            TestApiObject.instance().put( param, param.val() );
            if( suite.getXmlSuite().getParameters().containsKey( param.name().toLowerCase() ) )
            {
                String value = suite.getXmlSuite().getParameters().get( param.name().toLowerCase() );
                LOG.info( param + " : " + value );
                TestApiObject.instance().put( param, value );
            }
        }

        // JVM args
        LOG.info( "ARGUMENT LEVEL" );
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        List< String > argList = bean.getInputArguments();

        for( int i = 0; i < argList.size(); i++ )
        {
            // -Dparam=value
            String [] pair = argList.get( i ).substring( 2 ).split( "=" );

            if( pair == null || pair.length < 2 )
            {
                continue;
            }

            if( StringUtils.isEmpty( pair[ 1 ] ) )
            {
                pair[ 1 ] = "";
            }

            // argument level (highest priority)
            switch( ParameterType.getType( pair[ 0 ] ) )
            {
            case FRAMEWORK:
                LOG.info( "Overriding " + pair[ 0 ] + " with " + pair[ 1 ] );
                FrameworkObject.instance().put( Utility.getEnum( pair[ 0 ], FrameworkParameters.class ), pair[ 1 ] );
                break;
            case TEST_API:
                LOG.info( "Overriding " + pair[ 0 ] + " with " + pair[ 1 ] );
                TestApiObject.instance().put( Utility.getEnum( pair[ 0 ], TestApiParameters.class ), pair[ 1 ] );
                break;
            default:
                break;
            }
        }

        /**
         * save suite in a file
         */
        if( TestApiObject.instance().bool( TestApiParameters.API_SAVE_SUITE_FILE ) )
        {
            try
            {
                saveSuite( suite.getXmlSuite() );
            }
            catch( FileNotFoundException e )
            {
                LOG.warn( "Suite File not saved for " + suite.getName(), e );
            }
        }

        LOG.info( "CONFIGURATION SETTINGS" );

        Configuration.instance().init();
    }

    @Override
    public void onStart( ITestContext context )
    {
        // recreate the and test api instance for use with test level
        LOG.info( "onStart ITestContext" );

        LOG.info( "PARAMETER SETTINGS" );

        /**
         * FIXME test api parameters need to be cleared because there is no current mechanism to refresh already written parameters
         * if the parameter is already used in earlier tests, that parameter will be available on subsequent test which is not designed to
         * hold that api parameter
         */
        LOG.info( "clearing test api parameters" );
        TestApiObject.instance().clear();

        LOG.info( "setting test api parameters" );
        for( TestApiParameters key : TestApiParameters.values() )
        {
            // default level
            TestApiObject.instance().put( key, key.val() );

            String suiteParam = context.getSuite().getParameter( key.name().toLowerCase() );
            if( !StringUtils.isEmpty( suiteParam ) )
            {
                // suite level
                TestApiObject.instance().put( (TestApiParameters)key, suiteParam == null ? "" : suiteParam );
            }

            if( context.getCurrentXmlTest().getLocalParameters().get( key.name().toLowerCase() ) instanceof String )
            {
                String testParam = context.getCurrentXmlTest().getLocalParameters().get( key.name().toLowerCase() );
                if( !StringUtils.isEmpty( testParam ) )
                {
                    // test level
                    LOG.info( "Overridding " + key + " with " + testParam );
                    TestApiObject.instance().put( (TestApiParameters)key, testParam == null ? "" : testParam );
                }
            }
        }

        Configuration.instance().clear();
        Map< String, Object > paramMap = new HashMap<>();
        // suite level
        LOG.info( "SUITE PARAMETERS" );
        for( String key : context.getCurrentXmlTest().getSuite().getParameters().keySet() )
        {
            String val = String.valueOf( context.getCurrentXmlTest().getSuite().getParameters().get( key ) );
            LOG.info( key + " : " + val );
            paramMap.put( key, val == null ? "" : context.getCurrentXmlTest().getSuite().getParameters().get( key ) );
        }
        LOG.info( "TEST PARAMETERS" );
        for( String key : context.getCurrentXmlTest().getLocalParameters().keySet() )
        {
            String val = String.valueOf( context.getCurrentXmlTest().getLocalParameters().get( key ) );
            LOG.info( key + " : " + val );
            paramMap.put( key, val == null ? "" : context.getCurrentXmlTest().getLocalParameters().get( key ) );
        }

        // JVM args
        LOG.info( "ARGUMENT LEVEL" );
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        List< String > argList = bean.getInputArguments();

        for( int i = 0; i < argList.size(); i++ )
        {
            // -Dparam=value
            String [] pair = argList.get( i ).substring( 2 ).split( "=" );

            if( pair == null || pair.length < 2 )
            {
                continue;
            }

            if( StringUtils.isEmpty( pair[ 1 ] ) )
            {
                pair[ 1 ] = "";
            }

            // argument level (highest priority)
            switch( ParameterType.getType( pair[ 0 ] ) )
            {
            case TEST_API:
                LOG.info( "Overriding " + pair[ 0 ] + " with " + pair[ 1 ] );
                TestApiObject.instance().put( Utility.getEnum( pair[ 0 ], TestApiParameters.class ), pair[ 1 ] );
                break;
            default:
                LOG.info( "Overriding " + pair[ 0 ] + " with " + pair[ 1 ] );
                paramMap.put( pair[ 0 ], pair[ 1 ] );
                break;
            }
        }

        // override parameters based on test
        Configuration.instance().add( paramMap );
        context.setAttribute( Configuration.CONFIG, Configuration.instance() );
    }

    @Override
    public void onTestStart( ITestResult result )
    {
        Configuration.instance().refresh();
    }

    @Override
    public void onTestSuccess( ITestResult result )
    {
        /**
         * not implemented
         */
    }

    @Override
    public void onTestFailure( ITestResult result )
    {
        Configuration.instance().recover();
    }

    @Override
    public void onTestSkipped( ITestResult result )
    {
        /**
         * not implemented
         */
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage( ITestResult result )
    {
        /**
         * not implemented
         */
    }

    @Override
    public void onFinish( ITestContext context )
    {
        /**
         * not implemented
         */
    }

    @Override
    public void onFinish( ISuite suite )
    {
        Configuration.instance().release();
    }

    @Override
    public void onExecutionFinish()
    {
        /**
         * not implemented
         */
    }

    private void saveSuite( XmlSuite suite ) throws FileNotFoundException
    {
        LOG.info( "saveSuite " + suite.getName() );

        String filename = m_logpath + File.separator + suite.getName() + ".xml";
        try( PrintWriter out = new PrintWriter( filename ) )
        {
            out.println( suite.toXml() );
            out.close();
            LOG.info( "Suite file saved in " + filename );
        }
        catch( FileNotFoundException e )
        {
            throw e;
        }
    }
}
