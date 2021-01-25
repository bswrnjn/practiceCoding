package jp.co.rakuten.travel.framework.html;

public enum HtmlTag
{
    HTML, //
    HEAD,
    TITLE,
    BODY,
    IMG,
    FRAME,
    FRAMESET,
    H1,
    H2,
    TABLE,
    THEAD,
    TBODY,
    TR,
    TD,
    TH,
    DIV,
    SCRIPT,
    FONT,
    BR,
    PRE,
    SPAN,
    META,
    A,
    STYLE;

    public String open()
    {
        return enclose( name().toLowerCase() );
    }

    public String open( String params )
    {
        return enclose( name().toLowerCase(), params );
    }

    public String close()
    {
        return encloseEnd( name().toLowerCase() );
    }

    /**
     * "<tag params>"
     * @param params Insert string
     * @return String "&lt;tag params/&gt;"
     */
    public String insert( String params )
    {
        return LEFT + name().toLowerCase() + " " + params + END;
    }

    public static String link( String text, String link )
    {
        return A.open( "href=\"" + link + "\"" ) + text + A.close();
    }

    public static String link( String text, String link, String attrValue )
    {
        return A.open( "href=\"" + link + "\"target=\"" + attrValue + "\"" ) + text + A.close();
    }

    /**
     * <tag>
     * @param tag tag string
     * @return string "&lt;tag&gt;"
     */
    private static String enclose( String tag )
    {
        return LEFT + tag + RIGHT;
    }

    /**
     * <tag params>
     * @param tag Tag string
     * @param params Params string
     * @return string "&lt;tag params&gt;"
     */
    private static String enclose( String tag, String params )
    {
        return LEFT + tag + " " + params + RIGHT;
    }

    /**
     * </tag>
     * @param str Tag String
     * @return String "&lt;/tag&gt;"
     */
    private static String encloseEnd( String str )
    {
        return CLOSE + str + RIGHT;
    }

    private static String LEFT  = "<";
    private static String RIGHT = ">";
    private static String CLOSE = "</";
    private static String END   = "/>";
}
