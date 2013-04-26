package com.example.browsing;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.maple_android.R;

public class PopularAdsActivity extends BrowseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("Popular");
		super.onCreate(savedInstanceState);
		super.setLayout(R.layout.activity_popular_ads);
		super.requestUserAds(null);
		
		String success = getIntent().getStringExtra("successMessage");
		if (success != null) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, success, Toast.LENGTH_LONG);
			toast.show();	
		}
		
	}
}
