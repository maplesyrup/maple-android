package com.additt.browsing;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.additt.maple_android.MapleHttpClient;
import com.additt.maple_android.R;
import com.additt.maple_android.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PopularAdsActivity extends BrowseActivity {

	private static final String TAG = "PopularAds";
	// the view we are using to display the ads
	private GridView mGridview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("Popular");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popular_ads);
		
		mGridview = (GridView) findViewById(R.id.gridviewAdsPopular);
		RequestParams params = new RequestParams();
		User appUser = mApp.getUser();
		if (appUser != null) {
			params.put("auth_token", appUser.getAuthToken());
		}
		populateView(params, appUser.getAuthToken());
		// On Click event for Single Gridview Item
        mGridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Log.d(TAG, "clicked item");
            	Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                DisplayAd displayAd = (DisplayAd) parent.getAdapter().getItem(position);
				i.putExtra("url", displayAd.getUrl());
                i.putExtra("title", displayAd.getTitle());
                startActivity(i);
            }
        });
		
		String success = getIntent().getStringExtra("successMessage");
		if (success != null) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, success, Toast.LENGTH_LONG);
			toast.show();	
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
			    		Log.d(TAG, "Adding to arrlist");
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
						mGridview.setAdapter(new ImageAdapter(getApplicationContext(), ads, authToken));
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
