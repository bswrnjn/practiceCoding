package jp.co.rakuten.travel.framework.tools.parsers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import jp.co.rakuten.travel.framework.tools.constants.Constants.CommandConstants;

/**
 * Parse the command line parameters for the Yaml2Mongo parser
 * Parameters are parsed and provided in sequential order.
 * @version 1.0.0
 * @since 1.0.0 
 */
public class Yaml2MongoDBCommandParser implements CommandParser
{
    private final int m_parameterLength = 5;

    /**
     * Parser the command line parameters and return the list in sequential order
     * @param String[]
     * @return String[]
     */
    @Override
    public Map< CommandConstants, String > parser( String [] args )
    {
        // FIXME this method needs changes refering to SelectiveTestCaseCommandParser
        Map< CommandConstants, String > parameters = new HashMap< CommandConstants, String >();
        for( int index = 0; index < args.length; index++ )
        {
            String arg = args[ index ];
            if( arg.startsWith( "-" ) )
            {
                if( arg.equals( "--help" ) )
                {
                    throw new IllegalArgumentException( helpMessage() );
                }
                CommandConstants commandConstants = CommandConstants.getCommandConstatnt( arg );

                index++;
                if( index < args.length )
                {
                    String value = args[ index ];
                    if( !value.startsWith( "-" ) )
                    {
                        switch( commandConstants )
                        {
                        case YAML:
                            // parameters[ 0 ] = value;
                            break;
                        case DBHOST:
                            // parameters[ 1 ] = value;
                            break;
                        case DBPORT:
                            // parameters[ 2 ] = value;
                            break;
                        case DBNAME:
                            // parameters[ 3 ] = value;
                            break;
                        default:
                            break;
                        }
                    }
                    else
                    {
                        throw new IllegalArgumentException( helpMessage() );
                    }
                }
            }
        }

        /*
         * Validate the required parameters
         */
        // validate( parameters );
        return parameters;
    }

    private void validate( String [] parameters )
    {
        if( StringUtils.isEmpty( parameters[ 0 ] ) )
        {
            throw new IllegalArgumentException( "please specify yaml file: -y [Yaml file] " );
        }
        else if( !parameters[ 0 ].endsWith( ".yaml" ) )
        {
            throw new IllegalArgumentException( "please specify valid yaml file." );
        }
    }

    private String helpMessage()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append( "Usage: java -jar qa-booking-auto-test.jar -y [yaml files] -dbh [Database server host] -dbp [Database server port] -dbn [Database name] -dbc [MongoDB collection name]" );

        buffer.append( "-mn   --mongosuite    (Comma separated  MongoDB suite names) \n" );
        buffer.append( "-t    --testcase      (Comma separated Testcase names) \n" );
        buffer.append( "-y    --yaml          (Comma separated yaml files) \n" );
        buffer.append( "-p    --parsertype    (Parser type eg. YAML/ MONGO required in selective and group testcase execution) \n" );
        buffer.append( "-ld   --logdirectory  (log directory path) \n" );
        buffer.append( "-d                    (-d = directory if parser type is YAML -d = database collection name if parser type is MONGO). \n" );
        buffer.append( "-dbh  --dbhost        (MongoDB host) \n" );
        buffer.append( "-dbp  --dbport        (MongoDB port) \n" );
        buffer.append( "-dbn  --dbname        (MongoDB database name) \n" );
       

        return buffer.toString();
    }
}
