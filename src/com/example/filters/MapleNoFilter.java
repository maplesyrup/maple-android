package com.example.filters;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

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

}
