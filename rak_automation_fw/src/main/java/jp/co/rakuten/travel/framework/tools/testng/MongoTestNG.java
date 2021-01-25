package jp.co.rakuten.travel.framework.tools.testng;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

import jp.co.rakuten.travel.framework.connection.DBParameter;
import jp.co.rakuten.travel.framework.tools.constants.Constants.CommandConstants;
import jp.co.rakuten.travel.framework.tools.mongo.MongoParser;
import jp.co.rakuten.travel.framework.tools.mongo.MongoParserImpl;

/**
 * It is wrapper class over org.testng.TestNG.
 * It fetches the suites from MongoDB, run those suites against TestNG
 * @version 1.0.0
 * @since 1.0.0  
 */
public class MongoTestNG
{
    private List< String > m_suiteNames;
    private DBParameter    m_parameter;
    private String         m_outputDirectory;
    private static String  m_regex;
    private static boolean m_review     = false;
    private static int     m_random     = 0;
    private static int     m_splitCount = 0;

    public MongoTestNG( Map< CommandConstants, String > arguments, DBParameter dbParameter, List< String > suiteNames, String outputDirectory )
    {
        if( dbParameter == null )
        {
            throw new IllegalArgumentException( "Please provide DBParameter, it should not be null." );
        }
        if( jp.co.rakuten.travel.framework.utility.Utility.isEmpty( suiteNames ) )
        {
            throw new IllegalArgumentException( "Please provide list of SuiteNames." );
        }
        this.m_suiteNames = suiteNames;
        this.m_parameter = dbParameter;
        this.m_outputDirectory = outputDirectory;
        if( !StringUtils.isEmpty( arguments.get( CommandConstants.SELECTIVETESTS ) ) )
        {
            m_regex = arguments.get( CommandConstants.SELECTIVETESTS );
        }
        else if( arguments.get( CommandConstants.REVIEW ).equals( "true" ) )
        {
            m_review = true;
        }
        else if( !StringUtils.isEmpty( arguments.get( CommandConstants.RANDOM ) ) )
        {
            m_random = Integer.valueOf( arguments.get( CommandConstants.RANDOM ) );
        }
        else if( !StringUtils.isEmpty( arguments.get( CommandConstants.SPLIT ) ) )
        {
            m_splitCount = Integer.valueOf( arguments.get( CommandConstants.SPLIT ) );
        }
    }

    public static String getRegex()
    {
        return m_regex;
    }

    public static int getRandomCount()
    {
        return m_random;
    }

    public static int getSplitCount()
    {
        return m_splitCount;
    }

    public static ExecutionType getExecutionType()
    {
        ExecutionType executionType = ExecutionType.UNKNOWN;
        if( m_random > 0 )
        {
            executionType = ExecutionType.RANDOM;
        }
        else if( m_splitCount > 0 )
        {
            executionType = ExecutionType.SPLIT;
        }
        else if( m_review == true )
        {
            executionType = ExecutionType.REVIEW;
        }
        else if( !StringUtils.isEmpty( m_regex ) )
        {
            executionType = ExecutionType.SELECTIVE;
        }
        return executionType;
    }

    /**
     * Runs the TestSuites
     */
    public void run()
    {
        MongoParser parser = new MongoParserImpl( m_parameter );
        List< XmlSuite > suites = parser.parse( m_suiteNames );

        TestNG testNG = new TestNG();
        testNG.setXmlSuites( suites );
        testNG.setOutputDirectory( m_outputDirectory );
        testNG.setUseDefaultListeners( false );
        testNG.run();

    }

    public enum ExecutionType
    {
        RANDOM,
        SELECTIVE,
        REVIEW,
        SPLIT,
        UNKNOWN;
    }
}
