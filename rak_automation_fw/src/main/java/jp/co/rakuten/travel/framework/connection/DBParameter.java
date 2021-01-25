package jp.co.rakuten.travel.framework.connection;

/**
 * Hold Database connection parameters
 * Default, It initializes to host = 127.0.0.1, port = 27017, database = rakuten, collection = testsuites 
 * @version 1.0.0
 * @since 1.0.0  
 */
public class DBParameter
{
    private String  m_host;
    private Integer m_port;
    private String  m_database;

    public DBParameter( String hostname, Integer port, String databaseName )
    {
        this.m_host = hostname;
        this.m_port = port;
        this.m_database = databaseName;
    }

    public String getHost()
    {
        return m_host;
    }

    public void setHost( String host )
    {
        this.m_host = host;
    }

    public Integer getPort()
    {
        return m_port;
    }

    public void setPort( Integer port )
    {
        this.m_port = port;
    }

    public String getDatabase()
    {
        return m_database;
    }

    public void setDatabase( String database )
    {
        this.m_database = database;
    }
}
