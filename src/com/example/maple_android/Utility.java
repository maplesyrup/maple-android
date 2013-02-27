package com.example.maple_android;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.os.AsyncTask;
import android.util.Log;

public class Utility {
	
	public static void post(String url, List<NameValuePair> nameValuePairs) {
		AsyncHttpPost asyncHttpPost = new AsyncHttpPost(nameValuePairs);
		asyncHttpPost.execute(url);
	    
	}
}
