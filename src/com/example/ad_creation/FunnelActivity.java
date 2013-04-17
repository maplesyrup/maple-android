package com.example.ad_creation;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.ad_creation.FunnelActivity.Config;
import com.example.browsing.PopularAdsActivity;
import com.example.maple_android.AdCreationManager;
import com.example.maple_android.LoginActivity;
import com.example.maple_android.MainActivity;
import com.example.maple_android.MapleApplication;
import com.example.maple_android.R;
import com.example.maple_android.Utility;
import com.facebook.Session;

public abstract class FunnelActivity extends Activity {
	
	public enum Config {
		HELP_MESSAGE,
		NAME
	}
	
	/* Holds information specific to the activity */
	protected HashMap<Config, String> mConfig;
	
	protected MapleApplication mApp;
	protected AdCreationManager mAdCreationManager;
	protected Session mSession;
	
	private ImageView mLeftArrow;
	private ImageView mRightArrow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mConfig = new HashMap<Config, String>();
		mSession = Session.getActiveSession();
		mApp = (MapleApplication) getApplication();	
		mAdCreationManager = mApp.getAdCreationManager();
		
		// If user isn't logged in we need to redirect back to LoginActivity
		if (mSession == null) {
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);
		}
		
		setContentView(R.layout.funnel_common);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ad_creation, menu);
		return true;
	}
	
	/**
	 * Respond to each tab button
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		return Utility.myOnOptionsItemSelected(this, item);
	}
	
	/**
	 * Go to home page
	 * @param v
	 */
	public void home(View v) {
		Intent intent = new Intent(this, PopularAdsActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Displays the help dialog
	 * @param v
	 */
	public void getHelp(View v) {
		String title = "Step " + mAdCreationManager.getReadableCurrentStage() + " of " + mAdCreationManager.getNumStages();
		Utility.createHelpDialog(this, mConfig.get(Config.HELP_MESSAGE), title);
	}
	
	public void setCustomContent(int layout_id) {
		RelativeLayout common = (RelativeLayout) findViewById(R.id.common_layout);

		// Add inner custom view as child
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 
				RelativeLayout.LayoutParams.MATCH_PARENT);
	        
		params.addRule(RelativeLayout.BELOW, R.id.topbar_container);
		params.addRule(RelativeLayout.ABOVE, R.id.bottombar_container);

		
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
		
		RelativeLayout customContent = (RelativeLayout) layoutInflater.inflate(layout_id, common, false);
		
		common.addView(customContent, 1, params);
	}
	/**
	 * Gets config variable
	 * @return
	 */
	public HashMap<Config, String> getConfig() {
		return mConfig;
	}
}
