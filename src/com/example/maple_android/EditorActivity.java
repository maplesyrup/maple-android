package com.example.maple_android;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.facebook.Session;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.*;

public class EditorActivity extends Activity implements OnItemSelectedListener {
	
	
	/* Global app data */
	MapleApplication app;

	private ImageView photo;
	private Spinner filterSpinner;
	private boolean mInitializedView;

	/* for tagging a company */
	private String companyTag;
	private AutoCompleteTextView companySuggest;
	private ArrayList<String> companySuggestions;
	private boolean tagSet = false; // whether or not a company tag has been set
	private Session session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		
		//init app data
		app = (MapleApplication) getApplication();
		mInitializedView = true;
		
		session = Session.getActiveSession();
		// If user isn't logged in we need to redirect back to LoginActivity
		if (session == null) {
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);
		}
		
		Bundle extras = getIntent().getExtras();
		
		filterSpinner = (Spinner) findViewById(R.id.filters);
		filterSpinner.setOnItemSelectedListener(this);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.filters_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		filterSpinner.setAdapter(adapter);


		photo = (ImageView) this.findViewById(R.id.photo);
		photo.setImageBitmap(app.mAdCreationManager.getCurrentBitmap());

		/* for tagging a company */
		companySuggestions = CompanyList.getCompanyList(this);

		companySuggest = (AutoCompleteTextView) findViewById(R.id.companySuggest);
		companySuggest
				.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_dropdown_item_1line,
						companySuggestions));

		// check if a company tag has already been set
		String tag = app.getCurrentCompany();
		if (tag != null) {
			// if it was previously set, update the display to show this
			companySuggest.setText(tag);
			tagPicture(companySuggest);
		}

	}

	public void returnToMain(View view) {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}

	public void tagPicture(View view) {		
		// save company tag
		companyTag = companySuggest.getText().toString();
		app.setCurrentCompany(companyTag);

		// update header
		((TextView) this.findViewById(R.id.header))
				.setText("Customize Your Ad");

		// set company Button
		((Button) findViewById(R.id.changeTag)).setText(companyTag);

		toggleOptions();
	}

	// when called, this function toggles the ability to change the company tag
	// and the options to edit the ad.
	private void toggleOptions() {
		// toggle saved boolean
		tagSet = !tagSet;

		int taggingOptions;
		int editOptions;

		// if tagset is false, hide the edit options and show the tagging
		// options
		if (tagSet) {
			taggingOptions = View.INVISIBLE;
			editOptions = View.VISIBLE;
		} else {
			taggingOptions = View.VISIBLE;
			editOptions = View.INVISIBLE;
		}

		// tagging options
		findViewById(R.id.companySuggest).setVisibility(taggingOptions);
		findViewById(R.id.tagButton).setVisibility(taggingOptions);

		// edit options
		filterSpinner.setVisibility(editOptions);
		findViewById(R.id.post).setVisibility(editOptions);
		findViewById(R.id.logo).setVisibility(editOptions);
		findViewById(R.id.text).setVisibility(editOptions);
		findViewById(R.id.changeTag).setVisibility(editOptions);
		findViewById(R.id.spinnerText).setVisibility(editOptions);

	}

	// lets the user go back and edit the company tag
	public void changeTag(View view) {
		toggleOptions();
	}

	public void addLogo(View view) {
		Intent i = new Intent(this, LogoActivity.class);
		startActivity(i);
	}

	public void addText(View view) {
		Intent i = new Intent(this, TextActivity.class);
		startActivity(i);
	}

	public void postAd(View view) {
		Utility.saveBitmap(app.mAdCreationManager.getFileUri(), app.mAdCreationManager.getCurrentBitmap(), this);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		app.mAdCreationManager.getCurrentBitmap().compress(Bitmap.CompressFormat.JPEG, 90, stream);
		byte[] photoByteArray = stream.toByteArray();
		
		RequestParams params = new RequestParams();
		params.put("post[image]", new ByteArrayInputStream(photoByteArray), app.mAdCreationManager.getFileUri().getPath());
		params.put("post[title]", "Company: " + companyTag);
		params.put("token", session.getAccessToken());
		MapleHttpClient.post("posts", params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, String response) {
				Intent i = new Intent(EditorActivity.this , MainActivity.class);
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		if (mInitializedView) {
			mInitializedView = false;
			return;
		}

		String strFilter = filterSpinner.getSelectedItem().toString();
		
		app.mAdCreationManager.addFilter(strFilter);
		photo.setImageBitmap(app.mAdCreationManager.getCurrentBitmap());

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
