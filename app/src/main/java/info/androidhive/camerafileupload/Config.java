
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

/**
 * File Name: Config.java
 * <p/>
 * This file is used to locate the server address and the local directory where the image is stored
 * <p/>
 * the FILE_UPLOAD_URL contain the server location
 * <p/>
 * the IMAGE_DIRECTOR_NAME contain the folder name where the image stored in the
 * external folder.
 */

public class Config {
    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = "http://www.spkdroid.com/merlin/fileUpload.php";

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Alarms";
}
