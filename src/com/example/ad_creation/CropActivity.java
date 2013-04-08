package com.example.ad_creation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.custom_views.CropView;
import com.example.maple_android.AdCreationManager;
import com.example.maple_android.LoginActivity;
import com.example.maple_android.MapleApplication;
import com.example.maple_android.R;
import com.example.maple_android.Utility;
import com.facebook.Session;

/**
 * This activity crops an image
 * @author benrudolph
 *
 */
public class CropActivity extends Activity implements OnTouchListener {

	private MapleApplication mApp;
	private AdCreationManager mAdCreationManager;
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
		mAdCreationManager = mApp.getAdCreationManager();
		
		setContentView(R.layout.activity_crop);
		
		mCropView = (CropView) findViewById(R.id.cropView);
		mCropView.setBitmap(mApp.getAdCreationManager().getCurrentBitmap());
		mCropView.setOnTouchListener(this);
		
	}

	/**
	 * Will crop the ad and send it to next stage in funnel. This function is activated on a button click.
	 * @param v
	 */
	public void cropAd(View v) {
		
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
	
	public void getHelp(View v) {
		String message = "Select which part of your picture you want to be your ad!";
		String title = "Step " + mAdCreationManager.getCurrentStage() + " of " + mAdCreationManager.getNumStages();
		Utility.createHelpDialog(this, message, title);
	}
}



