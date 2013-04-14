package com.example.browsing;

import android.os.Bundle;

public class PopularAdsActivity extends BrowseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestUserAds(null);
	}
}
