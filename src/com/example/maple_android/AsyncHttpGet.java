package com.example.maple_android;

import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncHttpGet extends AsyncTask<String, String, Bitmap> {
	// Definitely should make this more general:
	// http://stackoverflow.com/questions/2022170/how-to-execute-web-request-in-its-own-thread
	protected Bitmap doInBackground(String... requestStrings) {
      String url = requestStrings[0];
      Bitmap bitmap = null;
      try{
    	  bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
      }
      catch(Exception ex){
        Log.e("AsyncHttpGet", ex.getMessage());
      }
      return bitmap;
     }

}
