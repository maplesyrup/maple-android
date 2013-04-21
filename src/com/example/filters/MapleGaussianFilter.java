package com.example.filters;

import com.jabistudio.androidjhlabs.filter.GaussianFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import com.example.maple_android.R;

/** Applies a gaussian blur. Box blur is
 * much faster but doesn't look as good.
 * 
 * parameters - Radius
 *
 */

public class MapleGaussianFilter extends MapleFilter {

	private final float RADIUS = 3.0f;
	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		//Find the bitmap's width height
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		GaussianFilter filter = new GaussianFilter();
		
		filter.setRadius(RADIUS);
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
		return R.drawable.filter_gaussian;
	}

	@Override
	public String getName() {
		return "Gaussian";
	}

}
