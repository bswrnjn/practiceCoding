package jp.co.rakuten.travel.framework.utility;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import jp.co.rakuten.travel.framework.configuration.Configuration;
import jp.co.rakuten.travel.framework.configuration.Equipment.EquipmentType;
import jp.co.rakuten.travel.framework.database.Dictionary;
import jp.co.rakuten.travel.framework.parameter.DictionaryKey;

/**
 * This class is to convert date for multiple language
 */
public class DateConversion
{
    /**
     * Convert date string for multiple language
     * @param date : The date which need to convert
     * @param dateFormatKey : DictionaryKey.name() string
     * @param domain : WebDomain for multiple language
     * @return String : specific language date string
     */
    public static String convert( Date date, DictionaryKey dateFormatKey )
    {
        // FIXME Should using the locale to identify the data key, may change how to use the Dictionary DB

        Dictionary dict = (Dictionary)Configuration.instance().equipment( EquipmentType.DICT_DB );
        // Get the date format string from DB by domain
        String dateFormatStr = dict.get( dateFormatKey );
        if( StringUtils.isEmpty( dateFormatStr ) )
        {
            return "";
        }
        // Return the converted date string
        return Utility.getTime( date, dateFormatStr );
    }
}
