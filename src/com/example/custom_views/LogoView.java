package com.example.custom_views;

import com.example.custom_views.CropView.Direction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;;


public class LogoView extends View implements OnTouchListener {	
	
	// Width and height of view
	private int mWidth;
	private int mHeight;
	
	private PointF mPrevTouch;
	
	private Bitmap mCurrLogo;
	private Bitmap mCurrAd;

	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;
	
	// Actual size of logo bitmap
	private Rect mDstRect;
	
	// Scaled version of logo bitmap
	private Rect mDstRectScaled;
	
	public LogoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		mDstRect = new Rect(0, 0, 100, 100);
		mDstRectScaled = new Rect(0, 0, 100, 100);
		mWidth = 400;
		mHeight = 400;
		
	}
	
	public void setAd(Bitmap ad) {
		mCurrAd = ad;
		mWidth = ad.getHeight();
		mHeight = ad.getWidth();
	}

	public void setLogo(Bitmap logo, float x, float y) {
		mCurrLogo = logo;
	}
	
	@Override
	protected void onMeasure(int width, int height) {
		setMeasuredDimension(mWidth, mHeight);
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.save();
		if (mCurrAd != null) {
			canvas.drawBitmap(mCurrAd, 0, 0, null);
		}
		if (mCurrLogo != null) {
			int width = (int)(mScaleFactor * (mDstRect.right - mDstRect.left));
			int height =  (int)(mScaleFactor * (mDstRect.bottom - mDstRect.top));
			mDstRectScaled.set(mDstRect.left, mDstRect.top, mDstRect.left + width, mDstRect.top + height);
			canvas.drawBitmap(mCurrLogo, null, mDstRectScaled, null);
			
		}
		
		invalidate();
		
		
		canvas.restore();
	}
	/**
	 * When it detects a scaling gesture, it will scale. Moves the logo if there is only
	 * one pointer and it's on the logo.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	    // Let the ScaleGestureDetector inspect all events.
	    mScaleDetector.onTouchEvent(ev);
	    
	    if (ev.getPointerCount() == 1 && mDstRectScaled.contains((int)ev.getX(), (int)ev.getY())) {
		    switch (ev.getAction()) {
			    case MotionEvent.ACTION_DOWN:
			    	if (mPrevTouch == null) {
			    		mPrevTouch = new PointF(ev.getX(), ev.getY());
			    	} else {
			    		mPrevTouch.set(ev.getX(), ev.getY());	
		    		}
			    	break;
				case MotionEvent.ACTION_MOVE:
					moveLogo(ev.getX() - mPrevTouch.x, ev.getY() - mPrevTouch.y);
		    		mPrevTouch.set(ev.getX(), ev.getY());
					break;
				case MotionEvent.ACTION_CANCEL:
					break;
				case MotionEvent.ACTION_UP:
					break;
				default:
					break;
		    }
	    }
	    return true;
	}
	
	
	public void moveLogo(float deltaX, float deltaY) {
		mDstRect.left += deltaX;
		mDstRect.right += deltaX;
		mDstRect.top += deltaY;
		mDstRect.bottom += deltaY;
	
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
	
	public Bitmap addLogo() {
		Bitmap scaledLogo = Bitmap.createScaledBitmap(mCurrLogo, 
				mDstRectScaled.right - mDstRectScaled.left, 
				mDstRectScaled.bottom - mDstRectScaled.top, false);
		
        Bitmap newAd = Bitmap.createBitmap(mCurrAd.getWidth(), mCurrAd.getHeight(), mCurrAd.getConfig());
        Canvas canvas = new Canvas(newAd);
        canvas.drawBitmap(mCurrAd, new Matrix(), null);
        canvas.drawBitmap(scaledLogo, mDstRect.left, mDstRect.top, null);

		return newAd;
	}
	
	/**
	 * Android has built in scale listener.
	 * 
	 *
	 */
	private class ScaleListener 
        extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	    @Override
	    public boolean onScale(ScaleGestureDetector detector) {
	        mScaleFactor *= detector.getScaleFactor();
	
	        // Don't let the object get too small or too large.
	        mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
	
	        invalidate();
	        return true;
	    }
	}
}
