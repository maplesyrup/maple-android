package com.example.maple_android;
import com.loopj.android.http.*;

/**
 * This class provides easy methods for get and post requests to our main website.
 * 
 *
 */

public class MapleHttpClient {
	 private static final String BASE_URL = "http://maplesyrup.herokuapp.com/";
 
	  private static AsyncHttpClient client = new AsyncHttpClient();

	  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(getAbsoluteUrl(url), params, responseHandler);
	  }

	  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.post(getAbsoluteUrl(url), params, responseHandler);
	  }

	  private static String getAbsoluteUrl(String relativeUrl) {
	      return BASE_URL + relativeUrl;
	  }
}
