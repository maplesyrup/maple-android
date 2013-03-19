package com.example.maple_android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LogoPickerActivity extends Activity {
	/* Global app */
	MapleApplication app;
	
	private GridView gridview;
	private ArrayList<Bitmap> logos;
	private byte[] byteArray;
	private String companyTag;
	public Context c = this;
	private byte[] logoArray = null;
	private Bitmap selectedLogo = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo_picker);
		
		//init app
		app = (MapleApplication) this.getApplication();
		
		// get available company logos
		logos = app.getCurrentCompanyLogos();

		// get company name
		companyTag = app.getCurrentCompany();
		
		// set activity header text
		TextView header = (TextView) findViewById(R.id.logoPickerTitle);
		header.setText("Pick A " + companyTag + " Logo");

		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				//highlight view with border
				setSelectedView(position);
				
				// update currently selected logo
				setLogo(v, position);
			}
		});
	}
	
	/** Places a border around the view in the 
	 * gridView at the given position. All other
	 * views have their border cleared.
	 * @param position The position of the view in the gridView
	 */
	public void setSelectedView(int position){
		// walk through each child in gridview
		final int size = gridview.getChildCount();
		for(int i = 0; i < size; i++) {
		     ImageView v = (ImageView) gridview.getChildAt(i);
		     // Since the views have padding, setting background color
		     //effectively gives them a border
		     if(i == position) v.setBackgroundColor(Color.BLUE);
		     else v.setBackgroundColor(Color.TRANSPARENT);
		}
	}
	
	/** Returns the selected logo to the
	 * LogoActivity
	 * 
	 * @param view
	 */
	public void save(View view){
		returnToLogoActivity();
	}

	/**
	 * Returns to the LogoActivity with no changes made
	 * 
	 * @param view
	 */
	public void cancel(View view) {
		// make the logo null so nothing is passed back
		selectedLogo = null;
		
		returnToLogoActivity();
	}
	
	public void returnToLogoActivity(){
		Intent i = new Intent(this, LogoActivity.class);
		i.putExtra("photoByteArray", getIntent().getExtras().getByteArray("photoByteArray"));
		i.putExtra("logoArray", Utility.bitmapToByteArray(selectedLogo));
		startActivity(i);
	}

	/**
	 * Saves the clicked image to be the company logo and returns to the
	 * LogoActivity
	 */
	public void setLogo(View view, int logoPosition) {
		// set logo
		selectedLogo = logos.get(logoPosition);
		
		// change save button to visible so logo can be saved
		findViewById(R.id.save).setVisibility(View.VISIBLE);
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
			return logos.size();
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

			imageView.setImageBitmap(logos.get(position));

			return imageView;
		}

	}

}
