package com.additt.browsing;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.additt.maple_android.AdCreationDialog;
import com.additt.maple_android.AdCreationManager;
import com.additt.maple_android.MapleApplication;
import com.additt.maple_android.R;
import com.additt.maple_android.Utility;
import com.google.analytics.tracking.android.EasyTracker;

/**
 * This is the base class for the ad browsing tabs on the home screen
 * @author rkpandey
 *
 */
public class BrowseActivity extends SherlockActivity {
	// Contains file uri of photo being taken
	protected MapleApplication mApp;
	private static final String TAG = "BrowseAds";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApp = (MapleApplication) getApplication();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSherlock().getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Respond to each tab button
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		return Utility.myOnOptionsItemSelected(this, item);
	}
	
	/**
	 * After a photo has been taken it gets routed to this function. This will retreive the
	 * photo that was saved to disk and initialize a new AdCreationManager with it
	 * 
	 * @param requestCode The code that identifies the action as coming from the camera
	 * @param resultCode The success or failure of the action
	 * @param data Various data about the photo that Android supplies us.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED) {
			return;
		}
		switch (requestCode) {

		case AdCreationDialog.CAMERA_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				// Don't actually have to do anything here						
			}
			break;
		case AdCreationDialog.PICK_IMAGE:
	        // if the image was picked from gallery, get the result Uri
			if(resultCode == Activity.RESULT_OK){  
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getContentResolver().query(
	                               selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String filePath = cursor.getString(columnIndex);
	            cursor.close();
	            // save Uri
	            mApp.setFileUri(Uri.parse(filePath));	            
	        }
	        break;
		}
		
		// Get the result bitmap and use it to init the Ad Creation Manager
		Bitmap currBitmap = Utility.retrieveBitmap(mApp.getFileUri(), AdCreationManager.AD_WIDTH, AdCreationManager.AD_HEIGHT);				
		mApp.initAdCreationManager(currBitmap, mApp.getFileUri());
		
		// Load the first activity in the ad creation funnel
		mApp.getAdCreationManager().nextStage(this, mApp.getAdCreationManager().getCurrentBitmap());
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		// start analytics tracking for this activity
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		
		// stop analytics tracking for this activity
		EasyTracker.getInstance().activityStop(this);
	}
	
}
