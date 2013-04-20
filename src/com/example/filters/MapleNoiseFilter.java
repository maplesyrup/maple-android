package com.example.filters;

import com.jabistudio.androidjhlabs.filter.NoiseFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/** 
 * NoiseFilter adds random noise to an image. 
 * You can specify the amount of noise, whether 
 * it has a uniform or Gaussian distribution and 
 * whether the noise is monochrome or coloured. 
 * This filter is useful for making images look 
 * 'less perfect' or as a starting point for 
 * producing textures.
 * 
 * int amount
 * float density
 * int distribution
 * bool monochrome
 *
 */

public class MapleNoiseFilter extends MapleFilter {
	// just making these up for now...
	private int amount = 5;
	private float density = 3;
	private int distribution = 3;
	private boolean monochrome = true;
	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		//Find the bitmap's width height
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		NoiseFilter filter = new NoiseFilter();
		
		filter.setAmount(amount);
		filter.setDensity(density);
		filter.setDistribution(distribution);
		filter.setMonochrome(monochrome);
		
		//Change int Array into a bitmap
		int[] src = AndroidUtils.bitmapToIntArray(srcBitmap);
		//Applies a filter.
		filter.filter(src, width, height);
		//Change the Bitmap int Array (Supports only ARGB_8888)
		Bitmap dstBitmap = Bitmap.createBitmap(src, width, height, Config.ARGB_8888);
		
		return dstBitmap;
	}

}
