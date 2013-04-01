package com.example.maple_android;

import com.facebook.Session;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class CropActivity extends Activity{

	private MapleApplication mApp;
	private Session mSession;
	private ImageView mAd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop);
		
		//init app data
		mApp = (MapleApplication) getApplication();	
		
		mSession = Session.getActiveSession();
		// If user isn't logged in we need to redirect back to LoginActivity
		if (mSession == null) {
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);
		}
		
		mAd = (ImageView) this.findViewById(R.id.ad);
		mAd.setImageBitmap(mApp.getAdCreationManager().getCurrentBitmap());
	}
}
