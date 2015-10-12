package core;

import java.io.File;
import java.io.IOException;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

/*
 * For reading and writing to files.
 */
public class FileHelper {
	private static final String LOG_TAG = null;

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public File getChatStorageDir(String chatName) {
	    // Get the directory for the user's public pictures directory.
		if (isExternalStorageWritable()){
			Log.e("File", "permission to write");
		    File file = new File(Environment.getExternalStoragePublicDirectory(
		            "/Downloads"), chatName);
		    if (!file.mkdirs()) {
		        Log.e(LOG_TAG, "Directory not created");
		    }
		    return file;
		}
		Log.e("File", "No permission to write");
	    return null;
	}
}
