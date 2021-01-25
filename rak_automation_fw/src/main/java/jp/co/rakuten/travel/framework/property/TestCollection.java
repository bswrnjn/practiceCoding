package jp.co.rakuten.travel.framework.property;

import jp.co.rakuten.travel.framework.parameter.TestObjects;

public interface TestCollection< T extends TestObjects >
{
    T testObject();
}
