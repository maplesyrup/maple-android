package com.additt.maple_android;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class encapsulates all of the JSON data that 
 * specifies a user (the person creating ads). This class
 * only contains user info of the active user.
 * 
 * @author rpandey1234
 *
 */

public class User {

	// defaults
	private String mName;
	private String mEmail;
	private String mId;
	private String mPersonalInfo;
	private String mToken;
	private String mAuthToken;

	public User(String jsonResponse, String token) throws JSONException {
		JSONObject jObject = new JSONObject(jsonResponse);
		mName = jObject.getString("name");
        mEmail = jObject.getString("email");
        mId = jObject.getString("id");
        mPersonalInfo = jObject.getString("personal_info"); 
        //Devise token
        mAuthToken = jObject.getString("authentication_token");
        
        //Facebook token
        mToken = token;
	}
	
	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getAuthToken() {
		return mAuthToken;
	}

	public void setAuthToken(String authToken) {
		this.mAuthToken = authToken;
	}
	
	public String getPersonalInfo() {
		return mPersonalInfo;
	}

	// If we eventually allow user to modify their info 
	public void setmPersonalInfo(String mPersonalInfo) {
		this.mPersonalInfo = mPersonalInfo;
	}

	public String getEmail() {
		return mEmail;
	}

	public String getId() {
		return mId;
	}

	public String getToken() {
		return mToken;
	}
}
