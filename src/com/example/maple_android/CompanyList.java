package com.example.maple_android;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;

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
	// file name where
	private final static String FILE_NAME = "company_list";

	// server url where list is stored
	private final static String SERVER_URL = "http://maplesyrup.herokuapp.com/companies/all";

	/**
	 * Syncs the local company list with the current list on the server. If
	 * there is an error contacting the server, no changes to the local copy are
	 * made.
	 * @param context The context of the application that will be accessing the list
	 */
	public static void syncListWithServer(Context context) {
		// simply a wrapper class to start the AsyncTask
		new AsyncListGet().execute(context);
	}

	/*
	 * Subclass AsyncTask in order to run task in background
	 * Execute technically takes multiple contexts, but only 
	 * one context should be passed in. Any extras won't do
	 * anything
	 */
	private static class AsyncListGet extends AsyncTask<Context, Void, Void> {

		@Override
		protected Void doInBackground(Context... context) {

			
			// allow this thread to use html connect
			// if (android.os.Build.VERSION.SDK_INT > 9) {
			// StrictMode.ThreadPolicy policy = new
			// StrictMode.ThreadPolicy.Builder()
			// .permitAll().build();
			// StrictMode.setThreadPolicy(policy);
			// }

			// set up URL for contacting server
			URL url = null;
			try {
				url = new URL(SERVER_URL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			// initialize http connection
			HttpURLConnection urlConnection;
			try {
				urlConnection = (HttpURLConnection) url.openConnection();
			} catch (IOException e) {
				// if a connection cannot be made, return without making any
				//changes to the local list
				return null;
			}

			// write http results to an input stream
			InputStream in = null;
			try {
				in = new BufferedInputStream(urlConnection.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
				}
			
			// disconnect from server
			urlConnection.disconnect();
			
			// convert the result to string format
			String file = convertStreamToString(in);
			
			// open an output stream and write the file to disk
			FileOutputStream fos = null;
			try {
				// only use the first Context that is passed in
				fos = context[0].openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			} catch (FileNotFoundException e) {e.printStackTrace();}
			try {
				fos.write(file.getBytes());
			} catch (IOException e) {e.printStackTrace();}
			try {
				fos.close();
			} catch (IOException e) {e.printStackTrace();}
			
			// task is finished. don't need to return anything
			return null;			
		}

		/*
		 * Helper function to convert an input stream to a string
		 */
		private String convertStreamToString(java.io.InputStream is) {
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}

	}

}
