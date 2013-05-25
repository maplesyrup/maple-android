package com.additt.ad_creation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.additt.browsing.PersonalAdsActivity;
import com.additt.custom_views.ProgressView;
import com.additt.maple_android.Campaign;
import com.additt.maple_android.MapleHttpClient;
import com.additt.maple_android.R;
import com.additt.maple_android.Utility;
import com.facebook.Session;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PublishActivity extends FunnelActivity implements OnItemSelectedListener {
	private ImageView mAdView;
	private ProgressView mProgressBar;
	private RelativeLayout mLoading;
	private Spinner mCampaignSpinner;
	private String mCampaignId;
	
	private final String NONE = "None";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setCustomContent(R.layout.activity_publish);
	
		mConfig.put(Config.HELP_MESSAGE, "You're done! Congrats");
		mConfig.put(Config.NAME, "Publish");
		
		setNextBtn(R.drawable.check, R.drawable.check_pressed);

		mAdCreationManager.setup(this);
		
		mCampaignSpinner = (Spinner) findViewById(R.id.campaign_spinner);
		ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getCampaignList());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mCampaignSpinner.setAdapter(adapter);
		mCampaignSpinner.setOnItemSelectedListener(this);
		mLoading = (RelativeLayout) findViewById(R.id.ad_loading);
	}

	private ArrayList<String> getCampaignList() {
		ArrayList<String> campaignList = new ArrayList<String>();
		campaignList.add(NONE);
		for (Campaign c : mAdCreationManager.getCompany().getCampaigns()) {
			campaignList.add(c.getTitle());
		}
		return campaignList;
	}
	
	public void publish(View view) {
		// get user's session details
		//TODO: Handle session error edge cases?
		Session session = Session.getActiveSession();
		
		EditText contentView = (EditText) findViewById(R.id.ad_content);
		EditText titleView = (EditText) findViewById(R.id.ad_title);

		
		Bitmap currBitmap = mApp.getAdCreationManager().getCurrentBitmap();
		Uri fileUri = mApp.getAdCreationManager().getFileUri();
		Utility.saveBitmap(fileUri, currBitmap, this);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		currBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] photoByteArray = stream.toByteArray();
		
		RequestParams params = new RequestParams();
		params.put("post[image]", new ByteArrayInputStream(photoByteArray), fileUri.getPath());
		params.put("post[title]", titleView.getText().toString());
		params.put("post[content]", contentView.getText().toString());
		params.put("post[company_id]", Integer.toString(mAdCreationManager.getCompany().getId()));
		if (mCampaignId != null) {
			params.put("post[campaign_id]", mCampaignId);
		}
		params.put("token", session.getAccessToken());
		mLoading.setVisibility(View.VISIBLE);
		MapleHttpClient.post("posts", params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, String response) {
				Intent i = new Intent(PublishActivity.this, PersonalAdsActivity.class);
				i.putExtra("successMessage", "Posted picture successfully!");
				startActivity(i);
				mLoading.setVisibility(View.GONE);

				
			}
			
			@Override
		    public void onFailure(Throwable error, String response) {
				Toast.makeText(getApplicationContext(), "Sugar! We ran into a problem!", Toast.LENGTH_LONG).show();
				mLoading.setVisibility(View.GONE);
			}
		});
	}
	/**
	 * Publish the ad to the website
	 * @param view
	 */
	public void nextStage(View view) {
		selectNext();
		publish(view);
	}

	/**
	 * Return to the previous stage without saving any changes
	 * 
	 * @param view
	 */
	public void prevStage(View view) {
		selectPrev();
		mAdCreationManager.previousStage(this);
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
			long id) {
		
		if (pos > 0) {
			// Subtract 1 for the offset of NONE
			Campaign c = mAdCreationManager.getCompany().getCampaigns().get(pos - 1);
			mCampaignId = c.getId();
			Toast.makeText(PublishActivity.this, "Your ad is now part of the " + c.getTitle() + " campaign!", Toast.LENGTH_SHORT).show();
		} else {
			mCampaignId = null;
			Toast.makeText(PublishActivity.this, "Your ad is not part of any campaign.", Toast.LENGTH_SHORT).show();

		}		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
}
