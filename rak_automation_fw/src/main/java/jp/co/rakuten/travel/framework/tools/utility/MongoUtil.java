package jp.co.rakuten.travel.framework.tools.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import jp.co.rakuten.travel.framework.tools.constants.Constants;

/**
 * Utility class to parse MongoDB objects to Java objects
 * @version 1.0.0
 * @since 1.0.0  
 */
public class MongoUtil
{
    /**
     * Map MongoDB Document to java.util.Map<String,String>
     * @param object Document object
     * @return Map<String, String>
     */
    public static Map< String, String > parseDBObject( Document object )
    {
        Map< String, String > values = new HashMap< String, String >();
        if( object != null )
        {
            Set< String > keys = object.keySet();
            Iterator< String > iterator = keys.iterator();

            while( iterator.hasNext() )
            {
                String key = iterator.next();
                String value = object.get( key ).toString();
                values.put( key, value );
            }
        }
        return values;
    }

    /**
     * Map List of testcases to java.util.List<XmlTest> 
     * @param suite XmlSuite
     * @param testCases Test cases
     * @return List List of XmlTest
     */
    @SuppressWarnings(
    { "rawtypes", "unchecked" } )
    public static List< XmlTest > parseTestCases( XmlSuite suite, List< ArrayList< Document > > testCases, List< String > classes )
    {
        List< XmlTest > testCase = new ArrayList< XmlTest >();

        if( testCases != null )
        {
            Iterator< ArrayList< Document > > iterator = testCases.iterator();
            while( iterator.hasNext() )
            {
                ArrayList< Document > dbObject = (ArrayList< Document >)iterator.next();

                for( Document object : dbObject )
                {
                    Map parameters = new ConcurrentHashMap( (Document)object.get( Constants.PARAMETERS ) );
                    XmlTest test = new XmlTest();
                    test.setSuite( suite );
                    test.setName( object.get( Constants.OBJECT_ID ).toString() );
                    test.setClasses( getXmlClass( classes ) );
                    test.setParameters( parseParameters( parameters ) );
                    testCase.add( test );
                }

            }
        }
        return testCase;
    }

    @SuppressWarnings(
    { "unchecked", "rawtypes" } )
    public static Map parseParameters( Map parameters )
    {
        for( Object entry : parameters.entrySet() )
        {
            Object value = ((Entry)entry).getValue();
            if( value instanceof List )
            {
                LinkedHashSet< Map< String, String > > maps = new LinkedHashSet< Map< String, String > >();
                List< Document > documents = (List< Document >)value;
                for( Document document : documents )
                {
                    maps.add( parseDBObject( document ) );
                }
                parameters.put( ((Entry)entry).getKey(), maps );
            }
            else
            {
                parameters.put( ((Entry)entry).getKey(), ((Entry)entry).getValue().toString() );
            }
        }
        return parameters;
    }

    /**
     * Map classes string List to java.util.List<XmlClass>
     * @param classes List of classes
     * @return List List of XmlClass
     */
    private static List< XmlClass > getXmlClass( List< String > classes )
    {
        List< XmlClass > xmlClasses = new ArrayList< XmlClass >();
        for( int index = 0; index < classes.size(); index++ )
        {
            XmlClass xmlClass = new XmlClass( classes.get( index ).toString(), index );
            xmlClasses.add( xmlClass );
        }
        return xmlClasses;
    }

    /**
     * Map List of testcases to java.util.List<XmlTest> 
     * @param suite XmlSuite
     * @param testCaseDocument Test case document
     * @return List List of XmlTest
     */
    @SuppressWarnings(
    { "rawtypes", "unchecked" } )
    private static XmlTest parseTestCase( XmlSuite suite, Document testCaseDocument )
    {
        XmlTest testCase = new XmlTest();
        testCase.setSuite( suite );
        testCase.setName( testCaseDocument.getString( Constants.NAME ) );
        Map map = new ConcurrentHashMap( (Document)testCaseDocument.get( Constants.PARAMETERS ) );
        for( Object entry : map.entrySet() )
        {
            Object object = ((Entry)entry).getValue();

            if( object instanceof List )
            {
                LinkedHashSet< Map< String, Object > > maps = new LinkedHashSet< Map< String, Object > >();
                List< Document > documents = (List< Document >)object;
                for( Document document : documents )
                {
                    maps.add( new LinkedHashMap<>( document ) );
                }
                map.put( ((Entry)entry).getKey(), maps );
            }
            else
                map.put( ((Entry)entry).getKey(), ((Entry)entry).getValue().toString() );
        }
        testCase.setParameters( map );
        testCase.setClasses( getXmlClass( (List)testCaseDocument.get( Constants.CLASSES ) ) );

        return testCase;
    }

    public static List< XmlTest > getTestCasesByIds( XmlSuite suite, Set< String > testCaseIds, List< Document > documents )
    {
        List< XmlTest > values = new ArrayList< XmlTest >();
        if( CollectionUtils.isEmpty( documents ) || CollectionUtils.isEmpty( testCaseIds ) )
            return values;

        for( Document document : documents )
        {
            String testCaseName = document.get( Constants.NAME ).toString();

            if( testCaseIds.contains( testCaseName ) )
            {
                XmlTest xmlTest = parseTestCase( suite, document );
                values.add( xmlTest );
            }
        }

        return values;
    }

    /**
     * Parse the MongoDB document to retrieve required testcases
     * Comparison logic supports regular testcases and testcases appended with wildcard
     * @param suite XmlSuite
     * @param testCaseIds Set of test caseIds
     * @param documents List of documents
     * @return List of XmlTest
     */
    public static List< XmlTest > getTestCasesByWildCardIds( XmlSuite suite, Set< String > testCaseIds, List< Document > documents )
    {
        List< XmlTest > values = new ArrayList< XmlTest >();
        if( CollectionUtils.isEmpty( documents ) || CollectionUtils.isEmpty( testCaseIds ) )
            return values;

        for( Document document : documents )
        {
            String testCaseName = document.get( Constants.NAME ).toString();

            for( String testCaseId : testCaseIds )
            {
                boolean wildCard = false;
                if( testCaseId.endsWith( "*" ) )
                {
                    wildCard = true;
                    testCaseId = testCaseId.replace( "*", "" );
                }

                if( wildCard && testCaseName.startsWith( testCaseId ) )
                {
                    XmlTest xmlTest = parseTestCase( suite, document );
                    values.add( xmlTest );
                }
                else if( !wildCard && testCaseName.equals( testCaseId ) )
                {
                    XmlTest xmlTest = parseTestCase( suite, document );
                    values.add( xmlTest );
                }
            }
        }
        return values;
    }
}
