package com.example.ad_creation;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.maple_android.CompanyData;
import com.example.maple_android.R;

public class CompanyTagActivity extends FunnelActivity {
	private AutoCompleteTextView mCompanySuggest;
	private ArrayList<String> mCompanySuggestions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setCustomContent(R.layout.activity_company_tag);
		
		mConfig.put(Config.HELP_MESSAGE, "Tag your ad from a company in the database. " +
				"As you type, the field will autocomplete with possible companies.");
		mConfig.put(Config.NAME, "Tag");
	
		mAdCreationManager.setup(this);
		
		/* Set up text entry for tagging a company */
		mCompanySuggestions = CompanyData.getCompanyList(this);
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
	
}
