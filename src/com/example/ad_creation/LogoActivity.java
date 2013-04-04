package com.example.ad_creation;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.example.maple_android.AdCreationManager;
import com.example.maple_android.EditorActivity;
import com.example.maple_android.MapleApplication;
import com.example.maple_android.R;
import com.example.maple_android.Utility;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LogoActivity extends Activity {
	private MapleApplication mApp;
	private AdCreationManager mAdCreationManager;

	private ImageView mPhoto; // the image view showing the current ad

	/* Logo details */
	private ImageView mLogoView; // ImageView storing the logo to overlay. This
									// view is shown on top of mPhoto
	private Bitmap mLogoSrc = null; // the original source bitmap of the logo.
	private Bitmap mLogoScaled = null; // the scaled logo that is shown
	private int mLogoWidth;
	private int mLogoHeight;
	private final double SCALE_FACTOR = 0.3; // the multiplier that the logo is
												// scaled with on each increase
												// or decrease
	private float mLogoXOffset; // the logo x position in relation to the photo
								// bitmap
	private float mLogoYOffset; // the logo y position in relation to the photo
								// bitmap

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);

		// Init app
		mApp = (MapleApplication) this.getApplication();
		mAdCreationManager = mApp.getAdCreationManager();

		// set photo
		mPhoto = (ImageView) this.findViewById(R.id.photo);
		mPhoto.setImageBitmap(mApp.getAdCreationManager().getCurrentBitmap());

		// initialize photo for clicking
		mPhoto.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				placeLogo(v, event);
				/*
				 * This callback function requires us to return a boolean.
				 * Return true just to appease it.
				 */
				return true;
			}
		});

		// Update page title to reflect the company
		TextView title = (TextView) this.findViewById(R.id.companyTag);
		title.setText("Add A " + mAdCreationManager.getCompanyName() + " Logo!");

		// Load Logo.
		// logoArray will only be non null if the user picked
		// a logo in the LogoPickerActivity. Otherwise we have to
		// direct them there to pick a logo before they can use one

		Bitmap logo = mAdCreationManager.getCompanyLogo();

		if (logo != null) {
			// save copy of the logo as bmp
			mLogoView = (ImageView) this.findViewById(R.id.logoPic);
			mLogoSrc = logo;

			// initialize for scaling
			mLogoWidth = mLogoSrc.getWidth();
			mLogoHeight = mLogoSrc.getHeight();
			mLogoScaled = mLogoSrc;

			// scale logo to a quarter of picture size
			while (mLogoScaled.getWidth() > mApp.getAdCreationManager()
					.getCurrentBitmap().getWidth()) {

				changeLogoSize(findViewById(R.id.decreaseSize));
			}

			// set logo bitmap to view
			mLogoView.setImageBitmap(mLogoSrc);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logo, menu);
		return true;
	}

	/**
	 * Called when the user clicks on the picture. This call back uses the click
	 * event to move the logo to the click location.
	 * 
	 * @param v
	 *            The picture that was clicked
	 * @param event
	 *            The click event that holds the coordinates
	 */
	private void placeLogo(View v, MotionEvent event) {
		// only allow them to place a logo if one has been picked
		// from the LogoPickerActivity
		if (mLogoSrc != null) {
			// hide instructions
			findViewById(R.id.logoInstructions).setVisibility(View.INVISIBLE);

			// show logo
			mLogoView.setVisibility(View.VISIBLE);

			mLogoXOffset = event.getX() - mLogoWidth / 2;
			mLogoYOffset = event.getY() - mLogoHeight / 2;

			mLogoView.setX(mLogoXOffset + v.getX());
			mLogoView.setY(mLogoYOffset + v.getY());
		}
	}

	/**
	 * Called when the user changes the logo size Both the increase and decrease
	 * logo size buttons call this method. We check the view id to see which one
	 * we were called by and scale the logo accordingly.
	 * 
	 * @param view
	 *            The button that was clicked
	 */
	public void changeLogoSize(View view) {
		// check if we are decreasing or increasing size
		// based on which button made the method call
		double modifier = SCALE_FACTOR;
		if (view.equals(findViewById(R.id.decreaseSize)))
			modifier *= -1.0;

		// change logo dimensions
		mLogoWidth = (int) (mLogoWidth * (1 + modifier));
		mLogoHeight = (int) (mLogoHeight * (1 + modifier));

		// Use source bitmap to make new scaled image to get best quality.
		// update the logo view with the new bitmap
		// make filter flag true to improve quality.
		mLogoScaled = Bitmap.createScaledBitmap(mLogoSrc, mLogoWidth,
				mLogoHeight, true);
		mLogoView.setImageBitmap(mLogoScaled);
	}

	/**
	 * Launch an activity that allows the user to choose a logo for the selected
	 * company. When the activity returns to the LogoPicker it will include a
	 * logo bytestream in the intent if the user successfully picked a logo.
	 * This is checked for in onCreate
	 * 
	 * @param view
	 *            The button that was clicked
	 */
	public void launchLogoPicker(View view) {
		Intent i = new Intent(this, LogoPickerActivity.class);
		startActivity(i);
	}

	/**
	 * Save modified ad and continue to the next stage in the funnel
	 * 
	 * @param view
	 */
	public void nextStage(View view) {
		// combine logo bitmap and ad bitmap
		Bitmap currBitmap = mApp.getAdCreationManager().getCurrentBitmap();
		Bitmap bmOverlay = Bitmap.createBitmap(currBitmap.getWidth(),
				currBitmap.getHeight(), currBitmap.getConfig());
		Canvas canvas = new Canvas(bmOverlay);
		canvas.drawBitmap(currBitmap, new Matrix(), null);
		canvas.drawBitmap(mLogoScaled, mLogoXOffset, mLogoYOffset, null);

		// pushed modified ad to stack
		mAdCreationManager.pushBitmap(bmOverlay);
		
		mAdCreationManager.nextStage(this);
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
