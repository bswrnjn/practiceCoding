package jp.co.rakuten.travel.framework.parameter;

import java.util.Map;

/**
 * The main handler for service parameters
 *
 */
// FIXME transfer to jp.co.rakuten.travel.framework.configuration
public interface TestObjects
{
    final String YES = "yes";
    final String NO  = "no";

    void purge();

    void add( Map< Parameters, String > map );
}
