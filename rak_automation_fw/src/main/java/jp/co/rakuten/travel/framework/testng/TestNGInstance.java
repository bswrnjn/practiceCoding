package jp.co.rakuten.travel.framework.testng;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import jp.co.rakuten.travel.framework.utility.ThreadInstance;

import org.apache.log4j.Logger;
import org.testng.TestNGException;
import org.testng.TestRunner;
import org.testng.xml.XmlSuite;

public class TestNGInstance extends ThreadInstance
{
    private final XmlSuite m_suite;
    private final Logger   LOG;

    public TestNGInstance( XmlSuite suite, Logger logger )
    {
        m_suite = suite;
        LOG = logger;
    }

    @Override
    protected boolean test()
    {
        LOG.info( "Starting Thread Instance for #" + getTask() + " " + getName() );

        boolean ret = false;
        try
        {
            ret = execute();
        }
        catch( TestNGException ex )
        {
            LOG.error( ex.getMessage(), ex );
            if( TestRunner.getVerbose() > 1 )
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter( sw );
                ex.printStackTrace( pw );
                sw.toString();
                LOG.error( sw );
            }
            else
            {
                LOG.error( ex.getMessage() );
            }
        }

        return ret;
    }

    public ThreadInstance getInstance()
    {
        return this;
    }

    private boolean execute()
    {
        LOG.info( "Execution of TestNG instance #" + m_task );

        LocalTestNG testNg = new LocalTestNG( LocalCommandLineArgs.instance() );
        List< XmlSuite > suiteList = new ArrayList<>();
        suiteList.add( m_suite );
        testNg.setXmlSuites( suiteList );
        boolean ret = true;
        try
        {
            testNg.run();
        }
        catch( Exception e )
        {
            LOG.error( "TestNG on #" + m_task + " failed with " + e.getClass().getSimpleName() + " " + e.getMessage(), e );
            ret = false;
        }
        return ret;
    }
}
