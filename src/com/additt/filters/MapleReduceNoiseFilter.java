package com.additt.filters;

import com.jabistudio.androidjhlabs.filter.ReduceNoiseFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.additt.maple_android.R;

/** 
 * This filter reduces noise in an image. 
 * It compares each pixel with its eight neighbours 
 * and if the pixel is larger or smaller in 
 * value than all eight, replaces it by the 
 * largest or smallest of the neighbours. 
 * This is good for removing single noisy pixels from an image.
 *
 */

public class MapleReduceNoiseFilter extends MapleFilter {

	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		//Find the bitmap's width height
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		ReduceNoiseFilter filter = new ReduceNoiseFilter();
		
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
		return R.drawable.filter_reducenoise;
	}

	@Override
	public String getName() {
		return "Reduce Noise";
	}

}
