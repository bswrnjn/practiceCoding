package jp.co.rakuten.travel.framework.tools.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.naming.NoPermissionException;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;

import jp.co.rakuten.travel.framework.tools.constants.Constants;
import jp.co.rakuten.travel.framework.tools.constants.Constants.FilePermission;
import jp.co.rakuten.travel.framework.tools.parsers.TestCase;
import jp.co.rakuten.travel.framework.tools.parsers.YamlTest;
import jp.co.rakuten.travel.framework.tools.parsers.YamlTestClass;

/**
 * Wrapper class over Application.properties, reads the parameters from property file
 * @version 1.0.0
 * @since 1.0.0  
 */
public class Utility
{
    static Logger LOG = Logger.getLogger( Utility.class );

    /**
     * Get the specified file and check the required permission for file
     * @param fileName              name of the file that need to open/ access
     * @param mode                  mode in which you want to open file, 
     * 					            it just checks whether that file satisfies that mode 
     * @return File                 file satisfying fileName and required mode
     * @throws IOException File not found
     * @throws NoPermissionException Missing permissions
     * @throws NoSuchFieldException Field not found
     */
    public static File getFile( String fileName, FilePermission mode ) throws NoPermissionException, NoSuchFieldException, IOException
    {
        File file = new File( fileName );

        if( !file.exists() && mode.equals( FilePermission.WRITE ) )
        {
            file.createNewFile();
        }

        if( !file.exists() || !file.isFile() )
        {
            throw new FileNotFoundException( "No such file found: " + fileName );
        }
        switch( mode )
        {
        case READ:
            if( !file.canRead() )
            {
                throw new NoPermissionException( "Can't read file: " + fileName );
            }
            break;

        case WRITE:
            if( !file.canWrite() )
            {
                throw new NoPermissionException( "Can't write into file: " + fileName );
            }
            break;

        case EXECUTION:
            if( !file.canExecute() )
            {
                throw new NoPermissionException( "Can't execute file: " + fileName );
            }
            break;

        default:
            throw new NoSuchFieldException( "Please specify the file opening mode" );
        }
        return file;
    }

    /**
     * Get Exception stacktrace into String
     * @param t Throwable
     * @return String
     */
    public static String getMessage( Throwable t )
    {
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter( sw, true );
        t.printStackTrace( writer );

        return sw.toString();
    }

    /**
     * Removes the character from the string
     * If string is empty
     *    returns '';
     * else
     *    returns the string by removing trailing and leading characters.
     * @param str Target string
     * @param character Want to be removed string
     * @return String Removes the character from the target string
     */
    public static String removeCharacter( String str, Character character )
    {
        String temp = str.replaceAll( "\'", "" ).trim();
        if( temp == null || temp.equals( "" ) )
        {
            return "''";
        }
        String regex = "^\\" + character + "|\\" + character + "$";
        return str.replaceAll( regex, "" ).trim();
    }

    public static String readConsole()
    {
        Scanner scanner = new Scanner( System.in );
        String value = scanner.nextLine();
        scanner.close();

        return value;
    }

    /**
     * Map List of test cases to java.util.List<YamlTest> 
     * @param testCases list of YamlTest
     * @return List<XmlTest>
     */
    public static List< TestCase > parseYamlTestCases( List< YamlTest > testCases )
    {
        List< TestCase > values = new LinkedList< TestCase >();

        if( !jp.co.rakuten.travel.framework.utility.Utility.isEmpty( testCases ) )
        {
            Iterator< YamlTest > iterator = testCases.iterator();
            while( iterator.hasNext() )
            {
                YamlTest testCase = iterator.next();
                TestCase test = new TestCase();

                DozerMapperUtil.mapper( testCase, test );
                values.add( test );
            }
        }
        return values;
    }

    /**
     * Map List of test cases to java.util.List<Test> 
     * @param testCases List of testCases
     * @return List<XmlTest>
     */
    @SuppressWarnings(
    { "rawtypes", "unchecked" } )
    public static List< TestCase > parseMongoTestCases( List testCases )
    {
        List< TestCase > values = new LinkedList< TestCase >();

        if( !jp.co.rakuten.travel.framework.utility.Utility.isEmpty( testCases ) )
        {
            Iterator< Object > iterator = testCases.iterator();
            while( iterator.hasNext() )
            {
                Document dbObject = (Document)iterator.next();

                TestCase testCase = new TestCase();
                testCase.setName( dbObject.getString( Constants.NAME ) );
                testCase.setParameters( new HashMap( (Document)dbObject.get( Constants.PARAMETERS ) ) );
                testCase.setClasses( (List)dbObject.get( Constants.CLASSES ) );

                values.add( testCase );
            }
        }
        return values;
    }

