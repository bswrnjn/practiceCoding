package jp.co.rakuten.travel.framework.tools.parsers;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNull;

import java.util.Map;

import jp.co.rakuten.travel.framework.utility.IniParser;
import jp.co.rakuten.travel.framework.utility.IniParser.ConfigKeys;
import jp.co.rakuten.travel.framework.utility.IniParser.ConfigSections;

import org.apache.commons.codec.binary.Base64;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/*
 * IniParser unit tests
 * Unit Test Method Naming Convention : [UnitOfWork_StateUnderTest_ExpectedBehavior]
 */
public class IniParserTests
{
    private static final String USER_SECTION                = "USER";
    private static final String BOX_LINKS_SECTION           = "BOX_LINKS";

    private static final String DEFAULT_USER_KEY            = "default_user";
    private static final String DEFAULT_USER_VALUE          = "username:qatester,password:UkBrdXRlbjIwMTU=";

    private static final String DOMESTIC_HOTEL_KEY          = "domestic_hotel";
    private static final String DOMESTIC_HOTEL_VALUE        = "domestic hotel";

    private static final String DOMESTIC_HOTEL_NEGATIVE_KEY = "domestic_hotel_negative";

    @Mock
    private Ini                 m_mockIni;
    @Mock
    private Section             m_mockUserSection;
    @Mock
    private Section             m_mockBoxLinkSection;

    private IniParser           m_iniParser;

    @BeforeClass
    public void setUp()
    {
        // Arrange
        MockitoAnnotations.initMocks( this );
        m_iniParser = new IniParser();

        // Mock ini file, unit tests will not have dependencies with external data sources
        m_mockIni = new Ini();

        m_mockIni.put( USER_SECTION, DEFAULT_USER_KEY, DEFAULT_USER_VALUE );

        m_mockUserSection = m_mockIni.get( USER_SECTION );

        m_mockIni.put( BOX_LINKS_SECTION, DOMESTIC_HOTEL_KEY, DOMESTIC_HOTEL_VALUE );
        m_mockIni.put( BOX_LINKS_SECTION, DOMESTIC_HOTEL_NEGATIVE_KEY, null );
        m_mockIni.put( BOX_LINKS_SECTION, DEFAULT_USER_KEY, DOMESTIC_HOTEL_VALUE );

        m_mockBoxLinkSection = m_mockIni.get( BOX_LINKS_SECTION );

        Whitebox.setInternalState( m_iniParser, "m_ini", m_mockIni );
    }

    @Test
    public void getTestFirstSectionExpectedSectionFound()
    {
        // Act
        Section sectionFound = m_iniParser.get( ConfigSections.USER );

        // Assert
        assertEquals( sectionFound, m_mockUserSection );
    }

    @Test
    public void getTestLastSectionExpectedSectionFound()
    {
        // Act
        Section sectionFound = m_iniParser.get( ConfigSections.BOX_LINKS );

        // Assert
        assertEquals( sectionFound, m_mockBoxLinkSection );
    }

    @Test
    public void getTestNonExistedSectionExpectedNull()
    {
        // Act & Assert
        assertNull( m_iniParser.get( ConfigSections.UNKNOWN ) );
    }

    @Test
    public void getTestNormalCaseExpectedValueOfSpecifiedKeyAndSection()
    {
        // Act
        String actualResult = m_iniParser.get( ConfigSections.BOX_LINKS, ConfigKeys.DOMESTIC_HOTEL );

        // Assert
        assertEquals( actualResult, DOMESTIC_HOTEL_VALUE );
    }

    @Test
    public void getTestEncodedPasswordExpectedPasswordDecoded()
    {
        // Act
        Map< String, String > user = m_iniParser.getUser( ConfigKeys.DEFAULT_USER );

        // Assert
        Assert.assertEquals( user.get( "username" ), "qatester" );
        Assert.assertEquals( user.get( "password" ), new String( Base64.decodeBase64( "UkBrdXRlbjIwMTU=".getBytes() ) ) );
    }

    @Test
    public void getTestNoValueKeyExpectedNull()
    {
        // Act
        String actualResult = m_iniParser.get( ConfigSections.BOX_LINKS, ConfigKeys.DOMESTIC_HOTEL_NEGATIVE );

        // Assert
        assertNull( actualResult );
    }

    @Test
    public void getTestDuplicatedKeyExpectedFirstKeyFound()
    {
        // Act
        String actualResult = m_iniParser.get( ConfigKeys.DEFAULT_USER );

        // Assert
        assertEquals( actualResult, DEFAULT_USER_VALUE );
    }

    @Test
    public void getTestNonExistedKeyExpectedNull()
    {
        // Act
        String actualResult = m_iniParser.get( ConfigKeys.UNKNOWN );

        // Assert
        assertNull( actualResult );
    }

}
