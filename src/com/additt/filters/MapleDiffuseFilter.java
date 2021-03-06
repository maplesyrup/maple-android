package com.additt.filters;

import com.jabistudio.androidjhlabs.filter.DiffuseFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.additt.maple_android.R;

/** 
 * This filter creates a diffusing effect by randomly 
 * displacing pixels in an image. You can change the 
 * radius of the diffusion and what to do with pixels 
 * which diffuse off the edges.
 * 
 * float scale
 *
 */

public class MapleDiffuseFilter extends MapleFilter {
	private float scale = 1.0f;
	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		//Find the bitmap's width height
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		DiffuseFilter filter = new DiffuseFilter();
		
		filter.setScale(scale);
		
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
		return R.drawable.filter_diffuse;
	}

	@Override
	public String getName() {
		return "Diffuse";
	}

}
