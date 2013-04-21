package com.example.filters;

import com.jabistudio.androidjhlabs.filter.GaussianFilter;
import com.jabistudio.androidjhlabs.filter.PosterizeFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.example.maple_android.R;

/** This filter posterizes an image by quantizing each 
 * channel to a specified number of levels. This is generally an 
 * ugly way to reduce colours, but is often used as a special effect.
 * 
 *Parameters: int levels - The number of levels to posterize to

 */
public class MaplePosterizeFilter extends MapleFilter {
	private final int NUM_LEVELS = 7;
	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		PosterizeFilter filter = new PosterizeFilter();
		
		filter.setNumLevels(NUM_LEVELS);
		
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
		return R.drawable.filter_posterize;
	}

	@Override
	public String getName() {
		return "Posterize";
	}

}
