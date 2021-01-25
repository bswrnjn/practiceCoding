package jp.co.rakuten.travel.framework.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jp.co.rakuten.travel.MongoData.Collection;
import jp.co.rakuten.travel.framework.parameter.Currency;
import jp.co.rakuten.travel.framework.parameter.DictionaryKey;
import jp.co.rakuten.travel.framework.parameter.Parameters;
import jp.co.rakuten.travel.framework.parameter.WebDomain;
import jp.co.rakuten.travel.framework.utility.Utility;

/**
 * Domain string database API to get translations
 */
@SuppressWarnings( "rawtypes" )
public class DictionaryImpl implements Dictionary
{
    private static final String     ID      = "_id";
    private static final String     KEYWORD = "Keyword";
    private static WebDomain        m_domain;

    private final Collection< Map > m_collection;

    public DictionaryImpl( Collection< Map > map, WebDomain domain )
    {
        m_collection = map;
        m_domain = domain;
    }

    @Override
    public String get( DictionaryKey key )
    {
        int index = Arrays.asList( m_collection.get( ID, KEYWORD ).values().toArray() ).indexOf( m_domain.name().toLowerCase() );
        return m_collection.get( ID, "^" + key.name() + "$" ).get( String.valueOf( index ) ).toString();
    }

    @Override
    public String get( String key )
    {
        int index = Arrays.asList( m_collection.get( ID, KEYWORD ).values().toArray() ).indexOf( m_domain.name().toLowerCase() );
        key = "^" + key + "$";
        return m_collection.get( ID, key ).get( String.valueOf( index ) ).toString();
    }

    public enum Keyword implements Parameters
    {
        // Breadcrumbs
        HOME( "home" ),
        JAPAN( "japan" ),

        // Prefectures
        AICHI( "aichi" ),
        AKITA( "akita" ),
        AOMORI( "aomori" ),
        CHIBA( "chiba" ),
        EHIME( "ehime" ),
        FUKUI( "fukui" ),
        FUKUOKA( "fukuoka" ),
        FUKUSHIMA( "fukushima" ),
        GIFU( "gifu" ),
        GUNMA( "gunma" ),
        HIROSHIMA( "hiroshima" ),
        HOKKAIDO( "hokkaido" ),
        HYOGO( "hyogo" ),
        IBARAKI( "ibaraki" ),
        ISHIKAWA( "ishikawa" ),
        IWATE( "iwate" ),
        KAGAWA( "kagawa" ),
        KAGOSHIMA( "kagoshima" ),
        KANAGAWA( "kanagawa" ),
        KOCHI( "kochi" ),
        KUMAMOTO( "kumamoto" ),
        KYOTO( "kyoto" ),
        MIE( "mie" ),
        MIYAGI( "miyagi" ),
        MIYAZAKI( "miyazaki" ),
        NAGANO( "nagano" ),
        NAGASAKI( "nagasaki" ),
        NARA( "nara" ),
        NIIGATA( "niigata" ),
        OITA( "oita" ),
        OKAYAMA( "okayama" ),
        OKINAWA( "okinawa" ),
        OSAKA( "osaka" ),
        SAGA( "saga" ),
        SAITAMA( "saitama" ),
        SHIGA( "shiga" ),
        SHIMANE( "shimane" ),
        SHIZUOKA( "shizuoka" ),
        TOCHIGI( "tochigi" ),
        TOKUSHIMA( "tokushima" ),
        TOKYO( "tokyo" ),
        TOTTORI( "tottori" ),
        TOYAMA( "toyama" ),
        WAKAYAMA( "wakayama" ),
        YAMAGATA( "yamagata" ),
        YAMAGUCHI( "yamaguchi" ),
        YAMANASHI( "yamanashi" ),

        // City
        TOKYO_CITY( "tokyoCity" ),
        ADACHI_CITY( "adachiCity" ),
        SHINAGAWA_CITY( "shinagawaCity" ),

        // Spots
        MEIJI_JINGU_STADIUM( "meijiJinguStadium" ),

        // Hotellist
        PRICE_PER_PERSON( "pricePerPerson" ),
        PRICE_PER_ROOM( "pricePerRoom" ),
        CHANGE( "change" ),
        SEARCH( "search" ),
        PAGE_NOT_FOUND( "pageNotFound" ),
        SEARCH_OTHER_HOTELS( "searchOtherHotels" ),

