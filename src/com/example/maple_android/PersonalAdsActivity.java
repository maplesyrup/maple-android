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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Session;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class PersonalAdsActivity extends Activity {

	private static final String TAG = "PersonalAds";
	private GridView mGridview; // the view we are using to display the ads
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_ads);
		mGridview = (GridView) findViewById(R.id.gridviewAds);
		requestUserAds();
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
		
		// No params means just getting the most popular ads
		//		params.put("user_id", user_id);
		MapleHttpClient.get("posts", params, new AsyncHttpResponseHandler(){
			// Example json response: http://maplesyrup.herokuapp.com/posts?user_id=3
			@Override
			public void onSuccess(int statusCode, String response) {
				Log.d(TAG, response);	
				try {
					JSONArray jObjectAds = new JSONArray(response);
					mGridview.setAdapter(new ImageAdapter(getApplicationContext(), jObjectAds));
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

	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	    private JSONArray ads;
	    private int MAX_TO_SHOW = 10;
	    
	    public ImageAdapter(Context c, JSONArray ads) {
	        mContext = c;
	        this.ads = ads;
	    }

	    public int getCount() {
	    	return Math.min(MAX_TO_SHOW, ads.length());
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        final ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(8, 8, 8, 8);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        String url = "http://s3.amazonaws.com/maplesyrup-assets/posts/images/000/000/006/medium/IMG_20130311_233546.jpg?1363070132";
	        try {
	        	url = ads.getJSONObject(position).getString("image_url");
	        } catch (JSONException e){
	        	Log.d(TAG, "unable to parse JSON");
	        }
	        ImageLoader imageLoader = ImageLoader.getInstance();
	        imageLoader.loadImage(url, new SimpleImageLoadingListener() {
	        	@Override
	        	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
	        		imageView.setImageBitmap(loadedImage);
	        	}
	        });
	        Log.d(TAG, "getting from url2");
	        return imageView;
	    }
	}

	
}
