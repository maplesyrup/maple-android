package com.example.maple_android;

import com.facebook.Session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CropActivity extends Activity implements OnTouchListener {

	private MapleApplication mApp;
	private Session mSession;
	private CropView mCropView;
	
	class CropView extends View {
		Paint mPaint;
		
		private float mTopLeftX;
		private float mTopLeftY;
		private float mBtmRightX;
		private float mBtmRightY;
		
		private float mLength;
		private Bitmap mCurrBitmap;
		
		private boolean mFirstRender;
		
		public CropView(Context context, Bitmap currBitmap) {
			super(context);
			mPaint = new Paint();
			mCurrBitmap = currBitmap;
			mLength = mCurrBitmap.getHeight();
			
			mFirstRender = true;
		}

		protected void onDraw(Canvas canvas) {
			float centerX = canvas.getWidth() / 2;
			float centerY = canvas.getHeight() / 2;
			
			if (mFirstRender) {
				mTopLeftX = centerX - mLength / 2;
				mTopLeftY = centerY - mLength / 2;
				mBtmRightX = centerX + mLength / 2;
				mBtmRightY = centerY + mLength / 2;
				mFirstRender = false;
			}
			canvas.drawARGB(50, 255, 255, 255);
			mPaint.setColor(Color.BLACK);
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(3.0f);
			
			Bitmap currBitmap = mApp.getAdCreationManager().getCurrentBitmap();
			
			
			float leftX = centerX - (currBitmap.getWidth() / 2);
			float topY = centerY - (currBitmap.getHeight() / 2);
			canvas.drawBitmap(currBitmap, leftX, topY, null);
			
			canvas.drawRect(mTopLeftX, mTopLeftY, mBtmRightX, mBtmRightY, mPaint);
			invalidate();
			
		}

		public void setRect(float x, float y) {
			mTopLeftX = x;
			mTopLeftY = y;
			mBtmRightX = x + mLength;
			mBtmRightY = y + mLength;
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSession = Session.getActiveSession();
		// If user isn't logged in we need to redirect back to LoginActivity
		if (mSession == null) {
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);
		}
		
		mApp = (MapleApplication) getApplication();	
		
		mCropView = new CropView(this, mApp.getAdCreationManager().getCurrentBitmap());
		setContentView(mCropView);
		mCropView.setOnTouchListener(this);
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent e) {
		mCropView.setRect(e.getX(), e.getY());
		return true;
	}
}