        // Trip Summary
        TRIP_SUMMARY( "tripSummary" ),
        TOTAL( "total" ),
        CHECKIN( "checkin" ),
        CHECKOUT( "checkout" ),
        PLAN( "plan" ),
        ROOM( "room" ),
        MEAL( "meal" ),
        FOR( "for" ),
        SINGLE_ROOM( "singleRoom" ),
        MAIN_BUILDING_SINGLE_ROOM( "mainSingleRoom" ),

        // Price Details
        PRICE_DETAILS( "priceDetails" ),
        PAY_AT_LOCAL_CURRENCY( "payAtLocalCurrency" ),
        TAX_INFO( "taxInfo" ),
        SUBTOTAL( "subtotal" ),
        TAX( "tax" ),
        ADULT( "adult" ),
        TOTAL_RIGHT( "totalRight" ),
        PAYMENT_METHOD( "paymentMethod" ),

        // Support
        LOCAL( "local" ),
        DATE_FORMAT( "dateFormat" ),

        // Plan list page
        BREAKFAST_INCLUDED( "breakfastIncluded" ),
        BREAKFAST_EXCLUDED( "breakfastExcluded" ),
        DINNER_INCLUDED( "dinnerIncluded" ),
        DINNER_EXCLUDED( "dinnerExcluded" ),
        INTERNET( "internet" ),
        SMOKING( "smoking" ),
        NON_SMOKING( "nonSmoking" ),
        TOILET( "toilet" ),
        TAXES_AND_FEES( "taxesAndFees" ),
        MORE_PLAN( "showMore" ),
        CHECK_AVAILABILITY( "checkAvailability" ),
        HOTEL_FEATURE_INFO( "hotelFeature" ),

        // Sort Options
        RECOMMENDED_SORT( "recommended" ),
        DECREASING_GUEST_RATING( "guestRating" ),
        DECREASING_HOTEL_RATING( "hotelRating" ),
        INCREASING_PRICE( "priceLowToHigh" ),
        DECREASING_PRICE( "priceHighToLow" ),

        // Hotellist
        DATE_FORMAT_YMD( "dateFormatYmd" ),
        PERIOD( "period" ),
        ROOM_CONDITIONS( "roomConditions" ),
        HOTEL_SEARCH_TOTAL( "hotelSearchTotal" ),
        HOTEL_SEARCH_FROM( "from" ),

        // Search page
        DESTINATION( "destination" ),
        SEARCH_CHECKIN( "checkin" ),
        SEARCH_CHECKOUT( "checkout" ),
        NUM_OF_ROOM( "numOfRoom" ),
        NUM_OF_ROOMS( "numOfRooms" ),
        NUM_OF_ADULT( "numOfAdult" ),
        NUM_OF_ADULTS( "numOfAdults" ),
        NUM_OF_CHILD( "numOfChild" ),
        NUM_OF_CHILDREN( "numOfChildren" ),
        WITH_BEDS( "withBed" ),
        NO_BED( "withoutBed" ),

        // Cancellation
        CANCELED_MESSAGE( "cancelMsg" ),

        // Currency
        CURRENCY( "currency" ),

        // TopPage keywords
        AREA( "area" ),
        STATION( "station" ),
        LANDMARK( "landmark" ),
        SPA( "spa" ),
        AIRPORT( "airport" ),

        // HOTSPRING
        IWAKURA( "iwakura" ),

        // Point
        POINTS( "points" ),
        CITY( "city" ),

        // Train Station
        TOKYO_STATION( "tokyoStation" ),
        SHINANOMACHI_STATION( "shinanomachiStation" ),
        ROPPONGI_STATION( "roppongiStation" ),

        // Date Format for input textbox
        DATE_FORMAT_INPUT( "dateFormatInput" ),

        // Airport
        TOKYO_HANEDA( "tokyoHaneda" ),
        UNKNOWN( Parameters.UNKNOWN );

        private final String m_val;

        Keyword( String val )
        {
            m_val = val;
        }

        @Override
        public final String val()
        {
            return m_val;
        }

        @Override
        public Parameters unknown()
        {
            return UNKNOWN;
        }

    }

