package jp.co.rakuten.travel.framework.utility;

import org.openqa.selenium.WebDriver;

import jp.co.rakuten.travel.framework.parameter.Parameters;

public enum ErrorType implements Parameters
{
    /**
     * Error relating to timeouts
     * <br> Usually happens with {@link WebDriver}, sockets, ports, both internal and external server connection and other API connections
     */
    TIME_OUT( "TimeoutException" ),

    /**
     * Error relating internal and/or external servers
     * <br> Usually happens when data is unexpectedly absent during lookup
     * <br> E.g hotel list, room list, plan list, airline list, car shop list, car list, etc.
     * <br> Usually happens when servers did not respond within accepted period of time
     * <br> Can also be thought of TIME_OUT or NO_TEST_DATA
     */
    SERVER_ERROR( "Server Error" ),

    /**
     * The proxy server is refusing connections
     * <br> Proxy server has been shut down
     * <br> Time out
     */
    PROXY_DOWN( "Proxy is down" ),

    /**
     * Error relating to data provided by server data
     * <br> Usually happens when data is unexpectedly absent during lookup
     * <br> E.g hotel list, room list, plan list, airline list, car shop list, car list, etc.
     */
    NO_TEST_DATA( "No Test Data" ),

    /**
     * Error when there is no specific cause or the cause is not yet decided
     * <br> Usually used by default error
     */
    UNVERIFIED( "Unverified" ),

    /**
     * Error relating to test cases with limitations due to input parameters
     * <br> Usually used in test level implementations
     */
    TEST_PARAMETERS_ERROR( "Test Parameters Error" ),

    /**
     * This should be used only to tag that the test code is buggy and needs some fix
     * <br> Usual test case should not have this type
     */
    AUTOMATION_ERROR( "Automation Error" ),

    /**
     * Error relating to be a verified BUG in product release scenario
     * <br> Usually used when an unexpected behavior is proven to be a BUG
     */
    PROBABLE_BUG( "Probable Bug" ),
    /**
     * Error relating to text area which must be filled, but not 
     */
    INPUT_CHECK_ERROR( "Input Check Error" ),

    /**
     * External Server Error
     * ANA or JAL Server Errors
     */
    EXTERNAL_SERVER_ERROR( "External Server Error" ),

    /**
     * Error occurred when sign in failed or session has expired
     */
    UNAUTHORIZED_ERROR( "Unauthorized Error" ),

    /**
     * Error occurred when some elements load incompletely
     */
    PAGE_LOAD_ERROR( "Page Load incompletely" ),

    /**
     * Appium Server Error
     */
    APPIUM_SERVER_ERROR( "Appium Server Error" ),

    UNKNOWN( "unknown" );

    private String m_val;

    ErrorType( String val )
    {
        m_val = val;
    }

    @Override
    public String val()
    {
        return m_val;
    }

    @Override
    public Parameters unknown()
    {
        return UNKNOWN;
    }
}
