package com.example.maple_android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LogoPickerActivity extends Activity {
	/* Global app */
	MapleApplication app;

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

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				setLogo(v, position);
			}
		});
	}

	/**
	 * Returns to the LogoActivity with no changes made
	 * 
	 * @param view
	 */
	public void cancel(View view) {
		Intent i = new Intent(this, LogoActivity.class);
		i.putExtra("photoByteArray", getIntent().getExtras().getByteArray("photoByteArray"));
		i.putExtra("logoArray", Utility.bitmapToByteArray(selectedLogo));
		i.putExtra("accessToken",
				getIntent().getExtras().getString("accessToken"));
		startActivity(i);
	}

	/**
	 * Saves the clicked image to be the company logo and returns to the
	 * LogoActivity
	 */
	public void setLogo(View view, int logoPosition) {
		// set logo
		selectedLogo = logos.get(logoPosition);

		// return to LogoActivity
		cancel(view);
	}

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
