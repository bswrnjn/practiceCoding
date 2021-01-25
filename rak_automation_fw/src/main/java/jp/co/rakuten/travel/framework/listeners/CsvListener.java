package jp.co.rakuten.travel.framework.listeners;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;

import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.Result;
import jp.co.rakuten.travel.framework.utility.ErrorType;
import jp.co.rakuten.travel.framework.utility.Utility;

/**
 * Description: CsvListener creates csv file with test_id, test_result , error type.
*/
public class CsvListener implements ITestListeners, IConstants
{
    private final Logger           LOG              = TestLogger.getLogger( this.getClass().getClass() );

    private String                 m_logPath        = "";
    private String                 m_csvFile        = "";
    private List< TestCaseResult > m_testResultList = new ArrayList< TestCaseResult >();

    // Constructor to set path and filename
    public CsvListener( String logpath, String csvFile )
    {
        m_logPath = logpath;
        m_csvFile = csvFile;
    }

    @Override
    public void onExecutionStart()
    {}

    @Override
    public void onExecutionFinish()
    {}

    // Initializes test object to be written in CSV file before each test case execution.
    @Override
    public void onStart( ISuite suite )
    {
        m_testResultList = new ArrayList< TestCaseResult >();
    }

    @Override
    public void onStart( ITestContext context )
    {}

    @Override
    public void onFinish( ISuite suite )
    {}

    @Override
    public void onTestStart( ITestResult result )
    {}

    @Override
    public void onTestSuccess( ITestResult result )
    {
        appendTestCaseResult( result );
    }

    @Override
    public void onTestFailure( ITestResult result )
    {
        appendTestCaseResult( result );
    }

    @Override
    public void onTestSkipped( ITestResult result )
    {
        appendTestCaseResult( result );
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage( ITestResult result )
    {}

    // Writes TestCaseResult object to CSV file.
    @Override
    public void onFinish( ITestContext context )
    {}

    // Add test object to list
    private void appendTestCaseResult( ITestResult result )
    {
        ErrorType errorType = Utility.getErrorType();
        m_testResultList.add( //
                new TestCaseResult( result.getTestContext().getCurrentXmlTest().getName()//
                        , Result.get( result.getStatus() ).name()//
                        , (errorType.equals( ErrorType.UNKNOWN )) ? "" : errorType.name() //
                ) );

        try( FileWriter fout = new FileWriter( new File( m_logPath, m_csvFile ) ) )
        {
            for( TestCaseResult testCaseResult : m_testResultList )
            {
                fout.write( testCaseResult.toString() );
            }
            fout.close();
        }
        catch( IOException ex )
        {
            LOG.error( "Error: Failed while accessing Csv Listener cause : " + ex.getMessage() );
        }
    }

    // Inner class to set members for each test case details.
    private class TestCaseResult
    {

        private final String m_testCaseId;
        private final String m_testStatus;
        private final String m_errorType;
        private final String m_delimiter;

        private TestCaseResult( String testCaseId, String testStatus, String errorType )
        {
            m_testCaseId = testCaseId;
            m_testStatus = testStatus;
            m_errorType = errorType;
            m_delimiter = DELIMITER;
        }

        @Override
        public String toString()
        {
            return m_testCaseId
                    .concat( m_delimiter )//
                    .concat( m_testStatus )//
                    .concat( m_delimiter )//
                    .concat( m_errorType )//
                    .concat( System.lineSeparator() );
        }
    }
}
