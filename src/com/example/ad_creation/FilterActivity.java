package com.example.ad_creation;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.custom_views.ProgressView;
import com.example.maple_android.AdCreationManager;
import com.example.maple_android.LoginActivity;
import com.example.maple_android.MapleApplication;
import com.example.maple_android.R;
import com.example.maple_android.Utility;
import com.facebook.Session;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.twotoasters.android.horizontalimagescroller.image.ImageToLoad;
import com.twotoasters.android.horizontalimagescroller.image.ImageToLoadDrawableResource;
import com.twotoasters.android.horizontalimagescroller.widget.HorizontalImageScroller;
import com.twotoasters.android.horizontalimagescroller.widget.HorizontalImageScrollerAdapter;

/**
 * This activity allows the user to add a filter to their ad
 * 
 * We are using the jhlabs filter library from 
 * http://www.jhlabs.com/ip/filters/index.html
 * 
 * To display the filters in a horizontal scrollview
 * we are using TwoToasters library
 * https://github.com/twotoasters/HorizontalImageScroller-Android
 * 
 * @author Eli
 * 
 */
public class FilterActivity extends FunnelActivity {
	
	private ProgressView mProgressBar;
	private ImageView mAdView;
	private Spinner mFilterSpinner;
	private Bitmap mOriginalAd; // the starting ad without any filters
	private Bitmap mFilteredAd; // the ad after the filter is applied
	private View mLastFilterSelected; // the last filter selected in the horizontal scroller

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		
		mConfig.put(Config.HELP_MESSAGE, "Select a filter for your photo!");
		mConfig.put(Config.NAME, "Filter");

		// initialize filtered image to original
		mOriginalAd = mAdCreationManager.getCurrentBitmap();
		mFilteredAd = mOriginalAd;
		// set imageview of ad
		mAdView = (ImageView) this.findViewById(R.id.ad);

		// make a list of ImageToLoad objects for image scroller
		ArrayList<ImageToLoad> images = new ArrayList<ImageToLoad>();
		images.add(new ImageToLoadDrawableResource(R.drawable.filter_none));
		images.add(new ImageToLoadDrawableResource(R.drawable.filter_gaussian));
		images.add(new ImageToLoadDrawableResource(R.drawable.filter_posterize));

		// set up the scroller with an adapter populated with the list of
		// ImageToLoad objects
		HorizontalImageScroller scroller = (HorizontalImageScroller) findViewById(R.id.filterScroller);
		scroller.setAdapter(new HorizontalImageScrollerAdapter(this, images));

		// add callback function when image in scroller is selected
		scroller.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// change background colors to indicate selection
				if(mLastFilterSelected != null)
					mLastFilterSelected.setBackgroundColor(Color.TRANSPARENT);
				view.setBackgroundColor(Color.BLACK);
				mLastFilterSelected = view;
				
				// apply filter to image
				mFilteredAd = mAdCreationManager.addFilter(mOriginalAd, pos);
				mAdView.setImageBitmap(mFilteredAd);
				
			}
		});

		mAdCreationManager.setup(this);
	}

	/**
	 * Save modified ad and continue to the next stage in the funnel
	 * 
	 * @param view
	 */
	public void nextStage(View view) {
		mAdCreationManager.nextStage(this, mFilteredAd);
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
