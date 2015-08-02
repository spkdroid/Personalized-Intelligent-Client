
/*****************************************************************
 * Author: Ravi Tamada
 * <p/>
 * This Code is adapted from the tutorial of the following link
 * <p/>
 * given below.
 * <p/>
 * http://www.androidhive.info/2014/12/android-uploading-camera-image-video-to-server-with-progress-bar/
 ******************************************************************/


package info.androidhive.camerafileupload;


import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

/**
 * Filename: AndroidMultiPartEntity.java
 * <p/>
 * This is the class file that is used to conver the image into byte stream
 * <p/>
 * The image is converted as stream and then it is sent to the server.
 * <p/>
 * The Server will read the byte stream and reconstruct the file in the
 * <p/>
 * server and the kiosk
 */
@SuppressWarnings("deprecation")
public class AndroidMultiPartEntity extends MultipartEntity

{

    private final ProgressListener listener;

    public AndroidMultiPartEntity(final ProgressListener listener) {
        super();
        this.listener = listener;
    }

    public AndroidMultiPartEntity(final HttpMultipartMode mode,
                                  final ProgressListener listener) {
        super(mode);
        this.listener = listener;
    }

    public AndroidMultiPartEntity(HttpMultipartMode mode, final String boundary,
                                  final Charset charset, final ProgressListener listener) {
        super(mode, boundary, charset);
        this.listener = listener;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream, this.listener));
    }

    public static interface ProgressListener {
        void transferred(long num);
    }

    public static class CountingOutputStream extends FilterOutputStream {

        private final ProgressListener listener;
        private long transferred;

        public CountingOutputStream(final OutputStream out,
                                    final ProgressListener listener) {
            super(out);
            this.listener = listener;
            this.transferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            this.transferred += len;
            this.listener.transferred(this.transferred);
        }

        public void write(int b) throws IOException {
            out.write(b);
            this.transferred++;
            this.listener.transferred(this.transferred);
        }
    }
}
