package com.additt.filters;

import com.jabistudio.androidjhlabs.filter.GainFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.additt.maple_android.R;

/** 
 * This filter changes the contrast of an image. 
 * More precisely, it allows you to change the gain 
 * and bias of the colors in the image. Changing 
 * the bias biases colors towards lighter or darker. 
 * changing the gain alters the contrast.
 * 
 * 
 *   float gain - The gain
 *   float bias - The bias
 *
 *
 */

public class MapleGainFilter extends MapleFilter {
	private float bias = 60.0f;
	private float gain = 20.0f;
	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		//Find the bitmap's width height
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		GainFilter filter = new GainFilter();
		
		filter.setBias(bias);
		filter.setGain(gain);
		
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
		return R.drawable.filter_gain;
	}

	@Override
	public String getName() {
		return "Gain";
	}

}
