package jp.co.rakuten.travel.framework.parameter;

import jp.co.rakuten.travel.framework.page.ServiceParameters;

/**
 * Interface handling GUI-based service parameters
 *
 */
public interface ServiceObjects< T extends ServiceParameters >
{
    T service();
}
