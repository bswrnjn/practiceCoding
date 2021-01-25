package jp.co.rakuten.travel.framework.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import oracle.jdbc.driver.OracleDriver;

/**
 * Reservation database instance to manage Reservation database connections
 */
public class ReservationDatabaseImpl implements OracleDatabaseConnection< java.sql.Driver, java.sql.Connection >
{
    protected TestLogger LOG = (TestLogger)TestLogger.getLogger( this.getClass() );

    protected Connection m_connection;

    protected Driver     m_driver;

    @Override
    public void init()
    {
        LOG.info( "init" );

        // TODO expand execution when other databases are required
        m_driver = new OracleDriver();

        try
        {
            DriverManager.registerDriver( m_driver );
        }
        catch( SQLException e )
        {
            LOG.error( e.getClass().getSimpleName() + " found with message " + e.getMessage() + ". Initialization failed" );
        }

        connect();
    }

    @Override
    public void release()

    {
        disconnect();
    }

    @Override
    public void recover()
    {
        reconnect();
    }

    @Override
    public void refresh()
    {

    }

    @Override
    public void errorInfo()
    {

    }

    @Override
    public void connect()
    {
        LOG.info( "-------- Oracle JDBC Connection ------" );

        String url = TestApiObject.instance().get( TestApiParameters.API_RESERV_DB_HOST ) //
                + ":" + TestApiObject.instance().get( TestApiParameters.API_RESERV_DB_PORT ) //
                + "/" + TestApiObject.instance().get( TestApiParameters.API_RESERV_DB_SERVICE_NAME );
        String username = TestApiObject.instance().get( TestApiParameters.API_RESERV_DB_USER );
        String password = TestApiObject.instance().get( TestApiParameters.API_RESERV_DB_PASSWORD );
        try
        {
            m_connection = DriverManager.getConnection( url, username, password );
        }
        catch( SQLException e )
        {
            LOG.error( "Database connection failed\n" + e.getMessage() );
        }
    }

    @Override
    public void disconnect()
    {
        try
        {
            m_connection.close();
            m_connection = null;
        }
        catch( Exception e )
        {
            LOG.error( "Database disconnection failed" );
        }
    }

    @Override
    public void reconnect()
    {
        disconnect();
        connect();
    }

    @Override
    public final Driver driver()
    {
        return m_driver;
    }

    @Override
    public Connection connection()
    {
        return m_connection;
    }
}
