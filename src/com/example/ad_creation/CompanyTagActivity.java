package com.example.ad_creation;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.custom_views.ProgressView;
import com.example.maple_android.AdCreationManager;
import com.example.maple_android.CompanyList;
import com.example.maple_android.MapleApplication;
import com.example.maple_android.R;
import com.example.maple_android.Utility;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

public class CompanyTagActivity extends FunnelActivity {
	private MapleApplication mApp;
	private AdCreationManager mAdCreationManager;
	private AutoCompleteTextView mCompanySuggest;
	private ArrayList<String> mCompanySuggestions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_tag);

		// Init app
		mApp = (MapleApplication) this.getApplication();
		mAdCreationManager = mApp.getAdCreationManager();

		ImageButton help = (ImageButton) findViewById(R.id.helpButton);
		SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.question);
		help.setImageDrawable(svg.createPictureDrawable());
		help.setBackgroundColor(Color.BLACK);


		mAdCreationManager.setup(this);
		
		/* Set up text entry for tagging a company */
		mCompanySuggestions = CompanyList.getCompanyList(this);
		// link list of companies to text field for auto recommendations
		mCompanySuggest = (AutoCompleteTextView) findViewById(R.id.companySuggest);
		mCompanySuggest
				.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_dropdown_item_1line,
						mCompanySuggestions));
	}

	/**
	 * Set company tag and 
	 * continue to the next stage in the funnel
	 * 
	 * @param view
	 */
	public void nextStage(View view) {
		mAdCreationManager.setCompanyName(mCompanySuggest.getText().toString());
		
		mAdCreationManager.nextStage(this, mAdCreationManager.getCurrentBitmap());
	}

	/**
	 * Return to the previous stage without saving any changes
	 * 
	 * @param view
	 */
	public void prevStage(View view) {
		mAdCreationManager.previousStage(this);
	}

	public void getHelp(View v) {
		String message = "Tag your ad from a company in the database. " +
					"As you type, the field will autocomplete with possible companies.";
		String title = "Step " + mAdCreationManager.getReadableCurrentStage() + " of " + mAdCreationManager.getNumStages();
		Utility.createHelpDialog(this, message, title);
	}
	
}
