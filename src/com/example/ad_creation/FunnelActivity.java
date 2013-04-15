package com.example.ad_creation;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.maple_android.R;
import com.example.maple_android.Utility;

public abstract class FunnelActivity extends Activity {
	
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
}
