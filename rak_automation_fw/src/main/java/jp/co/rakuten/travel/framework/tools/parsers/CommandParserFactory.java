package jp.co.rakuten.travel.framework.tools.parsers;

import java.util.Map;

import jp.co.rakuten.travel.framework.tools.constants.Constants.CommandConstants;
import jp.co.rakuten.travel.framework.tools.constants.Constants.CommandParserType;

/**
 * Factory to get the parsed parameters from required command line parameter parser
 * @version 1.0.0
 * @since 1.0.0 
 */
public class CommandParserFactory
{
    public static Map< CommandConstants, String > getParameters( String [] args )
    {
        Map< CommandConstants, String > parameters = null;
        CommandParserType parserType = getParserType( args );
        switch( parserType )
        {
        case MONGODB:
            parameters = new MongoDBCommandParser().parser( args );
            break;
        case YAML_MONGO:
            // parameters = new Yaml2MongoDBCommandParser().parser( args );
            break;
        case SELECTIVE_TESTCASE:
            parameters = new SelectiveTestcaseCommandParser().parser( args );
            break;
        case GROUP_TESTCASE:
            // parameters = new GroupTestcaseCommandParser().parser( args );
        case UNKNOWN:
        default:
            break;
        }
        return parameters;
    }

    private static CommandParserType getParserType( String [] args )
    {
        CommandParserType parserType = CommandParserType.UNKNOWN;
        for( int index = 0; index < args.length; index++ )
        {
            if( args[ index ].equals( "-t" ) || args[ index ].equals( "-r" ) || args[ index ].equals( "-review" ) )
            {
                index++;
                parserType = CommandParserType.MONGODB;
                break;
            }
            else if( args[ index ].equals( "-s" ) )
            {
                index++;
                parserType = CommandParserType.SELECTIVE_TESTCASE;
                break;
            }
            else if( args[ index ].equals( "-d" ) )
            {
                index++;
                parserType = CommandParserType.GROUP_TESTCASE;
                break;
            }
        }
        return parserType;
    }
}
