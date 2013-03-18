package com.example.maple_android;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;

/**
 * This class exists to store application global variables
 * that are accessible from any context.
 * 
 * You can access this from any activity by calling
 * getApplication()
 * 
 * We should be careful with how much stuff we keep
 * here, and try to keep it very clean.
 *
 */

public class MapleApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();

		// Initialize the singletons so their instances
		// are bound to the application process.
		initUniversalImageLoader();
	}

	/** Universal Image Loader is a library
	 * that helps with loading images from the web.
	 * It is asynchronous and multithreaded, and offers
	 * caching (plus a bunch of other features). 
	 * 
	 * It needs to
	 * be configured once, and then can be used by calling 
	 * ImageLoader.getInstance()
	 * 
	 * Documentation and code at:
	 * https://github.com/nostra13/Android-Universal-Image-Loader
	 */
	private void initUniversalImageLoader() {
		// Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            // insert other configuration details here
            .build();
        ImageLoader.getInstance().init(config);
		
	}

	

	
}