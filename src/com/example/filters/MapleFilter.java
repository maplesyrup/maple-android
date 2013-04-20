package com.example.filters;

import android.graphics.Bitmap;

/**
 * Filters are taken from http://www.jhlabs.com/ip/filters/
 * 
 * They must implement filterBitmap method
 *
 */

public abstract class MapleFilter {
	public abstract Bitmap filterBitmap(Bitmap srcBitmap);
}
