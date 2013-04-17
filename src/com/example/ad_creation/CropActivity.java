package com.example.ad_creation;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.example.custom_views.CropView;
import com.example.custom_views.ProgressView;
import com.example.maple_android.AdCreationManager;
import com.example.maple_android.LoginActivity;
import com.example.maple_android.MapleApplication;
import com.example.maple_android.R;
import com.example.maple_android.Utility;
import com.facebook.Session;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

/**
 * This activity crops an image
 * @author benrudolph
 *
 */
public class CropActivity extends FunnelActivity implements OnTouchListener {

	private CropView mCropView;
	private ProgressView mProgressBar;
	private RelativeLayout mTopBar;
	
	private float mPrevTouchX;
	private float mPrevTouchY;
	


	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mConfig.put(Config.HELP_MESSAGE, "Select which part of your picture you want to be your ad!");
		mConfig.put(Config.NAME, "Crop");
		
		setCustomContent(R.layout.activity_crop);
		
		mProgressBar = (ProgressView) findViewById(R.id.progressBar);
		
		mTopBar = (RelativeLayout) findViewById(R.id.topbar);
		
		mCropView = (CropView) findViewById(R.id.cropView);
		mCropView.setBitmap(mApp.getAdCreationManager().getCurrentBitmap());
		mCropView.setOnTouchListener(this);
		
		mAdCreationManager.setup(this);
		
	}

	/**
	 * Will crop the ad and send it to next stage in funnel. This function is activated on a button click.
	 * @param v
	 */
	public void nextStage(View v) {
		
		mAdCreationManager.nextStage(this, mCropView.crop());
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



