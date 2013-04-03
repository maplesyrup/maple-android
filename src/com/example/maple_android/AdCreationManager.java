package com.example.maple_android;

import java.util.ArrayList;
import java.util.Stack;


import android.graphics.Bitmap;
import android.net.Uri;
/**
 * This class exists to manage the creation of an ad between activities. It stores
 * various metadata about the ad and also stores a stack of all versions of the ad.
 * 
 * You can access this from any activity by calling
 * getApplication().getAdCreationManager
 *
 */
public class AdCreationManager {
	public enum Filters {
		GAUSSIAN("Gaussian"), POSTERIZE("Posterize"), NONE("None");

		private final String text;

		private Filters(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
	}
	
	private String mCompanyName;
	
	// Array of urls to logo images
	private ArrayList<String> mLogos;
	
	// Stack of bitmap layers
	private Stack<Bitmap> mBitmapStack;
	
	// Origin bitmap
	private Bitmap mOriginalBitmap;
	
	// File path of saved bitmap image
	private Uri mFileUri;
	
	// Current filter applied to image
	private Filters mFilter;
	
	public AdCreationManager(Bitmap currBitmap, Uri fileUri) {
		mBitmapStack = new Stack<Bitmap>();
		mBitmapStack.push(currBitmap);
		
		mOriginalBitmap = currBitmap;
		
		mFileUri = fileUri;
		
		mLogos = new ArrayList<String>();
		
		mCompanyName = null;
		
		mFilter = Filters.NONE;
	}

	/**
	 * Get the current bitmap
	 * 
	 * @return The current bitmap
	 */
	public Bitmap getCurrentBitmap() {
		return mBitmapStack.peek();
	}

	/**
	 * Pushes a bitmap onto the stack of changes that has taken place on the ad.
	 * 
	 * @param bitmap The bitmap to put on the stack.
	 * @return The bitmap that is being pushed onto the stack
	 */
	public Bitmap pushBitmap(Bitmap bitmap) {
		return mBitmapStack.push(bitmap);
	}

	/**
	 * Gets the very original bitmap that was saved from the camera.
	 * 
	 * @return The original bitmap saved from camera
	 */
	public Bitmap getOriginalBitmap() {
		return mOriginalBitmap;
	}

	/**
	 * This will get the file uri where the original bitmap is stored. This way we can save the ad to just one location.
	 * 
	 * @return The file path of the original photo taken by the camera.
	 */
	public Uri getFileUri() {
		return mFileUri;
	}

	/**
	 * Adds a filter to the current bitmap
	 * 
	 * @param strFilter Name of the filter we want to use
	 */
	public void addFilter(String strFilter) {
		MapleFilter mapleFilter = null;

		if (strFilter.equals(Filters.GAUSSIAN.toString())) {
			mapleFilter = new MapleGaussianFilter();
			mFilter = Filters.GAUSSIAN;
		} else if (strFilter.equals(Filters.POSTERIZE.toString())) {
			mapleFilter = new MaplePosterizeFilter();
			mFilter = Filters.POSTERIZE;
		} else if (strFilter.equals(Filters.NONE.toString())) {
			this.pushBitmap(this.getOriginalBitmap());
			mFilter = Filters.NONE;
			return;
		} 
		
		this.pushBitmap(mapleFilter.filterBitmap(this.getCurrentBitmap()));
		
	}

	/**
	 * Gets the current filter so we can set that filter in the spinner.
	 * 
	 * @return The Filter enum that is being applied
	 */
	public Filters getCurrentFilter() {
		return mFilter;
	}
	
	
	

}
