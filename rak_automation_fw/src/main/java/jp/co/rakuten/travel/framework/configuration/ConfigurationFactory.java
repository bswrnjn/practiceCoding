package jp.co.rakuten.travel.framework.configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.NotImplementedException;

import jp.co.rakuten.travel.framework.app.android.AndroidImpl;
import jp.co.rakuten.travel.framework.app.ios.IosImpl;
import jp.co.rakuten.travel.framework.browser.BrowserFactory;
import jp.co.rakuten.travel.framework.browser.BrowserType;
import jp.co.rakuten.travel.framework.configuration.Controller.ControllerType;
import jp.co.rakuten.travel.framework.configuration.Equipment.EquipmentType;
import jp.co.rakuten.travel.framework.database.DictionaryControllerImpl;
import jp.co.rakuten.travel.framework.database.DictionaryDatabaseImpl;
import jp.co.rakuten.travel.framework.database.ReservationDatabaseController;
import jp.co.rakuten.travel.framework.database.ReservationDatabaseImpl;
import jp.co.rakuten.travel.framework.database.SlmDatabaseController;
import jp.co.rakuten.travel.framework.database.SlmDatabaseImpl;
import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.page.AndroidControllerImpl;
import jp.co.rakuten.travel.framework.page.IosControllerImpl;
import jp.co.rakuten.travel.framework.page.WebControllerImpl;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import jp.co.rakuten.travel.framework.rest.HttpImpl;
import jp.co.rakuten.travel.framework.rest.RConnectRestControllerImpl;
import jp.co.rakuten.travel.framework.rest.RestControllerImpl;
import jp.co.rakuten.travel.framework.utility.Utility;

public final class ConfigurationFactory
{
    protected static TestLogger LOG = (TestLogger)TestLogger.getLogger( ConfigurationFactory.class );

    public static final Equipment equipment( EquipmentType type )
    {
        if( Configuration.instance().hasEquipment( type ) )
        {
            return Configuration.instance().equipment( type );
        }

        switch( type )
        {
        case TRAVEL_ANDROID:
            return new AndroidImpl();
        case TRAVEL_IOS:
            return new IosImpl();
        case BROWSER:
            return getBrowser();
        case HTTP:
            return new HttpImpl();
        case SLM_DB:
            return new SlmDatabaseImpl();
        case RESERVATION_DB:
            return new ReservationDatabaseImpl();
        case DICT_DB:
            return new DictionaryDatabaseImpl();
        default:
            break;
        }

        throw new NotImplementedException( "Equipment Type NOT supported " + type );
    }

    public static final Controller controller( ControllerType type )
    {
        if( Configuration.instance().hasController( type ) )
        {
            return Configuration.instance().controller( type );
        }

        switch( type )
        {
        case WEB:
            try
            {
                return new WebControllerImpl( new URL( TestApiObject.instance().get( TestApiParameters.API_URL ) ) );
            }
            catch( MalformedURLException e )
            {
                throw new NotImplementedException( "Exception found from MalformedURLException " + type + " with URL " + TestApiObject.instance().get( TestApiParameters.API_URL ) );
            }
        case REST:
            return new RestControllerImpl();
        case REST_RCONNECT:
            return new RConnectRestControllerImpl();
        case ANDROID:
            return new AndroidControllerImpl();
        case IOS:
            return new IosControllerImpl();
        case SLM_DB:
            return new SlmDatabaseController();
        case RESERVATION_DB:
            return new ReservationDatabaseController();
        case DICT_DB:
            return new DictionaryControllerImpl();
        default:
            break;
        }
        throw new NotImplementedException( "Controller Type NOT supported " + type );
    }

    private static Equipment getBrowser()
    {
        /**
         * get browser settings
         */
        BrowserType browserType = Utility.getEnum( TestApiObject.instance().get( TestApiParameters.API_BROWSER ), BrowserType.class );
        Equipment browser = null;
        switch( browserType )
        {
        case ANDROID_EMULATOR:
        case ANDROID_PHONE:
        case ANDROID_TABLET:
        case IOS_SIMULATOR:
        case IOS_PHONE:
        case IOS_TABLET:
        case CHROME:
        case FIREFOX:
        case INTERNET_EXPLORER:
        case SAFARI:
        case MOBILE_SAFARI:
        case FEATURE_PHONE:
        case MOBILE_FIREFOX:
        case MOBILE_CHROME:
        case PHANTOMJS:
            try
            {
                InputStream pacFileStream = ConfigurationFactory.class.getClassLoader().getResourceAsStream( TestApiObject.instance().get( TestApiParameters.API_FILE_PROXY ) );

                if( pacFileStream == null )
                {
                    throw new FileNotFoundException( "Pac file not found : " + TestApiObject.instance().get( TestApiParameters.API_FILE_PROXY ) );
                }
                // [patch] create temporary file.
                // TODO : After upgarding to Selenium 3.0, convert pac file into
                // appropriate data structure and feed it to browser.
                browser = (Equipment)BrowserFactory.getBrowser( browserType, Utility.createTempFileFromStream( pacFileStream, "pac" ) );
            }
            catch( IOException exception )
            {
                LOG.error( "Error occurred while creating equipment with cause : " + exception.getMessage() );
            }
            break;

        default:
            throw new UnsupportedOperationException( "Browser not supported " + browserType );
        }

        return browser;
    }
}
