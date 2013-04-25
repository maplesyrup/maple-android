package com.example.custom_views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * This is a custom view for displaying a bitmap and a moveable rectangle over it.
 * It makes it easy to crop images.
 * @author benrudolph
 *
 */
public class CropView extends ImageView {
	Paint mPaint;
	
	/* Top left and bottom right corner of crop box */
	
	private RectF mBoundingBox;
	private RectF mCropBox;

	private static final int MARGIN = 20;
	private static final int ALPHA = 160;
	
	private float mRatio;
	
	/* Length of crop square */
	private float mLength;
	
	private Bitmap mCurrBitmap;
	
	private boolean mFirstRender;
	public enum Direction {
		X,
		Y
	}
	
	private ArrayList<Rect> mShadowRects;
	
	public CropView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		mCurrBitmap = null;
		

		mBoundingBox = new RectF();
		mCropBox = new RectF();

		mShadowRects = new ArrayList<Rect>();
		
		for (int i = 0; i < 4; i++) {
			mShadowRects.add(new Rect());
		}
		
		
		mFirstRender = true;
	
	}
	
	/**
	 * Sets the current bitmap to be cropped and sets the length of the crop box to
	 * be the height of image.
	 * @param bitmap New bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		mCurrBitmap = bitmap;
		mFirstRender = true;
		invalidate();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = mCurrBitmap == null ? 480 : mCurrBitmap.getHeight();
	    int width = MeasureSpec.getSize(widthMeasureSpec);
	    
		setMeasuredDimension(width, height);

	}

	protected void onDraw(Canvas canvas) {
		
		if (mCurrBitmap != null) {
			
			if (mFirstRender) {				
				int bBoxWidth = canvas.getWidth() - 2*MARGIN;
				
				// Determine aspect ratio so we can accurately get height without skew
				mRatio = bBoxWidth / (float) mCurrBitmap.getWidth();
				int bBoxHeight = (int) (mRatio * mCurrBitmap.getHeight());
				
				mBoundingBox.set(MARGIN, 0, MARGIN + bBoxWidth, bBoxHeight);

				mLength = mBoundingBox.width() > mBoundingBox.height() ? mBoundingBox.height() : mBoundingBox.width();

				mCropBox.set(mBoundingBox.centerX() - mLength / 2,
							 mBoundingBox.centerY() - mLength / 2, 
							 mBoundingBox.centerX() + mLength / 2,
							 mBoundingBox.centerY() + mLength / 2);
				
				
	
				mFirstRender = false;
			}
			
			canvas.drawARGB(50, 255, 255, 255);
			mPaint.setColor(Color.BLACK);
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(3.0f);
			
			canvas.drawBitmap(mCurrBitmap, null, mBoundingBox, null);
			
			canvas.drawRect(mCropBox, mPaint);
			
			mPaint.setStyle(Style.FILL);
			mPaint.setAlpha(ALPHA);
			canvas.drawRect(mBoundingBox.left, mBoundingBox.top, mCropBox.left, mBoundingBox.bottom, mPaint);
			canvas.drawRect(mCropBox.left, mBoundingBox.top, mCropBox.right, mCropBox.top, mPaint);
			canvas.drawRect(mCropBox.right, mBoundingBox.top, mBoundingBox.right, mBoundingBox.bottom, mPaint);
			canvas.drawRect(mCropBox.left, mCropBox.bottom, mCropBox.right, mBoundingBox.bottom, mPaint);

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
			return mCropBox.left + delta > mBoundingBox.left && mCropBox.left + delta < mBoundingBox.right &&
					mCropBox.right + delta > mBoundingBox.left && mCropBox.right + delta < mBoundingBox.right;						
		case Y:
			return mCropBox.top + delta > mBoundingBox.top && mCropBox.top + delta < mBoundingBox.bottom &&
					mCropBox.bottom + delta >mBoundingBox.top && mCropBox.bottom + delta < mBoundingBox.bottom;				
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
			mCropBox.left += deltaX;
			mCropBox.right += deltaX;
		}
		if (isValidMove(Direction.Y, deltaY)) {
			mCropBox.top += deltaY;
			mCropBox.bottom += deltaY;
		}
			
	
	}
	/**
	 * Returns cropped bitmap based on crop box
	 * @return
	 */
	public Bitmap crop() {
		int x = (int) (mCropBox.left / mRatio);
		int y = (int) (mCropBox.top / mRatio);
		int length = (int) (mLength / mRatio);
		
		/* Take care of rounding errors */
		length = x + length > mCurrBitmap.getWidth() ? mCurrBitmap.getWidth() - x : length;
		length = y + length > mCurrBitmap.getHeight() ? mCurrBitmap.getHeight() - y : length;

		return Bitmap.createBitmap(mCurrBitmap, x, y, length, length);
	}
	
}