package jp.co.rakuten.travel.framework.configuration;

import jp.co.rakuten.travel.framework.parameter.Parameters;

public interface Equipment
{
    /**
     * initialization of {@link Equipment} resources
     */
    void init();

    /**
     * terminates, quits destroys all resources
     */
    void release();

    /**
     * reconnects and re-initializes {@link Equipment}
     */
    void recover();

    /**
     * per equipment option with an attempt to refresh some settings each and every test case
     */
    void refresh();

    /**
     * per equipment option with an attempt to get information on spot of error
     */
    void errorInfo();

    public enum EquipmentType implements Parameters
    {
        SLM_DB,
        RESERVATION_DB,
        DICT_DB,
        BROWSER,
        HTTP,
        TRAVEL_ANDROID,
        TRAVEL_IOS,
        UNKNOWN;

        @Override
        public String val()
        {
            return name();
        }

        @Override
        public Parameters unknown()
        {
            return UNKNOWN;
        }
    }
}
