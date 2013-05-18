package com.additt.maple_android;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * This class exists to store application global variables that are accessible
 * from any context.
 * 
 * You can access this from any activity by calling getApplication()
 * 
 * We should be careful with how much stuff we keep here, and try to keep it
 * very clean.
 * 
 */

public class MapleApplication extends Application {

	private AdCreationManager mAdCreationManager;
	private User mAppUser;
	// the most recently used picture
	private Uri mFileUri;


	public static final int GREEN = 0xff21ab27;

	// Exception handler for when the program crashes
	private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {

			// we can implement some sort of logging system here
			ex.printStackTrace();
			
			// exit the app			
			System.exit(2);
		}
	};
 

	@Override
	public void onCreate() {
		super.onCreate();

		// setup handler for uncaught exception
		Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);

		// Initialize the singletons so their instances
		// are bound to the application process.
		initUniversalImageLoader();

		// get most recent company data from server
		CompanyData.syncWithServer(this);
	}

	public AdCreationManager getAdCreationManager() {
		return mAdCreationManager;
	}

	/**
	 * Universal Image Loader is a library that helps with loading images from
	 * the web. It is asynchronous and multithreaded, and offers caching (plus a
	 * bunch of other features).
	 * 
	 * It needs to be configured once, and then can be used by calling
	 * ImageLoader.getInstance()
	 * 
	 * Documentation and code at:
	 * https://github.com/nostra13/Android-Universal-Image-Loader
	 */
	private void initUniversalImageLoader() {
		// Create global configuration and initialize ImageLoader with this
		// configuration
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
		// insert other configuration details here
				.build();
		ImageLoader.getInstance().init(config);

	}

	public void initAdCreationManager(Bitmap currBitmap, Uri fileUri) {
		mAdCreationManager = new AdCreationManager(this, currBitmap, fileUri);
	}

	public void setUser(User appUser) {
		mAppUser = appUser;
	}

	public User getUser() {
		return mAppUser;
	}

	/**
	 * Set the file Uri for a picture that was just taken.
	 * 
	 * @param u
	 */
	public void setFileUri(Uri u) {
		mFileUri = u;
	}

	/**
	 * Get the mostly recently taken picture file
	 * 
	 * @return
	 */
	public Uri getFileUri() {
		return mFileUri;
	}
}