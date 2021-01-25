package jp.co.rakuten.travel.framework.page;

import jp.co.rakuten.travel.framework.parameter.TestObjects;

public interface WebFactory
{
    ServiceController controller( ControllerType type, ServiceWindow window, TestObjects testobjects );

    ServiceWindow window( WindowType type );
}
