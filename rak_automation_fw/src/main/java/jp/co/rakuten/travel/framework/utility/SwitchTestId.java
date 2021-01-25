package jp.co.rakuten.travel.framework.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.yaml.snakeyaml.Yaml;

import jp.co.rakuten.travel.framework.logger.TestLogger;

public class SwitchTestId
{

    protected static TestLogger LOG = (TestLogger)TestLogger.getLogger( "" );

    /**
     * Reads a directory for YAML files or reads a YAML file and interchanges test case names for ID's 
     * @param args [0] path to a directory or a file
     */
    public static void main( String [] args )
    {
        if( Utility.isEmpty( args ) )
        {
            LOG.warn( "no directory or file specified" );
            return;
        }

        Path path = FileSystems.getDefault().getPath( args[ 0 ] );
        if( Files.notExists( path, new LinkOption []
        { LinkOption.NOFOLLOW_LINKS } ) )
        {
            LOG.warn( "path does not exist " + args[ 0 ] );
            return;
        }

        SwitchTestId switchtstId = new SwitchTestId();

        try
        {
            switchtstId.interchange( new File( args[ 0 ] ) );
        }
        catch( IOException | ParseException e )
        {
            LOG.debug( e.getClass().getSimpleName() + " found with message " + e.getMessage() );
            e.printStackTrace();
        }
    }

    public void interchange( File file ) throws ParseException, IOException
    {
        LOG.info( "interchange" );

        if( file == null )
        {
            throw new FileNotFoundException( "Argument is null" );
        }

        if( file.isDirectory() )
        {
            LOG.info( "loading directory " + file.getAbsolutePath() );
            File [] subFiles = file.listFiles();
            if( subFiles == null )
            {
                throw new FileNotFoundException( "directory " + file.getAbsolutePath() + "encountered unknown error and will be skipped in process" );
            }
            for( int i = 0; i < subFiles.length; ++i )
            {
                interchange( subFiles[ i ] );
            }

            return;
        }

        LOG.info( "loading file " + file.getAbsolutePath() );

        Map< Integer, String > testCaseNames = new HashMap< Integer, String >();
        Map< Integer, String > testCaseIds = new HashMap< Integer, String >();

        if( !FilenameUtils.getExtension( file.getAbsolutePath() ).toLowerCase().equals( "yaml" ) )
        {
            throw new ParseException( "File extension NOT supported", 0 );
        }

        Yaml yaml = new Yaml();

        // scour through the yaml and
        // save the test case names and ids

        @SuppressWarnings( "unchecked" )
        Map< String, Object > map = (Map< String, Object >)yaml.load( new FileReader( file ) );
        for( String key : map.keySet() )
        {
            switch( key )
            {
            case "tests":
                // System.out.println( "tests" );
                @SuppressWarnings( "unchecked" )
                List< Object > testlist = (List< Object >)map.get( key );
                testCaseNames = setTestCaseNames( testlist, testCaseNames );
                testCaseIds = setTestCaseIds( testlist, testCaseIds );
                break;
            default:
                // System.out.println( key + " : \"" + map.get( key ) + "\"" );
                break;
            }
        }

        if( testCaseNames.size() == testCaseIds.size() )
        {
            for( int i = 0; i < testCaseNames.size(); ++i )
            {
                LOG.info( testCaseIds.get( i ) + " : " + testCaseNames.get( i ) );
            }
        }

        // search and destroy

        try
        {
            String content = FileUtils.readFileToString( file, "UTF-8" );
            for( int i = 0; i < testCaseNames.size(); ++i )
            {
                LOG.info( "switching " + testCaseIds.get( i ) + " and " + testCaseNames.get( i ) );
                content = content.replace( "name: " + testCaseNames.get( i ) + "\r\n", "name: " + testCaseIds.get( i ) + "\r\n" );
                content = content.replace( "id: " + testCaseIds.get( i ) + "\r\n", "desc: " + testCaseNames.get( i ) + "\r\n" );
            }
            FileUtils.writeStringToFile( file, content, "UTF-8" );
        }
        catch( IOException e )
        {
            // Simple exception handling, replace with what's necessary for your use case!
            throw new IOException( "Generating file failed", e );
        }
    }

    private Map< Integer, String > setTestCaseNames( List< Object > testlist, Map< Integer, String > testCaseNames )
    {
        LOG.info( "setTestCaseNames" );

        if( !Utility.isEmpty( testlist ) )
        {
            for( Object testObject : testlist )
            {
                // System.out.println( TEST_SEPARATOR );
                @SuppressWarnings( "unchecked" )
                Map< String, Object > testMap = (Map< String, Object >)testObject;
                for( String testMapKey : testMap.keySet() )
                {
                    switch( testMapKey )
                    {
                    case "name":
                        testCaseNames.put( testCaseNames.size(), (String)testMap.get( testMapKey ) );
                        break;
                    default:
                        break;
                    }
                    // System.out.println( testMapKey + " : " + testMap.get( testMapKey ) );
                }
            }
        }
        return testCaseNames;
    }

    private Map< Integer, String > setTestCaseIds( List< Object > testlist, Map< Integer, String > testCaseIds )
    {
        LOG.info( "setTestCaseIds" );

        if( !Utility.isEmpty( testlist ) )
        {
            for( Object testObject : testlist )
            {
                // System.out.println( TEST_SEPARATOR );
                @SuppressWarnings( "unchecked" )
                Map< String, Object > testMap = (Map< String, Object >)testObject;
                for( String testMapKey : testMap.keySet() )
                {
                    switch( testMapKey )
                    {
                    case "parameters":
                        @SuppressWarnings( "unchecked" )
                        Map< String, Object > testParametersMap = (Map< String, Object >)testMap.get( testMapKey );
                        for( String testParametersMapKey : testParametersMap.keySet() )
                        {
                            switch( testParametersMapKey )
                            {
                            case "id":
                                testCaseIds.put( testCaseIds.size(), (String)testParametersMap.get( testParametersMapKey ) );
                                break;
                            default:
                                break;
                            }
                        }
                        break;
                    default:
                        break;
                    }
                    // System.out.println( testMapKey + " : " + testMap.get( testMapKey ) );
                }
            }
        }
        return testCaseIds;

    }
}
