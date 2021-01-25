package jp.co.rakuten.travel.framework.database;

public interface Connection< T >
{
    void connect();

    /**
     * terminates, disconnect database
     */
    void disconnect();

    /**
     * reconnects and re-initializes {@link OracleDatabaseConnection}
     */
    void reconnect();

    /**
     * Abstraction of actual connection
     * @return
     */
    T connection();
}
