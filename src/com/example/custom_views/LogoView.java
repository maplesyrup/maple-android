package com.example.custom_views;

import com.example.custom_views.CropView.Direction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class LogoView extends View {
	private Paint mPaint;
	/* Top left and bottom right corner of bounding box */
	private PointF mTopLeftBound;
	private PointF mBtmRightBound;
	
	/* Top left and bottom right corner of logo */
	private PointF mTopLeft;
	private PointF mBtmRight;
	
	private Bitmap mCurrBitmap;
	private Bitmap mCurrLogo;

	private boolean mFirstRender;
	public enum Direction {
		X,
		Y
	}
	
	public LogoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setBitmap(Bitmap bitmap) {
		mCurrBitmap = bitmap;
		mFirstRender = true;
	}
	
	public void setLogo(Bitmap logo, float x, float y) {
		mCurrLogo = logo;
		mTopLeft = new PointF(x, y);
		mBtmRight = new PointF(x + mCurrLogo.getWidth(), y + mCurrLogo.getHeight());
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
				mTopLeftBound.set(0, 0);
				mBtmRightBound.set(mCurrBitmap.getWidth(), mCurrBitmap.getHeight());
				
				mFirstRender = false;
			}
			canvas.drawARGB(50, 255, 255, 255);
			mPaint.setColor(Color.BLACK);
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(3.0f);
			
			canvas.drawBitmap(mCurrBitmap, mTopLeftBound.x, mTopLeftBound.y, null);
		}
		invalidate();
	}
	
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
	
	public void moveLogo(float deltaX, float deltaY) {
		
		if (isValidMove(Direction.X, deltaX)) {
			mTopLeft.x += deltaX;
			mBtmRight.x += deltaX;
		}
		if (isValidMove(Direction.Y, deltaY)) {
			mTopLeft.y += deltaY;
			mBtmRight.y += deltaY;
		}
	}
	
	public void resizeLogo(float deltaLeftX, float deltaRightX, float deltaTopY, float deltaBtmY) {
		mTopLeft.x += deltaLeftX;
		mBtmRight.x += deltaRightX;
		
		mTopLeft.y += deltaTopY;
		mBtmRight.y += deltaBtmY;
	}
}
