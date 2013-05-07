package com.additt.filters;

import com.jabistudio.androidjhlabs.filter.GammaFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.additt.maple_android.R;

/** 
 * GammaFilter changes the gamma of an image. 
 * Use it to change the brightness of your image. 
 * You can specify the new gamma as a parameter - 
 * values less than one make the image darker, 
 * values greater than one make it lighter.
 * 
 * float gamma - The new gamma value
 *
 */

public class MapleGammaFilter extends MapleFilter {
	private float gamma = 1.0f;
	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		//Find the bitmap's width height
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		GammaFilter filter = new GammaFilter();
		
		/* Can either set one gamma for all channels or
		 * specify a unique gamma for each channel
		 */
		//filter.setGamma(rGamma, gGamma, bGamma);
		filter.setGamma(gamma);
		
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
		return R.drawable.filter_gamma;
	}

	@Override
	public String getName() {
		return "Gamma";
	}

}
