package jp.co.rakuten.travel.framework.browser;

public enum ApiErrorInfo
{
    /**
     * Proxy is down
     */
    PROXY_DOWN( "proxy server" ),

    /**
     * Proxy connection full
     */
    PROXY_REFUSING_CONNECTIONS( "The proxy server is refusing connections" ),

    /**
     * General Server Error Info
     */
    UNKNOWN_SERVER_ERROR( "エラーが発生しました。" ),
    /**
     * ご迷惑をお掛けして誠に申し訳ございません。
     * システムエラーが発生しました
     */
    SYSTEM_ERROR( "システムエラー" ), //

    /**
     * MAUI system error in Booking step 
     */
    MAUI_SYSTEM_ERROR( "An error has occurred." ),

    MAUI_SYSTEM_ERROR_TW( "系統錯誤" ),

    MAUI_SYSTEM_ERROR_CN( "系统错误" ),

    MAUI_SYSTEM_ERROR_KR( "시스템 오류" ),

    /**
     * 申し訳ございません。情報の取得が正しく行われませんでした。
     */
    DATA_RETRIEVAL_ERROR( "情報の取得が正しく行われません" ),
    /**
     * ページを表示できません
     */
    UNABLE_TO_DISPLAY_PAGE( "ページを表示できません" ),
    /**
     * 指定されたページが見つかりません
     */
    UNABLE_TO_FIND_PAGE( "指定されたページが見つかりません" ),
    /**
     * 申し訳ございません。只今アクセスが集中しているため、しばらくした後もう一度予約を行ってください
     */
    SYSTEM_BUSY( "アクセスが集中" ),
    /**
     * サーバ内部でエラーが発生しました
     * エラー ： 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR( "Internal Server Error" ),
    /**
     * Service Temporarily Unavailable
     */
    TEMPORARY_SERVER_ERROR( "Service Temporarily Unavailable" ),
    /**
     * 400 Bad Request
     */
    BAD_REQUEST( "Bad Request" ),
    /**
     * Our service is temporary unavailable. We apologize for any inconveniences caused.
     */
    SERVICE_UNAVAILABLE_COM( "Our service is temporary unavailable. We apologize for any inconveniences caused." ),
    /**
     * 我們的服務暫時停止使用，對此給您帶來不便我們深感抱歉
     */
    SERVICE_UNAVAILABLE_TW( "我們的服務暫時停止使用，對此給您帶來不便我們深感抱歉" ),
    /**
     * 我们的服务暂时不可使用，对此给您带来的不便我们深感抱歉。
     */
    SERVICE_UNAVAILABLE_CN( "我们的服务暂时不可使用，对此给您带来的不便我们深感抱歉。" ),
    /**
     * 서비스를 일시적으로 이용할 수 없습니다
     */
    SERVICE_UNAVAILABLE_KR( "서비스를 일시적으로 이용할 수 없습니다" ),
    /**
     * ไม่สามารถใช้บริการได้ชั่วคราว ขออภัยในความไม่สะดวกที่เกิดขึ้น
     */
    SERVICE_UNAVAILABLE_TH( "ไม่สามารถใช้บริการได้ชั่วคราว ขออภัยในความไม่สะดวกที่เกิดขึ้น" ),
    /**
     * Layanan kami untuk sementara tak tersedia. Kami mohon maaf atas ketidaknyamanan yang timbul.
     */
    SERVICE_UNAVAILABLE_ID( "Layanan kami untuk sementara tak tersedia. Kami mohon maaf atas ketidaknyamanan yang timbul." ),
    /**
     * Notre service est momentanément indisponible. Veuillez nous excuser pour le désagrément.
     */
    SERVICE_UNAVAILABLE_FR( "Notre service est momentanément indisponible. Veuillez nous excuser pour le désagrément." ),
    /**
     * Service Unavailable
     */
    SERVICE_UNAVAILABLE( "Service unavailable" ),
    /**
     * Server Error 503
     */
    SERVICE_TEMPORARILY_UNAVAILABLE( "Sorry, service is temporarily unavailable" ),
    /**
     * Requested Unavailable
     */
    REQUESTED_UNAVAILABLE( "Sorry, the page you requested was unavailable." ),
    /**
     * Text area not filled error
     */
    INPUT_CHECK_ERROR( "以下の項目をご確認" ),

    PASSWORD_ERROR( "Password is mandatory" ),

    /**
     * 認証ができませんでした
     */
    UNAUTHORIZED_ERROR( "401 Unauthorized" ),
    /**
     * 誠に申し訳ございませんが、
     * 一時的に空室カレンダーが表示ができない状態となっております。
     */
    PAGE_LOAD_ERROR( "カレンダーが表示ができない状態" ),

    /**
     * 該当する宿泊プランが取得できません
     * in DP plan list page
     */
    NO_PLAN_LIST_ERROR( "該当する宿泊プランが取得できません" ),

    /**
     * General Server Error Info for Maui Domestic Hotel
     */
    UNKNOWN_SERVER_ERROR_EN( "An error has occurred." ),

    /**
     * General SSL Error Info while connecting to MyPage 
     */
    SSL_CONNECTION_FAILURE( "Secure Connection Failed" ),

    /**
     * Can't booked airline ticket 
     */
    NO_TICKET_CAN_BE_BOOKED( "航空券が予約できませんでした" );

    String m_errorMsg;

    ApiErrorInfo( String errorMsg )
    {
        m_errorMsg = errorMsg;
    }

    public String errorMsg()
    {
        return m_errorMsg;
    }
}
