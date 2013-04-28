package com.example.filters;

import com.jabistudio.androidjhlabs.filter.MaximumFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.example.maple_android.R;

/** 
 * This filter replaces each pixel by the 
 * maximum of the input pixel and its eight 
 * neighbours. Each of the RGB channels is 
 * considered separately.
 *
 */

public class MapleMaximumFilter extends MapleFilter {

	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		//Find the bitmap's width height
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		MaximumFilter filter = new MaximumFilter();
		
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
		return R.drawable.filter_maximum;
	}

	@Override
	public String getName() {
		return "Maximum";
	}

}
