package jp.co.rakuten.travel.framework.tools.parsers;

import java.util.Map;

import jp.co.rakuten.travel.framework.tools.constants.Constants.CommandConstants;

/**
 * Command Parser need to be implement by all custom Command line argument Parser
 * @version 1.0.0
 * @since 1.0.0 
 */
public interface CommandParser
{
    Map< CommandConstants, String > parser( String [] args );
}
