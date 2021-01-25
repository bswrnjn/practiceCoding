package jp.co.rakuten.travel.framework.tools.parsers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import jp.co.rakuten.travel.framework.tools.constants.Constants.CommandConstants;
import jp.co.rakuten.travel.framework.tools.constants.Constants.ExecutorType;

/**
 * Parse the command line parameters for the MongoTestNG
 * Parameters are parsed and provided in sequential order.
 * @version 1.0.0
 * @since 1.0.0 
 */
public class MongoDBCommandParser implements CommandParser
{
    /**
     * Parser the command line parameters and return the list in sequential order
     * @param String[]
     * @return String[]
     */
    @Override
    public Map< CommandConstants, String > parser( String [] args )
    {
        Map< CommandConstants, String > parameters = new HashMap< CommandConstants, String >();
        // set default parserType to MONGO
        parameters.put( CommandConstants.PARSERTYPE, ExecutorType.MONGO.toString() );
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
                    CommandConstants key = null;
                    if( !value.startsWith( "-" ) )
                    {
                        switch( commandConstants )
                        {
                        case LOGDIRECTORY:
                            key = CommandConstants.LOGDIRECTORY;
                            break;
                        case MONGOSUITES:
                            key = CommandConstants.MONGOSUITES;
                            break;
                        case PARSERTYPE:
                            key = CommandConstants.PARSERTYPE;
                            break;
                        case YAML:
                            key = CommandConstants.YAML;
                            break;
                        case DBHOST:
                            key = CommandConstants.DBHOST;
                            break;
                        case DBPORT:
                            key = CommandConstants.DBPORT;
                            break;
                        case DBNAME:
                            key = CommandConstants.DBNAME;
                            break;
                        case REVIEW:
                            key = CommandConstants.REVIEW;
                            break;
                        case USERID:
                            key = CommandConstants.USERID;
                            break;
                        case SPLIT:
                            key = CommandConstants.SPLIT;
                            break;
                        case RANDOM:
                            key = CommandConstants.RANDOM;
                            break;
                        default:
                            break;
                        }
                        parameters.put( key, value );
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
        validate( parameters );
        return parameters;
    }

    private void validate( Map< CommandConstants, String > parameters )
    {

        if( StringUtils.isEmpty( parameters.get( CommandConstants.USERID ) ) )
        {
            throw new IllegalArgumentException( "please specify user name : -u [ User Name ] " );
        }
        if( StringUtils.isEmpty( parameters.get( CommandConstants.MONGOSUITES ) ) )
        {
            throw new IllegalArgumentException( "please specify testcase Id's: -mn [ Comma separated MongoDB suite names ]" );
        }
        if( !StringUtils.isEmpty( parameters.get( CommandConstants.SELECTIVETESTS ) ) && parameters.get( CommandConstants.REVIEW ).equals( "true" ) )
        {
            throw new IllegalArgumentException( "Cannot specify -s [ -s selective test cases ] and -review  [ -review ]at the same time " );
        }
    }

    private String helpMessage()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append( "Usage: java -jar qa-booking-auto-test.jar -ld [log directory] -mn [Comma separated MongoDB suite names] -dbh [Database server host] -dbp [Database server port] -dbn [Database name] -dbc [MongoDB collection name]" );

        buffer.append( "-mn   --mongosuite    (Comma separated  MongoDB suite names) \n" );
        buffer.append( "-s    --selective      (Comma separated Testcase names) \n" );
        buffer.append( "-y    --yaml          (Comma separated yaml files) \n" );
        buffer.append( "-p    --parsertype    (Parser type eg. YAML/ MONGO required in selective and group testcase execution) \n" );
        buffer.append( "-ld   --logdirectory  (log directory path) \n" );
        buffer.append( "-d                    (-d = directory if parser type is YAML -d = database collection name if parser type is MONGO). \n" );
        buffer.append( "-dbh  --dbhost        (MongoDB host) \n" );
        buffer.append( "-dbp  --dbport        (MongoDB port) \n" );
        buffer.append( "-dbp  --dbport        (MongoDB port) \n" );
        buffer.append( "-dbn  --dbname        (MongoDB database name) \n" );
        buffer.append( "-review    --review \n" );
        buffer.append( "-r  --random (Random test cases to be run) \n" );
        buffer.append( "-t   --split (Testcase count every split should have) \n" );
        buffer.append( "-u   --userId (Build User ID to be specified) \n" );

        return buffer.toString();
    }
}
