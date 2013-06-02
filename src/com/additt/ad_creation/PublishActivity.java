package com.additt.ad_creation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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

public class PublishActivity extends FunnelActivity implements
		OnItemSelectedListener {
	private ImageView mAdView;
	private Spinner mCampaignSpinner;
	private String mCampaignId;
	private ProgressDialog mProgressDialog;

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
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item,
				getCampaignList());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mCampaignSpinner.setAdapter(adapter);
		mCampaignSpinner.setOnItemSelectedListener(this);
	}

	private ArrayList<String> getCampaignList() {
		ArrayList<String> campaignList = new ArrayList<String>();
		campaignList.add(NONE);
		for (Campaign c : mAdCreationManager.getCompany().getCampaigns()) {
			campaignList.add(c.getTitle());
		}
		return campaignList;
	}

	/**
	 * Publish the ad to the website
	 * 
	 * @param view
	 */
	public void nextStage(View view) {
		selectNext();

		PublishTask task = new PublishTask();
		task.execute();
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
			Campaign c = mAdCreationManager.getCompany().getCampaigns()
					.get(pos - 1);
			mCampaignId = c.getId();
			Toast.makeText(
					PublishActivity.this,
					"Your ad is now part of the " + c.getTitle() + " campaign!",
					Toast.LENGTH_SHORT).show();
		} else {
			mCampaignId = null;
			Toast.makeText(PublishActivity.this,
					"Your ad is not part of any campaign.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	/**
	 * Asynchronous class to handle the publishing. This makes the ui a lot more
	 * responsive instead of lagging when the publish button is pressed.
	 * 
	 * @author Eli
	 * 
	 */
	private class PublishTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog mProgressDialog;

		/** No parameters needed. Use .execute()
		 * to run once the task is created.
		 */
		public PublishTask() {}

		@Override
		protected void onPreExecute() {
			// start loading dialog
			mProgressDialog = ProgressDialog.show(PublishActivity.this,
					"Publishing your ad", "Please wait...", true);

		};

		@Override
		protected Void doInBackground(Void... params) {
			// get user's session details
			// TODO: Handle session error edge cases?
			Session session = Session.getActiveSession();

			EditText contentView = (EditText) findViewById(R.id.ad_content);
			EditText titleView = (EditText) findViewById(R.id.ad_title);

			Bitmap currBitmap = mApp.getAdCreationManager().getCurrentBitmap();
			Uri fileUri = mApp.getAdCreationManager().getFileUri();

			/*
			 * Save the finished ad. Fails for some reason. Also, we shouldn't
			 * overwrite the original.
			 */
			// Utility.saveBitmap(fileUri, currBitmap, this);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			currBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] photoByteArray = stream.toByteArray();

			// create post request parameters
			RequestParams requestParams = new RequestParams();
			requestParams.put("post[image]", new ByteArrayInputStream(
					photoByteArray), fileUri.getPath());
			requestParams.put("post[title]", titleView.getText().toString());
			requestParams
					.put("post[content]", contentView.getText().toString());
			requestParams.put("post[company_id]",
					Integer.toString(mAdCreationManager.getCompany().getId()));
			if (mCampaignId != null) {
				requestParams.put("post[campaign_id]", mCampaignId);
			}
			requestParams.put("token", session.getAccessToken());

			// do post in separate thread
			MapleHttpClient.post("posts", requestParams,
					new AsyncHttpResponseHandler() {

						@Override
						public void onStart() {
						}

						@Override
						public void onSuccess(int statusCode, String response) {
							Intent i = new Intent(PublishActivity.this,
									PersonalAdsActivity.class);
							i.putExtra("successMessage",
									"Your ad was submitted!");
							startActivity(i);
						}

						@Override
						public void onFailure(Throwable error, String response) {
							Toast.makeText(
									getApplicationContext(),
									"Oops, we ran into a problem! Please try again :)",
									Toast.LENGTH_LONG).show();

							// log error
							//TODO: Submit errors to additt
							//System.out.println(response);
							//error.printStackTrace();

							// allow them to try to submit again
							enableNext();
						}

						@Override
						public void onFinish() {
							mProgressDialog.dismiss();
							mProgressDialog = null;
						}
					});

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		};
	}

}