    /**
     * Get the prefectures string with different domain
     * @return String prefectures list string
     */
    public static List< Keyword > getPrefectures()
    {
        List< Keyword > pre = new ArrayList< Keyword >();
        pre.add( Keyword.HOKKAIDO );
        pre.add( Keyword.AOMORI );
        pre.add( Keyword.IWATE );
        pre.add( Keyword.MIYAGI );
        pre.add( Keyword.AKITA );
        pre.add( Keyword.YAMAGATA );
        pre.add( Keyword.FUKUSHIMA );
        pre.add( Keyword.IBARAKI );
        pre.add( Keyword.TOCHIGI );
        pre.add( Keyword.GUNMA );
        pre.add( Keyword.SAITAMA );
        pre.add( Keyword.CHIBA );
        pre.add( Keyword.TOKYO );
        pre.add( Keyword.KANAGAWA );
        pre.add( Keyword.NIIGATA );
        pre.add( Keyword.TOYAMA );
        pre.add( Keyword.ISHIKAWA );
        pre.add( Keyword.FUKUI );
        pre.add( Keyword.YAMANASHI );
        pre.add( Keyword.NAGANO );
        pre.add( Keyword.GIFU );
        pre.add( Keyword.SHIZUOKA );
        pre.add( Keyword.AICHI );
        pre.add( Keyword.MIE );
        pre.add( Keyword.SHIGA );
        pre.add( Keyword.KYOTO );
        pre.add( Keyword.OSAKA );
        pre.add( Keyword.HYOGO );
        pre.add( Keyword.NARA );
        pre.add( Keyword.WAKAYAMA );
        pre.add( Keyword.TOTTORI );
        pre.add( Keyword.SHIMANE );
        pre.add( Keyword.OKAYAMA );
        pre.add( Keyword.HIROSHIMA );
        pre.add( Keyword.YAMAGUCHI );
        pre.add( Keyword.TOKUSHIMA );
        pre.add( Keyword.KAGAWA );
        pre.add( Keyword.EHIME );
        pre.add( Keyword.KOCHI );
        pre.add( Keyword.FUKUOKA );
        pre.add( Keyword.SAGA );
        pre.add( Keyword.NAGASAKI );
        pre.add( Keyword.KUMAMOTO );
        pre.add( Keyword.OITA );
        pre.add( Keyword.MIYAZAKI );
        pre.add( Keyword.KAGOSHIMA );
        pre.add( Keyword.OKINAWA );

        return pre;
    }

    /**
     * Gets number of adults
     * @param adults
     * @return JP : 大人1人, EN : 1Adult
     */
    @Override
    public String getNumberOfAdults( int adults )
    {
        String adultRef = "";

        switch( m_domain )
        {
        case JAPAN:
            adultRef = "大人" + adults + "人";
            break;
        default:
            adultRef = adults > 1 ? adults + "Adults" : adults + "Adult";
            break;
        }
        return adultRef;
    }

    /**
     * Gets number of children
     * @param children
     * @return JP : 子供0人, EN : 1Child
     */
    @Override
    public String getNumberOfChildren( int children )
    {
        String childRef = "";

        switch( m_domain )
        {
        case JAPAN:
            childRef = "子供" + children + "人";
            break;
        default:
            childRef = children > 1 ? children + "Children" : children + "Child";
            break;
        }

        return childRef;
    }

    /**
     * Gets number of rooms
     * @param room
     * @return JP : 1部屋, EN : 1Room
     */
    @Override
    public String getNumberOfRooms( int rooms )
    {
        String roomCountRef = "";

        switch( m_domain )
        {
        case JAPAN:
            roomCountRef = rooms + "部屋";
            break;
        default:
            roomCountRef = rooms > 1 ? rooms + "rooms" : rooms + "Room";
            break;
        }

        return roomCountRef;
    }

    /**
     * Get the keyword for search by different domain
     * @param domain Webdomain
     * @param prefecture Keyword prefecture
     * @param keywordCategory Keyword category
     * @return String keyword
     */
    public static String getKeyword( WebDomain domain, String keywordResults, Keyword prefecture )
    {
        Map< Keyword, Object > keywordMap = Utility.generateMap( keywordResults, Keyword.class );
        return keywordMap.get( prefecture ).toString();
    }

    /**
     * Get the room capacity string with different domain
     * @param WebDomain Domain
     * @param Int Room capacity
     * @return String Room capacity string
     */
    public static String occupancyMax( WebDomain domain, int roomCapacity )
    {
        String str = "";
        switch( domain )
        {
        case COM:
        case SINGAPORE:
        case MALAYSIA:
            str = "Up to " + roomCapacity + " people";
            break;
        case TAIWAN:
        case CHINA:
        case HONGKONG:
            str = "最多" + roomCapacity + "人";
            break;
        case KOREA:
            str = "최대 " + roomCapacity + " 명";
            break;
        case INDONESIA:
            str = "Hingga " + roomCapacity + " orang";
            break;
        case THAILAND:
            str = "สูงสุดไม่เกิน " + roomCapacity + " คน";
            break;
        case FRANCE:
            str = "Jusqu'à " + roomCapacity + " personnes";
            break;

        case JAPAN:
        default:
            break;

        }

        return str;

    }

