package com.additt.filters;

import android.graphics.Bitmap;

/**
 * Filters are taken from http://www.jhlabs.com/ip/filters/
 * 
 * They must implement the methods filterBitmap, getName,
 * and getPreview
 *
 */

public abstract class MapleFilter {
	
	/** Gets an example image of the filter. This is represented
	 * as a resource id for an image in Drawable
	 * 
	 * @return A resource id of the image in Drawable
	 */
	public abstract int getPreview();
	
	/** Gets the name of the filter
	 * 
	 * @return The filter name
	 */
	public abstract String getName();
	
	/** Applies a filter to the given bitmap
	 * 
	 * @param srcBitmap The image we are transforming
	 * @return The resulting image after the filter is applied
	 */
	public abstract Bitmap filterBitmap(Bitmap srcBitmap);
}
