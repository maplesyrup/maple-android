package com.additt.filters;

import com.jabistudio.androidjhlabs.filter.ExposureFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.additt.maple_android.R;

/** 
 * This filter simulates changing the exposure of an image.
 * 
 * float exposure
 *
 */

public class MapleExposureFilter extends MapleFilter {
	private float exposure = 2.0f;
	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		//Find the bitmap's width height
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		ExposureFilter filter = new ExposureFilter();
		
		filter.setExposure(exposure);
		
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
		return R.drawable.filter_exposure;
	}

	@Override
	public String getName() {
		return "Exposure";
	}

}
