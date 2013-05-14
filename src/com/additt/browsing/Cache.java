package com.additt.browsing;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

public class Cache {

	private static Cache instance = new Cache();
	private String TAG = "Cache";
	private LruCache<String, Bitmap> mMemoryCache;
	
	/** Initialize image cache that can be accessed
	  * by all activities
	  */
	private Cache() {
		try {
		// Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
    			
    			int nBytes = 0;
    			try {
    				nBytes = bitmap.getByteCount() / 1024;
    			} catch (NoSuchMethodError e) {
    				ByteArrayOutputStream stream = new ByteArrayOutputStream();
    			    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    			    byte[] imageInByte = stream.toByteArray();
        			nBytes = imageInByte.length;
    			}
                return nBytes;
            }
        };

		}catch (Exception e) {
			Log.d(TAG, "Failed to instantiate cache");
		}
	}
	
	
	public static Cache getInstance() {
		return instance;
	}
    
    /**
     * Add bitmap with key (in our case, the url) to the cache
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }
	
    /**
     * Get bitmap from cache based on key
     */
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}
