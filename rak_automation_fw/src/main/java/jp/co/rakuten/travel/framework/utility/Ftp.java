package jp.co.rakuten.travel.framework.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;

/**
 * Utility to FTP related 
 */
public class Ftp
{
    private static FTPClient m_ftpClient = new FTPClient();

    static Logger            LOG         = Logger.getLogger( Ftp.class );

    /**
     * Set FTP host and password
     * Login in to FTP server
     * Check reply code
     * Set transfer type 
     * @return TRUE when success
     */
    public static boolean setConfiguration()
    {
        LOG.info( "setConfiguration" );

        try
        {
            m_ftpClient.connect( TestApiObject.instance().get( TestApiParameters.API_FTP_HOST ), Integer.valueOf( TestApiObject.instance().get( TestApiParameters.API_FTP_PORT ) ) );
            m_ftpClient.login( TestApiObject.instance().get( TestApiParameters.API_FTP_USER_NAME ), TestApiObject.instance().get( TestApiParameters.API_FTP_PASSWORD ) );
            int replyCode = m_ftpClient.getReplyCode();
            if( !FTPReply.isPositiveCompletion( replyCode ) )
            {
                LOG.warn( "Connected to FTP server failed" );
                return false;
            }
            m_ftpClient.setFileType( Integer.valueOf( TestApiObject.instance().get( TestApiParameters.API_FTP_FILE_TYPE ) ) );
        }
        catch( NumberFormatException | IOException e )
        {
            LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            return false;
        }
        return true;
    }

    /**
     * Download file from FTP Server
     * @param hostName:    FTP server name
     * @param port:        FTP port
     * @param userName:    FTP user name
     * @param password:    FTP user password
     * @param remotePath:  Remote file path name
     * @param fileName:    File name
     * @param localPath:   File save path
     * @return TRUE when download successfully
     */
    public static boolean downloadFile( String fileName )
    {
        LOG.info( "Download file" );

        try
        {
            if( !setConfiguration() )
            {
                LOG.warn( "Please check the FTP server: " + TestApiObject.instance().get( TestApiParameters.API_FTP_HOST ) );
            }
            m_ftpClient.changeWorkingDirectory( TestApiObject.instance().get( TestApiParameters.API_FTP_REMOTE_FILE_PATH ) );
            FTPFile [] ftpFiles = m_ftpClient.listFiles();
            for( FTPFile file : ftpFiles )
            {
                if( fileName.equals( file.getName() ) )
                {
                    File localFile = new File( TestApiObject.instance().get( TestApiParameters.API_FTP_LOCAL_FILE_PATH ) + File.separator + file.getName() );
                    OutputStream os = new FileOutputStream( localFile );
                    m_ftpClient.retrieveFile( file.getName(), os );
                    os.close();
                }
            }
            m_ftpClient.logout();
        }

        catch( IOException e )
        {
            LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage(), e );
            return false;
        }

        return true;

    }
}