    /**
     * Get the earn point string with different domain
     * @param WebDomain Domain
     * @param Int point
     * @return String Room capacity string
     */
    public static String earnPoint( WebDomain domain, int pointRate )
    {
        String str = "";
        switch( domain )
        {
        case TAIWAN:
            str = "獲得" + pointRate + "x點數!";
            break;
        case COM:
        case SINGAPORE:
        case MALAYSIA:
        case CHINA:
        case HONGKONG:
        case KOREA:
        case INDONESIA:
        case THAILAND:
        case FRANCE:
        case JAPAN:
        default:
            break;

        }

        return str;
    }

    /**
     * Get the room capacity string with different domain
     * @param WebDomain Domain
     * @param Int Room capacity
     * @return String Room capacity string
     */
    public static String resultCount( WebDomain domain, int start, int end, int total )
    {
        String str = "";
        switch( domain )
        {
        case COM:
        case SINGAPORE:
        case MALAYSIA:
            str = start + " - " + end + " of " + total + " Results";
            break;
        case TAIWAN:
        case HONGKONG:
            str = start + "  -  " + end + "共" + total + "個結果";
            break;
        case CHINA:
            str = start + "  -  " + end + "共" + total + "个结果";
            break;
        case KOREA:
            str = "예약 가능 숙소 " + total + "개 중 " + start + "  – " + end;
            break;
        case INDONESIA:
            str = start + " - " + end + " dari " + total + " Sewa";
            break;
        case THAILAND:
            str = start + " - " + end + " จากที่พัก" + total + " รายการ";
            break;
        case FRANCE:
            str = start + " - " + end + "  sur " + total + " locations";
            break;

        case JAPAN:
        default:
            break;

        }

        return str;
    }

    /**
     * Get the guest count string with different domain
     * @param WebDomain Domain
     * @param Int Adult count
     * @param Int Children count
     * @param Int Room count
     * @return String Guest count string
     */
    public static String guestCount( WebDomain domain, int adult, int children, int room )
    {
        int totalGuest = adult + children;
        String str = "";
        switch( domain )
        {
        case COM:
        case SINGAPORE:
        case MALAYSIA:
            String guestStr = totalGuest + (totalGuest > 1 ? "guests" : "guest");
            String adultStr = adult + (adult > 1 ? "adults" : "adult");
            String childrenStr = children + (children > 1 ? "children" : "child");
            String roomStr = room + (room > 1 ? "rooms" : "room");
            str = guestStr + "(" + adultStr + (children > 0 ? childrenStr : "") + " )" + roomStr;
            break;
        case TAIWAN:
        case HONGKONG:
            str = totalGuest + "人(" + adult + "成人" + (children > 0 ? children + "兒童" : "") + ")" + room + "間客房";
            break;
        case CHINA:
            str = totalGuest + "人(" + adult + "成人" + (children > 0 ? children + "儿童" : "") + ")" + room + "间客房";
            break;
        case KOREA:
            str = totalGuest + "명(" + adult + "성인" + (children > 0 ? children + "어린이" : "") + ")" + room + "객실";
            break;
        case INDONESIA:
            str = totalGuest + "Orang(" + adult + "Dewasa" + (children > 0 ? children + "Anak" : "") + ")" + room + "Kamar";
            break;
        case THAILAND:
            str = totalGuest + "คน(" + adult + "ผู้ใหญ่ " + (children > 0 ? children + "เด็ก" : "") + ")" + room + "ห้อง";
            break;
        case FRANCE:
            // FIXME This is a temporary compromise to bypass this production bug.
            guestStr = totalGuest + (totalGuest > 1 ? "Personnes" : "Personne");
            adultStr = adult + (adult > 1 ? "Adultes" : "Adulte");
            childrenStr = children + (children > 1 ? "Enfants" : "Enfant");
            roomStr = room + (room > 1 ? "Chambres" : "Chambre");
            str = guestStr + "(" + adultStr + (children > 0 ? childrenStr : "") + " )" + roomStr;
            break;

        case JAPAN:
        default:
            break;

        }

        return str;
    }

