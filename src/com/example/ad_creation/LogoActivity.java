package com.example.ad_creation;



import com.example.custom_views.LogoView;
import com.example.maple_android.AdCreationManager;
import com.example.maple_android.MapleApplication;
import com.example.maple_android.R;
import com.example.maple_android.Utility;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class LogoActivity extends Activity {
	private MapleApplication mApp;
	private AdCreationManager mAdCreationManager;

	private LogoView mLogoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);

		// Init app
		mApp = (MapleApplication) this.getApplication();
		mAdCreationManager = mApp.getAdCreationManager();

		// set photo
		//mPhoto = (ImageView) this.findViewById(R.id.photo);
		//mPhoto.setImageBitmap(mApp.getAdCreationManager().getCurrentBitmap());

		mLogoView = (LogoView) findViewById(R.id.logoView);
		mLogoView.setAd(mApp.getAdCreationManager().getCurrentBitmap());

		// Update page title to reflect the company
		TextView title = (TextView) this.findViewById(R.id.companyTag);
		title.setText("Add A " + mAdCreationManager.getCompanyName() + " Logo!");

		// Load Logo.
		// logoArray will only be non null if the user picked
		// a logo in the LogoPickerActivity. Otherwise we have to
		// direct them there to pick a logo before they can use one

		Bitmap logo = mAdCreationManager.getCompanyLogo();

		if (logo != null) {
			mLogoView.setLogo(logo, 0, 0);
		}
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logo, menu);
		return true;
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
		mAdCreationManager.nextStage(this, mLogoView.addLogo());
	}

	/**
	 * Return to the previous stage without saving any changes
	 * 
	 * @param view
	 */
	public void prevStage(View view) {
		mAdCreationManager.previousStage(this);
	}

	public void getHelp(View v) {
		String message = "Select which company logo you want to place on the ad, and move it around!";
		String title = "Step " + mAdCreationManager.getCurrentStage() + " of " + mAdCreationManager.getNumStages();
		Utility.createHelpDialog(this, message, title);
	}
	
}
