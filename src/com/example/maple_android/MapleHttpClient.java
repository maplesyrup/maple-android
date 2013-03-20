package com.example.maple_android;
import com.loopj.android.http.*;

/**
 * This class provides easy methods for get and post requests to our main website.
 * See: http://loopj.com/android-async-http/ for more documentation
 *
 */

public class MapleHttpClient {

	// Get and Post requests pass urls that build upon this base url. The method
	// getAbsoluteUrl combines the passed in url with the base url to get the 
	// complete url
	 private static final String BASE_URL = "http://maplesyrup.herokuapp.com/";
 
	  private static AsyncHttpClient client = new AsyncHttpClient();

	  /** Does an asynchronous get request and returns the result as a string 
	   * in the responseHandler call back function.
	   * @param url The Get destination. This is relative to our website's homename. To get http://maplesyrup.com/companies simply pass in "companies"
	   * @param params Any parameters you wish to include in the get request. Pass null if you have none
	   * @param responseHandler This handler acts as a callback function to provide the results of the get request on success or failure
	   */
	  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(getAbsoluteUrl(url), params, responseHandler);
	  }

	  /** Does an asynchronous post request
	   * @param url The post destination. This is relative to our website's homename. To reach http://maplesyrup.com/companies simply pass in "companies"
	   * @param params Any parameters you wish to include in the post request. Pass null if you have none
	   * @param responseHandler This handler acts as a callback function to provide the results of the post request on success or failure
	   */
	  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.post(getAbsoluteUrl(url), params, responseHandler);
	  }

	/** This help function combines the relative url with the base url to
	 * get the complete path.
	 * @param relativeUrl The postfix to the BASE_URL
	 * @return The complete destination url
	 */
	  private static String getAbsoluteUrl(String relativeUrl) {
	      return BASE_URL + relativeUrl;
	  }
}
