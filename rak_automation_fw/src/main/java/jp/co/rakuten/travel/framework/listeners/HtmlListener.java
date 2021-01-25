package jp.co.rakuten.travel.framework.listeners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.log4j.Logger;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;

import htmlflow.HtmlView;
import htmlflow.elements.HtmlBody;
import htmlflow.elements.HtmlDiv;
import htmlflow.elements.HtmlHead;
import htmlflow.elements.HtmlP;
import htmlflow.elements.HtmlTable;
import htmlflow.elements.HtmlTr;
import jp.co.rakuten.travel.framework.app.android.Android;
import jp.co.rakuten.travel.framework.app.ios.Ios;
import jp.co.rakuten.travel.framework.browser.Browser;
import jp.co.rakuten.travel.framework.configuration.Configuration;
import jp.co.rakuten.travel.framework.configuration.Equipment.EquipmentType;
import jp.co.rakuten.travel.framework.html.HtmlFrame;
import jp.co.rakuten.travel.framework.html.HtmlFrameset;
import jp.co.rakuten.travel.framework.html.HtmlImg;
import jp.co.rakuten.travel.framework.html.HtmlPre;
import jp.co.rakuten.travel.framework.html.HtmlSpan;
import jp.co.rakuten.travel.framework.html.HtmlStyle;
import jp.co.rakuten.travel.framework.html.HtmlTBody;
import jp.co.rakuten.travel.framework.html.HtmlTHead;
import jp.co.rakuten.travel.framework.html.HtmlTag;
import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.Result;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import jp.co.rakuten.travel.framework.utility.ErrorType;
import jp.co.rakuten.travel.framework.utility.Pair;
import jp.co.rakuten.travel.framework.utility.Utility;

public class HtmlListener implements ITestListeners, IConstants
{
    protected Logger                          LOG                     = TestLogger.getLogger( this.getClass().getClass() );

    protected final String                    m_logPath;

    /**
     * record error type
     */
    private Map< ErrorType, Integer >         m_errorMap              = new LinkedHashMap< ErrorType, Integer >();
    private Map< ErrorType, Integer >         m_skipMap               = new LinkedHashMap< ErrorType, Integer >();

    /**
     * Map containing all report data
     */
    private Map< String, Object >             m_reportData            = new HashMap< String, Object >();

    private Map< String, Object >             m_summaryMap            = new LinkedHashMap< String, Object >();

    private long                              m_testCasestartTime;

    /**
     * INDEX file for HTML Logging
     */
    private final String                      m_indexHtml;

    /**
     * Test HTML Summary file that holds the pass/fail/skip test count and a simple pie graph for clear visualization 
     */
    private final String                      SUMMARY_HTML            = "summary.html";

    private final String                      SUMMARY_HTML_CSS        = "\n table {width:60%;margin-bottom:10px;border-collapse:collapse;empty-cells:show;font-family: \"Times New Roman\", Times, serif;text-align:center;box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1)}\n th,td {border:1px solid #C1C3D1;padding:.25em .5em}\n th {vertical-align:top;text-align:center}\n td {vertical-align:top;text-align:center;font-family: \"Times New Roman\", Times, serif;}\n #test_result tr.pass th {background-color: #66B821}\n #test_result tr.fail th {background-color: #DD2826}\n #test_result tr.skip th {background-color: #888888;color: white}\n #test_result tr.total th {border:5px solid #009;padding:.25em .5em}\n th{color: black;background: #eeeeee}\n \n #errorType {margin-left:160px;margin-top:100px;box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1) }\n #errorType th{color: white;background: #dd2826}\n #errorType th,td{border: 1px solid #C1C3D1}\n #errorType tr:nth-child(odd) td{background: #EBEBEB}\n #errorType td:first-child{border-left: none}\n #errorType td:last-child{border-right: none}\n \n #skipType {margin-left:160px;margin-top:10px;box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1) }\n #skipType th{color: white;background: #888888}\n #skipType th,td{border: 1px solid #C1C3D1}\n #skipType tr:nth-child(odd) td{background: #EBEBEB}\n #skipType td:first-child{border-left: none}\n #skipType td:last-child{border-right: none} \n";

