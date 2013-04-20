package com.example.ad_creation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.browsing.PopularAdsActivity;
import com.example.custom_views.ProgressView;
import com.example.maple_android.MapleHttpClient;
import com.example.maple_android.R;
import com.example.maple_android.Utility;
import com.facebook.Session;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PublishActivity extends FunnelActivity {
	private ImageView mAdView;
	private ProgressView mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setCustomContent(R.layout.activity_publish);
	
		mConfig.put(Config.HELP_MESSAGE, "You're done! Congrats");
		mConfig.put(Config.NAME, "Publish");

		mAdCreationManager.setup(this);
	}

	/**
	 * Publish the ad to the website
	 * @param view
	 */
	public void nextStage(View view) {
		// get user's session details
		//TODO: Handle session error edge cases?
		Session session = Session.getActiveSession();
		
		Bitmap currBitmap = mApp.getAdCreationManager().getCurrentBitmap();
		Uri fileUri = mApp.getAdCreationManager().getFileUri();
		Utility.saveBitmap(fileUri, currBitmap, this);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		currBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] photoByteArray = stream.toByteArray();
		
		RequestParams params = new RequestParams();
		params.put("post[image]", new ByteArrayInputStream(photoByteArray), fileUri.getPath());
		params.put("post[title]", "Company: " + mAdCreationManager.getCompanyName());
		params.put("token", session.getAccessToken());

		MapleHttpClient.post("posts", params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, String response) {
				Intent i = new Intent(PublishActivity.this, PopularAdsActivity.class);
				i.putExtra("successMessage",
						"Posted picture successfully! Go to the website to check it out.");
				startActivity(i);
			}
			
			@Override
		    public void onFailure(Throwable error, String response) {
				Toast.makeText(getApplicationContext(), "Sugar! We ran into a problem!", Toast.LENGTH_LONG).show();
		    }
		});
	}

	/**
	 * Return to the previous stage without saving any changes
	 * 
	 * @param view
	 */
	public void prevStage(View view) {
		mAdCreationManager.previousStage(this);
	}
	
}
