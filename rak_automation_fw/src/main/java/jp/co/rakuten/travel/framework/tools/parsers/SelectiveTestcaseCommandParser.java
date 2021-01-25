package jp.co.rakuten.travel.framework.tools.parsers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import jp.co.rakuten.travel.framework.tools.constants.Constants.CommandConstants;
import jp.co.rakuten.travel.framework.tools.constants.Constants.ExecutorType;

/**
 * Parse the command line parameters for the Selective TestCase parser
 * Parameters are parsed and provided in sequential order.
 * @version 1.0.0
 * @since 1.0.0 
 */
public class SelectiveTestcaseCommandParser implements CommandParser
{
    /**
     * Parser the command line parameters and return the list in sequential order
     * @param String[]
     * @return Map< CommandConstants, String >
     */
    @Override
    public Map< CommandConstants, String > parser( String [] args )
    {
        Map< CommandConstants, String > parameters = new HashMap< CommandConstants, String >();
        // set default parserType to MONGO
        parameters.put( CommandConstants.PARSERTYPE, ExecutorType.MONGO.toString() );
        String yaml = null;

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
                        case SELECTIVETESTS:
                            key = CommandConstants.SELECTIVETESTS;
                            break;
                        case YAML:
                            key = CommandConstants.YAML;
                            break;
                        case MONGOSUITES:
                            key = CommandConstants.MONGOSUITES;
                            break;
                        case PARSERTYPE:
                            key = CommandConstants.PARSERTYPE;
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
        ExecutorType type = ExecutorType.YAML;
        if( !StringUtils.isEmpty( parameters.get( CommandConstants.PARSERTYPE ) ) && parameters.get( CommandConstants.PARSERTYPE ).toUpperCase().equals( ExecutorType.MONGO.name() ) )
        {
            parameters.put( CommandConstants.MONGOSUITES, parameters.get( CommandConstants.MONGOSUITES ) );
            type = ExecutorType.MONGO;
        }
        else
        {
            parameters.put( CommandConstants.YAML, yaml );
        }

        validate( parameters, type );
        return parameters;
    }

    private void validate( Map< CommandConstants, String > parameters, ExecutorType type )
    {
        if( StringUtils.isEmpty( parameters.get( CommandConstants.USERID ) ) )
        {
            throw new IllegalArgumentException( "please specify user name : -u [ User Name ] " );
        }
        if( StringUtils.isEmpty( parameters.get( CommandConstants.LOGDIRECTORY ) ) )
        {
            throw new IllegalArgumentException( "please specify log directory: -ld [log directory] " );
        }
        if( StringUtils.isEmpty( parameters.get( CommandConstants.SELECTIVETESTS ) ) )
        {
            throw new IllegalArgumentException( "please specify testcase Id's: -s [comma seperated testcase Id's] " );
        }
        if( !StringUtils.isEmpty( parameters.get( CommandConstants.SELECTIVETESTS ) ) && parameters.get( CommandConstants.REVIEW ).equals( "true" ) )
        {
            throw new IllegalArgumentException( "Cannot specify both testcase Id's: -s [comma seperated testcase Id's] and -review  at the same time " );
        }
        if( StringUtils.isEmpty( parameters.get( CommandConstants.PARSERTYPE ) ) )
        {
            if( type == ExecutorType.MONGO )
            {
                throw new IllegalArgumentException( "please specify MongoDB suite names: -mn [MongoDb suite names] " );
            }
            else
            {
                throw new IllegalArgumentException( "please specify Yaml file paths: -y [yaml files]" );
            }
        }
    }

    private String helpMessage()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append( "Usage: java -jar qa-booking-auto-test.jar -ld [log directory] -t [comma seperated testcase Id's] -y [yaml files] -p [YAML] \n or: " );
        buffer.append( "java -jar qa-booking-auto-test.jar -ld [log directory] -t [comma seperated testcase Id's] -mn [MongoDB suite names] -p [MONGO] -dbh [Database server host] -dbp [Database server port] -dbn [Database name]" );

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
        buffer.append( "-r   --random (Random test cases to be run) \n" );
        buffer.append( "-t   --split (Testcase count every split should have) \n" );
        buffer.append( "-u   --userId (Build User ID to be specified) \n" );

        return buffer.toString();
    }
}
