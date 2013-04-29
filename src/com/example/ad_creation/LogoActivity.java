package com.example.ad_creation;

import java.util.ArrayList;

import com.example.custom_views.LogoView;
import com.example.custom_views.ProgressView;
import com.example.maple_android.AdCreationManager;
import com.example.maple_android.Company;
import com.example.maple_android.MapleApplication;
import com.example.maple_android.R;
import com.example.maple_android.Utility;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.twotoasters.android.horizontalimagescroller.image.ImageToLoad;
import com.twotoasters.android.horizontalimagescroller.image.ImageToLoadUrl;
import com.twotoasters.android.horizontalimagescroller.widget.HorizontalImageScroller;
import com.twotoasters.android.horizontalimagescroller.widget.HorizontalImageScrollerAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LogoActivity extends FunnelActivity {
	private LogoView mLogoView;
	private HorizontalImageScroller mScroller;

	private static final int FRAME_COLOR = Color.TRANSPARENT; 
	private static final int FRAME_SELECTED_COLOR = Color.BLACK; 
	private static final int SCROLLER_VIEW = R.layout.horizontal_image_scroller_with_text_item;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setCustomContent(R.layout.activity_logo);

		mConfig.put(Config.HELP_MESSAGE,
				"Select which company logo you want to place on the ad, and move it around!");
		mConfig.put(Config.NAME, "Logo");

		mLogoView = (LogoView) findViewById(R.id.ad);
		mLogoView.setAd(mAdCreationManager.getCurrentBitmap(), mAdCreationManager.getRatio());

		mAdCreationManager.setup(this);

		// Load Logo.
		// logoArray will only be non null if the user picked
		// a logo in the LogoPickerActivity. Otherwise we have to
		// direct them there to pick a logo before they can use one

		Bitmap logo = mAdCreationManager.getCompanyLogo();

		if (logo != null) {
			mLogoView.setLogo(logo, 0, 0);
		}

//		// make a list of ImageToLoad objects for image scroller
//		ArrayList<ImageToLoad> logos = new ArrayList<ImageToLoad>();
//		ArrayList<LogoURL> urls = mAdCreationManager.getCompany().getLogoUrls();
//		for (LogoURL url : urls) {
//			logos.add(new ImageToLoadUrl(url.getThumb()));
//		}
//
//		// set up the scroller with an adapter populated with the list of
//		// ImageToLoad objects
//		mScroller = (HorizontalImageScroller) findViewById(R.id.companyScroller);
//		HorizontalImageScrollerAdapter adapter = new HorizontalImageScrollerAdapter(
//				this, logos);
//
//		// set adapter options
//		// shows the frame around the view
//		adapter.setShowImageFrame(true);
//		// only shows frame when item is selected
//		adapter.setHighlightActiveImage(true);
//		// the background color when selected
//		adapter.setFrameColor(FRAME_SELECTED_COLOR);
//		// the default background color
//		adapter.setFrameOffColor(FRAME_COLOR);
//		// set image to be used while loading
//		adapter.setLoadingImageResourceId(R.drawable.maple);
//		adapter.setImageLayoutResourceId(SCROLLER_VIEW);
//
//		mScroller.setAdapter(adapter);
//
//		// start with the first company selected
//		mScroller.setCurrentImageIndex(0);
//
//		// add callback function when image in scroller is selected
//		mScroller.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int pos,
//					long id) {
//				// Updates the background color to indicate selection
//				mScroller.setCurrentImageIndex(pos);
//
//				// change logoview to display this image
//				// TODO: Once heroku and logoview are working 
//			}
//		});
	}

	/**
	 * Save modified ad and continue to the next stage in the funnel
	 * 
	 * @param view
	 */
	public void nextStage(View view) {
		selectNext();

		// update bitmap to include logo
		Bitmap ad = mLogoView.addLogo();
		// null is returned if a logo hasn't been set
		if (ad == null)
			ad = mAdCreationManager.getCurrentBitmap();
		mAdCreationManager.nextStage(this, ad);
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

}