    private final String                      SUMMARY_HTML_JAVASCRIPT = "google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});\n google.setOnLoadCallback(drawChart);\n function drawChart() {\n var data = google.visualization.arrayToDataTable([['Result', 'Test Case Count'],['PASS',$pass],['FAIL',$fail],['SKIP',$skip],]);\n var options = {\n title:'Test Summary Chart',\n legend:'none',\n colors:['#66B821','#DD2826','#888888']};\n var chart = new google.visualization.PieChart(document.getElementById('piechart'));\n chart.draw(data, options);}\n";
    /**
     * Test HTML File file that lists the tests executed from the suite file 
     */
    private final String                      TESTS_HTML              = "tests.html";

    private final String                      TESTS_HTML_CSS          = "\n table {width:100%;margin-bottom:10px;border-collapse:collapse;empty-cells:show;table-layout:fixed;font-family: \"Times New Roman\", Times, serif;box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1)}\n th,td {border:1px solid #c1c3d1;padding:.25em .5em}\n th {vertical-align:top;cursor: pointer;color:black;background:#eeeeee}\n td {vertical-align:top;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;text-align:left}\n table a {font-weight:bold;color:#00008B}\n h2{font-family:'Enriqueta', arial, serif;line-height:1.25}\n .tablesorter tr.pass td {background-color:#66B821}\n .tablesorter tr.fail td {background-color:#DD2826}\n .tablesorter tr.skip td {background-color:#888888;color:white}\n";

    /**
     * Console HTML File file that displays the console log 
     */
    private final String                      CONSOLE_HTML            = "console.html";

    private final String                      JAVASCRIPT_EXTENSION    = "js";

    /**
     * Filename for each test case. A random string is added to make it unique in case of same test ID
     */
    protected String                          m_htmlLogFilename;

    /**
     * Print stream writers
     */
    private PrintStream                       m_indexHtmlWriter;

    private PrintStream                       m_consoleHtmlWriter;

    private PrintStream                       m_summaryHtmlWriter;

    private PrintStream                       m_testHtmlWriter;

    private PrintStream                       m_testTableHtmlWriter;

    private PrintStream                       m_testLogHtmlWriter;

    /**
     * Html views
     */

    private HtmlView< Map< String, Object > > m_indexHtmlView         = new HtmlView< Map< String, Object > >();

    private HtmlView< Map< String, Object > > m_summaryHtmlView       = new HtmlView< Map< String, Object > >();

    private HtmlView< Map< String, Object > > m_testHtmlView          = new HtmlView< Map< String, Object > >();

    /*
     * Test Result
     */

    private static final String               RESULT_FAIL             = "FAIL";
    private static final String               RESULT_SKIP             = "SKIP";

    public HtmlListener( String logpath, String indexFile )
    {
        m_logPath = logpath;
        m_indexHtml = indexFile;
    }

    @Override
    public void onExecutionStart()
    {}

    @Override
    public void onStart( ISuite suite )
    {
        try
        {
            // copy js files into log directory
            ClassLoader classLoader = this.getClass().getClassLoader();
            Collection< File > files = Utility.getFilesFromResources( JAVASCRIPT_EXTENSION );

            Iterator< File > fileIterator = files.iterator();
            while( fileIterator.hasNext() )
            {
                final File entry = fileIterator.next();
                FileUtils.copyInputStreamToFile( classLoader.getResourceAsStream( JAVASCRIPT_EXTENSION + "/" + entry.getName() ), new File( m_logPath + entry.getName() ) );
            }

            /* Insert title into the context */
            m_reportData.put( ModelKeys.TITLE.val(), suite.getName() );
            m_reportData.put( ModelKeys.SUITE_NAME.val(), suite.getName() );

            /* initialize html writers */
            m_indexHtmlWriter = new PrintStream( new FileOutputStream( new File( m_logPath, m_indexHtml ) ) );
            m_summaryHtmlWriter = new PrintStream( new FileOutputStream( new File( m_logPath, SUMMARY_HTML ) ) );
            m_consoleHtmlWriter = new PrintStream( new FileOutputStream( new File( m_logPath, CONSOLE_HTML ) ) );
            m_testHtmlWriter = new PrintStream( new FileOutputStream( new File( m_logPath, TESTS_HTML ) ) );
            m_testTableHtmlWriter = new PrintStream( new FileOutputStream( new File( m_logPath, "table.html" ) ) );

            /* Bind data with views */
            bindDataToModel( ModelTypes.INDEX, m_indexHtmlView );
            bindDataToModel( ModelTypes.TESTS, m_testHtmlView );
            bindDataToModel( ModelTypes.CONSOLE, new HtmlView< Map< String, Object > >() );
        }
        catch( IOException | URISyntaxException e )
        {
            LOG.error( "Error: Failed while starting html listener with cause : " + e.getMessage() );
        }
    }

