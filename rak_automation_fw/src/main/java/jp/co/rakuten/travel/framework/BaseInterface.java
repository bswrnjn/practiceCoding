package main.java.jp.co.rakuten.travel.framework;

import jp.co.rakuten.travel.framework.parameter.TestObjects;

@FunctionalInterface
public interface BaseInterface< T extends TestObjects >
{
    T testObjects();
}
