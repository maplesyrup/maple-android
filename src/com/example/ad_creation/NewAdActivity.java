package com.example.ad_creation;

import com.example.ad_creation.CropActivity;
import com.example.maple_android.MapleApplication;
import com.example.maple_android.R;
import com.example.maple_android.Utility;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;

/**
 * This class launches a new ad creation session. Users can either create an ad
 * using a picture they take, an image from file, or a previously saved ad.
 * 
 */
public class NewAdActivity extends Activity {
	/* Global app data */
	MapleApplication app;

	private Uri mFileUri; // the file path of the image we are going to use
	private static final int CAMERA_REQUEST = 1888;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_ad);

		// init app data
		app = (MapleApplication) getApplication();
	}

	/**
	 * Allow the user to create an ad using a picture they select from file
	 */
	public void useImageFromFile(View view) {
		// TODO: make this!
	}

	/**
	 * Allow the user to create an ad using a picture they take
	 */
	public void takeNewPicture(View view) {
		// launch the camera 
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// store the resulting picture at this URI
		mFileUri = Utility.getOutputMediaFileUri(Utility.MEDIA_TYPE_IMAGE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);

		startActivityForResult(intent, CAMERA_REQUEST);
	}

	/**
	 * Callback function after the user has taken a new picture. Use this
	 * picture to start the ad creation process
	 * 
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			// if the picture taking was successsful, start the ad creation
			startAdCreation();
		}
	}

	/**
	 * Allow the user to edit a previously saved ad
	 * 
	 */
	public void loadSavedAd(View view) {
		// load an image in file to mFileUri and start the ad creation process
		// TODO: make this!
		//startAdCreation();
	}
	
	/** Start the ad creation funnel using the selected image
	 * 
	 */
	private void startAdCreation(){
		// Scale the picture we are going to be using
		Bitmap currBitmap = Utility.retrieveBitmap(mFileUri, 240, 320);
		// create a new admanager object with the bitmap and file uri
		app.initAdCreationManager(currBitmap, mFileUri);

		// launch the first step in the ad creation funnel
		Intent intent = new Intent(this, CropActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_ad, menu);
		return true;
	}

}
