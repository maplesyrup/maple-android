package com.example.ad_creation;



import com.example.custom_views.LogoView;
import com.example.custom_views.ProgressView;
import com.example.maple_android.AdCreationManager;
import com.example.maple_android.MapleApplication;
import com.example.maple_android.R;
import com.example.maple_android.Utility;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LogoActivity extends FunnelActivity {

	private LogoView mLogoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setCustomContent(R.layout.activity_logo);
		
		mConfig.put(Config.HELP_MESSAGE, "Select which company logo you want to place on the ad, and move it around!");
		mConfig.put(Config.NAME, "Logo");

		mLogoView = (LogoView) findViewById(R.id.logoView);
		mLogoView.setAd(mApp.getAdCreationManager().getCurrentBitmap());

		mAdCreationManager.setup(this);	

		// Load Logo.
		// logoArray will only be non null if the user picked
		// a logo in the LogoPickerActivity. Otherwise we have to
		// direct them there to pick a logo before they can use one

		Bitmap logo = mAdCreationManager.getCompanyLogo();

		if (logo != null) {
			mLogoView.setLogo(logo, 0, 0);
		}
	}

	/**
	 * Launch an activity that allows the user to choose a logo for the selected
	 * company. When the activity returns to the LogoPicker it will include a
	 * logo bytestream in the intent if the user successfully picked a logo.
	 * This is checked for in onCreate
	 * 
	 * @param view
	 *            The button that was clicked
	 */
	public void launchLogoPicker(View view) {
		Intent i = new Intent(this, LogoPickerActivity.class);
		startActivity(i);
	}

	/**
	 * Save modified ad and continue to the next stage in the funnel
	 * 
	 * @param view
	 */
	public void nextStage(View view) {				
		// update bitmap to include logo
		Bitmap ad =  mLogoView.addLogo();
		// null is returned if a logo hasn't been set
		if(ad == null) ad = mAdCreationManager.getCurrentBitmap();
		mAdCreationManager.nextStage(this, ad);
	}

	/**
	 * Return to the previous stage without saving any changes
	 * 
	 * @param view
	 */
	public void prevStage(View view) {
		mAdCreationManager.previousStage(this);
	}
	
}
