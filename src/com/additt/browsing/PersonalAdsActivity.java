package com.additt.browsing;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.additt.maple_android.MapleHttpClient;
import com.additt.maple_android.R;
import com.additt.maple_android.User;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PersonalAdsActivity extends BrowseActivity {

	private ProfilePictureView mProfilePictureView;
	// the view we are using to display the ads
	private GridView mGridview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("My Ads");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_ads);

		mGridview = (GridView) findViewById(R.id.gridviewAdsPersonal);
		
		RequestParams params = new RequestParams();
		User appUser = mApp.getUser();
		if (appUser != null) {
			params.put("auth_token", appUser.getAuthToken());
			params.put("user_id", appUser.getId());
			populateView(params, appUser.getAuthToken());
		}
		
		// success message after posting an ad
		String success = getIntent().getStringExtra("successMessage");
		if (success != null) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, success, Toast.LENGTH_LONG);
			toast.show();	
		}
		
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
		
	}
	
	public void populateView(RequestParams params, final String authToken) {
		MapleHttpClient.get("posts", params, new AsyncHttpResponseHandler(){
			// Example request: http://maplesyrup.herokuapp.com/posts?user_id=3
			@Override
			public void onSuccess(int statusCode, String response) {
				ArrayList<DisplayAd> ads = new ArrayList<DisplayAd>();
				try {
					JSONArray jObjectAds = new JSONArray(response);
			    	// Build up ArrayList of DisplayAds from JSON array
			    	for (int i = 0; i < jObjectAds.length(); i++) {
			    		DisplayAd dAd = new DisplayAd(jObjectAds.getJSONObject(i));
			    		ads.add(dAd);
			    	}
			    	if (ads.size() == 0) {
						((RelativeLayout) mGridview.getParent()).removeView(mGridview);
						TextView adsTitle = (TextView) findViewById(R.id.adsTitle);
						adsTitle.setText("There are no ads to show; you should create one!");
						LayoutParams p = (LayoutParams) mGridview.getLayoutParams();
						p.addRule(RelativeLayout.CENTER_HORIZONTAL);
						adsTitle.setLayoutParams(p);
						adsTitle.setTextSize(22);
						adsTitle.setTypeface(null, Typeface.BOLD);
					} else {
						mGridview.setAdapter(new ImageAdapterPopular(getApplicationContext(), ads, authToken));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					// TODO: sometimes mGridview.SetAdapter crashes with a null pointer. Which one of these
					// is null in that case? Need to figure this bug out
					System.out.println("application context:" + getApplicationContext());
					System.out.println("authToken:" + mApp.getUser());
				}
			}
			
			@Override
			public void onFailure(Throwable error, String response) {
				Toast.makeText(getApplicationContext(), "Sugar! We ran into a problem fetching user ads!", Toast.LENGTH_LONG).show();
		    }
		});
	}
	
	
	@Override
	public void onStart() {
		/* Super handles starting tracking */
		super.onStart();
	}

	
	@Override
	public void onStop() {
		/* Super handles stopping tracking */
		super.onStop();
	}
}
