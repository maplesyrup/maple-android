package com.example.maple_android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ad_creation.NewAdActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

public class MainActivity extends Activity {
	/* Global app data */
	MapleApplication app;
	
	private static final String TAG = "MainActivity";
	private static final int CAMERA_REQUEST = 1888;
	private ProfilePictureView mProfilePictureView;
	private Uri mFileUri;
	private Session mSession;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//init app data
		app = (MapleApplication) getApplication();
		
		mSession = Session.getActiveSession();
		// If user isn't logged in we need to redirect back to LoginActivity
		if (mSession == null) {
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);
		}
		
		// list of supported tags in textview: http://commonsware.com/blog/Android/2010/05/26/html-tags-supported-by-textview.html
		// no support for li tag
		String htmlStr = "<h1>Sticky Advertising with Maple: publish your ad in 30 seconds</h1>" +
				"&#8226; Take a picture<br/>" +
				"&#8226; Add a logo, text, and tag a company<br/>" +
				"&#8226; Publish with a click of a button to http://maplesyrup.herokuapp.com/ and get votes!<br/>";
		((TextView) findViewById(R.id.tvInstructions)).setText(Html.fromHtml(htmlStr));
		
		mProfilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
		mProfilePictureView.setCropped(true);
		
		Intent i = getIntent();
		if (i == null || i.getExtras() == null) {
			return;
		}
		String success = getIntent().getExtras().getString("successMessage");
		if (success != null) {
			Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
		}
		if (mSession.isOpened()) {
			// make request to the /me API
			Request.executeMeRequestAsync(mSession,
					new Request.GraphUserCallback() {
						// callback after Graph API response with user object
						@Override
						public void onCompleted(GraphUser user,
							Response response) {
							if (user != null) {
								TextView greeting = (TextView) findViewById(R.id.greeting);
								greeting.setText("Welcome " + user.getName() + "!");
							    mProfilePictureView.setProfileId(user.getId());							
							}
						}
					});
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "Receiving image");
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			// Load bitmap into byteArray so that we can pass the data to the
			// new Activity
			
			Bitmap currBitmap = Utility.retrieveBitmap(mFileUri, 240, 320);
			
			app.initAdCreationManager(currBitmap, mFileUri);		

			// Reset companyTag from any previous ad creations
			app.setCurrentCompany(null);

			Intent intent = new Intent(this, EditorActivity.class);
			startActivity(intent);
		}
	}
	
	/** Launch a new ad creation process 
	 * 
	 * @param view
	 */
	public void createNewAd(View view){
		Intent intent = new Intent(this, NewAdActivity.class);
		startActivity(intent);
	}
	
	

	public void openCamera(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mFileUri = Utility.getOutputMediaFileUri(Utility.MEDIA_TYPE_IMAGE); 
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);

		startActivityForResult(intent, CAMERA_REQUEST);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// respond to menu item selection
		switch (item.getItemId()) {
		case R.id.logout:
			onClickLogout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void onClickLogout() {
		Session session = Session.getActiveSession();
		if (session != null && !session.isClosed()) {
			session.closeAndClearTokenInformation();
		}
		Intent i = new Intent(this, LoginActivity.class);
		startActivity(i);
	}

}