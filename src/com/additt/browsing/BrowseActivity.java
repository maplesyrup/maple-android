package com.additt.browsing;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.additt.maple_android.AdCreationDialog;
import com.additt.maple_android.AdCreationManager;
import com.additt.maple_android.MapleApplication;
import com.additt.maple_android.MapleHttpClient;
import com.additt.maple_android.R;
import com.additt.maple_android.User;
import com.additt.maple_android.Utility;
import com.google.analytics.tracking.android.EasyTracker;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * This is the base class for the ad browsing tabs on the home screen
 * @author rkpandey
 *
 */
public class BrowseActivity extends SherlockActivity {
	// the view we are using to display the ads
	private GridView mGridview; 
	// Contains file uri of photo being taken
	protected MapleApplication mApp;
	private static final String TAG = "BrowseAds";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApp = (MapleApplication) getApplication();	
	}
	
	public void setGridview(GridView gridView) {
		mGridview = gridView;
	}
	
	public void requestUserAds(RequestParams params) {

		MapleHttpClient.get("posts", params, new AsyncHttpResponseHandler(){
			// Example json response: http://maplesyrup.herokuapp.com/posts?user_id=3
			@Override
			public void onSuccess(int statusCode, String response) {
				JSONArray jObjectAds = null;
				try {
					jObjectAds = new JSONArray(response);
					if (jObjectAds.length() == 0) {
						((RelativeLayout) mGridview.getParent()).removeView(mGridview);
						TextView adsTitle = (TextView) findViewById(R.id.adsTitle);
						adsTitle.setText("There are no ads to show; you should create one!");
						LayoutParams p = (LayoutParams) mGridview.getLayoutParams();
						p.addRule(RelativeLayout.CENTER_HORIZONTAL);
						adsTitle.setLayoutParams(p);
						adsTitle.setTextSize(22);
						adsTitle.setTypeface(null, Typeface.BOLD);
					} else {
						User appUser = mApp.getUser();
						mGridview.setAdapter(new ImageAdapter(getApplicationContext(), jObjectAds, appUser.getAuthToken()));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					// TODO: sometimes mGridview.SetAdapter crashes with a null pointer. Which one of these
					// is null in that case? Need to figure this bug out
					System.out.println("gridview:" + mGridview);
					System.out.println("application context:" + getApplicationContext());
					System.out.println("jObjectAds:" + jObjectAds);
					System.out.println("authToken:" + mApp.getUser());
				}
			}
			
			@Override
		    public void onFailure(Throwable error, String response) {
				Toast.makeText(getApplicationContext(), "Sugar! We ran into a problem fetching user ads!", Toast.LENGTH_LONG).show();
		    }
		});
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
