package jp.co.rakuten.travel.framework.utility;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByLinkText;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By.ByPartialLinkText;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.support.ByIdOrName;

import io.appium.java_client.MobileBy.ByAccessibilityId;

/**
 * Used in a way to search properly for an element 
 */
public enum ByType
{
    BY_ACCESSIBILITY_ID( ByAccessibilityId.class ), //
    BY_CLASS_NAME( ByClassName.class ), //
    BY_CSS_SELECTOR( ByCssSelector.class ), //
    BY_ID( ById.class ), //
    BY_ID_OR_NAME( ByIdOrName.class ), //
    BY_LINK_TEXT( ByLinkText.class ), //
    BY_NAME( ByName.class ), //
    BY_PARTIAL_LINK_TEXT( ByPartialLinkText.class ), //
    BY_TAG_NAME( ByTagName.class ), //
    BY_X_PATH( ByXPath.class ), //
    BY_DATA_LOCATOR( ByDataLocator.class ), //
    UNKNOWN( By.class ); //

    Class< ? extends By > m_by;

    /**
     * It sets member m_by to specify locator to be used to locate on GUI.
     * @param by -  locator type e.g. ById, ByXpath, ByName etc.
     */
    ByType( Class< ? extends By > by )
    {
        m_by = by;
    }

    public Class< ? extends By > val()
    {
        return m_by;
    }

    public String sval()
    {
        return m_by.getName();
    }

    public static ByType getType( Class< ? extends By > by )
    {
        for( ByType type : values() )
        {
            if( type.val().equals( by ) )
            {
                return type;
            }
        }

        return UNKNOWN;
    }
}
