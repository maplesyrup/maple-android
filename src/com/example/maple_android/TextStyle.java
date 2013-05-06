package com.example.maple_android;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Paint;

/**
 * This class represents a text style that can be created with multiple paint
 * objects. It is essentially a list of paints, which should be used in a
 * specific order to create the intended effect.
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
