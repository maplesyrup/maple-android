package com.example.filters;

import com.jabistudio.androidjhlabs.filter.ContrastFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.example.maple_android.R;

/** 
 * This filter changes the brightness and contrast of an image.
 * 
 * float brightness
 * float contrast
 *
 */

public class MapleContrastFilter extends MapleFilter {

	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		//Find the bitmap's width height
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		ContrastFilter filter = new ContrastFilter();
		
		//filter.setBrightness(brightness);
		//filter.setContrast(contrast);
		//filter.setDimensions(w, h);
		
		//Change int Array into a bitmap
		int[] src = AndroidUtils.bitmapToIntArray(srcBitmap);
		//Applies a filter.
		filter.filter(src, width, height);
		//Change the Bitmap int Array (Supports only ARGB_8888)
		Bitmap dstBitmap = Bitmap.createBitmap(src, width, height, Config.ARGB_8888);
		
		return dstBitmap;
	}

	@Override
	public int getPreview() {
		return R.drawable.filter_contrast;
	}

	@Override
	public String getName() {
		return "Contrast";
	}

}
