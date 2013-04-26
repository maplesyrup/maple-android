package com.example.ad_creation;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.example.custom_views.CropView;
import com.example.custom_views.ProgressView;
import com.example.maple_android.R;

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
		
		/* first stage previous arrow doesn't make sense */
		disablePrev();
		
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
		selectNext();
		mAdCreationManager.nextStage(this, mCropView.crop());
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent e) {
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mPrevTouchX = e.getX();
			mPrevTouchY = e.getY();
			mCropView.setDragState(e.getX(), e.getY(), e.getAction());
			break;
		case MotionEvent.ACTION_MOVE:
			float x = e.getX();
			float y = e.getY();
			float deltaX = x - mPrevTouchX;
			float deltaY = y - mPrevTouchY;
			mCropView.update(deltaX, deltaY);
			mPrevTouchX = x;
			mPrevTouchY = y;
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mCropView.setDragState(e.getX(), e.getY(), e.getAction());
			break;
		}
		return true;
	}

	@Override
	void prevStage(View v) {
		// Do nothing
	}

}



