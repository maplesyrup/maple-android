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

import com.example.browsing.PopularAdsActivity;
import com.example.maple_android.AdCreationManager;
import com.example.maple_android.LoginActivity;
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
	
	private ImageView mPrevBtn;
	private ImageView mNextBtn;
	
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
		
		mNextBtn = (ImageView) findViewById(R.id.right_arrow);
		mPrevBtn = (ImageView) findViewById(R.id.left_arrow);
	}
	
	/**
	 * Disables the next button
	 */
	public void disableNext() {
		mNextBtn.setImageResource(R.drawable.right_arrow_disabled);
		mNextBtn.setOnClickListener(null);
	}
	
	/**
	 * Enables the next button
	 */
	public void enableNext() {
		mNextBtn.setImageResource(R.drawable.right_arrow);
		mNextBtn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		        nextStage(v);
		    }
		});
	}
	
	/**
	 * Disables the previous button
	 */
	public void disablePrev() {
		mPrevBtn.setImageResource(R.drawable.left_arrow_disabled);
		mPrevBtn.setOnClickListener(null);

	}
	
	/**
	 * Enables the previous button
	 */
	public void enablePrev() {
		mPrevBtn.setImageResource(R.drawable.left_arrow);
		mPrevBtn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		        prevStage(v);
		    }
		});
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
	
	/**
	 * Sets the custom layout for that particular activity.
	 * @param layout_id
	 */
	public void setCustomContent(int layout_id) {
		RelativeLayout common = (RelativeLayout) findViewById(R.id.common_layout);

		// Add inner custom view as child
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 
				RelativeLayout.LayoutParams.MATCH_PARENT);
	        
		// Ensure that layout is between top and bottom bar
		params.addRule(RelativeLayout.BELOW, R.id.topbar_container);
		params.addRule(RelativeLayout.ABOVE, R.id.bottombar_container);

		
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
		
		RelativeLayout customContent = (RelativeLayout) layoutInflater.inflate(layout_id, common, false);
		
		// Index 1 sets the element to the second child (after the topbar)
		common.addView(customContent, 1, params);
	}
	/**
	 * Gets config variable
	 * @return
	 */
	public HashMap<Config, String> getConfig() {
		return mConfig;
	}
	
	abstract void nextStage(View v);
	
	abstract void prevStage(View v);
}
