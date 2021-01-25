package jp.co.rakuten.travel.framework.database;

import jp.co.rakuten.travel.framework.configuration.Equipment;
import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * MongoDB instance handling connections between MongodB client and MongDB server
 */
public class DictionaryDatabaseImpl implements Connection< MongoDatabase >, Equipment
{
    protected TestLogger  LOG = (TestLogger)TestLogger.getLogger( this.getClass() );
    private MongoClient   m_client;
    private MongoDatabase m_database;

    @Override
    public void init()
    {
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
        disconnect();
        connect();
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
        LOG.info( "-------- MongoDB Connection ------" );

        try
        {
            this.m_client = new MongoClient( TestApiObject.instance().get( TestApiParameters.API_DICT_HOST ), Integer.valueOf( TestApiObject.instance().get( TestApiParameters.API_DICT_PORT ) ) );
            this.m_database = m_client.getDatabase( TestApiObject.instance().get( TestApiParameters.API_DICT_DB_NAME ) );
        }
        catch( Exception e )
        {
            LOG.error( "Database connection failed\n" + e.getMessage() );
        }

    }

    @Override
    public void disconnect()
    {
        try
        {
            m_client.close();
            m_client = null;
        }
        catch( Exception e )
        {
            LOG.error( "Database disconnection failed\n" + e.getMessage() );
        }

    }

    @Override
    public void reconnect()
    {
        disconnect();
        connect();
    }

    @Override
    public MongoDatabase connection()
    {
        return m_database;
    }

}
