package jp.co.rakuten.travel.framework.utility;

/**
 * Wrapper to handle phone related queries
 *
 */
public class PhoneNumber
{
    private final String m_phoneNumber;

    public PhoneNumber( String phone )
    {
        m_phoneNumber = phone.replaceAll( "[^\\d.]+", "" );
    }

    /**
     * 
     * @param sub integer array to divide the phone number with separator. e.g {3,4,3} with separator '-' yields 123-4567-890
     * <br> if array is more than the length of the phone number, phone number is returned
     * <br> if array is less than the length of the phone number, truncated phone number is returned
     * @param separator
     * @return
     */
    public String toString( int [] sub, char separator )
    {
        StringBuilder phone = new StringBuilder();
        if( sub.length > m_phoneNumber.length() )
        {
            return phone.toString();
        }

        int index = 0;
        for( int i = 0; i < sub.length; ++i )
        {
            try
            {
                phone.append( m_phoneNumber.substring( index, sub[ i ] + index ) );
                if( sub[ i ] + index < m_phoneNumber.length() && i < sub.length - 1 )
                {
                    phone.append( separator );
                }
            }
            catch( IndexOutOfBoundsException e )
            {
                phone.append( m_phoneNumber.substring( index, m_phoneNumber.length() ) );
                break;
            }
            index += sub[ i ];
        }

        return phone.toString();
    }

    @Override
    public String toString()
    {
        return m_phoneNumber;
    }
}
