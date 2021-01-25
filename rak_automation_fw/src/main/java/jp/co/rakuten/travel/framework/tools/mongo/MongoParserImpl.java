package jp.co.rakuten.travel.framework.tools.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.bson.Document;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import jp.co.rakuten.travel.framework.connection.DBParameter;
import jp.co.rakuten.travel.framework.connection.MongoDao;
import jp.co.rakuten.travel.framework.connection.MongoDaoImpl;
import jp.co.rakuten.travel.framework.testng.LocalTestNG;
import jp.co.rakuten.travel.framework.tools.constants.Constants;
import jp.co.rakuten.travel.framework.tools.testng.MongoTestNG;
import jp.co.rakuten.travel.framework.tools.testng.MongoTestNG.ExecutionType;
import jp.co.rakuten.travel.framework.tools.utility.MongoUtil;

/**
 * MongoParser fetch the data from MongoDB collection using MongoDao
 * @version 1.0.0
 * @since 1.0.0  
 */
public class MongoParserImpl implements MongoParser
{
    private DBParameter m_parameter;

    public MongoParserImpl( DBParameter parameter )
    {
        this.m_parameter = parameter;
    }

    /**
     * get the List&lt;XmlSuite&gt; against List of suiteNames
     * @param List of suiteNames
     * @return List &lt;XmlSuite&gt;
     */
    @SuppressWarnings(
    { "unchecked", "rawtypes" } )
    @Override
    public List< XmlSuite > parse( List< String > suiteNames )
    {

        MongoDao dao = new MongoDaoImpl( m_parameter );
        List< Document > documents = dao.getSuites( suiteNames );
        if( jp.co.rakuten.travel.framework.utility.Utility.isEmpty( documents ) )
        {
            throw new NoSuchElementException( "No documents found related to specified suiteNames in MongoDB." );
        }

        List< XmlSuite > suites = new ArrayList< XmlSuite >();

        for( Document doc : documents )
        {
            /*
             * Create XmlSuite object by parsing the MongoDB document
             */
            XmlSuite suite = new XmlSuite();
            List< XmlTest > tests = new ArrayList< XmlTest >();
            String parallel = "false";
            if( doc.get( Constants.PARALLEL ) != null )
            {
                parallel = doc.get( Constants.PARALLEL ).toString();
            }

            suite.setParallel( parallel );
            suite.setListeners( getListeners() );
            suite.setName( doc.getString( Constants.NAME ) );
            String suiteId = doc.get( Constants.OBJECT_ID ).toString();
            List< Document > scenarios = dao.getScenarios( suiteId );

            if( jp.co.rakuten.travel.framework.utility.Utility.isEmpty( scenarios ) )
            {
                throw new NoSuchElementException( "No documents found related to specified suiteNames in MongoDB." );
            }

            List< ArrayList< Document > > testCases = new ArrayList< ArrayList< Document > >();
            for( Document scenario : scenarios )
            {
                List< String > classes = new ArrayList<>();
                String scenarioId = scenario.get( Constants.OBJECT_ID ).toString();
                testCases = dao.getTestCases( scenarioId );

                if( jp.co.rakuten.travel.framework.utility.Utility.isEmpty( testCases ) )
                {
                    throw new NoSuchElementException( "No documents found related to specified suiteNames in MongoDB." );
                }

                /*
                 * Create HashMap with all suite parameters
                 */
                Map parameters = new ConcurrentHashMap( (Document)doc.get( Constants.PARAMETERS ) );
                suite.setParameters( MongoUtil.parseParameters( parameters ) );
                /*
                 * Attach related list of XmlTest to XmlSuite
                 */
                classes.add( scenario.get( Constants.CLASSES ).toString() );
                List< XmlTest > testCaseList = MongoUtil.parseTestCases( suite, testCases, classes );
                tests.addAll( testCaseList );
            }
            suite.setTests( tests );

            // For Review / selective test cases
            ExecutionType type = MongoTestNG.getExecutionType();
            switch( type )
            {
            case SELECTIVE:
                suite = LocalTestNG.selectiveSuite( suite, MongoTestNG.getRegex() );
                break;
            case REVIEW:
                suite = LocalTestNG.reviewSuite( suite );
                break;
            case RANDOM:

                suite = LocalTestNG.randomizeSuite( suite, MongoTestNG.getRandomCount() );
                break;
            case SPLIT:
                List< XmlSuite > list = LocalTestNG.split( suite, MongoTestNG.getSplitCount() );
                suites.addAll( list );
                break;
            case UNKNOWN:
            default:
                // full regression will be run
                break;
            }
            suites.add( suite );
        }
        return suites;
    }

    /**
     * Returns a list<String> containing all listeners
     * @return : List< String > 
     */
    private List< String > getListeners()
    {
        List< String > classes = new ArrayList< String >();
        classes.add( "jp.co.rakuten.travel.framework.listeners.TestListeners" );
        classes.add( "org.testng.reporters.XMLReporter" );
        return classes;
    }
}
