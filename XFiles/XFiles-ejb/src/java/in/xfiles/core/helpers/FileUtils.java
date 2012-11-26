package in.xfiles.core.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The class contains several useful methods 
 * for working with files and streams.
 * 
 * @author danon
 */
public abstract class FileUtils {
    
    public static final int DEFAULT_STREAM_BUFFER_SIZE = 1024*16;
    
    /**
     * Copy all data from one stream to another.
     * @param is input stream
     * @param os output stream
     * @throws IOException 
     */
    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        if(is != null && os != null) {
            final byte[] buffer = new byte[DEFAULT_STREAM_BUFFER_SIZE];
            int read;
            while((read = is.read(buffer)) >= 0) {
                os.write(buffer, 0, read);
            }
        }
    }
    
}