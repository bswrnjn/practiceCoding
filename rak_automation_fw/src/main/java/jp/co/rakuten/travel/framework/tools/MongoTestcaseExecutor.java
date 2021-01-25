package jp.co.rakuten.travel.framework.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import jp.co.rakuten.travel.framework.connection.DBParameter;
import jp.co.rakuten.travel.framework.tools.constants.Constants.CommandConstants;
import jp.co.rakuten.travel.framework.tools.parsers.CommandParserFactory;
import jp.co.rakuten.travel.framework.tools.testng.MongoTestNG;
import jp.co.rakuten.travel.framework.tools.utility.LoggerUtil;
import jp.co.rakuten.travel.framework.tools.utility.Utility;

public class MongoTestcaseExecutor
{
    public static void main( String [] args )
    {
        Map< CommandConstants, String > parameter = CommandParserFactory.getParameters( args );

        String logFilePath = parameter.get( CommandConstants.LOGDIRECTORY );
        LoggerUtil.init( logFilePath, parameter.get( CommandConstants.LOGDIRECTORY ) );

        List< String > suiteNames = new ArrayList<>( Utility.commaSeperatedStringToSet( parameter.get( CommandConstants.MONGOSUITES ) ) );

        DBParameter dbParameters = new DBParameter( parameter.get( CommandConstants.DBHOST ), Integer.valueOf( parameter.get( CommandConstants.DBPORT ) ), parameter.get( CommandConstants.DBNAME ) );

        if( !StringUtils.isEmpty( parameter.get( CommandConstants.DBHOST ) ) )
        {
            dbParameters.setHost( parameter.get( CommandConstants.DBHOST ) );
        }

        if( !StringUtils.isEmpty( parameter.get( CommandConstants.DBPORT ) ) )
        {
            dbParameters.setPort( Integer.valueOf( parameter.get( CommandConstants.DBPORT ) ) );
        }

        if( !StringUtils.isEmpty( parameter.get( CommandConstants.DBNAME ) ) )
        {
            dbParameters.setDatabase( parameter.get( CommandConstants.DBNAME ) );
        }
        MongoTestNG mongoTestNG = new MongoTestNG( parameter, dbParameters, suiteNames, logFilePath );
        mongoTestNG.run();

    }
}
