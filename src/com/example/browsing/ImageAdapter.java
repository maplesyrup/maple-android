package com.example.browsing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maple_android.MapleApplication;
import com.example.maple_android.MapleHttpClient;
import com.example.maple_android.R;
import com.example.maple_android.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/*
 * Extend BaseAdapter to allow grid to show pictures
 */
public class ImageAdapter extends BaseAdapter {
	private String TAG = "ImageAdapter";
	private Context mContext;
	private String mToken;
    private JSONArray ads;
    private int MAX_TO_SHOW = 10;
    
    public ImageAdapter(Context c, JSONArray ads, String token) {
        mContext = c;
        this.ads = ads;
        mToken = token;
    }

    public int getCount() {
    	return Math.min(MAX_TO_SHOW, ads.length());
    }

    public Object getItem(int position) {
        try {
        	/**
        	 * We get the proper JSON object from the response, and extract 
        	 * relevant data in the setOnItemClickListener on the gridview in BrowseActivity 
        	 */
			return ads.getJSONObject(position);
		} catch (JSONException e) {
			e.printStackTrace();
			return new JSONObject();
		}
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	View adView = convertView;

        String votedOn = "";
        String imageId = "61";
        if (convertView == null) {
        	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	adView = inflater.inflate(R.layout.ad_view, null);
        	final ImageView imageView = (ImageView) adView.findViewById(R.id.ad);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

        	
        	// Example image:
            //http://s3.amazonaws.com/maplesyrup-assets/posts/images/000/000/006/medium/IMG_20130311_233546.jpg?1363070132
            String url = "drawable://" + R.drawable.maple;
            String title = "";
            String creator = "";
            String numVotes = "";
            String relativeTime = "";
            try {
            	JSONObject jObject = ads.getJSONObject(position); 
            	url = jObject.getString("image_url");
    	        title = jObject.getString("title");
    	        creator = jObject.getJSONObject("user").getString("name");
    	        numVotes = jObject.getString("total_votes");
    	        relativeTime = jObject.getString("relative_time");
    	        votedOn = jObject.getString("voted_on");
    	        imageId = jObject.getString("id");
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
        	TextView titleView = (TextView) adView.findViewById(R.id.adTitle);
        	titleView.setText(title);
        	titleView.setTextColor(Color.BLACK); 
        	
        	TextView creatorText = (TextView) adView.findViewById(R.id.creatorName);
        	creatorText.setText(creator);
        	
        	TextView numVotesText = (TextView) adView.findViewById(R.id.numVotes);
        	numVotesText.setText("Votes: " + numVotes);
        	
        	TextView createdText = (TextView) adView.findViewById(R.id.dateCreated);
        	createdText.setText(relativeTime + " ago");
        }
        Button voteButton = (Button) adView.findViewById(R.id.voteBtn);
        if (votedOn.equals("yes")) {
        	voteButton.setText("Voted");
        	voteButton.setEnabled(false);
        } else {
        	final String imageIdFinal = imageId;
	        voteButton.setOnClickListener(new OnClickListener() {
	        	@Override
	        	public void onClick(View v) {
	        		RequestParams params = new RequestParams();
	        		// Another way to do the below, but depends on Facebook
//	        		Session session = Session.getActiveSession();
//	        		params.put("token", session.getAccessToken());
	        		params.put("post_id", imageIdFinal);
	        		params.put("token", mToken);
	            	doVote(params);
	        	}
	        });
        }
        return adView;
    }
    
    public void doVote(RequestParams params) {
    	MapleHttpClient.post("posts/vote_up", params, new AsyncHttpResponseHandler(){
    		@Override
			public void onSuccess(int statusCode, String response) {
				Log.d(TAG, response);
				Toast.makeText(mContext, "clicked on vote!", Toast.LENGTH_LONG).show();
			}
			@Override
		    public void onFailure(Throwable error, String response) {
				Toast.makeText(mContext, "Voting response failure", Toast.LENGTH_LONG).show();
		    }
    	});
    }
}
