package com.example.filters;

import com.jabistudio.androidjhlabs.filter.OilFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.example.maple_android.R;

/** 
 * This filter produces an oil painting effect 
 * as described in the book "Beyond Photography - 
 * The Digital Darkroom". You can specify the 
 * smearing radius. It's quite a slow filter 
 * especially with a large radius.
 * 
 * int levels
 * int range
 *
 */

public class MapleOilFilter extends MapleFilter {
	private int levels = 25;
	private int range = 2;
	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		//Find the bitmap's width height
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		OilFilter filter = new OilFilter();
		
		filter.setLevels(levels);
		filter.setRange(range);
		
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
		return R.drawable.filter_oil;
	}

	@Override
	public String getName() {
		return "Oil";
	}

}
