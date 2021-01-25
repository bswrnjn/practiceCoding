package jp.co.rakuten.travel.framework.utility;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Empty container class to hold empty structures
 */
public class Empty
{
    /**
     * returns empty list
     * @param obj
     * @return
     */
    public static < T > List< T > list()
    {
        return Collections.emptyList();
    }

    /**
     * returns empty map
     * @param obj
     * @return
     */
    public static < T > Map< T, T > map()
    {
        return Collections.emptyMap();
    }

    /**
     * returns empty set
     * @param obj
     * @return
     */
    public static < T > Set< T > set()
    {
        return Collections.emptySet();
    }

    /**
     * returns empty array
     * @param obj
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public static < T > T [] array( Class< ? extends Object > clazz )
    {
        return (T [])Array.newInstance( clazz, 0 );
    }
}
