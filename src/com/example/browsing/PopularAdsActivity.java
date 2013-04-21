package com.example.browsing;

import com.example.maple_android.R;

import android.os.Bundle;

public class PopularAdsActivity extends BrowseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_popular_ads);
		super.requestUserAds(null);
	}
}