    /**
     * Display Menu and return choose option
     * @return Menu
     */
    public static Menu displayMenu()
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append( "1) Update Testcase\n" + "2) Update Suite Parameters\n" + "3) Update Testcase and Suite Parameters\n" + "Please choose option:" );

        LOG.info( stringBuffer.toString() );
        String value = readConsole();

        Menu menu = Menu.getMenu( value );

        return menu;
    }

    /**
     * Map List of Suite Parameters to java.util.Map<String, String>
     * @param suiteParameters Document of suite parameters
     * @return Map of parameters
     */
    public static Map< String, String > parseSuiteParametes( Document suiteParameters )
    {
        Map< String, String > parameters = new HashMap< String, String >();
        parameters.putAll( parseDBObject( (Document)suiteParameters.get( Constants.FRAMEWORKPARAMETERS ) ) );
        parameters.putAll( parseDBObject( (Document)suiteParameters.get( Constants.APIPARAMETERS ) ) );
        parameters.putAll( parseDBObject( (Document)suiteParameters.get( Constants.TESTPARAMETERS ) ) );

        return parameters;
    }

    /**
     * Map MongoDB Document to java.util.Map<String,String>
     * @param object Document of MongoDB
     * @return Map<String, String> of values
     */
    private static Map< String, String > parseDBObject( Document object )
    {
        Map< String, String > values = new HashMap< String, String >();
        if( object != null )
        {
            Set< String > keys = object.keySet();
            Iterator< String > iterator = keys.iterator();

            while( iterator.hasNext() )
            {
                String key = iterator.next();
                String value = object.getString( key );
                values.put( key, value );
            }
        }

        return values;
    }

    /**
     * Get first duplicate element from list
     * If no duplicates found
     *  returns NULL;
     * @param yamlTests List of YamlTest
     * @return YamlTest
     */
    public static YamlTest getDuplicateElementFromList( List< YamlTest > yamlTests )
    {
        CopyOnWriteArrayList< YamlTest > yamlTests2 = new CopyOnWriteArrayList<>( yamlTests );
        for( YamlTest yamlTest : yamlTests )
        {
            yamlTests2.remove( yamlTest );
            for( YamlTest yamlTest2 : yamlTests2 )
            {
                if( yamlTest2.getName().equalsIgnoreCase( yamlTest.getName() ) )
                {
                    return yamlTest;
                }
            }
        }
        return null;
    }

    /**
     * 
     * @param cases1 Append target list
     * @param cases2 Append source list
     * @return List< Test Case > 
     */
    public static List< TestCase > mergeLists( List< TestCase > cases1, List< TestCase > cases2 )
    {
        List< TestCase > testCases = new CopyOnWriteArrayList< TestCase >( cases1 );

        for( TestCase testCase : cases2 )
        {
            boolean found = false;
            for( TestCase testCase2 : testCases )
            {
                if( !testCase2.getName().equalsIgnoreCase( testCase.getName() ) )
                {
                    found = true;
                }
            }
            if( !found )
            {
                testCases.add( testCase );
            }
        }
        return testCases;
    }

    public static XmlTest getXmlTestCases( YamlTest yamlTest )
    {
        XmlTest test = new XmlTest();
        test.setName( yamlTest.getName() );
        test.setParameters( yamlTest.getParameters() );
        test.setClasses( getXmlClass( yamlTest.getClasses() ) );

        return test;
    }

    public static List< XmlClass > getXmlClass( List< YamlTestClass > yamlTestClass )
    {
        List< XmlClass > classes = new ArrayList< XmlClass >();
        for( int index = 0; index < yamlTestClass.size(); index++ )
        {
            XmlClass xmlClass = new XmlClass( yamlTestClass.get( index ).getName(), index );
            classes.add( xmlClass );
        }
        return classes;
    }

    public static Set< String > commaSeperatedStringToSet( String str )
    {
        return new HashSet<>( Arrays.asList( str.split( "\\s*,\\s*" ) ) );
    }
}
