package com.additt.browsing;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.additt.maple_android.R;
import com.loopj.android.http.RequestParams;

public class PopularAdsActivity extends BrowseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("Popular");
		super.onCreate(savedInstanceState);
		super.setLayout(R.layout.activity_popular_ads);
		
		RequestParams params = new RequestParams();

		if (mApp.getUser() != null) {
			params.put("auth_token", mApp.getUser().getAuthToken());
		}
		super.requestUserAds(params);
		
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
