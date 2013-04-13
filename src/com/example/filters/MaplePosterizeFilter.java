package com.example.filters;

import com.jabistudio.androidjhlabs.filter.GaussianFilter;
import com.jabistudio.androidjhlabs.filter.PosterizeFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class MaplePosterizeFilter extends MapleFilter {

	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		PosterizeFilter filter = new PosterizeFilter();
		
		//Change int Array into a bitmap
		int[] src = AndroidUtils.bitmapToIntArray(srcBitmap);
		//Applies a filter.
		filter.filter(src, width, height);
		//Change the Bitmap int Array (Supports only ARGB_8888)
		Bitmap dstBitmap = Bitmap.createBitmap(src, width, height, Config.ARGB_8888);
		return dstBitmap;
	}

}
