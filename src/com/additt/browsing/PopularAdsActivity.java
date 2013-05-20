package com.additt.browsing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.additt.maple_android.R;
import com.loopj.android.http.RequestParams;

public class PopularAdsActivity extends BrowseActivity {

	private static final String TAG = "PopularAds";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("Popular");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popular_ads);
		
		GridView mGridview = (GridView) findViewById(R.id.gridviewAdsPopular);
		Log.d(TAG, "found grid view");
		// On Click event for Single Gridview Item
        mGridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Log.d(TAG, "clicked item");
            	Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                DisplayAd displayAd = (DisplayAd) parent.getAdapter().getItem(position);
				i.putExtra("url", displayAd.getUrl());
                i.putExtra("title", displayAd.getTitle());
                startActivity(i);
            }
        });
		setGridview(mGridview);
        
		RequestParams params = new RequestParams();

		if (mApp.getUser() != null) {
			params.put("auth_token", mApp.getUser().getAuthToken());
		}
		requestUserAds(params);
		
		String success = getIntent().getStringExtra("successMessage");
		if (success != null) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, success, Toast.LENGTH_LONG);
			toast.show();	
		}
		
	}
	
	@Override
	public void onStart() {
		/* Super handles starting tracking */
		super.onStart();
	}

	
	@Override
	public void onStop() {
		/* Super handles stopping tracking */
		super.onStop();
	}
	
}
