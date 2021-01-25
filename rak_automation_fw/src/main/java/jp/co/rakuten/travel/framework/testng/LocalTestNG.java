
package jp.co.rakuten.travel.framework.testng;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.CommandLineArgs;
import org.testng.TestNG;
import org.testng.TestNGException;
import org.testng.TestRunner;
import org.testng.xml.Parser;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.xml.sax.SAXException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.google.common.collect.Lists;

import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.utility.ThreadController;
import jp.co.rakuten.travel.framework.utility.Time;
import jp.co.rakuten.travel.framework.utility.Utility;

/**
 * this is the main entry point for the test framework
 * <br> originally copied from org.testng.remote.RemoteTestNG.class
 * <br> created to support multiple parameters for execution
 *
 */
public class LocalTestNG extends TestNG
{
    private static Logger              LOG = null;

    private final LocalCommandLineArgs m_args;

    public LocalTestNG( LocalCommandLineArgs args )
    {
        m_args = args;
    }

    public void configure()
    {
        super.configure( m_args );
    }

    public static void main( String [] args )
    {
        // try native testng
        try
        {
            CommandLineArgs clArgs = new CommandLineArgs();
            new JCommander( clArgs, args );

            // set log path
            validateCommandLineParameters( clArgs );

            TestNG.main( args );
            return;
        }
        catch( ParameterException e )
        {
            /**
             * 
             */
        }

        // try custom testng
        try
        {
            new JCommander( LocalCommandLineArgs.instance(), args );
            validateCommandLineParameters( LocalCommandLineArgs.instance() );
        }
        catch( ParameterException e )
        {
            usage();
            throw e;
        }

        LocalCommandLineArgs.instance().isNative = false;

        // no suite file, no test
        if( Utility.isEmpty( LocalCommandLineArgs.instance().suiteFiles ) )
        {
            usage();
            throw new ParameterException( "Suite file not found" );
        }

        // suite files will be processed independently
        // split the suite into smaller suites if requested
        Iterator< String > iter = LocalCommandLineArgs.instance().suiteFiles.iterator();
        while( iter.hasNext() )
        {
            // get next item
            String suitefile = iter.next();
            // original suite file will be removed
            iter.remove();

            // create xml suite from file
            XmlSuite suite = null;
            try
            {
                suite = createXmlSuite( suitefile );
            }
            catch( NullPointerException e )
            {
                throw e;
            }

            if( suite == null )
            {
                throw new NullPointerException( "Invalid Suite file" );
            }

            // change suite name if specified
            if( !StringUtils.isEmpty( LocalCommandLineArgs.instance().suiteName ) )
            {
                suite.setName( LocalCommandLineArgs.instance().suiteName );
            }

            // set log path
            String username = StringUtils.isEmpty( LocalCommandLineArgs.instance().username ) ? "" : "_" + LocalCommandLineArgs.instance().username;
            String logPath = LocalCommandLineArgs.instance().outputDirectory + File.separator + suite.getName() + File.separator //
                    + Utility.getTime( "yyyyMM" + File.separator + "yyyyMMddHHmmss" ) + username + File.separator;

            LocalCommandLineArgs.instance().logPath = logPath;

            LOG = TestLogger.setupLogger( logPath, LocalTestNG.class );
            LOG.info( "LOGGER setup " + logPath );

            XmlSuite selectiveSuite = null;
            XmlSuite reviewSuite = null;
            if( !StringUtils.isEmpty( LocalCommandLineArgs.instance().selective ) )
            {
                selectiveSuite = selectiveSuite( suite, LocalCommandLineArgs.instance().selective );
                LOG.info( "Selective test with REGEX \"" + LocalCommandLineArgs.instance().selective + "\"" );
            }
            if( LocalCommandLineArgs.instance().isReview )
            {
                reviewSuite = reviewSuite( suite );
                LOG.info( "Review Regression Test Cases Selected." );
            }
            if( selectiveSuite != null && reviewSuite != null )
            {
                suite = selectiveSuite;
                for( XmlTest test : reviewSuite.getTests() )
                {
                    if( !suite.getTests().contains( test ) )
                    {
                        suite.addTest( test );
                    }
                }

            }
            else if( selectiveSuite != null )
            {
                suite = selectiveSuite;
            }
            else if( reviewSuite != null )
            {
                suite = reviewSuite;
            }

            if( Utility.isEmpty( suite.getTests() ) )
            {
                throw new ParameterException( "No tests specified in Suite \"" + suite.getName() + "\"" );
            }

            if( LocalCommandLineArgs.instance().random > 0 )
            {
                LOG.info( "Random test with " + LocalCommandLineArgs.instance().random + " out of " + suite.getTests().size() + " test cases" );
                suite = randomizeSuite( suite, LocalCommandLineArgs.instance().random );
            }

            LOG.info( "Executing " + suite.getTests().size() + " test cases" );

            if( LocalCommandLineArgs.instance().splitCount > 0 )
            {
                List< XmlSuite > suiteList = split( suite, LocalCommandLineArgs.instance().splitCount );

                // multi-threaded parallel execution
                if( LocalCommandLineArgs.instance().isParallelSuite && !parallelExecute( suite.getName(), logPath, suiteList ) )
                {
                    LOG.error( "Parallel execution of multi TestNG Failed" );
                }
                // single thread sequential execution
                else
                {
                    sequentialExecute( suite.getName(), logPath, suiteList );
                    LOG.error( "Sequential execution of multi TestNG Failed" );
                }
            }
            else
            {
                if( !singleExecute( suite ) )
                {
                    System.exit( HAS_FAILURE );
                }
            }
        }
    }

