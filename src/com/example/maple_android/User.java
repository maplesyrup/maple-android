package com.example.maple_android;

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
	private String mName = "Name";
	private String mEmail = "name@email.com";
	private String mId = "3";
	private String mPersonalInfo = "A litte about me";

	public User(String jsonResponse) throws JSONException {
		JSONObject jObject = new JSONObject(jsonResponse);
		mName = jObject.getString("name");
        mEmail = jObject.getString("email");
        mId = jObject.getString("id");
        mPersonalInfo = jObject.getString("personal_info"); 
	}
	
	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmPersonalInfo() {
		return mPersonalInfo;
	}

	// If we eventually allow user to modify their info 
	public void setmPersonalInfo(String mPersonalInfo) {
		this.mPersonalInfo = mPersonalInfo;
	}

	public String getmEmail() {
		return mEmail;
	}

	public String getmId() {
		return mId;
	}
}