    /**
     * Get the room count string with different domain
     * @param WebDomain Domain
     * @param Int Room count
     * @return String Room count string
     */
    public static String roomCount( WebDomain domain, int room )
    {
        String roomcount = "";
        switch( domain )
        {
        case COM:
        case SINGAPORE:
        case MALAYSIA:
            roomcount = room + (room > 1 ? " Rooms" : " Room");
            break;
        case TAIWAN:
        case HONGKONG:
            roomcount = room + " 間客房";
            break;
        case CHINA:
            roomcount = room + " 间客房";
            break;
        case KOREA:
            roomcount = room + "실";
            break;
        case INDONESIA:
            roomcount = room + " Kamar";
            break;
        case THAILAND:
            roomcount = room + " ห้อง";
            break;
        case FRANCE:
            roomcount = room + (room > 1 ? " chambres" : " chambre");
            break;

        case JAPAN:
        default:
            break;

        }

        return roomcount;
    }

    /**
     * Get the adult count string with different domain
     * @param WebDomain Domain
     * @param Int Adult count
     * @return String Adult count string
     */
    public static String adultCount( WebDomain domain, int adult )
    {
        String adultcount = "";
        switch( domain )
        {
        case COM:
        case SINGAPORE:
        case MALAYSIA:
            adultcount = adult + (adult > 1 ? " Adults" : " Adult");
            break;
        case TAIWAN:
        case HONGKONG:
            adultcount = adult + " 位成人";
            break;
        case CHINA:
            adultcount = adult + " 名成人";
            break;
        case KOREA:
            adultcount = "어른 " + adult + "인";
            break;
        case INDONESIA:
            adultcount = adult + " Dewasa";
            break;
        case THAILAND:
            adultcount = "ผู้ใหญ่ " + adult + " คน";
            break;
        case FRANCE:
            adultcount = adult + (adult > 1 ? " adultes" : " adulte");
            break;

        case JAPAN:
        default:
            break;

        }

        return adultcount;
    }

    /**
     * Get the child count string with different domain
     * @param WebDomain Domain
     * @param Int Child count
     * @return String Child count string
     */
    public static String childCount( WebDomain domain, int child )
    {
        String childcount = "";
        switch( domain )
        {
        case COM:
        case SINGAPORE:
        case MALAYSIA:
            childcount = child + (child > 1 ? " Children" : " Child");
            break;
        case TAIWAN:
        case HONGKONG:
            childcount = child + " 位兒童";
            break;
        case CHINA:
            childcount = child + " 名儿童";
            break;
        case KOREA:
            childcount = "어린이 " + child + " 인";
            break;
        case INDONESIA:
            childcount = child + " Anak";
            break;
        case THAILAND:
            childcount = "เด็ก " + child + " คน";
            break;
        case FRANCE:
            childcount = child + (child > 1 ? " enfants" : " enfant");
            break;

        case JAPAN:
        default:
            break;

        }

        return childcount;
    }

    /**
     * Get the From Price string with different domain
     * @param WebDomain Domain
     * @param Currency
     * @param String Total Foreign Price
     * @return String From Price string
     */
    // FIXME - to be called through m_dict later or left as it is
    @Deprecated
    public static String fromPrice( WebDomain domain, Currency currency, String price )
    {
        String fromPrice = "";
        switch( domain )
        {
        case COM:
            fromPrice = "from " + currency.symbol() + " " + price;
            break;
        case SINGAPORE:
        case MALAYSIA:
            fromPrice = currency.symbol() + " " + price;
            break;
        case TAIWAN:
        case HONGKONG:
        case CHINA:
        case KOREA:
        case INDONESIA:
        case THAILAND:
        case FRANCE:
            fromPrice = currency.symbol() + " " + price + "～";
            break;
        case JAPAN:
        default:
            break;
        }

        return fromPrice;
    }

    /**
     * Get the Total Price string with different domain
     * @param WebDomain Domain
     * @param Currency
     * @param String Total Foreign Price
     * @return String Total Price string
     */
    public static String totalPrice( WebDomain domain, Currency currency, String price )
    {
        String totalPrice = "";
        switch( domain )
        {
        case COM:
        case SINGAPORE:
        case MALAYSIA:
        case INDONESIA:
        case FRANCE:
            totalPrice = "Total: " + currency.symbol() + " " + price;
            break;
        case TAIWAN:
        case HONGKONG:
            totalPrice = "預訂總金額: " + currency.symbol() + " " + price;
            break;
        case CHINA:
            totalPrice = "预订总金额: " + currency.symbol() + " " + price;
            break;
        case KOREA:
            totalPrice = "합계: " + currency.symbol() + " " + price;
            break;
        case THAILAND:
            totalPrice = "ทั้งหมด: " + currency.symbol() + " " + price;
            break;
        case JAPAN:
        default:
            break;
        }

        return totalPrice;
    }
}