    public static XmlSuite selectiveSuite( XmlSuite suite, String regex )
    {
        XmlSuite newSuite = (XmlSuite)suite.clone();
        newSuite.setParameters( suite.getParameters() );

        Pattern p = Pattern.compile( regex );

        for( XmlTest test : suite.getTests() )
        {
            // find in test name first
            Matcher m = p.matcher( test.getName() );

            if( m.find() )
            {
                newSuite.addTest( test );
            }
            else
            {
                // if not found, look for a class name that is the first class ONLY
                m = p.matcher( test.getXmlClasses().get( 0 ).getName() );

                if( m.find() )
                {
                    newSuite.addTest( test );
                }
            }
        }

        return newSuite;
    }

    public static XmlSuite randomizeSuite( XmlSuite suite, int testCount )
    {
        List< XmlTest > tests = suite.getTests();
        Collections.shuffle( tests );
        suite.setTests( (testCount >= tests.size() || testCount == 0) ? tests : tests.subList( 0, testCount ) );
        return suite;
    }

    private static XmlSuite createXmlSuite( String suitefile )
    {
        Parser parser = new Parser( suitefile );
        List< XmlSuite > xmlSuites = new ArrayList<>();
        try
        {
            xmlSuites.addAll( parser.parseToList() );
        }
        catch( ParserConfigurationException | SAXException | IOException e )
        {
            throw new NullPointerException( e.getMessage() );
        }

        if( Utility.isEmpty( xmlSuites ) )
        {
            throw new NullPointerException( "no valid xml suite found in " + suitefile );
        }

        if( xmlSuites.size() == 1 )
        {
            return xmlSuites.get( 0 );
        }

        XmlSuite suite = null;
        try
        {
            suite = XmlSuite.class.cast( xmlSuites.get( 0 ).clone() );
            xmlSuites.remove( 0 );
        }
        catch( ClassCastException e )
        {
            throw e;
        }

        for( XmlSuite remainingSuite : xmlSuites )
        {
            for( XmlTest test : remainingSuite.getTests() )
            {
                suite.addTest( test );
            }
        }

        return suite;
    }

    /**
     * Executes the list of {@link TestNG} instances in sequence one after another
     * @param suite XmlSuite
     * @return True if no problem encountered and False otherwise
     */
    private static boolean singleExecute( XmlSuite suite )
    {
        LOG.info( "Single Execution of TestNG instance" );

        LocalTestNG testNg = new LocalTestNG( LocalCommandLineArgs.instance() );
        testNg.configure();
        List< XmlSuite > suiteList = new ArrayList<>();
        suiteList.add( suite );
        testNg.setXmlSuites( suiteList );
        return testNg.execute();
    }

    /**
     * Executes the list of {@link TestNG} instances in sequence one after another
     * @param suiteName Suite Name
     * @param logPath The Path for Log
     * @param suiteList XmlSuite list
     */
    private static void sequentialExecute( String suiteName, String logPath, List< XmlSuite > suiteList )
    {
        LOG.info( "Sequential Execution of " + suiteList.size() + " suites" );

        for( int i = 0; i < suiteList.size(); ++i )
        {
            String suiteLogPath = logPath + i + File.separator;
            LocalCommandLineArgs.instance().logPath = suiteLogPath;
            // LOG = TestLogger.setupLogger( suiteLogPath, LocalCommandLineArgs.instance().consoleFile, LocalTestNG.class );

            LOG.info( "Executing " + (i + 1) + "/" + suiteList.size() + " \"" + suiteList.get( i ).getName() + "\"" );

            LocalTestNG testNg = new LocalTestNG( LocalCommandLineArgs.instance() );
            List< XmlSuite > tempSuiteList = new ArrayList<>();
            tempSuiteList.add( suiteList.get( i ) );
            testNg.setXmlSuites( tempSuiteList );
            if( !testNg.execute() )
            {
                LOG.warn( "TestNG execution for " + suiteList.get( i ).getName() + " failed" );
            }
        }
    }

