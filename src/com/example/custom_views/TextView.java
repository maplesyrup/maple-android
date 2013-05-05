package com.example.custom_views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class TextView extends ImageView implements OnTouchListener {

	private Bitmap mCurrAd;
	private String mText;
	private PointF mTextPos;
	private Paint mPaint;
    private static final float DEFAULT_TEXT_SIZE = 14;
	private PointF mPrevTouch;
	
	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;

	public TextView(Context context, AttributeSet attrs) {
		super(context);
		mCurrAd = null;
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		mText = "";
		mPaint = new Paint();
		mPaint.setTextSize(DEFAULT_TEXT_SIZE);
		mTextPos = new PointF(0, 0);
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (mText != null && mText != "") {
			canvas.drawText(mText, mTextPos.x, mTextPos.y, mPaint);
		}
	}
	
	public void setText(String text) {
		mText = text;
		invalidate();
	}
	
	private void moveText(float x, float y) {
		mTextPos.set(x, y);
		invalidate();
	}
	
	public void setStyle(Paint paint) {
		mPaint = paint;
		invalidate();
	}
	/**
	 * Sets the ad. Also takes a ratio so that we can scale the logo to the same size later.
	 * @param ad
	 * @param ratio
	 */
	public void setAd(Bitmap ad) {
		setImageBitmap(ad);
		mCurrAd = ad;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	}
	
	/**
	 * When it detects a scaling gesture, it will scale. Moves the logo if there is only
	 * one pointer and it's on the logo.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	    // Let the ScaleGestureDetector inspect all events.
	    mScaleDetector.onTouchEvent(ev);
	    
	    if (ev.getPointerCount() == 1) {
		    switch (ev.getAction()) {
			    case MotionEvent.ACTION_DOWN:
			    	if (mPrevTouch == null) {
			    		mPrevTouch = new PointF(ev.getX(), ev.getY());
			    	} else {
			    		mPrevTouch.set(ev.getX(), ev.getY());	
		    		}
			    	break;
				case MotionEvent.ACTION_MOVE:
					moveText(ev.getX() - mPrevTouch.x, ev.getY() - mPrevTouch.y);
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
	
	public Bitmap addText() {
		Bitmap newAd = Bitmap.createBitmap(mCurrAd.getWidth(), mCurrAd.getHeight(), mCurrAd.getConfig());
        Canvas canvas = new Canvas(newAd);
        canvas.drawBitmap(mCurrAd, new Matrix(), null);
        if (mText != null && mText != "") {
			canvas.drawText(mText, mTextPos.x, mTextPos.y, mPaint);
		}
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
	        mPaint.setTextSize(DEFAULT_TEXT_SIZE * mScaleFactor);
	        invalidate();
	        return true;
	    }
	}
}
