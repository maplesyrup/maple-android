package com.example.custom_views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * This is a custom view for displaying a bitmap and a moveable rectangle over it.
 * It makes it easy to crop images.
 * @author benrudolph
 *
 */
public class CropView extends View {
	Paint mPaint;
	
	/* Top left and bottom right corner of crop box */
	private PointF mTopLeft;
	private PointF mBtmRight;
	
	/* Top left and bottom right corner of bounding box */
	private PointF mTopLeftBound;
	private PointF mBtmRightBound;
	
	/* Length of crop square */
	private float mLength;
	
	private Bitmap mCurrBitmap;
	
	private boolean mFirstRender;
	public enum Direction {
		X,
		Y
	}
	
	public CropView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		mCurrBitmap = null;
		
		mTopLeft = new PointF();
		mBtmRight = new PointF();
		
		mTopLeftBound = new PointF();
		mBtmRightBound = new PointF();
		
		mFirstRender = true;
	}
	
	/**
	 * Sets the current bitmap to be cropped and sets the length of the crop box to
	 * be the height of image.
	 * @param bitmap New bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		mCurrBitmap = bitmap;
		mLength = bitmap.getHeight();
		mFirstRender = true;
	}
	
	@Override
	protected void onMeasure(int width, int height) {
		
		if (mCurrBitmap != null) {
			setMeasuredDimension(mCurrBitmap.getWidth(), mCurrBitmap.getHeight());
		} else {
			setMeasuredDimension(0, 0);
		}
	}

	protected void onDraw(Canvas canvas) {
		if (mCurrBitmap != null) {
			float centerX = mCurrBitmap.getWidth() / 2;
			float centerY = mCurrBitmap.getHeight() / 2;
			
			if (mFirstRender) {
				mTopLeft.set(centerX - mLength / 2, centerY - mLength / 2);
				mBtmRight.set(centerX + mLength / 2, centerY + mLength / 2);
				
				mTopLeftBound.set(0, 0);
				mBtmRightBound.set(mCurrBitmap.getWidth(), mCurrBitmap.getHeight());
				
				mFirstRender = false;
			}
			canvas.drawARGB(50, 255, 255, 255);
			mPaint.setColor(Color.BLACK);
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(3.0f);
			
			canvas.drawBitmap(mCurrBitmap, mTopLeftBound.x, mTopLeftBound.y, null);
			
			canvas.drawRect(mTopLeft.x, mTopLeft.y, mBtmRight.x, mBtmRight.y, mPaint);
		}
		invalidate();
		
	}
	
	/**
	 * Determines if the movement of the rectangle is valid given a direction and amount to move
	 * @param dir The direction of movement
	 * @param delta The distance the rect will move
	 * @return
	 */
	private boolean isValidMove(Direction dir, float delta) {
		switch (dir) {
		case X:
			return mTopLeft.x + delta > mTopLeftBound.x && mTopLeft.x + delta < mBtmRightBound.x &&
					mBtmRight.x + delta > mTopLeftBound.x && mBtmRight.x + delta < mBtmRightBound.x;						
		case Y:
			return mTopLeft.y + delta > mTopLeftBound.y && mTopLeft.y + delta < mBtmRightBound.y &&
					mBtmRight.y + delta > mTopLeftBound.y && mBtmRight.y + delta < mBtmRightBound.y;				
		default:
			return false;
		}
		
	}
	
	/**
	 * If possible, moves rect delta distance from current position
	 * @param deltaX distance to move in X direction
	 * @param deltaY distance to move in Y direction
	 */
	public void moveRect(float deltaX, float deltaY) {
		
		if (isValidMove(Direction.X, deltaX)) {
			mTopLeft.x += deltaX;
			mBtmRight.x += deltaX;
		}
		if (isValidMove(Direction.Y, deltaY)) {
			mTopLeft.y += deltaY;
			mBtmRight.y += deltaY;
		}
			
	
	}
	/**
	 * Returns cropped bitmap based on crop box
	 * @return
	 */
	public Bitmap crop() {
		return Bitmap.createBitmap(mCurrBitmap, (int) mTopLeft.x, (int) mTopLeft.y, (int) mLength, (int) mLength);
	}
	
}