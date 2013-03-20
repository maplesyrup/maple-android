package com.example.maple_android;

import java.util.ArrayList;
import java.util.Stack;


import android.graphics.Bitmap;
import android.net.Uri;

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

	public Bitmap getCurrentBitmap() {
		return mBitmapStack.peek();
	}

	public Bitmap pushBitmap(Bitmap bitmap) {
		return mBitmapStack.push(bitmap);
	}

	public Bitmap getOriginalBitmap() {
		return mOriginalBitmap;
	}

	public Uri getFileUri() {
		return mFileUri;
	}

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

	public Filters getCurrentFilter() {
		return mFilter;
	}
	
	
	

}
