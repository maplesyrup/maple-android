package com.example.maple_android;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/** This activity allows the user to select a logo to add to 
 * the ad they are creating. The available logos shown to the 
 * user based on the current company tag, and the logos for
 * that company on our server.
 * 
 * Logos are displayed in a grid view. When selected they are given a 
 * border to indicate the user has successfully chosen one. If the user
 * is happy with the choice they can hit save to return their choice to LogoActivity,
 * or they can change their selection.
 * 
 * A cancel button returns the user to LogoActivity with no changes made.
 *
 */

public class LogoPickerActivity extends Activity {
	/* Global app */
	MapleApplication mApp;
	

	private GridView mGridview; // the view we are using to display the logos
	private ArrayList<Bitmap> mLogos; // list of available logos
	private Bitmap mSelectedLogo = null; // the currently selected logo. Null until a first choice is made

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo_picker);
		
		//init app
		mApp = (MapleApplication) this.getApplication();
		
		// get available company logos
		mLogos = mApp.getCurrentCompanyLogos();
				
		// set activity header text to reflect company
		TextView header = (TextView) findViewById(R.id.logoPickerTitle);
		header.setText("Pick A " + mApp.getCurrentCompany() + " Logo");

		// set up grid view with adapter to show logos
		mGridview = (GridView) findViewById(R.id.gridview);
		mGridview.setAdapter(new ImageAdapter(this));

		// callback function for when a logo is clicked
		mGridview.setOnItemClickListener(new OnItemClickListener() {
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
		final int size = mGridview.getChildCount();
		for(int i = 0; i < size; i++) {
		     ImageView v = (ImageView) mGridview.getChildAt(i);
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
		mSelectedLogo = null;
		
		returnToLogoActivity();
	}
	
	/** Returns to the LogoActivity, passing back any logo
	 * that has been selected. If no logo was selected then
	 * null is passed.
	 */
	public void returnToLogoActivity(){
		Intent i = new Intent(this, LogoActivity.class);
		i.putExtra("logoArray", Utility.bitmapToByteArray(mSelectedLogo));
		startActivity(i);
	}

	/**
	 * Saves the clicked image to be the company logo and returns to the
	 * LogoActivity
	 * @param view The button that was clicked to call this method
	 * @param logoPosition The selected logo's position in the list
	 */
	public void setLogo(View view, int logoPosition) {
		// set logo
		mSelectedLogo = mLogos.get(logoPosition);
		
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
			return mLogos.size();
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

			imageView.setImageBitmap(mLogos.get(position));

			return imageView;
		}

	}

}
