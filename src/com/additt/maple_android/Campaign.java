package com.additt.maple_android;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Campaign {
	private static final String TAG = "CAMPAIGN";
	private String mTitle;
	private String mDesription;

	public Campaign(JSONObject jsonCampaign) {
		// get urls for each logo size
		try {
			mTitle = jsonCampaign.getString("title");
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d(TAG, "Couldn't extract title field");
			mTitle = "<No Title>";
		}
		
		try {
			mDesription = jsonCampaign.getString("description");
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d(TAG, "Couldn't extract description field");
			mDesription = "<No Description>";
		}
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmDesription() {
		return mDesription;
	}

	public void setmDesription(String mDesription) {
		this.mDesription = mDesription;
	}

}
