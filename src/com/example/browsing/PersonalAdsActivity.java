package com.example.browsing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maple_android.MapleHttpClient;
import com.example.maple_android.R;
import com.example.maple_android.Utility;
import com.example.maple_android.R.drawable;
import com.example.maple_android.R.id;
import com.example.maple_android.R.layout;
import com.example.maple_android.R.menu;
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
		params.put("user_id", user_id);
		MapleHttpClient.get("posts", params, new AsyncHttpResponseHandler(){
			// Example json response: http://maplesyrup.herokuapp.com/posts?user_id=3
			@Override
			public void onSuccess(int statusCode, String response) {
				Log.d(TAG, response);	
				try {
					JSONArray jObjectAds = new JSONArray(response);
					mGridview.setAdapter(new ImageAdapter(getApplicationContext(), jObjectAds));
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
	    private LayoutInflater mInflator;
	    
	    public ImageAdapter(Context c, JSONArray ads) {
	        mContext = c;
	        this.ads = ads;
	        mInflator = LayoutInflater.from(c);
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
	        View adView = mInflator.inflate(R.layout.ad_view, null);
	    	final ImageView imageView = (ImageView) adView.findViewById(R.id.ad);
	    	
            imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
	    	
	    	// Example image:
	        //http://s3.amazonaws.com/maplesyrup-assets/posts/images/000/000/006/medium/IMG_20130311_233546.jpg?1363070132
	        String url = "drawable://" + R.drawable.maple;
	        String title = "";
	        try {
	        	JSONObject jObject = ads.getJSONObject(position); 
	        	url = jObject.getString("image_url");
		        title = jObject.getString("title");
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

	    	TextView textView = (TextView) adView.findViewById(R.id.adInfo);
	    	textView.setText(title);
	        return adView;
	    }
	}

	
}
