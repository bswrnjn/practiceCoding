package jp.co.rakuten.travel.framework.browser;

import java.io.File;

public class BrowserFactory
{
    private BrowserFactory()
    {
        /**
         * sonar recommendation for hiding default constructor with pure static class
         */
    }

    public static Browser getBrowser( BrowserType browser, File pacFile )
    {
        Browser config;
        switch( browser )
        {
        case CHROME:
            config = new ChromeBrowser( pacFile );
            break;
        case FIREFOX:
            config = new FirefoxBrowser( pacFile );
            break;
        case INTERNET_EXPLORER:
            config = new InternetExplorerBrowser( pacFile );
            break;
        case ANDROID_EMULATOR:
        case ANDROID_PHONE:
        case ANDROID_TABLET:
        case IOS_SIMULATOR:
        case IOS_PHONE:
        case IOS_TABLET:
        case MOBILE_SAFARI:
        case FEATURE_PHONE:
        case MOBILE_FIREFOX:
        case MOBILE_CHROME:
            config = new MobileBrowser( pacFile );
            break;
        case SAFARI:
        default:
            throw new NoClassDefFoundError( "Not supported yet" );
        }

        return config;
    }
}
