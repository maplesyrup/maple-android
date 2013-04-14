package com.example.browsing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
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

//public class ImageAdapter extends BaseAdapter {
//    private Context mContext;
//
//    public ImageAdapter(Context c) {
//        mContext = c;
//    }
//
//    public int getCount() {
//        return mThumbIds.length;
//    }
//
//    public Object getItem(int position) {
//        return null;
//    }
//
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    // create a new ImageView for each item referenced by the Adapter
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView;
//        if (convertView == null) {  // if it's not recycled, initialize some attributes
//            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);
//        } else {
//            imageView = (ImageView) convertView;
//        }
//
//        imageView.setImageResource(mThumbIds[position]);
//        return imageView;
//    }
//
//    // references to our images
//    private Integer[] mThumbIds = {
//            R.drawable.cs210, R.drawable.maple,
//            R.drawable.com_facebook_loginbutton_blue_normal, R.drawable.cs210,
//            R.drawable.cs210, R.drawable.maple,
//            R.drawable.com_facebook_loginbutton_blue_normal, R.drawable.cs210,
//            R.drawable.cs210, R.drawable.maple,
//            R.drawable.com_facebook_loginbutton_blue_normal, R.drawable.cs210,
//            R.drawable.cs210, R.drawable.maple,
//            R.drawable.com_facebook_loginbutton_blue_normal, R.drawable.cs210,
//            R.drawable.cs210, R.drawable.maple
//    };
//}

