package com.example.ad_creation;

import com.example.maple_android.AdCreationManager;
import com.example.maple_android.MapleApplication;
import com.example.maple_android.R;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.SeekBar;

public class ColorAdjustmentActivity extends Activity {
	/* Global app */
	private MapleApplication mApp;
	private AdCreationManager mAdCreationManager;

	private Bitmap mOriginalAd;
	private Bitmap mAdjustedAd;
	private ImageView mAdView;

	/* Variables for color adjustment */
	private double mGammaRed = 1.0;
	private double mGammaGreen = 1.0;
	private double mGammaBlue = 1.0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_adjustment);

		// Init app
		mApp = (MapleApplication) this.getApplication();
		mAdCreationManager = mApp.getAdCreationManager();

		// get most recent ad of stack
		mOriginalAd = mAdCreationManager.getCurrentBitmap();
		mAdView = (ImageView) findViewById(R.id.colorAdjustPhoto);
		mAdView.setImageBitmap(mOriginalAd);

		// set up gamma slider callbacks
		// red
		((SeekBar) findViewById(R.id.gammaRedSeek))
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						int progress = seekBar.getProgress();
						mGammaRed = progress / 10.0;
						doGamma();
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

					}
				});

		// green
		((SeekBar) findViewById(R.id.gammaGreenSeek))
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						int progress = seekBar.getProgress();
						mGammaGreen = progress / 10.0;
						doGamma();
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

					}
				});

		// blue
		((SeekBar) findViewById(R.id.gammaBlueSeek))
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						int progress = seekBar.getProgress();
						mGammaBlue = progress / 10.0;
						doGamma();
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

					}
				});

	}

	/**
	 * Updates the current ad with the gamma values stored in the gamma private
	 * instance variables
	 */
	private void doGamma() {
		// create output image
		Bitmap bmOut = Bitmap.createBitmap(mOriginalAd.getWidth(),
				mOriginalAd.getHeight(), mOriginalAd.getConfig());
		// get image size
		int width = mOriginalAd.getWidth();
		int height = mOriginalAd.getHeight();
		// color information
		int A, R, G, B;
		int pixel;
		// constant value curve
		final int MAX_SIZE = 256;
		final double MAX_VALUE_DBL = 255.0;
		final int MAX_VALUE_INT = 255;
		final double REVERSE = 1.0;

		// gamma arrays
		int[] gammaR = new int[MAX_SIZE];
		int[] gammaG = new int[MAX_SIZE];
		int[] gammaB = new int[MAX_SIZE];

		// setting values for every gamma channels
		for (int i = 0; i < MAX_SIZE; ++i) {
			gammaR[i] = (int) Math.min(
					MAX_VALUE_INT,
					(int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE
							/ mGammaRed)) + 0.5));
			gammaG[i] = (int) Math.min(
					MAX_VALUE_INT,
					(int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE
							/ mGammaGreen)) + 0.5));
			gammaB[i] = (int) Math.min(
					MAX_VALUE_INT,
					(int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE
							/ mGammaBlue)) + 0.5));
		}

		// apply gamma table
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get pixel color
				pixel = mOriginalAd.getPixel(x, y);
				A = Color.alpha(pixel);
				// look up gamma
				R = gammaR[Color.red(pixel)];
				G = gammaG[Color.green(pixel)];
				B = gammaB[Color.blue(pixel)];
				// set new color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// update image
		mAdjustedAd = bmOut;
		mAdView.setImageBitmap(mAdjustedAd);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.color_adjustment, menu);
		return true;
	}

}
