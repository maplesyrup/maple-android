package com.example.browsing;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.maple_android.R;
import com.example.maple_android.Utility;

public class PopularAdsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popular_ads);

	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    JSONArray jObjectAds = new JSONArray();
	    gridview.setAdapter(new ImageAdapter(this, jObjectAds));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(PopularAdsActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	/**
	 * Respond to each tab button
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		return Utility.myOnOptionsItemSelected(this, item);
	}
}
