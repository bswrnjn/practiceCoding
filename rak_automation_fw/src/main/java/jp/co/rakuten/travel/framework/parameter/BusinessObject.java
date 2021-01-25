package jp.co.rakuten.travel.framework.parameter;

import jp.co.rakuten.travel.framework.property.Business;

public interface BusinessObject< T extends TestObjects >
{
    Business< T > business();
}
