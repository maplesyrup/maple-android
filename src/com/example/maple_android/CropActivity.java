package com.example.maple_android;

import com.example.custom_views.CropView;
import com.facebook.Session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CropActivity extends Activity implements OnTouchListener {

	private MapleApplication mApp;
	private Session mSession;
	private CropView mCropView;
	
	private float mPrevTouchX;
	private float mPrevTouchY;
	


	
	
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
		
		//LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//View v = inflater.inflate(R.layout.activity_crop, null);
		
		
		setContentView(R.layout.activity_crop);
		
		mCropView = (CropView) findViewById(R.id.cropView);
		mCropView.setBitmap(mApp.getAdCreationManager().getCurrentBitmap());
		mCropView.invalidate();
		mCropView.setOnTouchListener(this);
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent e) {
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mPrevTouchX = e.getX();
			mPrevTouchY = e.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			float x = e.getX();
			float y = e.getY();
			float deltaX = x - mPrevTouchX;
			float deltaY = y - mPrevTouchY;
			mCropView.moveRect(deltaX, deltaY);
			mPrevTouchX = x;
			mPrevTouchY = y;
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return true;
	}
}
