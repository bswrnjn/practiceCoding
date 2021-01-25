package jp.co.rakuten.travel.framework.parameter;

/**
 * Enum with Currency rates to convert from yen to different currency or vice versa
 */
public enum Currency implements Parameters
{
    // United Arab Emirates Dirham
    AED( "United Arab Emirates Dirham", "AED" ),
    // Australian Dollar
    AUD( "Australian Dollar", "AUD" ),
    // Swiss Franc
    CHF( "Swiss Franc", "CHF" ),
    // Chinese Yuan
    CNY( "Chinese Yuan", "CNY" ),
    // Euro
    EUR( "Euro", "€" ),
    // Fijian Dollar
    FJD( "Fijian Dollar", "FJ$" ),
    // British Pound
    GBP( "Pound Sterling", "￡" ),
    // Hong Kong Dollar
    HKD( "Hong Kong Dollar", "HKD" ),
    // Indonesian Rupiah
    IDR( "Indonesian Rupiah", "Rp" ),
    // Indian Rupee
    INR( "Indian Rupee", "Rs." ),
    // Korean Won
    KRW( "Korean Won", "₩" ),
    // Macanese Pataca
    MOP( "Macanese Pataca", "MOP$" ),
    // Malaysian Ringgit
    MYR( "Malaysian Ringgit", "RM" ),
    // New Zealand Dollar
    NZD( "New Zealand Dollar", "$" ),
    // Philippine Peso
    PHP( "Philippine Peso", "P" ),
    // Singapore Dollar
    SGD( "Singapore Dollar", "S$" ),
    // Thai Baht
    THB( "Thai Baht", "THB" ),
    // New Taiwan Dollar
    TWD( "New Taiwan Dollar", "TWD" ),
    // US Dollar
    USD( "US Dollar", "US$" ),
    // Vietnamese Dong
    VND( "Vietnamese Dong", "₫" ),
    // Japanese Yen
    JPY( "Japanese Yen", "JPY" ),
    // Canadian dollar
    CAD( "Canadian dollar", "C$" ),
    // Brazilian real
    BRL( "Brazilian real", "R$" ),
    // Russian ruble
    RUB( "Russian ruble", "RUB" ),
    UNKNOWN( Parameters.UNKNOWN, "unknown" );

    private final String m_name;
    private final String m_symbol;

    Currency( String name, String symbol )
    {
        m_name = name;
        m_symbol = symbol;
    }

    @Override
    public String val()
    {
        return m_name;
    }

    /**
     * This will return symbol of currency
     */
    public String symbol()
    {
        return m_symbol;
    }

    @Override
    public Parameters unknown()
    {
        return UNKNOWN;
    }
}
