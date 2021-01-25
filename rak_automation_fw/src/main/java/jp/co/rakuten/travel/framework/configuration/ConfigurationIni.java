package jp.co.rakuten.travel.framework.configuration;

import jp.co.rakuten.travel.framework.utility.IniParser;

public interface ConfigurationIni
{
    void load();

    IniParser getIniParser();
}
