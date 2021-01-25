package jp.co.rakuten.travel.framework.utility;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

/**
 * Ini parser API, a wrapper to parse information from ini file
 * @version 1.0.0
 * @since 1.0.0
 */
public class IniParser
{
    private Ini m_ini;

    public IniParser()
    {
        m_ini = new Ini();
    }

    public IniParser( String iniFile ) throws IOException
    {
        this();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File( classLoader.getResource( iniFile ).getFile() );
        m_ini.load( new FileReader( file ) );
    }

    public IniParser( InputStream inStream ) throws IOException
    {
        this();
        m_ini.load( inStream );
    }

    /**
     * Return all sections from ini
     * @param section IniSection
     * @return Section
     */
    public Section get( IniSection section )
    {
        return m_ini.get( section.toString() );
    }

    /**
     * Return the value of the specified section and key
     * @param IniSection and IniKey
     * @return String
     * Do not use this method for requesting account information
     */
    public String get( IniSection section, IniKey key )
    {
        Section iniSecton = get( section );

        return iniSecton.get( key.toString() );
    }

    /**
     * Return the first instance value of a key
     * @param key IniKey
     * @return String
     * Do not use this method for requesting account information
     */
    public String get( IniKey key )
    {
        Set< String > sectionNames = m_ini.keySet();

        return m_ini.get( sectionNames.iterator().next() ).get( key.toString() );
    }

    /**
     * Method use for requesting account information
     * @param ConfigKeys.user
     * @return username, password Map
     */
    public Map< String, String > getUser( ConfigKeys user )
    {
        Map< String, String > userInfo = new HashMap<>();
        String userInfoText = get( ConfigSections.USER, user );
        Pattern pattern = Pattern.compile( "username:(.+),password:(.+),easy_id:(.+)" );
        Matcher matcher = pattern.matcher( userInfoText );
        matcher.matches();

        userInfo.put( "username", matcher.group( 1 ) );
        userInfo.put( "password", new String( Base64.decodeBase64( matcher.group( 2 ) ) ) );
        userInfo.put( "easy_id", matcher.group( 3 ) );
        return userInfo;
    }

    /**
     * Section names in ini file
     */
    public enum ConfigSections implements IniSection
    {
        USER,
        BOX_LINKS,
        SUZAKU,
        COMPANY_LOGIN,
        CARD_INFO,
        UNKNOWN;

    }

    /**
     * Key names in ini file
     */
    public enum ConfigKeys implements IniKey
    {
        DEFAULT_USER,
        RACCO_CORPORATE_USER,
        RACCO_PERSONAL_USER,
        EXTRANET_USER,
        EXTRANET_CANCEL_USER,
        SUZAKU_USER,
        DOMESTIC_HOTEL,
        DOMESTIC_HOTEL_NEGATIVE,
        MAUI_DOMESTIC_HOTEL,
        MAUI_DOMESTIC_HOTEL_NEGATIVE,
        DOMESTIC_CAR_RENTAL,
        DOMESTIC_CAR_RENTAL_NEGATIVE,
        RACCO_DOMESTIC_HOTEL,
        RACCO_DOMESTIC_HOTEL_NEGATIVE,
        RACCO_DOMESTIC_RENTAL_CAR,
        RACCO_DOMESTIC_RENTAL_CAR_NEGATIVE,
        HITACHI,
        TOSHIBA,
        MITSUBISHI,
        ANADESK,
        CARD_NAME,
        CARD_PIN_CODE,
        CARD_VALID_YEAR,
        CARD_VALID_MONTH,
        CARD_JCB_NUMBER,
        CARD_VISA_NUMBER,
        CARD_MASTER_NUMBER,
        CARD_AMERICAN_EXPRESS_NUMBER,
        CARD_DINERS_NUMBER,
        CARD_RAKUTEN_NUMBER,

        UNKNOWN;

        @Override
        public String toString()
        {
            return name().toLowerCase();
        }
    }
}