    /**
     * Executes the list of {@link TestNG} parallel
     * @param suiteName  Suite Name
     * @param logPath The Path for Log
     * @param suiteList XmlSuite list
     * @return True if no problem encountered and False otherwise
     */
    private static boolean parallelExecute( String suiteName, String logPath, List< XmlSuite > suiteList )
    {
        LOG.info( "Parallel Execution of " + suiteList.size() + " TestNG instances" );

        ThreadController.instance();

        for( int i = 0; i < suiteList.size(); ++i )
        {
            TestNGInstance threadInstance = new TestNGInstance( suiteList.get( i ), LOG );
            // delay the other thread to minimize errors in creating selenium clients
            threadInstance.setDelay( (long) (5 * Time.SECOND) * i );
            ThreadController.instance().addTest( threadInstance );
        }

        return ThreadController.instance().execute();
    }

    /**
     * 
     * @param suite Suite object to be split
     * @param splitCount maximum number of test case in a suite
     * @return List of split suite object with maximum test case count of Parameter <splitCount>
     */
    public static List< XmlSuite > split( XmlSuite suite, int splitCount )
    {
        LOG.info( "Splitting " + suite.getName() + " into suites with maximum of " + splitCount + " test case" );
        List< XmlSuite > resultList = new ArrayList<>();

        String name = suite.getName();
        try
        {
            List< List< XmlTest > > testLists = Lists.partition( suite.getTests(), splitCount );
            LOG.info( "Suite was Split into " + testLists.size() + " suites" );
            for( List< XmlTest > testList : testLists )
            {
                XmlSuite suiteClone = XmlSuite.class.cast( suite.clone() );
                suiteClone.setParameters( suite.getParameters() );

                resultList.add( suiteClone );

                suiteClone.setName( name + " " + resultList.size() + "/" + testLists.size() );
                suiteClone.setTests( testList );
                LOG.info( "Suite \"" + suiteClone.getName() + "\" was has " + suiteClone.getTests().size() + " test cases" );
            }
        }
        catch( ClassCastException e )
        {
            LOG.error( e.getClass().getSimpleName() + " found with " + e.getMessage() + ". Processing " + resultList.size() + " suites", e );
        }

        return resultList;
    }

    private static XmlTest getMaximumTest( List< XmlTest > tests )
    {
        int maximum = 0;
        XmlTest maxTest = null;
        for( XmlTest test : tests )
        {
            int parameterSize = test.getLocalParameters().size();
            if( parameterSize > maximum )
            {
                maximum = parameterSize;
                maxTest = test;
            }
        }
        return maxTest;
    }

    private static XmlTest getMinimumTest( List< XmlTest > tests )
    {
        int minimum = Integer.MAX_VALUE;
        XmlTest minTest = null;
        for( XmlTest test : tests )
        {
            int next = Integer.valueOf( test.getName().substring( test.getName().lastIndexOf( "_" ) + 1, test.getName().length() ) );
            if( next < minimum )
            {
                minimum = next;
                minTest = test;
            }
        }
        return minTest;
    }

    public static XmlSuite reviewSuite( XmlSuite suite )
    {
        // get the first test case in each test scenario by Regex
        String firstTestCases = "[A-Z]{2,8}_[A-Z0-9]{2,4}[_]{0,1}[0-9]{0,3}";
        XmlSuite firstTestSuite = selectiveSuite( suite, firstTestCases );
        XmlSuite newSuite = (XmlSuite)suite.clone();
        newSuite.setParameters( suite.getParameters() );
        List< String > scenario = new ArrayList<>();
        for( XmlTest test : firstTestSuite.getTests() )
        {
            // get the scenario name by getting the string before _01 for each first test case
            String scenarioName = StringUtils.substringBeforeLast( test.getName(), "_" ) + "_";
            if( scenario.contains( scenarioName ) )
            {
                continue;
            }
            else
            {
                scenario.add( scenarioName );
            }
            // get tests in each scenario by scenario name
            List< XmlTest > testsPerScenario = selectiveSuite( suite, scenarioName ).getTests();

            if( !Utility.isEmpty( testsPerScenario ) )
            {
                // get the test with maximum parameters in a scenario
                XmlTest maxTest = getMaximumTest( testsPerScenario );
                XmlTest minTest = getMinimumTest( testsPerScenario );
                if( maxTest.equals( minTest ) )
                {
                    newSuite.addTest( minTest );
                    continue;
                }
                if( minTest != null )
                {
                    newSuite.addTest( minTest );
                }
                if( maxTest != null )
                {
                    newSuite.addTest( maxTest );
                }
            }

        }
        return newSuite;
    }

    /**
     * Prints {@link TestNG} usage with reference to {@link CommandLineArgs}
     */
    private static void usage()
    {
        new JCommander( LocalCommandLineArgs.instance() ).usage();
    }

    /**
     * Execution of {@link TestNG} in a normal fashion
     * @param testNg {@link TestNG} instance
     * @return True if no problem encountered and False otherwise
     */
    private boolean execute()
    {
        try
        {
            run();
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
            return false;
        }
        catch( Exception e )
        {
            LOG.error( e.getMessage(), e );
            if( TestRunner.getVerbose() > 1 )
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter( sw );
                e.printStackTrace( pw );
                sw.toString();
                LOG.error( sw, e );
            }
            else
            {
                LOG.error( e.getMessage() );
            }
            return false;
        }

        return true;
    }
}
