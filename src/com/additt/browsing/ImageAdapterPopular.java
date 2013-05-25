package com.additt.browsing;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.additt.maple_android.MapleHttpClient;
import com.additt.maple_android.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Extend BaseAdapter to allow grid to show pictures and other attributes of ad
 */
public class ImageAdapterPopular extends ImageAdapter {    
    public ImageAdapterPopular(Context c, ArrayList<DisplayAd> ads, String token) {
    	super(c, ads, token);
    } 

    /**
     * Set up fields in view based on data received for each item 
     * referenced by the Adapter
     */
    public View getView(int position, View convertView, ViewGroup parent) {
    	View adView = convertView;
    	final DisplayAd dAd = getAds().get(position);
    	LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	adView = inflater.inflate(R.layout.ad_view_popular, null);
    	ImageView imageView = (ImageView) adView.findViewById(R.id.ad);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);
        loadBitmap(dAd.getUrl(), imageView);
        return adView;
    }
}
