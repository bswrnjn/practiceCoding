package jp.co.rakuten.travel.framework.tools.parsers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import jp.co.rakuten.travel.framework.tools.constants.Constants.CommandConstants;
import jp.co.rakuten.travel.framework.tools.constants.Constants.ExecutorType;

/**
 * Parse the command line parameters for the Group TestCase parser
 * Parameters are parsed and provided in sequential order.
 * @version 1.0.0
 * @since 1.0.0 
 */
public class GroupTestcaseCommandParser implements CommandParser
{
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
                        case LOGDIRECTORY:
                            // parameters[ 0 ] = value;
                            break;
                        case SELECTIVETESTS:
                            // parameters[ 1 ] = value;
                            break;
                        case D:
                            // parameters[ 2 ] = value;
                            break;
                        case PARSERTYPE:
                            // parameters[ 3 ] = value;
                            break;
                        case DBHOST:
                            // parameters[ 4 ] = value;
                            break;
                        case DBPORT:
                            // parameters[ 5 ] = value;
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
        ExecutorType type = ExecutorType.YAML;
        // if( !StringUtils.isEmpty( parameters[ 3 ] ) && parameters[ 3 ].toUpperCase().equals( ExecutorType.MONGO.name() ) )
        {
            type = ExecutorType.MONGO;
        }

        // validate( parameters, type );
        return parameters;
    }

    private void validate( String [] parameters, ExecutorType type )
    {
        if( StringUtils.isEmpty( parameters[ 0 ] ) )
        {
            throw new IllegalArgumentException( "please specify log directory: -ld [log directory] " );
        }
        if( StringUtils.isEmpty( parameters[ 1 ] ) )
        {
            throw new IllegalArgumentException( "please specify testcase Id's: -t [comma seperated testcase Id's] " );
        }
        if( StringUtils.isEmpty( parameters[ 2 ] ) )
        {
            if( type == ExecutorType.MONGO )
            {
                throw new IllegalArgumentException( "please specify DB collection name: -d [DB Collection name] " );
            }
            else
            {
                throw new IllegalArgumentException( "please specify yaml directory: -d [yaml directory] " );
            }
        }
    }

    private String helpMessage()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append( "Usage: java -jar qa-booking-auto-test.jar -ld [log directory] -t [comma seperated testcase Id's] -d [yaml directory] -p [YAML] \n or: " );
        buffer.append( "java -jar qa-booking-auto-test.jar -ld [log directory] -t [comma seperated testcase Id's] -d [DB collection name] -p [MONGO] -dbh [Database server host] -dbp [Database server port] -dbn [Database name] \n\n" );

        buffer.append( "-mn   --mongosuite    (Comma separated  MongoDB suite names) \n" );
        buffer.append( "-t    --testcase      (Comma separated Testcase names) \n" );
        buffer.append( "-y    --yaml          (Comma separated yaml files) \n" );
        buffer.append( "-p    --parsertype    (Parser type eg. YAML/ MONGO required in selective and group testcase execution) \n" );
        buffer.append( "-ld   --logdirectory  (log directory path) \n" );
        buffer.append( "-d                    (-d = directory if parser type is YAML -d = database collection name if parser type is MONGO). \n" );
        buffer.append( "-dbh  --dbhost        (MongoDB host) \n" );
        buffer.append( "-dbp  --dbport        (MongoDB port) \n" );
        buffer.append( "-dbn  --dbname        (MongoDB database name) \n" );
        buffer.append( "-r    --review \n" );

        return buffer.toString();
    }
}
