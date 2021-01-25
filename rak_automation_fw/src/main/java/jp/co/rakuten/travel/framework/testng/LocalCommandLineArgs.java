package jp.co.rakuten.travel.framework.testng;

import org.testng.CommandLineArgs;

import com.beust.jcommander.Parameter;

public class LocalCommandLineArgs extends CommandLineArgs
{
    private static LocalCommandLineArgs s_instance = null;

    public static LocalCommandLineArgs instance()
    {
        if( s_instance == null )
        {
            s_instance = new LocalCommandLineArgs();
        }
        return s_instance;
    }

    /**
    * used to execute only test cases that has test ID's (unique test names) which contains the keyword, supports regex, | and &
    * <br> disregards all options when set to true
    */
    // public static final String NATIVE = "-native";
    // @Parameter( names = NATIVE, description = "TestNg Default Native Mode" )
    public Boolean             isNative        = true;

    /**
     * used to execute only test cases that has test ID's (unique test names) which contains the keyword, supports regex, | and &
     */
    public static final String SELECTIVE       = "-selective";
    @Parameter( names = SELECTIVE, description = "Selective Mode" )
    public String              selective       = "";

    /**
     * used to execute certain number test cases that finish in the set amount of time in hours
     */
    public static final String TIME            = "-time";
    @Parameter( names = TIME, description = "Time bound Mode" )
    public Integer             boundTime;

    /**
     * used to split the test suite into multiple suites and will contain number of test cases not greater than the split value
     * <br> current support for split can only cover single suite file
     */
    public static final String SPLIT           = "-split";
    @Parameter( names = SPLIT, description = "Multiple Suite Mode" )
    public Integer             splitCount      = 0;

    /**
     * used to select review regression test suite from full regression test suites. 
     * For each scenario, it will contain the first test case, and the first test case that contains the most parameters
     */

    public static final String REVIEW          = "-review";
    @Parameter( names = REVIEW, description = "Review Regression Mode (can not be used with SELECTIVE)" )
    public Boolean             isReview        = Boolean.FALSE;

    /**
     * used to randomize the test cases in a suite before execution
     */
    public static final String RANDOM          = "-random";
    @Parameter( names = RANDOM, description = "Select a number of tests and randomize the test cases before execution" )
    public int                 random          = -1;

    /**
     * used to randomize the test cases in a suite before execution
     */
    public static final String PARALLEL_SUITE  = "-parallelSuite";
    @Parameter( names = PARALLEL_SUITE, description = "Parallel Suite Execution" )
    public Boolean             isParallelSuite = Boolean.FALSE;

    /**
     * username who executed the suite
     */
    public static final String USERNAME        = "-username";
    @Parameter( names = USERNAME, description = "Username who executed the suite" )
    public String              username        = "";

    /**
     * log path to be used by TestNG when not in Native mode
     */
    public static final String LOG_PATH        = "-logPath";
    @Parameter( names = LOG_PATH, description = "Default Log Path" )
    public String              logPath         = "";

    /**
     * used to execute only test cases that has test ID's (unique test names) which contains the keyword, supports regex, | and &
     */
    public static final String CONSOLE_FILE    = "-consoleFile";
    @Parameter( names = CONSOLE_FILE, description = "Default Console Filename" )
    public String              consoleFile     = "console.log";
}
