package jp.co.rakuten.travel.framework.database;

import jp.co.rakuten.travel.framework.configuration.Equipment;

public interface OracleDatabaseConnection< T, U > extends Connection< U >, Equipment
{
    T driver();
}
