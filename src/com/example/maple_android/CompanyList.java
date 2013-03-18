package com.example.maple_android;

import java.io.BufferedInputStream;
import com.loopj.android.http.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.FailReason;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.View;

/**
 * CompanyList ---------------- This class assists with retrieving the list of
 * companies from the sever and then storing that list locally on the android
 * device.
 * 
 * The list is retrieved as a background task using AsyncTask
 * 
 * It is stored on the device's internal storage under MODE_PRIVATE so that the
 * file is private to the application.
 * 
 * Functionality is also provided to retrieve the local list, which is returned
 * as
 */

public class CompanyList {
	// file name where the list is stored locally on device
	private final static String FILE_NAME = "company_list";

	// server url where list is stored
	private final static String LIST_URL = "companies/all";

	/**
	 * Syncs the local company list with the current list on the server. If
	 * there is an error contacting the server, no changes to the local copy are
	 * made.
	 * 
	 * @param context The context of the application that will be accessing the list
	 */
	public static void syncListWithServer(Context context) {
		// context must be declared final to be used in callback
		final Context c = context;
		
		// start an Async get request 
		MapleHttpClient.get(LIST_URL, null, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String response){
				// open an output stream and write the file to disk
				FileOutputStream fos = null;
				try {						
					fos = c.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					fos.write(response.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		// If there is a failure we don't have to do anything, just leave 
		// the local file alone.
	}

	/**
	 * Retrieves the local company list from file, parses it, and returns the
	 * contents as an ArrayList of strings. Each string is a company name.
	 * 
	 * @param context
	 *            The context of the application
	 * @return An ArrayList where each String is a company name
	 */
	public static ArrayList<String> getCompanyList(Context context) {
		// retrieve file for local storage
		FileInputStream in = null;
		// init ArrayList
		ArrayList<String> companyList = new ArrayList<String>();

		try {
			in = context.openFileInput(FILE_NAME);
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
			return companyList;
		}

		// transfer file stream to String
		StringBuffer strBuff = new StringBuffer();
		byte[] buffer = new byte[1024];
		int length;
		
		try {
			while ((length = in.read(buffer)) != -1) {
				strBuff.append(new String(buffer));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return companyList;
		}
		
		String file = strBuff.toString();

		
		// parse file for companies and load into arraylist
		int index = 0;
		String key = "\"name\":\"";
		while (true) {
			// find the next "name" field
			index = file.indexOf(key, index);
			if (index == -1)
				break;

			// jump to start of company name
			index += key.length();

			// get end of company name
			int end = file.indexOf("\"", index);

			// add company to list
			companyList.add(file.substring(index, end));
		}

		return companyList;
	}
	
	/**
	 * Retrieves any available logos for the 
	 * given company from the server and stores
	 * them locally
	 * @param companyName The name of the company to get logos for
	 */
	public static void syncCompanyLogos(String companyName){
		//TODO: implement this
	}

	/**
	 * For a given company name, return an ArrayList of
	 * Bitmaps containing all the logos available for
	 * that company. If no logos are available the list
	 * will be empty.
	 * @param companyTag The company name
	 * @return All available logos for the given company
	 */
	public static ArrayList<Bitmap> getCompanyLogos(String companyTag, Context c) {
		// using Universal Image Loader library for easy loading of images from url
		// https://github.com/nostra13/Android-Universal-Image-Loader
		
		String testPic = "http://www.wpclipart.com/sign_language/thumbs_up_large.png";
		
		// init ArrayList
		final ArrayList<Bitmap> logos = new ArrayList<Bitmap>();
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.loadImage(testPic, new SimpleImageLoadingListener() {
		    @Override
		    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		        logos.add(loadedImage);
		        System.out.println("Loading succeeded");
		    }
		    @Override
		    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
		        System.out.println("Loading failed" + failReason.toString());
		    }
		    @Override
		    public void onLoadingStarted(String imageUri, View view) {
		    	 System.out.println("Loading started");
		    }
		});
		
		return logos;
	}
}
