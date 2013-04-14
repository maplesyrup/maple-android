package com.example.browsing;

import android.os.Bundle;

import com.facebook.Session;
import com.loopj.android.http.RequestParams;

public class PersonalAdsActivity extends BrowseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Session session = Session.getActiveSession();
		final String user_id = getUserId(session.getAccessToken());
		RequestParams params = new RequestParams();
		
		// No params means just getting the most popular ads
		params.put("user_id", user_id);
		super.requestUserAds(params);
	}

	/**
	 * Stub method for now to get user id
	 * @param accessToken
	 * @return
	 */
	private String getUserId(String accessToken) {
		return "3";
	}
}
