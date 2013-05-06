package com.example.ad_creation;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.example.maple_android.R;
import com.example.maple_android.StyleList;

import com.twotoasters.android.horizontalimagescroller.image.ImageToLoad;
import com.twotoasters.android.horizontalimagescroller.image.ImageToLoadUrl;
import com.twotoasters.android.horizontalimagescroller.widget.HorizontalImageScroller;
import com.twotoasters.android.horizontalimagescroller.widget.HorizontalImageScrollerAdapter;
import com.twotoasters.android.horizontalimagescroller.widget.TextStyle;

public class TextActivity extends FunnelActivity {

	// to show text styles
	private HorizontalImageScroller mScroller;
	private ArrayList<TextStyle> mStyles;

	private float mTextXPos;
	private float mTextYPos;
	private TextView mPhotoText;	

	// the background color of the scroller styles
	private final int FRAME_COLOR = Color.TRANSPARENT; 
	// the color behind the selected style
	private final int FRAME_SELECTED_COLOR = Color.rgb(247, 187, 57); 
	private final int SCROLLER_VIEW = R.layout.horizontal_image_scroller_text_style;
	// the text of the style preview shown in the scroller
	private final String SCROLLER_TEXT = "Style";
	// size of text in scroller
	private final float TEXT_SIZE = 40.0f;
	// height of text image display in scroller
	private final int TEXT_HEIGHT = 50;
	// width of text
	private final int TEXT_WIDTH = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setCustomContent(R.layout.activity_text);

		mConfig.put(Config.HELP_MESSAGE,
				"Add humorous, sincere, or sophic text to your ad, and decide where to put it.");
		mConfig.put(Config.NAME, "Text");
		
		mAdCreationManager.setup(this);

		/****** Dialog for user to enter text ****************/
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Add Text");
		alert.setMessage("Enter the text you would like to include in your ad");

		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				// Do something with value!
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		// alert.show();
		/******************************************************/

		/******* Set up scroller for style options ************/
		// set up the scroller with an adapter populated with the list of
		// ImageToLoad objects
		mScroller = (HorizontalImageScroller) findViewById(R.id.styleScroller);
		
		// get list of text styles
		mStyles = StyleList.getStyles();

		// We need to make sure not having
		// images doesn't break things, so we create as many images
		// as we have paints
		List<ImageToLoad> images = new ArrayList<ImageToLoad>();
		for (int i = 0; i < mStyles.size(); i++) {
			images.add(new ImageToLoadUrl("www.thiswillneverbeused.com"));
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
		// set up scroller to show off text styles
		adapter.setTextOnlyMode(true, SCROLLER_TEXT, TEXT_SIZE, mStyles,
				TEXT_WIDTH, TEXT_HEIGHT);

		mScroller.setAdapter(adapter);

		// start with the first company selected
		mScroller.setCurrentImageIndex(0);

		// add callback function when image in scroller is selected
		mScroller.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// Updates the background color to indicate selection
				mScroller.setCurrentImageIndex(pos);

				// do something with the selected style
				// TODO: apply mStyles.get(pos)
			}
		});
		/******************************************************/

	}

	/**
	 * This function is used to generate a bitmap from the TextView that holds
	 * the currently created text
	 * 
	 * @param v
	 *            The TextView containing the text we want to write
	 * @return The text converted to a bitmap
	 */
	private Bitmap loadBitmapFromView(View v) {
		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(0, 0, v.getWidth(), v.getHeight());
		v.draw(c);
		return b;
	}

	/**
	 * Save ad and continue to the next stage in the funnel
	 * 
	 * @param view
	 */
	public void nextStage(View view) {
		selectNext();

		// if text has been added, merge it with the ad
		Bitmap bmOverlay = mAdCreationManager.getCurrentBitmap();
		if (!mPhotoText.getText().equals("")) {
			// get text bitmap
			Bitmap textBitmap = loadBitmapFromView(mPhotoText);
			Bitmap currBitmap = mAdCreationManager.getCurrentBitmap();

			// combine two bitmaps
			bmOverlay = Bitmap.createBitmap(currBitmap.getWidth(),
					currBitmap.getHeight(), currBitmap.getConfig());
			Canvas canvas = new Canvas(bmOverlay);
			canvas.drawBitmap(currBitmap, new Matrix(), null);
			canvas.drawBitmap(
					textBitmap,
					mTextXPos / mAdCreationManager.getRatio(),
					(mTextYPos - mPhotoText.getHeight())
							/ mAdCreationManager.getRatio(), null);
		}
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

}
