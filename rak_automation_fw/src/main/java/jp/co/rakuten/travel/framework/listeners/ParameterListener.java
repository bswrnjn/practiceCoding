package jp.co.rakuten.travel.framework.listeners;

import java.util.HashMap;
import java.util.Map;

import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.Parameters;

import org.apache.log4j.Logger;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;

/**
 * Main handler for getting input parameters from suite and every test case
 *
 */
public class ParameterListener implements ITestListeners
{
    protected Logger                                      LOG      = TestLogger.getLogger( this.getClass() );

    protected Map< Enum< ? extends Parameters >, String > m_params = new HashMap<>();

    @Override
    public void onExecutionFinish()
    {

    }

    @Override
    public void onStart( ISuite suite )
    {

    }

    @Override
    public void onStart( ITestContext context )
    {

    }

    @Override
    public void onTestStart( ITestResult result )
    {
        LOG.info( "onTestStart ITestResult" );

    }

    @Override
    public void onTestSuccess( ITestResult result )
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
    public void onTestFailedButWithinSuccessPercentage( ITestResult result )
    {

    }

    @Override
    public void onFinish( ITestContext context )
    {

    }

    @Override
    public void onFinish( ISuite suite )
    {

    }

    @Override
    public void onExecutionStart()
    {

    }

}
