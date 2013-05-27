package com.additt.ad_creation;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;

import com.additt.custom_views.MapleTextView;
import com.additt.maple_android.R;
import com.additt.maple_android.StyleList;
import com.twotoasters.android.horizontalimagescroller.image.ImageToLoad;
import com.twotoasters.android.horizontalimagescroller.image.ImageToLoadDrawableResource;
import com.twotoasters.android.horizontalimagescroller.image.ImageToLoadUrl;
import com.twotoasters.android.horizontalimagescroller.widget.HorizontalImageScroller;
import com.twotoasters.android.horizontalimagescroller.widget.HorizontalImageScrollerAdapter;
import com.twotoasters.android.horizontalimagescroller.widget.TextStyle;

public class TextActivity extends FunnelActivity {

	// to show text styles
	private HorizontalImageScroller mScroller;
	private ArrayList<TextStyle> mStyles;

	private MapleTextView mAdView;

	// the background color of the scroller styles
	private final int FRAME_COLOR = Color.BLACK; 
	// the color behind the selected style
	private final int FRAME_SELECTED_COLOR = Color.rgb(247, 187, 57); 
	private final int SCROLLER_VIEW = R.layout.horizontal_image_scroller_item;
	// keep track of the last selected frame 
	private FrameLayout mLastFrame;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setCustomContent(R.layout.activity_text);

		mConfig.put(Config.HELP_MESSAGE,
				"Add humorous, sincere, or sophic text to your ad, and decide where to put it.");
		mConfig.put(Config.NAME, "Text");
		
		mAdView = (MapleTextView) findViewById(R.id.ad);
		mAdView.setAd(mAdCreationManager.getCurrentBitmap(), mAdCreationManager.getRatio());
		mAdCreationManager.setup(this);
		

		/******************************************************/

		/******* Set up scroller for style options ************/
		// set up the scroller with an adapter populated with the list of
		// ImageToLoad objects
		mScroller = (HorizontalImageScroller) findViewById(R.id.styleScroller);
		
		// get list of text styles
		mStyles = StyleList.getStyles();

		// Load the preview image for each style
		List<ImageToLoad> images = new ArrayList<ImageToLoad>();
		for (int i = 0; i < mStyles.size(); i++) {
			images.add(new ImageToLoadDrawableResource(mStyles.get(i).getPreview()));
		}

		// set adapter options
		HorizontalImageScrollerAdapter adapter = new HorizontalImageScrollerAdapter(
				this, images);
		
		// shows the frame around the view
		adapter.setShowImageFrame(true);
		// only shows frame when item is selected
		adapter.setHighlightActiveImage(true);
		// the background color when selected
		adapter.setFrameColor(FRAME_SELECTED_COLOR);
		// the default background color
		adapter.setFrameOffColor(FRAME_COLOR);
		// which view to use for layout
		adapter.setImageLayoutResourceId(SCROLLER_VIEW);
	
		mScroller.setAdapter(adapter);
		
		// set scroller, so styles can be hidden when there is no text
		mAdView.setStyleScroller(mScroller);

		// default to first style in the list
		mAdView.setStyle(mStyles.get(0));
		
		// add callback function when image in scroller is selected
		mScroller.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// visually select the chosen style				
				mScroller.setCurrentImageIndex(pos);
				
				// update the text with the new style
				mAdView.setStyle(mStyles.get(pos));
			}
		});
		/******************************************************/

	}

	/**
	 * Save ad and continue to the next stage in the funnel
	 * 
	 * @param view
	 */
	public void nextStage(View view) {
		selectNext();

		Bitmap bmOverlay = mAdView.addText();
		mAdCreationManager.nextStage(this, bmOverlay);
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
	
	
}
