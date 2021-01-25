package jp.co.rakuten.travel.framework.utility;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.INPUT;

import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.Parameters;
import jp.co.rakuten.travel.framework.parameter.WebDomain;

public class Utility
{
    protected static TestLogger LOG       = (TestLogger)TestLogger.getLogger( "Utility" );
    /**
     * to be used as suite level reference time
     */
    private static final Date   DATE      = new Date();
    /**
     * to be used as test level reference time
     * <br>make use with resetReferenceTime
     * <br>test level resets this time
     */
    private static Date         s_refDate = DATE;

    /***************************************************************************************
     * Values
     ***************************************************************************************/

    public static final float   EPSILON   = (float)0.00000001;

    /***************************************************************************************
     * File and Directory Related
     ***************************************************************************************/

    private Utility()
    {

    }

    /**
     * Gets the current working directory
     * @return current working directory
     */
    public static String getCurrentDirectory()
    {
        return System.getProperty( "user.dir" );
    }

    /**
     * Fetches the collection of files exclusively from the jar file only, from the specified directory.
     * if directory is null it throws IOException.
     * @param directory
     * @return <code>Collection< File ></code> containing entries of all the available file in 
     * the directory.
     * @throws IOException 
     */
    public static Collection< File > getFilesFromResources( String directory ) throws IOException, URISyntaxException
    {
        Collection< File > fileList = new ArrayList< File >();
        final File jarFile = new File( Utility.class.getProtectionDomain().getCodeSource().getLocation().getPath() );

        if( directory == null )
        {
            throw new IOException( "Directory can not be null" );
        }

        if( jarFile.isFile() )
        { // Run with JAR file
            final JarFile jar = new JarFile( jarFile );
            final Enumeration< JarEntry > entries = jar.entries(); // gives ALL entries in jar

            while( entries.hasMoreElements() )
            {
                JarEntry entry = entries.nextElement();
                if( entry.getName().startsWith( directory ) && !entry.isDirectory() )
                {
                    fileList.add( new File( entry.getName() ) );
                }
            }
            jar.close();
        }
        else
        { // Run with IDE
            final URL url = Utility.class.getClassLoader().getResource( directory );
            if( url != null )
            {
                final File resources = new File( url.toURI() );
                for( File resource : resources.listFiles() )
                {
                    if( resource.isFile() )
                    {
                        fileList.add( resource );
                    }
                }
            }
        }
        return fileList;

    }

    /**
     * Get the content in the specified file and return as InputStream
     * @param filePath
     * @return Content in the file as Stream
     * @throws IOException
     * @throws URISyntaxException
     */
    public static InputStream getResource( String filePath ) throws IOException, URISyntaxException
    {
        InputStream stream = Utility.class.getClassLoader().getResourceAsStream( filePath );

        if( stream == null )
        {
            throw new FileNotFoundException( "no file found for : " + filePath );
        }

        return stream;

    }

    /**
     * Create a temporary file in the temporary directory of your system.
     * Also deletes the file when all the references of the file is closed.
     * @param java.io.InputStream
     * @param fileNamePrefix : String, with which file name will start
     * @return Temporary java.io.File object
     * @throws IOException
     */
    public static File createTempFileFromStream( InputStream inputStream, String fileNamePrefix ) throws IOException
    {
        final File tempFile = File.createTempFile( fileNamePrefix, null );
        tempFile.deleteOnExit();
        try( FileOutputStream fileOutputStream = new FileOutputStream( tempFile ) )
        {
            IOUtils.copy( inputStream, fileOutputStream );
        }
        return tempFile;
    }

    /***************************************************************************************
     * DATE Related
     ***************************************************************************************/

    /**
     * @param fromDate From date 
     * @param toDate To date
     * @return Duration days
     */
    public static int getDays( Date fromDate, Date toDate )
    {
        return (int) ( (getEndOfDay( toDate ).getTime() - getEndOfDay( fromDate ).getTime()) / (1000 * 60 * 60 * 24));
    }

    public static boolean isValidDate( String ref, String format )
    {
        SimpleDateFormat df = new SimpleDateFormat( format );
        try
        {
            df.parse( ref );
            return true;
        }
        catch( ParseException e )
        {
            return false;
        }
    }

    public static boolean hasValidDate( String ref, String format )
    {
        SimpleDateFormat df = new SimpleDateFormat( format );
        try
        {
            df.parse( ref );
            return true;
        }
        catch( ParseException e )
        {
            return false;
        }
    }

