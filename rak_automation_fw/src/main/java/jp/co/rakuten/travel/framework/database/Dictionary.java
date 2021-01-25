package jp.co.rakuten.travel.framework.database;

import jp.co.rakuten.travel.framework.parameter.DictionaryKey;

/**
 * Interface class for getting translation
 *
 */
public interface Dictionary
{
    /**
     * Interface method for getting translation
     *
     */
    String get( DictionaryKey key );

    String get( String key );

    String getNumberOfChildren( int children );

    String getNumberOfAdults( int adults );

    String getNumberOfRooms( int rooms );
}
