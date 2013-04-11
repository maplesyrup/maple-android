package com.example.maple_android;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Session;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PersonalAdsActivity extends Activity {

	private static final String TAG = "PersonalAds";
	private GridView mGridview; // the view we are using to display the ads
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_ads);
		requestUserAds();
		Log.d(TAG, "trying to identify the user");
		setContentView(R.layout.activity_personal_ads);
	}

	/**
	 * Stub method for now to get user id
	 * @param accessToken
	 * @return
	 */
	private String getUserId(String accessToken) {
		return "3";
	}
	
	private void requestUserAds() {
		Session session = Session.getActiveSession();
		final String user_id = getUserId(session.getAccessToken());
		RequestParams params = new RequestParams();

		mGridview = (GridView) findViewById(R.id.gridviewAds);
		mGridview.setAdapter(new ImageAdapter(this));
		
		mGridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(PersonalAdsActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
		
		// No params means just getting the most popular ads
		//		params.put("user_id", user_id);
		MapleHttpClient.get("posts", params, new AsyncHttpResponseHandler(){
			// Example json response: http://maplesyrup.herokuapp.com/posts?user_id=3
			@Override
			public void onSuccess(int statusCode, String response) {
				Log.d(TAG, response);	
				try {
					JSONArray jObjectAds = new JSONArray(response);
					populateView(jObjectAds);
					Log.d(TAG, "Number ads created for user_id " + user_id + ": " + jObjectAds.length());
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

	public void populateView(JSONArray arr) {
		int LIMIT = 15;
		Log.d(TAG, "going to populate the view here");
		try {
			JSONObject jObject = arr.getJSONObject(0);
			String imageUrl = jObject.getString("full_image_url");
			String companyTitle = jObject.getString("title");
//			String user_email = jObject.getString("");
			
		} catch (JSONException e) {
			Log.d(TAG, "Could not parse JSON; unexpected format.");	
			e.printStackTrace();
		}
		
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
	
	/*
	 * Extend BaseAdapter to allow grid to show pictures
	 */
	private class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return 4;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

//			imageView.setImageBitmap(getBitmap(position));
			imageView.setImageResource(R.drawable.maple);

			return imageView;
		}

		private Bitmap getBitmap(int position) {
			Log.d(TAG, "getting bitmaps from url!");
			return Utility.getBitmapFromUrl("http://s3.amazonaws.com/maplesyrup-assets/posts/images/000/000/030/original/IMG_20130314_002848.jpg?1363246119");
		}
	}

	
}
