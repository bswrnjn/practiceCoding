package jp.co.rakuten.travel.framework.parameter;

import org.apache.commons.net.ftp.FTP;

import jp.co.rakuten.travel.framework.browser.BrowserType;
import jp.co.rakuten.travel.framework.configuration.Equipment;

public enum TestApiParameters implements Parameters
{
    //

    /**
     * Device name based on {@link Device}
     */
    API_DEVICE( "" ),

    /**
     * Browser name based on {@link BrowserType}
     */
    API_BROWSER( "" ),

    /**
     * Browser version string based on {@link BrowserType}
     */
    API_BROWSER_VERSION( "" ),

    /**
     * Platform version
     */
    API_PLATFORM_VERSION( "11.0" ),

    /**
     * This is to change the user agent of the browser. Used for FP, SP and Tablet.
     */
    API_USER_AGENT( "" ),

    /**
     * Browser firefox path
     */
    BROWSER_FIREFOX_PATH( "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe" ),

    /**
     * Travel default URL
     */
    API_URL( "https://travel.rakuten.co.jp" ),

    /**
     * Staging server proxy file
     */
    API_FILE_PROXY( "proxy/JPE1stgProxy.pac" ),

    /**
     * Staging server proxy URI
     */
    API_HTTP_PROXY( "stb-dev-proxy.db.rakuten.co.jp" ),

    /**
     * Staging server proxy port
     */
    API_HTTP_PROXY_PORT( "9502" ),

    /**
     * flag to check if application uses any kind of SSL for authentication
     */
    API_WITH_SSL( "no" ),

    /**
     * Page wait default timeout in seconds
     */
    API_PAGE_TIMEOUT( "30" ),

    /**
     * page element default timeout in seconds
     */
    API_ELEMENT_TIMEOUT( "" ),

    /**
     * page element default timeout in seconds
     */
    API_ELEMENT_WAIT_TIMEOUT( "15" ),

    /**
     *  service element default timeout for fluent wait in seconds
     */
    API_ELEMENT_FLUENT_WAIT_TIME_OUT( "30" ),

    /**
     * Allow site redirection (may result to unexpected behavior)
     */
    API_REDIRECT_ALLOW( "no" ),

    /**
     * Type of passing criteria based on {@link PassCriteria} 
     */
    API_PASS_CRITERIA( "type_1" ),

    /**
     * Used by pass criteria {@link PassCriteria} TYPE_2
     */
    API_ITERATION( "1" ),

    /**
     * Number of times that the WebDriver will locate an element
     */
    API_ELEMENT_RETRY_COUNT( "2" ),

    API_PROXY_HOST( "" ),

    API_PROXY_PORT( "" ),

    /**
     * if API_ELEMENT_RETRY_COUNT is more than 1, this will be the wait time in between element lookup
     */
    API_ELEMENT_RETRY_WAIT( "500" ),

    /**
     * used in configuration to create equipments, i.e. browser, http 
     * <br> Refer to {@link Equipment}
     */
    API_EQUIPMENTS( "browser" ),

    /**
     * used in configuration to create controllers, i.e. web, rest
     * <br> Refer to {@link Controller}
     */
    // FIXME remove default parameter and add in test specs or suite file
    API_CONTROLLERS( "web" ),

    /**
     * VM args to support disabling checks based on service and its price
     * <br>e.g Search Price 
     */
    API_DISABLE_CHECK( "" ),

    /**
     * Capability to print TestNG suite file for reference
     */
    API_SAVE_SUITE_FILE( "no" ),

    /**
     * Capability to print html in a file during error
     */
    API_PRINT_HTML_ON_ERROR( "no" ),

    /**
     * Check Server Error
     */
    API_SERVER_ERROR_CHECKING( "" ),

    /**
     * Negative Test
     * <br> Flag to check that if the test FAIL, the test will PASS
     * <br> This is NOT targeted negative test case, NOT specific and NOT accurate
     * <br> The only advantage is this set up is generic and works on all test cases
     */
    API_NEGATIVE_TEST( "no" ),

    /**
     * BUG Tracker site
     * <br> this is a bug tracker to be used in this framework
     */
    API_BUG_TRACKER( "https://jira.rakuten-it.com/jira/browse/" ),

