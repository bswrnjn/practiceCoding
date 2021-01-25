package jp.co.rakuten.travel.framework.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class Wget
{
    public static File getFile( URL url ) throws IOException
    {
        InputStream inStream = getStream( url );
        File file = new File( FilenameUtils.getName( url.getPath() ) );
        OutputStream outStream = new FileOutputStream( file );
        IOUtils.copy( inStream, outStream );
        outStream.close();
        return file;
    }

    public static InputStream getStream( URL url ) throws IOException
    {
        URLConnection connection = url.openConnection();
        return connection.getInputStream();
    }
}
