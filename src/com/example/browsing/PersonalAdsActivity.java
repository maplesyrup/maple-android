package com.example.browsing;

import android.os.Bundle;
import android.widget.TextView;

import com.example.maple_android.MapleApplication;
import com.example.maple_android.R;
import com.example.maple_android.User;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.loopj.android.http.RequestParams;

public class PersonalAdsActivity extends BrowseActivity {

	private ProfilePictureView mProfilePictureView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("My Ads");
		super.onCreate(savedInstanceState);

		super.setLayout(R.layout.activity_personal_ads);
		Session session = Session.getActiveSession();
		mProfilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
		mProfilePictureView.setCropped(true);
		if (session.isOpened()) {
			// make request to the /me API
			Request.executeMeRequestAsync(session,
					new Request.GraphUserCallback() {
						// callback after Graph API response with user object
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if (user != null) {
								TextView greeting = (TextView) findViewById(R.id.selection_user_name);
								greeting.setText(user.getName());
							    mProfilePictureView.setProfileId(user.getId());							
							}
						}
					});
		}
		MapleApplication mApp = (MapleApplication) this.getApplication();
		User appUser = mApp.getUser();
		
		RequestParams params = new RequestParams();
		// No params means just getting the most popular ads
		params.put("user_id", appUser.getId());
		super.requestUserAds(params);
	}
}