    public static Date getEndOfDay( Date date )
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( date );
        calendar.set( Calendar.HOUR_OF_DAY, 23 );
        calendar.set( Calendar.MINUTE, 59 );
        calendar.set( Calendar.SECOND, 59 );
        calendar.set( Calendar.MILLISECOND, 999 );
        return calendar.getTime();
    }

    public static Date getStartOfDay( Date date )
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( date );
        calendar.set( Calendar.HOUR_OF_DAY, 0 );
        calendar.set( Calendar.MINUTE, 0 );
        calendar.set( Calendar.SECOND, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );
        return calendar.getTime();
    }

    /**
     * Adjusts the Date object forward or backward in number of days
     * @param date Input DATE
     * @param days + increments the days, - decrements the days
     * @return date Adjusted Date
     */
    public static Date adjustDays( Date date, int days )
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        cal.add( Calendar.DATE, days );
        return cal.getTime();
    }

    /**
     * Adjusts the Date object forward or backward in specific time
     * @param date Input DATE
     * @param time Time in String 24 hour format "HHmm"
     * @return date Adjusted Date
     */
    public static Date setTime( Date date, String time )
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        try
        {
            cal.set( Calendar.HOUR_OF_DAY, Integer.valueOf( time.substring( 0, 2 ) ) );
            cal.set( Calendar.MINUTE, Integer.valueOf( time.substring( 2 ) ) );
        }
        catch( ArrayIndexOutOfBoundsException | NumberFormatException e )
        {
            // LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
        }
        // cal.set(Calendar.SECOND,0);
        // cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }

    /**
     * With reference to a suite level start time
     * @return Reference time to be used as such
     */
    public static Date getTime()
    {
        return DATE;
    }

    /**
     * With reference to a suite level start time
     * @param format
     * @return Reference time based from input String format
     */
    public static String getTime( String format )
    {
        return getTime( getTime(), format );
    }

    /**
     * With reference to a certain start time
     * @param format Date format
     * @return Reference time based from input SimpleDateFormat format
     */
    public static String getTime( SimpleDateFormat format )
    {
        return getTime( getTime(), format );
    }

    /**
     * With reference to a certain start time
     * @return Reference time to be used as such
     */
    public static Date getReferenceTime()
    {
        return s_refDate;
    }

    /**
     * With reference to a certain start time
     * @param format Date format
     * @return Reference time based from input String format
     */
    public static String getReferenceTime( String format )
    {
        return getTime( getReferenceTime(), format );
    }

    /**
     * With reference to a certain start time
     * @param format Date format
     * @return Reference time based from input SimpleDateFormat format
     */
    public static String getReferenceTime( SimpleDateFormat format )
    {
        return getTime( getReferenceTime(), format );
    }

    /**
     * Resets test case time
     */
    public static void resetReferenceTime()
    {
        s_refDate = new Date();
    }

    /**
     * With reference to a certain start time
     * @return Reference time to be used as such
     */
    public static Date getCurrentTime()
    {
        return new Date();
    }

    /**
     * With reference to a certain start time
     * @param format Date format
     * @return Reference time based from input String format
     */
    public static String getCurrentTime( String format )
    {
        return getTime( getCurrentTime(), format );
    }

    /**
     * With reference to a certain start time
     * @param format Date format
     * @return Reference time based from input SimpleDateFormat format
     */
    public static String getCurrentTime( SimpleDateFormat format )
    {
        return getTime( getCurrentTime(), format );
    }

    public static String getTime( Date date, String format )
    {
        return getTime( date, new SimpleDateFormat( format ) );
    }

    public static String getTime( Date date, SimpleDateFormat format )
    {
        return format.format( date );
    }

    public static long getDuration()
    {
        return getDuration( new Date() );
    }

    public static long getDuration( Date current )
    {
        return current.getTime() - DATE.getTime();
    }

    public static GregorianCalendar getGregorianTime()
    {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime( Utility.getTime() );
        return c;
    }

    public static GregorianCalendar getGregorianReferenceTime()
    {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime( Utility.getReferenceTime() );
        return c;
    }

    /**
     * Wrapper for String to Integer unboxed translation
     * @param val Numerical String to be translated
     * @return integer of translation and 0 if any problem or error occurs
     */
    public static int getInt( String val )
    {
        try
        {
            return Integer.valueOf( val );
        }
        catch( NumberFormatException e )
        {
            // LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            return 0;
        }
    }

    /**
     * Converts a string of boolean values to a array of primitive booleans.
     * eg : "true;false;false;true -> [true, false, false, true]"
     * @param booleanString : String containing boolean eg : "true,false,false,true"
     * @param useDelimiters : String specifying the separator character between booleans
     * @return boolean []
     */
    public static boolean [] parseBooleans( String booleanString, String useDelimiters )
    {
        boolean [] booleans = new boolean [] {};
        if( booleanString != null && booleanString.contains( useDelimiters ) )
        {
            String str[] = booleanString.split( useDelimiters );
            if( str.length > 0 )
            {
                booleans = new boolean [ str.length ];
                for( int index = 0; index < str.length; index++ )
                {
                    booleans[ index ] = Boolean.parseBoolean( StringUtils.trim( str[ index ] ) );
                }
            }
        }
        return booleans;
    }

    /***************************************************************************************
     * STRING Related
     ***************************************************************************************/

    /**
     * Accept the sample string which needs to be converted into camel case.
     * @param sample : String 
     * @param useDelimiters : String[], delimiters which can be used to capitalize letter with in the word 
     * @return
     */
    public static String toCamelCase( String sample, String [] useDelimiters )
    {
        StringBuilder camelCaseStr = new StringBuilder();

        if( StringUtils.isNotEmpty( sample ) )
        {
            sample = sample.toLowerCase();

            if( useDelimiters == null )
            {
                camelCaseStr.append( StringUtils.capitalize( sample ) );
            }

            else if( useDelimiters.length > 0 )
            {
                camelCaseStr.append( sample );
                String pattern = "[^\\s";

                for( String temp : useDelimiters )
                {
                    pattern += temp;
                }
                pattern += "]+";
                Matcher regexMatcher = Pattern.compile( pattern ).matcher( sample );

                while( regexMatcher.find() )
                {
                    camelCaseStr.replace( regexMatcher.start(), regexMatcher.end(), StringUtils.capitalize( regexMatcher.group() ) );
                }
            }
        }
        return camelCaseStr.toString();
    }

    /**
     * Checks if all samples in String Array can be found inside the reference String
     * <br>Case insensitive checking
     * @param ref Reference String to check with
     * @param samples String array that contains the Strings to be checked
     * @return TRUE is check is successful and FALSE otherwise
     */
    public static boolean contains( String ref, String [] samples )
    {
        return contains( ref, samples, false );
    }

    /**
     * Checks if all samples in String Array can be found inside the reference String
     * @param ref Reference String to check with
     * @param samples String array that contains the Strings to be checked
     * @param caseSensitive TRUE is checking is case sensitive and FALSE otherwise
     * @return TRUE is check is successful and FALSE otherwise
     */
    public static boolean contains( String ref, String [] samples, boolean caseSensitive )
    {
        for( String sample : samples )
        {
            if( (caseSensitive ? !ref.contains( sample ) : !ref.toLowerCase().contains( sample.toLowerCase() )) )
            {
                return false;
            }
        }

        return true;
    }

    public static boolean contains( String [] outer, String [] inner )
    {
        return Arrays.asList( outer ).containsAll( Arrays.asList( inner ) );
    }

    public static String [] parseLineBreaks( String ref )
    {
        if( ref.contains( "\r\n" ) )
        {
            return ref.split( "\r\n" );
        }
        else if( ref.contains( "\n" ) )
        {
            return ref.split( "\n" );
        }

        return ref.split( "" );
    }

    /**
     * Generates ALPHANUMERIC characters with length based on length parameters
     * @param length Length of String with random ALPHANUMERIC characters
     * @return randomly generated string
     * @throws IllegalArgumentException Parameter error
     */
    public static String random( int length )
    {
        return random( RandomType.ALPHANUMERIC, length );
    }

    /**
     * Generates characters with length based on length parameters and character combinations based on {@link RandomType}
     * @param type Type of characters based on {@link RandomType}
     * @param length Parameter length
     * @return randomly Generated string
     * @throws IllegalArgumentException Parameter error
     */
    public static String random( RandomType type, int length )
    {
        if( length < 1 )
        {
            throw new IllegalArgumentException( "length < 1: " + length );
        }

        StringBuilder sb = new StringBuilder();
        switch( type )
        {
        case ALPHABET:
            sb = appendLowercase( sb );
            sb = appendUppercase( sb );
            break;
        case NUMERIC:
            sb = appendNumeric( sb );
            break;
        case ALPHANUMERIC:
            sb = appendLowercase( sb );
            sb = appendUppercase( sb );
            sb = appendNumeric( sb );
            break;
        case HIRAGANA:
            sb = appendHiragana( sb );
            break;
        case KATAKANA:
            sb = appendKatakana( sb );
            break;
        }

        char [] buf = new char [ length ];

        char [] symbols = sb.toString().toCharArray();
        Random random = new Random();
        for( int idx = 0; idx < length; ++idx )
        {
            buf[ idx ] = symbols[ random.nextInt( symbols.length ) ];
        }

        return new String( buf );
    }

    private static StringBuilder appendNumeric( StringBuilder sb )
    {
        for( char ch = '0'; ch <= '9'; ++ch )
        {
            sb.append( ch );
        }
        return sb;
    }

    private static StringBuilder appendLowercase( StringBuilder sb )
    {
        for( char ch = 'a'; ch <= 'z'; ++ch )
        {
            sb.append( ch );
        }
        return sb;
    }

    private static StringBuilder appendUppercase( StringBuilder sb )
    {
        for( char ch = 'A'; ch <= 'Z'; ++ch )
        {
            sb.append( ch );
        }
        return sb;
    }

    private static StringBuilder appendHiragana( StringBuilder sb )
    {
        for( char ch = 0x304A; ch <= 0x3062 /* 0x308F 0x3093 */; ++ch ) // excluding ゐ, ゑ, を, ん, and small hiragana
        {
            sb.append( ch );
        }
        return sb;
    }

    private static StringBuilder appendKatakana( StringBuilder sb )
    {
        for( char ch = 0x30A1; ch <= 0x30F6; ++ch )
        {
            switch( ch )
            {
            case 0x30F0: // ヰ
            case 0x30F1: // ヱ
            case 0x30F2: // ヲ
            case 0x30F5: // ヵ
            case 0x30F6: // ヶ
            case 0x30F8: // ヸ
            case 0x30F9: // ヹ
            case 0x30FA: // ヺ
            case 0x30EE: // ヮ
            case 0x30FB: // ・
                continue;
            default:
                break;
            }
            sb.append( ch );
        }
        return sb;
    }

    // /**
    // * Generates age from 18-99
    // * @return
    // */
    // public static int randomAdultAge()
    // {
    // return random( 12, 99 );
    // }

    // /**
    // * Generates age from 0-17
    // * @return
    // */
    // public static int randomChildAge()
    // {
    // return random( 0, 11 );
    // }

    public static int random()
    {
        return ThreadLocalRandom.current().nextInt();
    }

    public static int random( int min, int max )
    {
        // +1 to make inclusive
        return min == max ? min : ThreadLocalRandom.current().nextInt( min, max + 1 );
    }

    /***************************************************************************************
     * ENUM Related
     ***************************************************************************************/

    /**
     * 
     * @param <T> ENUM type
     * @param ref CSV string
     * @param clazz ENUM type
     * @return set ENUM set
     */
    public static < T extends Enum< T > > EnumSet< T > generateSet( String ref, Class< T > clazz )
    {
        EnumSet< T > set = EnumSet.noneOf( clazz );
        if( StringUtils.isEmpty( ref ) )
        {
            return set; // ( better, NPE )
        }
        String [] elements = ref.split( "," );
        for( String element : elements )
        {
            element = element.trim();
            for( T type : EnumSet.allOf( clazz ) )
            {
                if( type.name().equalsIgnoreCase( element ) )
                {
                    set.add( type );
                    break;
                }
            }
            // ( Do we really want to ignore spurious values? )
        }
        return set;
    }

    /**
     * 
     * @param HashMap< T ENUM, Object > filters
     * @param ref CSV string
     * @param enumm ENUM type
     * @return set ENUM set
     */
    public static < T extends Enum< T > > Map< T, Object > generateMap( String ref, Class< T > enumm )
    {
        Map< T, Object > map = new HashMap<>();
        if( StringUtils.isEmpty( ref ) )
        {
            return map; // ( better, NPE )
        }
        String [] elements = ref.split( "," );
        for( String element : elements )
        {
            String keyValuePair[] = element.trim().split( ":" );
            if( keyValuePair.length == 2 )
            {
                for( T type : EnumSet.allOf( enumm ) )
                {
                    if( type.name().equalsIgnoreCase( keyValuePair[ 0 ] ) )
                    {
                        map.put( type, keyValuePair[ 1 ] );
                        break;
                    }
                } // ( Do we really want to ignore spurious values? )
            }
            else if( keyValuePair.length == 1 )
            {// this means only key is specified and value is default i.e null
                for( T type : EnumSet.allOf( enumm ) )
                {
                    if( type.name().equalsIgnoreCase( keyValuePair[ 0 ] ) )
                    {
                        map.put( type, null );
                        break;
                    }
                }
            }
            else
            {
                // illegal argument exception, it has to be a key value pair
                throw new IllegalArgumentException();
            }

        }
        return map;
    }

    /**
     * Finds the ENUM member in which the {@link String} ref is the same as {@link Parameters} name()
     * @param <T> ENUM type
     * @param ref Reference {@link String} array
     * @param clazz ENUM type
     * @return A List of ENUM member of clazz
     */
    public static < T extends Enum< T > & Parameters > List< T > getEnum( String [] ref, Class< T > clazz )
    {
        return getEnum( ref, clazz, true );
    }

    /**
     * Finds the ENUM member in which the {@link String} ref is the same as {@link Parameters} name()
     * @param <T> ENUM type
     * @param ref Reference {@link String} array
     * @param clazz ENUM type
     * @param exact TRUE if equals or FALSE if contains
     * @return A List of ENUM member of clazz
     */
    public static < T extends Enum< T > & Parameters > List< T > getEnum( String [] ref, Class< T > clazz, boolean exact )
    {
        List< T > list = new ArrayList< T >();
        for( String s : ref )
        {
            T param = getEnum( s, clazz, exact );
            if( param.unknown() == param )
            {
                continue;
            }
            list.add( param );
        }
        return list;
    }

    /**
     * Finds the ENUM member in which the {@link String} ref is the same as {@link Parameters} name()
     * @param <T> ENUM type
     * @param ref Reference {@link String}
     * @param clazz ENUM type
     * @return ENUM member of clazz
     */
    public static < T extends Enum< T > & Parameters > T getEnum( String ref, Class< T > clazz )
    {
        return getEnum( ref, clazz, true );
    }

    /**
     * Finds the ENUM member in which the {@link String} ref is the same or contains in {@link Parameters} name()
     * @param <T> ENUM type
     * @param ref Reference {@link String}
     * @param clazz ENUM type
     * @param exact TRUE if equals or FALSE if contains
     * @return ENUM member of clazz
     */
    @SuppressWarnings( "unchecked" )
    public static < T extends Enum< T > & Parameters > T getEnum( String ref, Class< T > clazz, boolean exact )
    {
        boolean found = false;
        for( T type : EnumSet.allOf( clazz ) )
        {
            if( exact && type.name().equalsIgnoreCase( ref ) )
            {
                return type;
            }
            else if( !exact && StringUtils.containsIgnoreCase( ref, type.name() ) )
            {
                return type;
            }
        }
        for( T type : EnumSet.allOf( clazz ) )
        {
            if( !found )
            {
                return (T) ((Parameters)type).unknown();
            }
            else
            {
                break;
            }
        }

        return (T) (new Parameters()
        {
            @Override
            public String val()
            {
                return "";
            }

            @Override
            public Parameters unknown()
            {
                return null;
            }
        }).unknown();
    }

    /**
     * Finds the ENUM member in which the {@link String} ref is the same as {@link Parameters} val()
     * @param <T> ENUM type
     * @param ref Reference {@link String}
     * @param clazz ENUM type
     * @return ENUM member of clazz
     */
    public static < T extends Enum< T > & Parameters > T getEnumVal( String ref, Class< T > clazz )
    {
        return getEnumVal( ref, clazz, true );
    }

    /**
     * Finds the ENUM member in which the {@link String} ref is the same or contains in {@link Parameters} val()
     * @param <T> ENUM type
     * @param ref Reference {@link String}
     * @param clazz ENUM type
     * @param exact TRUE if equals or FALSE if contains
     * @return ENUM member of clazz
     */
    @SuppressWarnings( "unchecked" )
    public static < T extends Enum< T > & Parameters > T getEnumVal( String ref, Class< T > clazz, boolean exact )
    {
        boolean found = false;
        for( T type : EnumSet.allOf( clazz ) )
        {
            String val = ((Parameters)type).val();
            if( StringUtils.isEmpty( val ) )
            {
                continue;
            }
            if( exact && val.equalsIgnoreCase( ref ) )
            {
                return type;
            }
            else if( !exact && ref.toLowerCase().contains( val.toLowerCase() ) )
            {
                return type;
            }
        }
        for( T type : EnumSet.allOf( clazz ) )
        {
            if( !found )
            {
                return (T) ((Parameters)type).unknown();
            }
            else
            {
                break;
            }
        }

        return (T) (new Parameters()
        {
            @Override
            public String val()
            {
                return "";
            }

            @Override
            public Parameters unknown()
            {
                return null;
            }
        }).unknown();
    }

    public static boolean isEmpty( List< ? > list )
    {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty( Map< ?, ? > map )
    {
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty( Set< ? > set )
    {
        return set == null || set.isEmpty();
    }

    public static boolean isEmpty( Object [] array )
    {
        return array == null || array.length == 0;
    }

    public static boolean isNull( Object ... objs )
    {
        for( Object obj : objs )
        {
            if( obj == null )
            {
                return true;
            }
        }

        return false;
    }

    public static String [] toStringArray( List< String > list )
    {
        return list.toArray( new String [ 0 ] );
    }

    public static List< String > toStringList( String [] array )
    {
        return Arrays.asList( array );
    }

    public static String arrayToString( String [] array )
    {
        return Arrays.toString( array );
    }

    public static String listToString( List< String > list )
    {
        return arrayToString( toStringArray( list ) );
    }

    /**
     * Sends keystroke to currently focused window
     * <br>The window MUST be on-focus, and on top for this to work
     * <br>Java has no native implementation of making sure that a keystroke will be sent to a specific non-focus Window (opinion)
     * @param keycode {@link KeyEvent}, {@link MouseEvent} or simple character
     */
    public static void sendkey( int keycode )
    {
        keyPressed( keycode );
        keyReleased( keycode );
    }

    private static DWORD keyPressed( int keycode )
    {
        INPUT ip = new INPUT();

        ip.type = new DWORD( INPUT.INPUT_KEYBOARD );
        ip.input.setType( "ki" );
        ip.input.ki.wScan = new WinDef.WORD( 0 );
        ip.input.ki.time = new WinDef.DWORD( 0 );
        ip.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR( 0 );

        ip.input.ki.wVk = new WinDef.WORD( keycode );
        ip.input.ki.dwFlags = new WinDef.DWORD( 0 );
        return User32.INSTANCE.SendInput( new WinDef.DWORD( 1 ), (INPUT [])ip.toArray( 1 ), ip.size() );
    }

    private static DWORD keyReleased( int keycode )
    {
        INPUT ip = new INPUT();

        ip.type = new DWORD( INPUT.INPUT_KEYBOARD );
        ip.input.setType( "ki" );
        ip.input.ki.wScan = new WinDef.WORD( 0 );
        ip.input.ki.time = new WinDef.DWORD( 0 );
        ip.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR( 0 );

        ip.input.ki.wVk = new WinDef.WORD( keycode );
        ip.input.ki.dwFlags = new WinDef.DWORD( WinUser.KEYBDINPUT.KEYEVENTF_KEYUP );
        return User32.INSTANCE.SendInput( new WinDef.DWORD( 1 ), (INPUT [])ip.toArray( 1 ), ip.size() );
    }

    /**
     * Check the Reference string object if contains the Target string object without every known whitespace
     * @param reference Reference string
     * @param target Target string
     * @return return true when reference contains target regardless whitespace
     */
    public static boolean containsIgnoreWhitespace( String reference, String target )
    {
        if( removeWhitespace( reference ).contains( removeWhitespace( target ) ) )
        {
            return true;
        }
        return false;
    }

    /**
     * Check the string object without every known whitespace if equals
     * @param reference Reference string
     * @param target Target string
     * @return return true when reference equals target regardless whitespace
     */
    public static boolean equalsIgnoreWhitespace( String reference, String target )
    {
        if( removeWhitespace( reference ).equals( removeWhitespace( target ) ) )
        {
            return true;
        }
        return false;
    }

    /**
     * Remove the whitespace in the string object
     * @param string Target string
     * @return return string without whitespace
     */
    private static String removeWhitespace( String string )
    {
        String whitespace_chars = "" /* dummy empty string for homogeneity */
                + "\\u0009" // CHARACTER TABULATION
                + "\\u000A" // LINE FEED (LF)
                + "\\u000B" // LINE TABULATION
                + "\\u000C" // FORM FEED (FF)
                + "\\u000D" // CARRIAGE RETURN (CR)
                + "\\u0020" // SPACE
                + "\\u0085" // NEXT LINE (NEL)
                + "\\u00A0" // NO-BREAK SPACE
                + "\\u1680" // OGHAM SPACE MARK
                + "\\u180E" // MONGOLIAN VOWEL SEPARATOR
                + "\\u2000" // EN QUAD
                + "\\u2001" // EM QUAD
                + "\\u2002" // EN SPACE
                + "\\u2003" // EM SPACE
                + "\\u2004" // THREE-PER-EM SPACE
                + "\\u2005" // FOUR-PER-EM SPACE
                + "\\u2006" // SIX-PER-EM SPACE
                + "\\u2007" // FIGURE SPACE
                + "\\u2008" // PUNCTUATION SPACE
                + "\\u2009" // THIN SPACE
                + "\\u200A" // HAIR SPACE
                + "\\u2028" // LINE SEPARATOR
                + "\\u2029" // PARAGRAPH SEPARATOR
                + "\\u202F" // NARROW NO-BREAK SPACE
                + "\\u205F" // MEDIUM MATHEMATICAL SPACE
                + "\\u3000" // IDEOGRAPHIC SPACE
        ;
        /* A \s that actually works for Java’s native character set: Unicode */
        String whitespace_class = "[" + whitespace_chars + "]";
        return string.replaceAll( whitespace_class + "+", "" );
    }

    /**
     * Get the locale by domain
     * @param domain
     * @return Locale Locale by domain
     */
    public static Locale getLocale( WebDomain domain )
    {
        switch( domain )
        {
        case COM:
        case MALAYSIA:
        case SINGAPORE:
            return Locale.ENGLISH;
        case TAIWAN:
        case HONGKONG:
            return Locale.TRADITIONAL_CHINESE;
        case CHINA:
            return Locale.SIMPLIFIED_CHINESE;
        case FRANCE:
            return Locale.FRENCH;
        case INDONESIA:
            return new Locale( "in" );
        case THAILAND:
            return new Locale( "th" );
        case KOREA:
            return Locale.KOREAN;
        default:
            return Locale.getDefault();

        }
    }

    /**
     * Get error type for reporting.
     * @return ErrorType object with Error type with it. Returns ErrorType.UNKNOWN with Passed test cases.
     */
    public static ErrorType getErrorType()
    {
        ErrorType erType = ErrorType.UNKNOWN;
        boolean found = false;
        for( Pair< String, ErrorType > error : TestLogger.errors() )
        {
            switch( error.second() )
            {
            case NO_TEST_DATA:
            case EXTERNAL_SERVER_ERROR:
            case PROBABLE_BUG:
            case AUTOMATION_ERROR:
            case SERVER_ERROR:
            case PROXY_DOWN:
            case TEST_PARAMETERS_ERROR:
            case TIME_OUT:
            case INPUT_CHECK_ERROR:
            case APPIUM_SERVER_ERROR:
                found = true;
                break;
            default:
                break;
            }
            erType = error.second();
            if( found )
            {
                break;
            }
        }
        return erType;
    }

    /**
     * Converts unicode numbers to UTF-8 numbers
     * for e.g. ８ will be returned as 8.
     * http://www.fileformat.info/info/unicode/category/Nd/list.htm
     * @param str
     * @return String
     */
    public static String fetchUnicodeNumber( String input )
    {
        StringBuilder output = new StringBuilder();
        for( char ch : input.toCharArray() )
        {
            int numeriValue = Character.getNumericValue( ch );
            if( numeriValue > -1 )
            {
                output.append( numeriValue );
            }
        }
        return output.toString();
    }

    /**
     * Converts String to Date
     * for e.g. 2017-06-01 will be returned as Thu Jun 01 00:00:00 GMT 2017.
     * @param input, format
     * @return Date
     */
    public static Date stringToDate( String input, String format )
    {
        DateFormat df = new SimpleDateFormat( format, Locale.ENGLISH );
        Date date = null;
        try
        {
            date = df.parse( input );
            return date;
        }
        catch( ParseException e )
        {
            return date;
        }
    }

    /**
     * Convert String to Date
     * @param input
     * @return
     */
    public static Date stringToDate( String input )
    {
        DateFormat simpleDateFormat = new SimpleDateFormat();
        Date date = null;
        if( !StringUtils.isEmpty( input ) )
        {
            try
            {
                date = (simpleDateFormat.parse( input ));
            }
            catch( ParseException e )
            {
                LOG.error( "String to Date conversion failed" );
            }
        }
        return date;
    }

    /**
     * Convert String to Time
     * @param input
     * @return
     */
    public static Date [] stringToTime( String input )
    {
        DateFormat simpleDateFormat = new SimpleDateFormat( "HH:mm" );
        if( StringUtils.isEmpty( input ) )
        {
            return null;
        }

        List< Date > dateList = new ArrayList< Date >();
        String [] dateStrList = input.split( "," );
        try
        {
            if( input != null || input.equalsIgnoreCase( "," ) )
            {
                for( int i = 0; i < dateStrList.length; i++ )
                {
                    dateList.add( simpleDateFormat.parse( dateStrList[ i ] ) );
                }
            }
        }
        catch( ParseException e )
        {
            LOG.warn( "Check Input Format ! Found " + e.getClass().getSimpleName() );
        }
        catch( NullPointerException e )
        {
            LOG.warn( "Input is Null! Found " + e.getClass().getSimpleName() );
        }
        catch( Exception e )
        {
            LOG.warn( "Input string problem found : " + input );
        }
        return dateList.toArray( new Date [ dateList.size() ] );
    }

    public static < T, E > Set< T > getKeysByValue( Map< T, E > map, E value )
    {
        return map.entrySet().stream().filter( entry -> Objects.equals( entry.getValue(), value ) ).map( Map.Entry::getKey ).collect( Collectors.toSet() );
    }

    /**
     * Converts "," separated String to array of Date
     * @param input
     * @return
     * @throws ParseException
     */
    public static Date [] stringToDates( String input )
    {
        DateFormat simpleDateFormat = new SimpleDateFormat();
        if( StringUtils.isEmpty( input ) )
        {
            return null;
        }

        List< Date > dateList = new ArrayList< Date >();
        String [] dateStrList = input.split( "," );
        try
        {
            if( input != null | input.equalsIgnoreCase( "," ) )
            {
                for( int i = 0; i < dateStrList.length; i++ )
                {
                    dateList.add( simpleDateFormat.parse( dateStrList[ i ] ) );
                }
            }
        }
        catch( ParseException e )
        {
            LOG.warn( "Check Input Format ! Found " + e.getClass().getSimpleName() );
        }
        catch( NullPointerException e )
        {
            LOG.warn( "Input is Null! Found " + e.getClass().getSimpleName() );
        }
        catch( Exception e )
        {
            LOG.warn( "Input string problem found : " + input );
        }
        return (Date [])dateList.toArray();
    }

    /**
     * Create a json mapper
     * @return ObjectMapper
     */
    private static ObjectMapper jsonMapper()
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        AnnotationIntrospector primary = new JaxbAnnotationIntrospector( mapper.getTypeFactory() );
        AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
        AnnotationIntrospectorPair pair = new AnnotationIntrospectorPair( primary, secondary );
        mapper.setAnnotationIntrospector( pair );
        mapper.setSerializationInclusion( Include.NON_NULL );
        return mapper;
    }

    /**
     * Pretty print Json
     * @param message
     * @return String
     */
    public static String printPrettyJson( Object message )
    {
        String result = null;
        try
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jParser = new JsonParser();
            ObjectMapper jaxbMapper = jsonMapper();
            LOG.info( jaxbMapper.writeValueAsString( message ) );
            JsonElement jElement = jParser.parse( jaxbMapper.writeValueAsString( message ) );
            result = gson.toJson( jElement );
        }
        catch( JsonSyntaxException | JsonProcessingException e )
        {
            LOG.warn( "Something went wrong: " + e.getMessage() );
        }

        return result;
    }

    /**
     * Writes only string content to a file.
     * @param fileContent : Text which needs to be written
     * @param filePath : Location on disk i.e File path
     * @param fileName : Name of the file
     * @param isAppend : True if you want to append a file, if file doesn't exists file will be created.
     */
    public static void writeToFile( String fileContent, String filePath, String fileName, boolean isAppend )
    {
        LOG.info( "writeToFile" );

        if( StringUtils.isEmpty( filePath ) || StringUtils.isEmpty( fileName ) )
        {
            LOG.error( "File path or name can not be empty." );
            return;
        }
        try
        {
            File file = new File( filePath, fileName );
            if( !file.isFile() )
            {
                file.createNewFile();
            }
            FileUtils.writeStringToFile( file, fileContent, isAppend );
        }
        catch( IOException e )
        {
            LOG.warn( "Failed to generate api result with error message " + e.getMessage() );
        }
    }

    /**
     * Checks if the file exists in the specified filePath
     * @param filePath the string to the location of the file
     * @return True if the file exists, False if the file does NOT exist
     */
    public static boolean isFileExisted( String filePath )
    {
        File file = new File( filePath );
        if( file.exists() )
        {
            return true;
        }

        return false;
    }

    public static Set< String > commaSeperatedStringToSet( String str )
    {
        return new HashSet<>( Arrays.asList( str.split( "\\s*,\\s*" ) ) );
    }

    /**
     * Returns the minimum value from all the accepted arguments
     * @param lengths : int values
     * @return -1 if no values are passed, else the minimum value
     */
    @SafeVarargs
    public static < T > int getMinFrom( T [] ... arr )
    {
        if( arr.length <= 0 )
        {
            return -1;
        }

        int min = Integer.MAX_VALUE;
        for( int index = 0; index < arr.length; index++ )
        {
            min = Math.min( min, arr[ index ].length );
        }
        return min;
    }

    /**
     * Compare all arguments and return boolean
     * @param lengths
     * @return true if all arguments are equal or nothing and false otherwise
     */
    @SafeVarargs
    public static < T > boolean isLengthEqual( T [] ... arr )
    {
        if( arr == null || arr.length == 0 )
        {
            return false;
        }
        for( int i = 1; i < arr.length; ++i )
        {
            if( arr[ 0 ].length != arr[ i ].length )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the max length of many array
     * @param arr The array which need to get max length
     * @return max length
     */
    @SafeVarargs
    public static < T > int getMaxLength( T [] ... arr )
    {
        int max = 0;
        for( int index = 0; index < arr.length; index++ )
        {
            if( arr[ index ] != null )
            {
                max = Math.max( max, arr[ index ].length );
            }
        }

        return max;
    }

    /**
     * Get the specific value of array
     * @return null / array[index]
     */
    public static < T > T getArrValue( T [] arr, int index )
    {
        if( arr != null && arr.length > index )
        {
            return arr[ index ];
        }
        else
        {
            return null;
        }
    }
}
