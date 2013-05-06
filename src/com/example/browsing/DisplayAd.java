package com.example.browsing;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.maple_android.R;

/**
 * This class encapsulates all of the JSON data that 
 * specifies an ad from the site. It's main job is to do
 * JSON parsing and present a clean interface for accessing
 * image attributes
 * 
 * @author rpandey1234
 *
 */

public class DisplayAd {
	// Default for mUrl is what will be shown while image is loading
	private String mUrl = "drawable://" + R.drawable.maple;
    private String mTitle;
    private String mCreator;
    private int mNumVotes;
	private String mRelativeTime;
    private boolean mVotedOn;
    private String mImageId;
    private String TAG = "DisplayAd";
	
	public DisplayAd(JSONObject ad) throws JSONException { 
    	mUrl = ad.getString("image_url");
        mTitle = ad.getString("title");
        mCreator = ad.getJSONObject("user").getString("name");
        mNumVotes = Integer.parseInt(ad.getString("total_votes"));
        mRelativeTime = ad.getString("relative_time");
        
		// Currently the mobile response to this gives back "unavailable" for all images,
		// probably because there are issues with current user auth
        String strVotedOn = ad.getString("voted_on");
        if (strVotedOn.equals("yes")) {
        	mVotedOn = true;
        } else {
        	mVotedOn = false;
        }
        mImageId = ad.getString("id");
	}
    
	// Example image:
    //http://s3.amazonaws.com/maplesyrup-assets/posts/images/000/000/006/medium/IMG_20130311_233546.jpg?1363070132
    public String getUrl() {
		return mUrl;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getCreator() {
		return mCreator;
	}

	public int getNumVotes() {
		return mNumVotes;
	}

	public String getRelativeTime() {
		return mRelativeTime;
	}

	public boolean getVotedOn() {
		return mVotedOn;
	}

	public String getImageId() {
		return mImageId;
	}
}
