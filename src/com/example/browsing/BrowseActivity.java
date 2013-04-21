package com.example.browsing;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.maple_android.AdCreationDialog;
import com.example.maple_android.MapleApplication;
import com.example.maple_android.MapleHttpClient;
import com.example.maple_android.R;
import com.example.maple_android.Utility;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * This is the base class for the ad browsing tabs on the home screen
 * @author rkpandey
 *
 */
public class BrowseActivity extends Activity {
	private static final String TAG = "BrowseAds";
	private GridView mGridview; // the view we are using to display the ads
	
	// Contains file uri of photo being taken
	protected Uri mFileUri;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_ads);
		mGridview = (GridView) findViewById(R.id.gridviewAds);

		/**
         * On Click event for Single Gridview Item
         * */
        mGridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Sending image id to FullScreenActivity
                Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                String url = (String) parent.getAdapter().getItem(position);
                i.putExtra("url", url);
                startActivity(i);
            }
        });
	}
	
	public void requestUserAds(RequestParams params) {
		MapleHttpClient.get("posts", params, new AsyncHttpResponseHandler(){
			// Example json response: http://maplesyrup.herokuapp.com/posts?user_id=3
			@Override
			public void onSuccess(int statusCode, String response) {
				Log.d(TAG, response);	
				try {
					JSONArray jObjectAds = new JSONArray(response);
					mGridview.setAdapter(new ImageAdapter(getApplicationContext(), jObjectAds));
				} catch (JSONException e) {
					Log.d(TAG, "Could not parse JSON; unexpected response from the server.");	
					e.printStackTrace();
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
		getMenuInflater().inflate(R.menu.activity_main, menu);
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
		MapleApplication app = ((MapleApplication) this.getApplication());
		
		switch (requestCode) {
		case AdCreationDialog.CAMERA_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				// Load bitmap into byteArray so that we can pass the data to the
				// new Activity
				
				Bitmap currBitmap = Utility.retrieveBitmap(mFileUri, 240, 320);
				
				app.initAdCreationManager(currBitmap, mFileUri);		
			}
			break;
		case AdCreationDialog.PICK_IMAGE:
	        if(resultCode == Activity.RESULT_OK){  
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getContentResolver().query(
	                               selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String filePath = cursor.getString(columnIndex);
	            cursor.close();

	            app.initAdCreationManager(BitmapFactory.decodeFile(filePath), Uri.parse(filePath));
	            
	        }
	        break;

		}
		// Reset companyTag from any previous ad creations
		app.getAdCreationManager().nextStage(this, app.getAdCreationManager().getCurrentBitmap());
	}
	
	public void setFileUri(Uri fileUri) {
		mFileUri = fileUri;
	}
	
	public Uri getFileUri() {
		return mFileUri;
	}
}
