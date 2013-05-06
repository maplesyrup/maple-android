package com.additt.filters;

import com.additt.maple_android.R;
import com.jabistudio.androidjhlabs.filter.BlurFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/** 
 * This filter blurs an image very slightly 
 * using a 3x3 blur kernel. 
 * 
 * There are no parameters to this filter.
 *
 */

public class MapleBlurFilter extends MapleFilter {

	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		//Find the bitmap's width height
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		BlurFilter filter = new BlurFilter();
		
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
		return R.drawable.filter_blur;
	}

	@Override
	public String getName() {
		return "Blur";
	}

}
