package com.twotoasters.android.horizontalimagescroller.widget;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Paint;

/**
 * This class represents a text style that can be created with multiple paint
 * objects. It is essentially a list of paints, which are applied from first to
 * last to achieve a text style. 
 * <p>
 * For example, to achieve white text with a black
 * outline, first add a paint with a black stroke, and then
 * add a white paint of the same size.
 * 
 * @author Eli
 * 
 */

public class TextStyle implements Iterable<Paint> {
	ArrayList<Paint> mPaints;

	/**
	 * Create a new TextStyle. It will be empty at first. Call addPaint() to add
	 * paints to the style after it is created
	 */
	public TextStyle() {
		mPaints = new ArrayList<Paint>();
	}
	
	/** Returns the height of the given style
	 * 
	 * @return
	 */
	public int getHeight(){
		// height is descent + ascent
		Paint p = mPaints.get(0);
		return (int) (p.ascent() + p.descent());
	}
	
	/** Returns the width of the given text
	 * when this style is used
	 * @param text
	 * @return
	 */
	public int getWidth(String text){
		// assume first paint is the largest since
		// it should be the background 
		return (int) mPaints.get(0).measureText(text);
	}

	/** Adds a paint to the style. When the style is used
	 * paints are applied in order from first to last.
	 * @param paint
	 */
	public void addPaint(Paint paint) {
		mPaints.add(paint);
	}

	/**
	 * Returns the number of paints that make up this style
	 */
	public int getNumPaints() {
		return mPaints.size();
	}

	@Override
	public Iterator<Paint> iterator() {
		Iterator<Paint> it = new Iterator<Paint>() {

			private int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return currentIndex < mPaints.size()
						&& mPaints.get(currentIndex) != null;
			}

			@Override
			public Paint next() {
				return mPaints.get(currentIndex++);
			}

			@Override
			public void remove() {
				
			}
		};		return it;
	}
}
