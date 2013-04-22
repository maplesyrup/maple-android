package com.example.filters;

import com.jabistudio.androidjhlabs.filter.GlowFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.example.maple_android.R;

/** 
 * This filter produces a glowing effect 
 * on an image by adding a blurred version of 
 * the image to subtracted from the original image.
 *
 */

public class MapleGlowFilter extends MapleFilter {
	private float amount = 4; 
	private float softness = 5;
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		//Find the bitmap's width height
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		GlowFilter filter = new GlowFilter();
		
		filter.setAmount(amount);
		filter.setRadius(softness);
		
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
		return R.drawable.filter_glow;
	}

	@Override
	public String getName() {
		return "Glow";
	}

}
