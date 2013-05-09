package com.additt.filters;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.additt.maple_android.R;

/** 
 * Returns a copy of the bitmap passed in.
 * 
 * No filter is applied, this is just done to make
 * the option of no filter easier to implement.
 *
 */

public class MapleNoFilter extends MapleFilter {

	
	@Override
	public Bitmap filterBitmap(Bitmap srcBitmap) {
		
		return srcBitmap.copy(Config.ARGB_8888, true);

	}

	@Override
	public int getPreview() {
		return R.drawable.filter_none;
	}

	@Override
	public String getName() {
		return "No Filter";
	}

}
