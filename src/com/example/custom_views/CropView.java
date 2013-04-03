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

public class CropView extends View {
	Paint mPaint;
	
	private PointF mTopLeft;
	private PointF mBtmRight;
	
	private PointF mTopLeftBound;
	private PointF mBtmRightBound;
	
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
	
	public void setBitmap(Bitmap bitmap) {
		mCurrBitmap = bitmap;
		mLength = bitmap.getHeight();
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
	
	private boolean isValidMove(PointF p1, PointF p2, Direction dir, float delta) {
		switch (dir) {
		case X:
			return p1.x + delta > mTopLeftBound.x && p1.x + delta < mBtmRightBound.x &&
					p2.x + delta > mTopLeftBound.x && p2.x + delta < mBtmRightBound.x;						
		case Y:
			return p1.y + delta > mTopLeftBound.y && p1.y + delta < mBtmRightBound.y &&
					p2.y + delta > mTopLeftBound.y && p2.y + delta < mBtmRightBound.y;				
		default:
			return false;
		}
		
	}

	public void moveRect(float deltaX, float deltaY) {
		
		if (isValidMove(mTopLeft, mBtmRight, Direction.X, deltaX)) {
			mTopLeft.x += deltaX;
			mBtmRight.x += deltaX;
		}
		if (isValidMove(mTopLeft, mBtmRight, Direction.Y, deltaY)) {
			mTopLeft.y += deltaY;
			mBtmRight.y += deltaY;
		}
			
	
	}

	public Bitmap crop() {
		return Bitmap.createBitmap(mCurrBitmap, (int) mTopLeft.x, (int) mTopLeft.y, (int) mLength, (int) mLength);
	}
	
}