package jp.co.rakuten.travel.framework.browser;

import jp.co.rakuten.travel.framework.parameter.Parameters;

/**
    FIREFOX,
    INTERNET_EXPLORER,
    CHROME,
    ANDROID_EMULATOR,
    ANDROID_PHONE,
    ANDROID_TABLET,
    IOS_SIMULATOR,
    IOS_PHONE,
    IOS_TABLET,
    SAFARI,
    MOBILE_SAFARI,
    FEATURE_PHONE,
    MOBILE,
 */
public enum BrowserType implements Parameters
{
    //
    FIREFOX,
    INTERNET_EXPLORER,
    CHROME,
    ANDROID_EMULATOR,
    ANDROID_PHONE,
    ANDROID_TABLET,
    IOS_SIMULATOR,
    IOS_PHONE,
    IOS_TABLET,
    SAFARI,
    MOBILE_SAFARI,
    FEATURE_PHONE,
    /**
     * Anything not PC
     */
    MOBILE_FIREFOX,
    MOBILE_CHROME,
    /**
     * Headless browser. Only as an option
     */
    PHANTOMJS,
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
