package jp.co.rakuten.travel.framework.listeners;

public interface IConstants
{

    String NEW_LINE    = "\r\n";
    String TOTAL_TESTS = "TOTAL TESTS";
    String SUITE_NAME  = "Suite Name : ";
    String TABLE_ID    = "includedContent";
    String DELIMITER   = ",";

    /**
     * Used by HTML listener to bind Model with data
     */
    public enum ModelKeys
    {
        //
        PASS( "pass" ),
        FAIL( "fail" ),
        SKIP( "skip" ),
        TOTAL_TESTS( "totalTests" ),
        STARTED_ON( "startedOn" ),
        RUNNING_TIME( "runningTime" ),
        SUITE_NAME( "suiteName" ),
        CLASS_NAME( "className" ),
        LOGS( "logs" ),
        TITLE( "title" ),
        ERROR_TYPE_MAP( "errorTypeMap" ),
        SUMMARY_MAP( "summaryMap" ),
        SKIP_TYPE_MAP( "skipTypeMap" );

        public String m_val;

        ModelKeys( String val )
        {
            m_val = val;
        }

        public String val()
        {
            return m_val;
        }
    }

    /**
     * Used by HTML listener to identify Model
     */
    public enum ModelTypes
    {
        INDEX, //
        SUMMARY, //
        TESTS, //
        CONSOLE,
        TESTS_LOG;
    }
}