    @Override
    public void onStart( ITestContext context )
    {
        // create a test html log file
        m_htmlLogFilename = "TEST_" + context.getCurrentXmlTest().getName() + "_" + Utility.getReferenceTime( "MMddHHmmss" ) + ".html";
        System.setProperty( "TEST_NAME", context.getCurrentXmlTest().getName() );
        try
        {
            m_testLogHtmlWriter = new PrintStream( new FileOutputStream( new File( m_logPath, m_htmlLogFilename ) ) );
        }
        catch( FileNotFoundException e )
        {
            LOG.error( "Error: File not found while initializing writer, " + e.getMessage() );
        }
    }

    @Override
    public void onTestStart( ITestResult result )
    {
        m_testCasestartTime = Utility.getReferenceTime().getTime();
    }

    @Override
    public void onTestSuccess( ITestResult result )
    {
        LOG.info( "onTestSuccess" );
        appendResult( result );
    }

    @Override
    public void onTestFailure( ITestResult result )
    {
        LOG.info( "onTestFailure" );
        appendResult( result );
    }

    @Override
    public void onTestSkipped( ITestResult result )
    {
        LOG.info( "onTestSkipped" );
        appendResult( result );
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage( ITestResult result )
    {}

    @Override
    public void onFinish( ITestContext context )
    {
        StringBuilder logs = new StringBuilder( NEW_LINE );

        // log html test log
        for( String log : Reporter.getOutput() )
        {
            logs.append( log + NEW_LINE );
        }

        m_reportData.put( ModelKeys.TITLE.val(), m_htmlLogFilename );
        m_reportData.put( ModelKeys.LOGS.val(), logs.toString() );

        /* Write log file in HTML form e.g. : DOM*_*.html */
        bindDataToModel( ModelTypes.TESTS_LOG, new HtmlView< Map< String, Object > >() );

        // test case log ends here
        // removing the string content will enable message re-use for the succeeding test
        Reporter.clear();
    }

    @Override
    public void onFinish( ISuite suite )
    {
        int pass = 0;
        int fail = 0;
        int skip = 0;
        for( ISuiteResult suiteResult : suite.getResults().values() )
        {
            ITestContext context = suiteResult.getTestContext();

            IResultMap passMap = context.getPassedTests();
            pass += passMap.getAllResults().size();

            IResultMap failmap = context.getFailedTests();
            fail += failmap.getAllResults().size();

            IResultMap skipMap = context.getSkippedTests();
            skip += skipMap.getAllResults().size();
        }

        m_summaryMap.put( ModelKeys.PASS.val(), pass );
        m_summaryMap.put( ModelKeys.FAIL.val(), fail );
        m_summaryMap.put( ModelKeys.SKIP.val(), skip );
        m_summaryMap.put( TOTAL_TESTS, (pass + fail + skip) );
        m_reportData.put( ModelKeys.SUMMARY_MAP.val(), m_summaryMap );
        m_reportData.put( ModelKeys.STARTED_ON.val(), Utility.getTime( "EEE, dd MMM yyyy HH:mm:ss z" ) );
        m_reportData.put( ModelKeys.RUNNING_TIME.val(), DurationFormatUtils.formatDuration( Utility.getDuration(), "HH:mm:ss" ) );
        m_reportData.put( ModelKeys.ERROR_TYPE_MAP.val(), m_errorMap );
        m_reportData.put( ModelKeys.SKIP_TYPE_MAP.val(), m_skipMap );

        // Write Summary.html file on disk
        bindDataToModel( ModelTypes.SUMMARY, m_summaryHtmlView );
    }

    @Override
    public void onExecutionFinish()
    {
        LOG.info( "Please check HTML log file at " + m_logPath + m_indexHtml );
    }

    private void bindDataToModel( ModelTypes modelType, HtmlView< Map< String, Object > > view )
    {
        // set title in view
        HtmlHead< Map< String, Object > > htmlHead = view.head().title( (String)m_reportData.get( ModelKeys.TITLE.m_val ) );

        switch( modelType )
        {
        case INDEX:
            // set map data
            HtmlFrameset< Object > frameSet = view.addChild( new HtmlFrameset<>() ).addAttr( "rows", "35%,65%" );

            frameSet.addChild( new HtmlFrame<>( SUMMARY_HTML, FilenameUtils.getBaseName( SUMMARY_HTML ) ) );
            frameSet.addChild( new HtmlFrame<>( TESTS_HTML, FilenameUtils.getBaseName( TESTS_HTML ) ) );

            // set print stream
            view.setPrintStream( m_indexHtmlWriter ).write( m_reportData );

            break;

        case SUMMARY:
            setSummaryData( htmlHead, view );
            break;

        case TESTS:
            bindTestsData( htmlHead, view );
            break;

        case TESTS_LOG:
            // set map data
            view
                    .body()
                    .addChild( new HtmlPre<>() ) // Added <pre></pre> tag
                    .addChild( new HtmlSpan<>() )
                    .classAttr( "inner-pre" )
                    .addAttr( "style", "font-size: 11px" )
                    .text( (String)m_reportData.get( ModelKeys.LOGS.m_val ) );

            // set print stream
            view.setPrintStream( m_testLogHtmlWriter ).write( m_reportData );

            m_testLogHtmlWriter.close();
            break;

        case CONSOLE:
            bindConsoleData( htmlHead, view );
            break;

        default:
            LOG.error( "Error: Modeltype \"" + modelType + "\" not yet supported. " );
            break;
        }

    }

    private void appendLogo( HtmlDiv< Map< String, Object > > topSubContainer )
    {
        topSubContainer
                .div() //
                .addAttr( "style", "float:left" )
                .addChild( new HtmlImg<>( "https://img.travel.rakuten.co.jp/share/common/images/bnr_ogp_logo.gif" ) )
                .addAttr( "style", "width:160px;height:160px;float:left;padding-top:1%;" );
    }

    private void appendErrorTypeTable( HtmlDiv< Map< String, Object > > topSubContainer )
    {
        if( m_errorMap.keySet().size() > 0 )
        {
            HtmlTable< Map< String, Object > > errorTypeTable = topSubContainer
                    .table()//
                    .idAttr( "errorType" );

            errorTypeTable
                    .tr() //
                    .th()
                    .addAttr( "colspan", "2" )
                    .text( "ERROR TYPE SUMMARY" );

            for( Map.Entry< ErrorType, Integer > entry : m_errorMap.entrySet() )
            {
                HtmlTr< Map< ErrorType, Integer > > row = new HtmlTr< Map< ErrorType, Integer > >();

                row.td().text( entry.getKey().toString().toUpperCase() );

                row.td().text( entry.getValue().toString() );

                errorTypeTable.addChild( row );
            }
        }
    }

    private void appendSkipTypeTable( HtmlDiv< Map< String, Object > > topSubContainer )
    {
        if( m_skipMap.keySet().size() > 0 )
        {
            HtmlTable< Map< String, Object > > skipTypeTable = topSubContainer
                    .table()//
                    .idAttr( "skipType" );

            skipTypeTable
                    .tr() //
                    .th()
                    .addAttr( "colspan", "2" )
                    .text( "SKIP TYPE SUMMARY" );

            for( Map.Entry< ErrorType, Integer > entry : m_skipMap.entrySet() )
            {
                HtmlTr< Map< ErrorType, Integer > > row = new HtmlTr< Map< ErrorType, Integer > >();

                row.td().text( entry.getKey().toString().toUpperCase() );

                row.td().text( entry.getValue().toString() );

                skipTypeTable.addChild( row );
            }
        }
    }

    private void appendPieChart( HtmlDiv< Map< String, Object > > bottomSubContainer )
    {
        bottomSubContainer
                .div() //
                .idAttr( "piechart" )
                .addAttr( "style", "padding-top:1px;width:400px;height:400px;float:right;padding-right:15%;" );

        // update summary html javascript
        String javascript = new String( SUMMARY_HTML_JAVASCRIPT );
        javascript = javascript
                .replace( "$pass", m_summaryMap.get( ModelKeys.PASS.val() ).toString() ) //
                .replace( "$fail", m_summaryMap.get( ModelKeys.FAIL.val() ).toString() )
                .replace( "$skip", m_summaryMap.get( ModelKeys.SKIP.val() ).toString() );

        bottomSubContainer
                .addChild( new jp.co.rakuten.travel.framework.html.HtmlDiv() ) //
                .scriptLink( "https://www.google.com/jsapi" )
                .scriptBlock()
                .code( javascript );
    }

    private void appendConsoleLink( HtmlDiv< Map< String, Object > > topSubContainer )
    {
        topSubContainer//
                .addChild( new HtmlP<>() )
                .addAttr( "style", "font-family:Times New Roman;float:left;margin-left:162px" )
                .text( HtmlTag.link( "<i>" + "View full console log" + "</i>", CONSOLE_HTML, "_blank" ) );
    }

    private void appendStartEndTime( HtmlDiv< Map< String, Object > > topSubContainer )
    {
        topSubContainer
                .addChild( new HtmlP<>() ) //
                .addAttr( "style", "font-family:Times New Roman;float:left" )
                .text( "Started on: " + m_reportData.get( ModelKeys.STARTED_ON.m_val ) + "<br>" + "Total running time :" + m_reportData.get( ModelKeys.RUNNING_TIME.m_val ) + "<br>" );
    }

    private void appendSummaryTable( HtmlDiv< Map< String, Object > > topSubContainer )
    {

        HtmlTable< Map< String, Object > > summaryTable = topSubContainer
                .div() //
                .table()
                .idAttr( "test_result" );

        summaryTable
                .tr() //
                .addChild( new HtmlTHead<>() )
                .th()
                .addAttr( "colspan", "2" )
                .text( "SUMMARY" );//

        HtmlTBody< Map< String, Object > > summaryTableBody = summaryTable.addChild( new HtmlTBody< Map< String, Object > >() );

        for( Map.Entry< String, Object > entry : m_summaryMap.entrySet() )
        {
            HtmlTr< Map< String, Object > > row = new HtmlTr< Map< String, Object > >().classAttr( entry.getKey() );

            row.th().text( entry.getKey().toUpperCase() );

            row.td().text( entry.getValue().toString() );

            summaryTableBody.addChild( row );
        }
    }

    private String getErrorTypeFromResult( ITestResult result )
    {
        ErrorType erType = Utility.getErrorType();

        if( result.getStatus() == ITestResult.FAILURE )
        {
            int errCount = m_errorMap.get( erType ) == null ? 0 : m_errorMap.get( erType );
            m_errorMap.put( erType, errCount + 1 );
        }
        else if( result.getStatus() == ITestResult.SKIP )
        {
            int errCount = m_skipMap.get( erType ) == null ? 0 : m_skipMap.get( erType );
            m_skipMap.put( erType, errCount + 1 );
        }

        return erType.equals( ErrorType.UNKNOWN ) ? "" : erType.name();
    }

    private void appendResult( ITestResult result )
    {
        TestCaseResult testCaseResult = new TestCaseResult();
        /*
         * populate test case data into the test case result object
         */
        testCaseResult.setResult( Result.get( result.getStatus() ).name() );

        testCaseResult.setDesc( result.getTestContext().getCurrentXmlTest().getParameter( "desc" ) );
        testCaseResult.setTestCaseClassName( result.getTestClass().getRealClass().getName() );

        testCaseResult.setTestCaseLog( m_htmlLogFilename );
        testCaseResult.setTestCaseId( result.getTestContext().getCurrentXmlTest().getName() );

        // put each test case duration
        long duration = System.currentTimeMillis() - m_testCasestartTime;
        testCaseResult.setTime( (new SimpleDateFormat( "mm:ss" )).format( duration ).toString() );

        // put a link to the last web page screenshot
        // if the test has non-unique test id, then the last test
        // case will overwrite the previous failed test with the same test id
        Result res = Result.get( result.getStatus() );
        if( res.equals( Result.FAIL ) || res.equals( Result.SKIP ) )
        {
            testCaseResult.setScreenShot( result.getTestContext().getCurrentXmlTest().getName() + ".jpg" );
        }

        // display warning comments ONLY IF failed
        StringBuilder comment = new StringBuilder();
        if( !result.isSuccess() )
        {
            for( String warning : TestLogger.warnings() )
            {
                comment.append( (StringUtils.isEmpty( comment ) ? "" : HtmlTag.BR.open()) + HtmlTag.FONT.open( "color=\"#DCDCDC\"" ) + warning + HtmlTag.FONT.close() );
            }
        }

        // display error comments
        for( Pair< String, ErrorType > output : TestLogger.errors() )
        {
            comment.append( (StringUtils.isEmpty( comment ) ? "" : HtmlTag.BR.open()) + output.first() );
        }

        // print bug ID(s) if available
        String bugId = TestApiObject.instance().get( TestApiParameters.API_BUG_ID );
        if( !StringUtils.isEmpty( bugId ) )
        {
            String [] bugIds = bugId.split( "," );
            for( String id : bugIds )
            {
                id = id.trim();
                // <a href="url">link text</a>
                comment.append( HtmlTag.BR.open() + HtmlTag.A.open( "href=\"" + TestApiObject.instance().get( TestApiParameters.API_BUG_TRACKER ) + id + "\"" ) + id + HtmlTag.A.close() );
            }
        }

        // add another log file location, if any
        final String FILE = result.getTestContext().getCurrentXmlTest().getName() + ".json";
        final String LOG_PATH = "var/log/qa";
        final String LOG_FULL_PATH = "var/log/qa_full";
        String logs = System.getProperty( "LOG_PATH" ) + FILE;

        if( Utility.isFileExisted( logs ) )
        {
            logs = logs.contains( LOG_FULL_PATH ) ? logs.replaceAll( LOG_FULL_PATH, "log" ) : logs.replaceAll( LOG_PATH, "log" );
            comment.append( HtmlTag.BR.open() + HtmlTag.A.open( "href=\"" + logs + "\"" ) + FILE + HtmlTag.A.close() );
        }

        testCaseResult.setComments( comment.toString() );

        testCaseResult.setErrorType( getErrorTypeFromResult( result ) );

        try
        {
            appendTestsToHtml( testCaseResult );
        }
        catch( IOException e )
        {
            LOG.error( "Error occured while appending test case data, " + e.getMessage() );
        }
    }

    private void appendTestsToHtml( TestCaseResult testCaseResult ) throws IOException
    {
        HtmlTr< TestCaseResult > dataRow = new HtmlTr< TestCaseResult >();

        dataRow.classAttr( testCaseResult.result().toLowerCase() );

        dataRow
                .td() //
                .a( "./" + testCaseResult.testCaseLog() )
                .text( testCaseResult.testCaseId() );
        dataRow
                .td() //
                .text( testCaseResult.desc() );
        dataRow
                .td() //
                .text( testCaseResult.testCaseClassName() );

        // check if the equipment is null
        boolean equipment = false;
        if( (Browser)Configuration.instance().equipment( EquipmentType.BROWSER ) != null || (Android)Configuration.instance().equipment( EquipmentType.TRAVEL_ANDROID ) != null
                || (Ios)Configuration.instance().equipment( EquipmentType.TRAVEL_IOS ) != null )
        {
            equipment = true;
        }
        if( (testCaseResult.result().equalsIgnoreCase( RESULT_FAIL ) || testCaseResult.result().equalsIgnoreCase( RESULT_SKIP )) && equipment )
        {
            dataRow
                    .td() //
                    .a( "./" + testCaseResult.screenShot() )
                    .text( testCaseResult.result().toUpperCase() );
        }
        else
        {
            dataRow.td().text( testCaseResult.result().toUpperCase() );
        }

        dataRow.td().text( testCaseResult.time() );
        dataRow.td().text( testCaseResult.comments() );
        dataRow.td().text( testCaseResult.errorType() );

        dataRow
                .setPrintStream( m_testTableHtmlWriter )//
                .write();
    }

    private void setSummaryData( HtmlHead< Map< String, Object > > htmlHead, HtmlView< Map< String, Object > > view )
    {
        LOG.info( "setSummaryData" );

        // set map data
        htmlHead.addChild( new HtmlStyle<>() ).addAttr( "type", "text/css" ).text( SUMMARY_HTML_CSS );

        HtmlDiv< Map< String, Object > > container = view.body().div();

        HtmlDiv< Map< String, Object > > topSubContainer = container.div().addAttr( "style", "width:580px;float:left" );

        appendLogo( topSubContainer );

        appendSummaryTable( topSubContainer );

        HtmlDiv< Map< String, Object > > bottomSubContainer = container.div().addAttr( "style", "float:right" );

        appendStartEndTime( topSubContainer );

        appendErrorTypeTable( topSubContainer );

        appendSkipTypeTable( topSubContainer );

        appendConsoleLink( topSubContainer );

        appendPieChart( bottomSubContainer );

        // set print stream
        view.setPrintStream( m_summaryHtmlWriter ).write( m_reportData );
    }

    private void bindConsoleData( HtmlHead< Map< String, Object > > htmlHead, HtmlView< Map< String, Object > > view )
    {
        LOG.info( "bindConsoleData" );

        htmlHead
                .scriptLink( "jquery.min.js" )//
                .scriptLink( "custom.js" );

        view.body().div().idAttr( "logs" );

        // set print stream
        view
                .setPrintStream( m_consoleHtmlWriter )//
                .write( m_reportData );
    }

    private void bindTestsData( HtmlHead< Map< String, Object > > htmlHead, HtmlView< Map< String, Object > > view )
    {
        LOG.info( "bindTestsData" );

        // set map data
        htmlHead
                .scriptLink( "jquery.min.js" )//
                .scriptLink( "jquery.tablesorter.min.js" )
                .scriptLink( "colResizable.min.js" )
                .scriptLink( "custom.js" );

        htmlHead
                .addChild( new HtmlStyle<>() ) //
                .addAttr( "type", "text/css" )
                .text( TESTS_HTML_CSS );

        HtmlBody< Map< String, Object > > htmlBody = view.body();
        htmlBody.heading( 2, SUITE_NAME + m_reportData.get( ModelKeys.SUITE_NAME.m_val ) );

        HtmlTable< Map< String, Object > > testCaseResultTable = htmlBody.table().classAttr( "tablesorter" );

        HtmlTr< Map< String, Object > > headerRow = testCaseResultTable.addChild( new HtmlTHead< Map< String, Object > >() ).tr();

        headerRow
                .th() //
                .addAttr( "width", "5%" )
                .text( "TEST ID" );
        headerRow
                .th() //
                .addAttr( "width", "30%" )
                .text( "TEST NAME" );

        headerRow
                .th() //
                .addAttr( "width", "20%" )
                .text( "CLASS" );

        headerRow
                .th() //
                .addAttr( "width", "5%" )
                .text( "RESULT" );

        headerRow
                .th() //
                .addAttr( "width", "5%" )
                .text( "TIME" );

        headerRow
                .th() //
                .addAttr( "width", "40%" )
                .text( "COMMENTS" );

        headerRow
                .th() //
                .addAttr( "width", "8%" )
                .text( "ERROR TYPE" );

        testCaseResultTable //
                .addChild( new HtmlTBody< Map< String, Object > >() )
                .idAttr( TABLE_ID );

        // set print stream
        view
                .setPrintStream( m_testHtmlWriter )//
                .write( m_reportData );

    }

    public class TestCaseResult
    {
        private String m_result;
        private String m_testCaseLog;
        private String m_testCaseId;
        private String m_desc;
        private String m_testCaseClassName;
        private String m_time;
        private String m_comments;
        private String m_errorType;
        private String m_screenShot;

        public String testCaseId()
        {
            return m_testCaseId;
        }

        public void setTestCaseId( String testCaseId )
        {
            this.m_testCaseId = testCaseId;
        }

        public String desc()
        {
            return m_desc;
        }

        public void setDesc( String desc )
        {
            this.m_desc = desc;
        }

        public String testCaseClassName()
        {
            return m_testCaseClassName;
        }

        public void setTestCaseClassName( String testCaseClassName )
        {
            this.m_testCaseClassName = testCaseClassName;
        }

        public String time()
        {
            return m_time;
        }

        public void setTime( String time )
        {
            this.m_time = time;
        }

        public String comments()
        {
            return m_comments;
        }

        public void setComments( String comments )
        {
            this.m_comments = comments;
        }

        public String errorType()
        {
            return m_errorType;
        }

        public void setErrorType( String errorType )
        {
            this.m_errorType = errorType;
        }

        public String result()
        {
            return m_result;
        }

        public void setResult( String result )
        {
            this.m_result = result;
        }

        public String testCaseLog()
        {
            return m_testCaseLog;
        }

        public void setTestCaseLog( String testCaseLog )
        {
            this.m_testCaseLog = testCaseLog;
        }

        public String screenShot()
        {
            return m_screenShot;
        }

        public void setScreenShot( String screenShot )
        {
            this.m_screenShot = screenShot;
        }
    }
}
