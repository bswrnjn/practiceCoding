package jp.co.rakuten.travel.framework.tools.constants;

import jp.co.rakuten.travel.framework.parameter.Parameters;

/**
 * Holds Constants for excel-parser
 * @version 1.0.1
 * @since 1.0.0  
 */
public class Constants
{
    public static final String  INPUT_PARAMETERS       = "input parameters in yaml file";
    public static final String  TESTCASE_ID            = "testcase_id";
    public static final String  TESTCASE_DESCRIPTION   = "Description";
    public static final String  USE_CASE               = "Use case";
    public static final String  NAME                   = "name";
    public static final String  PARALLEL               = "parallel";
    public static final String  SUITE_LISTENER         = "listeners";
    public static final String  PARAMETERS             = "parameters";
    public static final String  SUITE_PARAMS           = "SUITE PARAMETERS";
    public static final String  FRAMEWORK_PARAMS       = "FRAMEWORK PARAMETERS";
    public static final String  API_PARAMS             = "API PARAMETERS";
    public static final String  TEST_PARAMS            = "TEST PARAMETERS";

    public static final String  READ                   = "r";
    public static final String  WRITE                  = "w";
    public static final String  EXECUTE                = "x";
    // FIXME - desc attribute to be refactored which will not be printed to yaml for execution anymore
    public static final String  DESCRIPTION            = "desc";
    public static final String  SCENARIO               = "scenario";
    public static final String  SUITE_CONFIG           = "config";
    public static final String  FORMAT                 = "format";
    public static final String  MODULE_LIST            = "ModuleList";
    public static final String  MODULE_ID              = "Module ID";
    public static final int     TESTCASE_COUNT_COUMN   = 2;
    public static final int     MODULE_COLUMN          = 3;
    public static final int     PARAMETER_COLUMN       = 9;
    public static final int     USECASE_COLUMN         = 0;
    public static final String  ENCODING               = "UTF-8";
    public static final String  TESTS                  = "tests";

    /**
     * Holds Constants for mongo-testng
     */
    public static final String  FRAMEWORKPARAMETERS    = "frameworkparameters";
    public static final String  APIPARAMETERS          = "apiparameters";
    public static final String  TESTPARAMETERS         = "testparameters";
    public static final String  TESTCASES              = "testcases";
    public static final String  CLASSES                = "classes";
    public static final String  LISTENERS              = "listeners";
    public static final String  SUITEPARAMETERS        = "suiteParameters";

    /**
     * Excel parser web utility constants
     */
    public static final String  YAML_EXTENSION         = "yaml";
    public static final String  COOKIE_NAME            = "isDownloaded";
    public static final String  MIME_TYPE_OCTET_STREAM = "APPLICATION/OCTET-STREAM";
    public static final String  COOKIE_ERROR_MSG       = "errorMsgContent";
    public static final String  MIME_TYPE_XLSX         = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /**
     * Mongo Database connection constants
     */
    public static final String  HOSTNAME               = "172.27.139.140";
    public static final Integer PORT                   = 27017;
    public static final String  DATABASE_NAME          = "rakuten_travel";
    public static final String  TABLE_NAME             = "test_case";

    /**
     * Yaml to Mongo parsers constants
     */
    public final static String  SUITE_NAME             = "name";
    public final static String  OBJECT_ID              = "_id";
    public final static String  TEST_ID_NAME           = "- name";
    public final static String  COLON                  = ":";
    // public final static String ID = "ID";
    public final static String  TEST_CLASS             = "Test Class";
    public final static String  TEST_SCENARIO          = "Test Scenario";
    public final static String  SUITE_ID               = "suite_id";
    public final static String  SCENARIO_ID            = "scenario_id";

    /**
     * File permission constants
     */
    public enum FilePermission
    {
        READ( Constants.READ ),
        WRITE( Constants.WRITE ),
        EXECUTION( Constants.EXECUTE );

        private String mode;

        private FilePermission( String mode )
        {
            this.mode = mode;
        }

        public String getMode()
        {
            return this.mode;
        }
    }

    /**
     * Enumeration with constants by using these,
     * we can identify the parameter type
     */
    public enum CommandConstants
    {
        MONGOSUITES( "-mn", "--mongosuite" ),
        SELECTIVETESTS( "-s", "--selectivetests" ),
        YAML( "-y", "--yaml" ),
        PARSERTYPE( "-p", "--parsertype" ),
        LOGDIRECTORY( "-ld", "--logdirectory" ),
        D( "-d" ),
        DBHOST( "-dbh", "--dbhost" ),
        DBPORT( "-dbp", "--dbport" ),
        DBNAME( "-dbn", "--dbname" ),
        USERID( "-u", "--userId" ),
        SPLIT( "-t", "--split" ),
        RANDOM( "-r", "--random" ),
        REVIEW( "-review", "--review" );

        String [] representation;

        private CommandConstants( String ... representation )
        {
            this.representation = representation;
        }

        public static CommandConstants getCommandConstatnt( String value )
        {
            for( CommandConstants commandConstants : CommandConstants.values() )
            {
                for( String constantValue : commandConstants.representation )
                {
                    if( value.toLowerCase().equals( constantValue ) )
                    {
                        return commandConstants;
                    }
                }
            }
            return null;
        }
    }

    /**
     * ParserType for the CommandLine parameter parser
     */
    public enum CommandParserType implements Parameters
    {
        MONGODB, //
        YAML_MONGO, //
        SELECTIVE_TESTCASE, //
        GROUP_TESTCASE,
        UNKNOWN;

        @Override
        public String val()
        {
            return null;
        }

        @Override
        public Parameters unknown()
        {
            return null;
        }
    }

    /**
     * Types of executor services
     */
    public enum ExecutorType
    {
        YAML, //
        MONGO;

        public static ExecutorType getType( String name )
        {
            for( ExecutorType executorType : ExecutorType.values() )
            {
                if( executorType.name().equals( name.toUpperCase() ) )
                {
                    return executorType;
                }
            }
            return null;
        }
    }
}
