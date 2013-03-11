package com.example.maple_android;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncHttpPost extends AsyncTask<String, String, String> {
	
	private List<NameValuePair> mData;
	
	public AsyncHttpPost(List<NameValuePair> nameValuePairs) {
		
		mData = nameValuePairs;
	}
	@Override
	protected String doInBackground(String... params) {
		String url = params[0];
		HttpClient httpClient = new DefaultHttpClient();
	    HttpContext localContext = new BasicHttpContext();
	    HttpPost httpPost = new HttpPost(url);

	    try {
	        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

	        for(int index=0; index < mData.size(); index++) {
	            if(mData.get(index).getName().equalsIgnoreCase("post[image]")) {
	                // If the key equals to "image", we use FileBody to transfer the data
	                entity.addPart(mData.get(index).getName(), new FileBody(new File (mData.get(index).getValue())));
	            } else {
	                // Normal string data
	                entity.addPart(mData.get(index).getName(), new StringBody(mData.get(index).getValue()));
	            }
	        }

	        httpPost.setEntity(entity);

	        HttpResponse response = httpClient.execute(httpPost, localContext);
	    } catch (IOException e) {
	    	Log.d("Utility", "Failed http request");
	        e.printStackTrace();
	    }
	    
	    return null;
	}

}