package com.example.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

public class ProgressView extends View {
	
	private static final int RADIUS = 10;
	private static final float WIDTH_PERCENT = 0.10f;
	
	private Paint mPaint;
	private int mNumStages;
	private int mCurrStage;
	private int mHeight;
	private int mWidth;

	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		mHeight = 0;
		mWidth = 0;

		mNumStages = 0;
		mCurrStage = 0;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int desiredWidth = 100;
	    int desiredHeight = 70;

	    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

	    int width;
	    int height;

	    //Measure Width
	    if (widthMode == MeasureSpec.EXACTLY) {
	        //Must be this size
	        width = widthSize;
	    } else if (widthMode == MeasureSpec.AT_MOST) {
	        //Can't be bigger than...
	        width = Math.min(desiredWidth, widthSize);
	    } else {
	        //Be whatever you want
	        width = desiredWidth;
	    }

	    //Measure Height
	    if (heightMode == MeasureSpec.EXACTLY) {
	        //Must be this size
	        height = heightSize;
	    } else if (heightMode == MeasureSpec.AT_MOST) {
	        //Can't be bigger than...
	        height = Math.min(desiredHeight, heightSize);
	    } else {
	        height = desiredHeight;
	    }

	    mWidth = width;
	    mHeight = height;
	    setMeasuredDimension(width, height);
		
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Style.FILL_AND_STROKE);
		canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
		
		for (int i = 0; i < mNumStages; i ++) {
			if (i == mCurrStage) {
				mPaint.setColor(Color.BLUE);
			} else {
				mPaint.setColor(Color.WHITE);
			}
			canvas.drawCircle(((float) (i + 1) / mNumStages) * mWidth - (((float) 1 / (2 * mNumStages)) * mWidth), (float) mHeight / 2, 10, mPaint);
		}
		invalidate();
	}

	public void setCurrentStage(int currentStage) {
		mCurrStage = currentStage;
	}

	public void setNumStages(int numStages) {
		mNumStages = numStages;
	}
}