    /**
     * BUG ID
     * <br> this is a unique bug tracking id that is known to the specified bug tracker site
     */
    API_BUG_ID( "" ),

    /**
     * CSV in the format of 'bug_id#1:link,bug_id#2:link,bug_id#3:link'
     * <br> functionalities covered with this list will be ignored
     */
    API_IGNORE_BUG( "" ),

    /**
     * Sauce labs usage
     */
    API_SAUCE_LABS_PLATFORM( "" ), //
    API_SAUCE_LABS_USERNAME( "" ), //
    API_SAUCE_LABS_PASSWORD( "" ),

    /**
     * Sauce labs usage flag
     */
    API_SAUCE_LABS_ENABLED( "false" ),

    /**
     * Ini file path
     */
    API_INI_PATH( "config.ini" ),
    /**
     * Network Monitor Flag
     */
    API_NETWORK_MONITOR( "no" ),

    // DB parameters

    /**
     * SLM DB user name
     */
    API_SLM_DB_USER( "" ),

    /**
     * Reservation DB user name
     */
    API_RESERV_DB_USER( "" ),

    /**
     * SLM DB password
     */
    API_SLM_DB_PASSWORD( "" ),

    /**
     * Reservation DB password
     */
    API_RESERV_DB_PASSWORD( "" ),

    /**
     * SLM DB host
     */
    API_SLM_DB_HOST( "" ),

    /**
     * Reservation DB host
     */
    API_RESERV_DB_HOST( "" ),

    /**
     * SLM DB port
     */
    API_SLM_DB_PORT( "" ),

    /**
     * Reservation DB port
     */
    API_RESERV_DB_PORT( "" ),

    /**
     * SLM DB service name
     */
    API_SLM_DB_SERVICE_NAME( "" ),

    /**
     * Reservation DB service name
     */
    API_RESERV_DB_SERVICE_NAME( "" ),

    /**
     * Dictionary host
     */
    API_DICT_HOST( "" ),

    /**
     * Dictionary port
     */
    API_DICT_PORT( "" ),

    /**
     * Dictionary collection name
     */
    API_DICT_COLLECTION( "" ),

    /**
     * Dictionary database name
     */
    API_DICT_DB_NAME( "" ),

    /**
     * DB table name
     */
    API_DB_TABLE( "" ),

    API_DB_CONNECT( "yes" ),

    /**
     * FTP host name
     */
    API_FTP_HOST( "" ),

    /**
     * FTP port
     */
    API_FTP_PORT( "21" ),

    /**
     * FTP user name
     */
    API_FTP_USER_NAME( "anonymous" ),

    /**
     * FTP password
     */
    API_FTP_PASSWORD( "" ),

    /**
     * FTP remote file path
     */
    API_FTP_REMOTE_FILE_PATH( "" ),

    /**
     * FTP target file name
     */
    API_FTP_FILE_NAME( "1" ),

    /**
     * FTP local file path
     */
    API_FTP_LOCAL_FILE_PATH( System.getProperty( "user.dir" ) ),

    /**
     * FTP file transfer type
     */
    API_FTP_FILE_TYPE( String.valueOf( FTP.BINARY_FILE_TYPE ) ),

    /**
     * Flag for explicit XML to XSD check
     */
    API_XSD_RESPONSE_CHECK( "false" ),

    /**
     * Flag for unitils check
     */
    API_UNITILS_CHECK( "false" ),

    /**
     * IOS/Android Driver Server URL
     */
    DRIVER_SERVER_URL( "http://127.0.0.1:4723/wd/hub" ),

    /**
     * API PROFILE LISTENER
     */
    API_PROFILE_LISTENER( "false" ),

    API_UNKNOWN( Parameters.UNKNOWN );

    private final String m_val;

    TestApiParameters( String val )
    {
        m_val = val;
    }

    @Override
    public String val()
    {
        return m_val;
    }

    public int numeric()
    {
        try
        {
            return Integer.valueOf( m_val );
        }
        catch( NumberFormatException e )
        {
            return 0;
        }
    }

    @Override
    public Parameters unknown()
    {
        return API_UNKNOWN;
    }
}
