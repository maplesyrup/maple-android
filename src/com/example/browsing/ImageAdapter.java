package com.example.browsing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maple_android.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/*
 * Extend BaseAdapter to allow grid to show pictures
 */
public class ImageAdapter extends BaseAdapter {
	private String TAG = "ImageAdapter";
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
    	View adView = convertView;
        
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
        	textView.setGravity(Gravity.CENTER);
        	textView.setTextColor(Color.BLACK);
        }
        return adView;
    }
}
