package com.additt.maple_android;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/** This class is used to grab the details of an uncaught exception
 * and report the exception to the Additt server for logging.
 * 
 * @author Eli
 *
 */
public class AddittException {
	// where report() Posts to
	private final String SERVER_PATH = "log_entries";
	private Thread mThread;
	private Throwable mEx;
	private MapleApplication mApp;

	public AddittException(MapleApplication app, Thread thread, Throwable ex) {
		mThread = thread;
		mEx = ex;
		mApp = app;
	}

	public void report() {
		// get the version of Additt in use
		String versionName = "";
		try {
			versionName = mApp.getPackageManager().getPackageInfo(mApp.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			versionName = "Version Name not available";
		}
		
		// get the stacktrace in string form
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		mEx.printStackTrace(pw);
		String stackTrace = sw.toString();

		// log whatever details we want to report
		RequestParams params = new RequestParams();
		params.put("additt_version", versionName);
		params.put("android_build", "" + Build.VERSION.SDK_INT);
		// put time in PST
		params.put("time", Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"), Locale.US).getTime().toString());
		params.put("stack_trace", stackTrace);	
		params.put("ad_creation_log", mApp.getAdCreationManager().getLog());
		
		// post report details to the server. We don't need
		// to register a response callback because we don't care
		// about the response
		MapleHttpClient.post(SERVER_PATH, params, new AsyncHttpResponseHandler());
	}

}
