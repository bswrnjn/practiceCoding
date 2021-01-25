package jp.co.rakuten.travel.framework.utility;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class UTestUtility
{
    /*
     * removeWhitespace
     */
    @Test( groups =
    { "removeWhitespace" } )
    public void removeWhiteSpace() throws IOException
    {
        Assert.assertTrue( Utility.equalsIgnoreWhitespace( " Text Text Text ", "TextTextText" ), "Remove Space text." );
    }

    @Test( groups =
    { "removeWhitespace" } )
    public void removeMultipleSpace() throws IOException
    {
        Assert.assertTrue( Utility.equalsIgnoreWhitespace( "      Text       Text         ", "TextText" ), "Remove Space at the head of text." );
    }

    @Test( groups =
    { "removeWhitespace" } )
    public void removeFullWidthSpace() throws IOException
    {
        Assert.assertTrue( Utility.equalsIgnoreWhitespace( "　Text　Text　", "TextText" ), "Remove Full-width space" );
    }

    @Test( groups =
    { "removeWhitespace" } )
    public void removeTabSpace() throws IOException
    {
        Assert.assertTrue( Utility.equalsIgnoreWhitespace( "\tText	Text	", "TextText" ), "Remove Tab space" );
    }

    @Test( groups =
    { "removeWhitespace" } )
    public void removeReturnSpace() throws IOException
    {
        Assert.assertTrue( Utility.equalsIgnoreWhitespace( "\rText\rText\r", "TextText" ), "Remove Return space" );
    }

    @Test( groups =
    { "removeWhitespace" } )
    public void removeEnterSpace() throws IOException
    {
        Assert.assertTrue( Utility.equalsIgnoreWhitespace( "\nText\nText\n", "TextText" ), "Remove Enter space" );
    }

    /*
     * equalsIgnoreWhitespace
     */
    @Test( groups =
    { "equalsIgnoreWhitespace" } )
    public void equalsIgnoreSpaceInFirstParam() throws IOException
    {
        Assert.assertTrue( Utility.equalsIgnoreWhitespace( "　Text　Text　", "TextText" ), "Equals Space in First Parameter" );
    }

    @Test( groups =
    { "equalsIgnoreWhitespace" } )
    public void equalsIgnoreSpaceInLastParam() throws IOException
    {
        Assert.assertTrue( Utility.equalsIgnoreWhitespace( "TextText", "　Text　Text　" ), "Equals Space in Last Parameter" );
    }

    @Test( groups =
    { "equalsIgnoreWhitespace" } )
    public void equalsIgnoreSpaceInBoth() throws IOException
    {
        Assert.assertTrue( Utility.equalsIgnoreWhitespace( "　Text　Text　", "　　　　Text　　　　　　Text　　　　　" ), "Equals Space in Both Parameter" );
    }

    @Test( groups =
    { "equalsIgnoreWhitespace" } )
    public void notEqualsCauseCases() throws IOException
    {
        Assert.assertFalse( Utility.equalsIgnoreWhitespace( "　Text　Text　", "　　　　text　　　　　　text　　　　　" ), "No Equals Cause different Cases" );
    }

    /*
     * containsIgnoreWhitespace
     */
    @Test( groups =
    { "containsIgnoreWhitespace" } )
    public void containsIgnoreSpaceFrontSame() throws IOException
    {
        Assert.assertTrue( Utility.containsIgnoreWhitespace( "abc def", "　ab　cd　" ), "Contains Space Text" );
    }

    @Test( groups =
    { "containsIgnoreWhitespace" } )
    public void containsIgnoreSpaceEndSame() throws IOException
    {
        Assert.assertTrue( Utility.containsIgnoreWhitespace( "abc def", "cd ef" ), "Contains Space Text" );
    }

    @Test( groups =
    { "containsIgnoreWhitespace" } )
    public void containsIgnoreSpaceSame() throws IOException
    {
        Assert.assertTrue( Utility.containsIgnoreWhitespace( " abc def ", " ab cd ef " ), "Contains Space Text" );
    }

    @Test( groups =
    { "containsIgnoreWhitespace" } )
    public void notContainsIgnoreSpace() throws IOException
    {
        Assert.assertFalse( Utility.containsIgnoreWhitespace( " abc def ", "abcddd" ), "Contains Space Text" );
    }

    @Test( groups =
    { "containsIgnoreWhitespace" } )
    public void notContainsIgnoreSpaceCauseCases() throws IOException
    {
        Assert.assertFalse( Utility.containsIgnoreWhitespace( " abc def ", " Ab cd " ), "Contains Space Text" );
    }

}
