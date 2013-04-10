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
 * This class assists with retrieving the list of
 * companies from the sever and then storing that list locally on the android
 * device.
 * <p>
 * The list is retrieved as a background task using AsyncTask
 * <p>
 * It is stored on the device's internal storage under MODE_PRIVATE so that the
 * file is private to the application.
 * <p>
 * Functionality is also provided to retrieve the local list, which parses the
 * local list and returns the list of companies as an ArrayList of Strings
 * <p>
 * This class also assists with retrieving available logos for a given company.
 */

public class CompanyList {
	// file name where the list is stored locally on device
	private final static String FILE_NAME = "company_list";

	// server url where list is stored
	// this is relative to the base URL in MapleHttpClient
	private final static String LIST_URL = "companies";

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
		MapleHttpClient.get(LIST_URL, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
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
	 * @param context The context of the application
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
	 * For a given company name, return an ArrayList of Bitmaps containing all
	 * the logos available for that company. If no logos are available the list
	 * will be empty.
	 * <p>
	 * The images are loaded and added to the ArrayLlist asynchronously in call
	 * back methods so the list won't be immediately populated when it is
	 * returned!
	 * <p>
	 * If you are loading large images or many images it may be a while before the
	 * list is fully populated.
	 * 
	 * @param companyTag The company name
	 * @return All available logos for the given company. Initially this list will be empty,
	 * and will be populated over time as each url finished loading. (~1 sec)
	 */
	public static ArrayList<Bitmap> getCompanyLogosFromServer(String companyTag) {
		// using Universal Image Loader library for easy loading of images from
		// url
		// https://github.com/nostra13/Android-Universal-Image-Loader

		// right now the companyTag is ignored and the following urls are loaded as tests
		String[] testPics = {
				"http://www.thailandsnakes.com/wp-content/uploads/2011/10/golden-tree-snake1.jpg",
				"http://farm6.staticflickr.com/5112/7411003852_91ef21d9f8_q.jpg",
				"http://farm7.staticflickr.com/6049/6250538830_78d4ccaa01_q.jpg",
				"http://farm9.staticflickr.com/8303/7797439002_8b659ec91e_q.jpg",
				"http://farm4.staticflickr.com/3161/5836507805_fd4df96bef_q.jpg",
				"http://farm8.staticflickr.com/7014/6444873537_6be113c907_q.jpg",
				"http://farm9.staticflickr.com/8364/8345145499_9a6a4ea6e0_q.jpg"
				};

		// init ArrayList
		final ArrayList<Bitmap> logos = new ArrayList<Bitmap>();

		ImageLoader imageLoader = ImageLoader.getInstance();

		// get each url started loading
		for (String url : testPics) {
			imageLoader.loadImage(url, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					logos.add(loadedImage);
				}
			});

		}

		// return the arraylist. It will be populated in the background
		// as each url loads
		return logos;
	}
}
