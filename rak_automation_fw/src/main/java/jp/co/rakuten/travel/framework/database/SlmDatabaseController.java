package jp.co.rakuten.travel.framework.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jp.co.rakuten.travel.framework.configuration.Configuration;
import jp.co.rakuten.travel.framework.configuration.Equipment.EquipmentType;
import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.page.WebController;

/**
 * SLM database instance to send query operation
 */
public class SlmDatabaseController implements OracleDatabase
{
    protected TestLogger                                             LOG  = (TestLogger)TestLogger.getLogger( WebController.class );

    @SuppressWarnings( "unchecked" )
    OracleDatabaseConnection< java.sql.Driver, java.sql.Connection > m_db = (OracleDatabaseConnection< java.sql.Driver, java.sql.Connection >)Configuration.instance().equipment( EquipmentType.SLM_DB );

    /*
     * (non-Javadoc)
     * 
     * @see jp.co.rakuten.travel.framework.database.DatabaseController#sendCommand(java.lang.String)
     */
    @Override
    public ResultSet send( String query ) throws NullPointerException
    {
        ResultSet resultSet = null;
        try
        {
            Statement statement = m_db.connection().createStatement( ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );
            resultSet = statement.executeQuery( query );
        }
        catch( SQLException e )
        {
            LOG.error( e.getClass().getSimpleName() + " found with message " + e.getMessage() );
        }

        return resultSet;
    }

}
