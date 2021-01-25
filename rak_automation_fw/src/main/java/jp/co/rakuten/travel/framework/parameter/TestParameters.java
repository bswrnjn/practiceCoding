package jp.co.rakuten.travel.framework.parameter;

public enum TestParameters implements Parameters
{
    ID( "" ), //

    URL( "https://travel.rakuten.co.jp" ),

    /**
     * User related parameters
     */
    USER_TYPE( "default_user" ),

    COMPANY( "" ),

    USERNAME( "taro rakuten" ),

    FIRST_NAME( "taro" ),

    LAST_NAME( "rakuten" ),

    FIRST_NAME_FURIGANA( "タロウ" ),

    LAST_NAME_FURIGANA( "ラクテン" ),

    FIRST_NAME_HRAGANA( "たろう" ),

    LAST_NAME_HRAGANA( "らくてん" ),

    BIRTH_DATE( "06/06/1992" ),

    GENDER( "" ),

    TELEPHONE( "+813-1111-2222" ),

    FAX( "00-0000-0000" ),

    MOBILE_NUMBER( "+8190-1111-2222" ),

    EMERGENCY_NUMBER( "+8190-1111-2222" ),

    EMAIL( "trv-tech-ta-report@mail.rakuten.com" ),

    ZIP_CODE( "158-0094" ),

    ADDRESS( "Rakuten Crimson House, 1-14-1 Tamagawa, Setagaya-ku, Tokyo" ),

    ADDRESS_KANJI( "東京都世田谷区多摩川１－１４－１楽天クリムソンハウス" ),

    ADDRESS_HIRAGANA( "とうきょうとせたがやくたまがわ１－１４－１らくてんくりむそんはうす" ),

    POINTS( "1000000" ),

    COUNTRY( "Japan" ),

    REGION( "Tokyo" ),

    /**
     * onsite_cash,
     * onsite_credit_card,
     * convenience_store,
     * online_credit_card,
     * online_my_page,
     */
    PAYMENT_TYPE( "onsite_cash" ),

    /**
     * User Card Related parameters
     */

    CARD_NAME( "TARO RAKUTEN" ),

    CARD_BRAND( "VISA" ),

    CARD_NUMBER( "" ), // 1111-2222-3333-4444

    CARD_PIN_CODE( "1234" ),

    CARD_VALID_YEAR( "2020" ),

    CARD_VALID_MONTH( "12" ),

    /**
     * Service Info
     */

    /**
     * Default tax value
     */
    TAX( "8" ),
    DOMAIN( "jp" ),

    /** String variable for hotel check in time in format of 24 hour based with 18:00 */
    CHECK_IN_TIME( "18:00" ),

    UNKNOWN( "" );//

    private final String m_val;

    TestParameters( String val )
    {
        m_val = val;
    }

    @Override
    public String val()
    {
        return m_val;
    }

    @Override
    public Parameters unknown()
    {
        return UNKNOWN;
    }
}
