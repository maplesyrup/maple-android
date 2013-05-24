package com.additt.maple_android;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Campaign {
	private static final String TAG = "CAMPAIGN";
	private String mTitle;
	private String mDesription;
	private String mId;

	public Campaign(JSONObject jsonCampaign) {
		// get urls for each logo size
		try {
			mId = jsonCampaign.getString("id");
		} catch(JSONException e) {
			e.printStackTrace();
			Log.d(TAG, "Couldn't extract id field");
			mId = null;
		}
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

	public String getId() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getDesription() {
		return mDesription;
	}
}
