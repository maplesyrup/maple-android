package com.additt.filters;

import com.jabistudio.androidjhlabs.filter.GrayscaleFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;
import com.additt.maple_android.R;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/** 
 * This filter converts an image to a grayscale image. 
 * To do this it finds the brightness of each pixel 
 * and sets the red, green and blue of the output to 
 * the brightness value. But what is the brightness? 
 * 
 * The simplest answer might be that it is the average 
 * of the RGB components, but that neglects the way in 
 * which the human eye works. The eye is much more sensitive 
 * to green and red than it is to blue, and so we need to 
 * take less acount of the blue and more account of the green. 
 * The weighting used by GrayscaleFilter is: luma = 77R + 151G + 28B
 *
 *There are no parameters to this filter.
 *
 */

public class MapleGrayscaleFilter extends MapleFilter {

	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		//Find the bitmap's width height
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		GrayscaleFilter filter = new GrayscaleFilter();
		
		//Change int Array into a bitmap
		int[] src = AndroidUtils.bitmapToIntArray(srcBitmap);
		//Applies a filter.
		int[] result = filter.filter(src, width, height);		
		//Change the Bitmap int Array (Supports only ARGB_8888)
		Bitmap dstBitmap = Bitmap.createBitmap(result, width, height, Config.ARGB_8888);
		
		return dstBitmap;
	}

	@Override
	public int getPreview() {
		return R.drawable.filter_grayscale;
	}

	@Override
	public String getName() {
		return "Gray Scale";
	}

}
