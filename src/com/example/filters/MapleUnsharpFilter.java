package com.example.filters;

import com.jabistudio.androidjhlabs.filter.UnsharpFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/** 
 * This filter sharpens an image by Unsharp masking, 
 * where a blurred version of the image is subtracted 
 * from the original image.
 * 
 * int amount
 * int threshold
 * float radius
 *
 */

public class MapleUnsharpFilter extends MapleFilter {
	private int amount = 3;
	private int threshold = 2;
	private float radius = 1.0f;
	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		//Find the bitmap's width height
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		UnsharpFilter filter = new UnsharpFilter();
		
		filter.setAmount(amount);
		filter.setThreshold(threshold);
		filter.setRadius(radius);
		
		//Change int Array into a bitmap
		int[] src = AndroidUtils.bitmapToIntArray(srcBitmap);
		//Applies a filter.
		filter.filter(src, width, height);
		//Change the Bitmap int Array (Supports only ARGB_8888)
		Bitmap dstBitmap = Bitmap.createBitmap(src, width, height, Config.ARGB_8888);
		
		return dstBitmap;
	}

}
