package jp.co.rakuten.travel.framework.property;

import jp.co.rakuten.travel.framework.parameter.Pages;

/**
 * Handler for going to home URL.
 *
 */
public interface Browsable< T extends Pages >
{
    boolean gotoHomeUrl( T page );
}
