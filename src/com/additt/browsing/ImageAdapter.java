package com.additt.browsing;

import java.util.ArrayList;

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

import com.additt.maple_android.MapleHttpClient;
import com.additt.maple_android.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * Extend BaseAdapter to allow grid to show pictures and other attributes of ad
 */
public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private String mToken;
    private ArrayList<DisplayAd> mAds;
    private int MAX_TO_SHOW = 40;
    private ImageLoader mImageLoader;
    private static final String TAG = "ImageAdapter";
    
    public ImageAdapter(Context c, ArrayList<DisplayAd> ads, String token) {
    	mAds = ads;
    	mContext = c;
        mToken = token;
        mImageLoader = ImageLoader.getInstance();
    }
    
    public int getCount() {
    	return Math.min(MAX_TO_SHOW, mAds.size());
    }

    public Object getItem(int position) {
		return mAds.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public Context getContext() {
    	return mContext;
    }
    
    public String getToken() {
    	return mToken;
    }
    
    public ArrayList<DisplayAd> getAds() {
    	return mAds;
    }
    
    /**
     * Uses caching in the Android Image Loader lib 
     * @param url the image to load from server
     * @param imageView the Android view to populate
     */
    public void loadBitmap(String url, ImageView imageView) {
    	mImageLoader.displayImage(url, imageView); //, new ImageLoadingListener()
    	// could pass in new ImageLoadingListener to get callbacks for:
    	// onLoadingStarted, Failed, Complete, Cancelled
    }
    
    // 

    /**
     * Set up fields in view based on data received for each item 
     * referenced by the Adapter
     */
    public View getView(int position, View convertView, ViewGroup parent) {
    	View adView = convertView;
    	final DisplayAd dAd = mAds.get(position);
    	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	adView = inflater.inflate(R.layout.ad_view, null);
    	ImageView imageView = (ImageView) adView.findViewById(R.id.ad);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);
        loadBitmap(dAd.getUrl(), imageView);
        
    	TextView titleView = (TextView) adView.findViewById(R.id.adTitle);
    	titleView.setText(dAd.getTitle());
    	titleView.setTextColor(Color.BLACK); 
    	TextView creatorText = (TextView) adView.findViewById(R.id.creatorName);
    	creatorText.setText(dAd.getCreator());
    	final TextView numVotesText = (TextView) adView.findViewById(R.id.numVotes);
    	numVotesText.setText("Votes: " + dAd.getNumVotes());
    	TextView createdText = (TextView) adView.findViewById(R.id.dateCreated);
    	createdText.setText(dAd.getRelativeTime() + " ago");
    	
        final Button voteButton = (Button) adView.findViewById(R.id.voteBtn);
    	addButtonActions(dAd, voteButton, numVotesText);

        return adView;
    }
    
    public void addButtonActions(final DisplayAd ad, final Button voteButton, final TextView numVotesText) {
		final RequestParams params = new RequestParams();
		// Another way to get the token
		// Session session = Session.getActiveSession();
		// params.put("token", session.getAccessToken());
		params.put("post_id", ad.getImageId());
		params.put("auth_token", mToken);
		
        if (ad.getVotedOn()) {
			disableButton(voteButton);
        } else {
	        voteButton.setOnClickListener(new OnClickListener() {
	        	@Override
	        	public void onClick(View v) {
	        		MapleHttpClient.post("posts/vote_up", params, new AsyncHttpResponseHandler(){
	            		@Override
	        			public void onSuccess(int statusCode, String response) {
	        				disableButton(voteButton);
	        				// Add one more to the numVotes textview
	        				int numVotesAdded = ad.getNumVotes() + 1;
	        				numVotesText.setText("Votes: " + numVotesAdded);
	        				Toast.makeText(mContext, "You voted!", Toast.LENGTH_SHORT).show();
	        			}
	        			@Override
	        		    public void onFailure(Throwable error, String response) {
	        				Toast.makeText(mContext, "Voting failed", Toast.LENGTH_SHORT).show();
	        		    }
	            	});
	        	}
	        });
        }
    }
    
    private void disableButton(final Button voteButton) {
		voteButton.setBackgroundResource(R.drawable.thumbs_up_pressed);
		voteButton.setClickable(false);
    	voteButton.setEnabled(false);
    }
}
