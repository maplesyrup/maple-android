package com.example.ad_creation;

import com.example.custom_views.CropView;
import com.example.custom_views.ProgressView;
import com.example.maple_android.AdCreationManager;
import com.example.maple_android.LoginActivity;
import com.example.maple_android.MapleApplication;
import com.example.maple_android.R;
import com.facebook.Session;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * This activity allows the user to add a filter to their ad
 * 
 * @author Eli
 * 
 */
public class FilterActivity extends Activity implements OnItemSelectedListener  {
	private MapleApplication mApp;
	private AdCreationManager mAdCreationManager;
	private ProgressView mProgressBar;
	private Session mSession;
	private ImageView mAdView;
	private Spinner mFilterSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);

		mSession = Session.getActiveSession();
		// If user isn't logged in we need to redirect back to LoginActivity
		if (mSession == null) {
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);
		}

		mApp = (MapleApplication) this.getApplication();
		mAdCreationManager = mApp.getAdCreationManager();

		// set photo
		mAdView = (ImageView) this.findViewById(R.id.ad);
		mAdView.setImageBitmap(mAdCreationManager.getCurrentBitmap());

		ImageButton help = (ImageButton) findViewById(R.id.helpButton);
		SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.question);
		help.setImageDrawable(svg.createPictureDrawable());
		help.setBackgroundColor(Color.BLACK);
		
		
		// set up filter spinner
		mFilterSpinner = (Spinner) findViewById(R.id.filters);
		mFilterSpinner.setOnItemSelectedListener(this);

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.filters_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mFilterSpinner.setAdapter(adapter);
		mFilterSpinner.setSelection(adapter.getPosition(mApp.getAdCreationManager().getCurrentFilter().toString()));

		mProgressBar = (ProgressView) findViewById(R.id.progressBar);

		mAdCreationManager.setup(null, null, mProgressBar);
	}

	/**
	 * Save modified ad and continue to the next stage in the funnel
	 * 
	 * @param view
	 */
	public void nextStage(View view) {
		// mAdCreationManager.nextStage(this, ad);
	}

	/**
	 * Return to the previous stage without saving any changes
	 * 
	 * @param view
	 */
	public void prevStage(View view) {
		mAdCreationManager.previousStage(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {

		String strFilter = mFilterSpinner.getSelectedItem().toString();
		
		mAdCreationManager.addFilter(strFilter);
		mAdView.setImageBitmap(mAdCreationManager.getCurrentBitmap());

		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
