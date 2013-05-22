package com.additt.maple_android;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.additt.browsing.PopularAdsActivity;
import com.facebook.Session;
import com.facebook.SessionState;
import com.google.analytics.tracking.android.EasyTracker;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends Activity {

	private static final String TAG = "Login Activity";

	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	private Button mButtonLoginLogout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// hide action bar for login page
		try {
			getActionBar().hide();
		} catch (NoSuchMethodError e) {
			Log.d(TAG,
					"Well, not high enough SDK for action bar so it won't be shown anyways");
		}

		mButtonLoginLogout = (Button) findViewById(R.id.loginB);
		mButtonLoginLogout.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onClickLogin();
			}
		});
		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback,
						savedInstanceState);
			}
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(this)
						.setCallback(statusCallback));
			}
		} else {
			updateView();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);

		// start analytics tracking for this activity
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);

		// stop analytics tracking for this activity
		EasyTracker.getInstance().activityStop(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	private void updateView() {
		Session session = Session.getActiveSession();
		if (session.isOpened()) {
			saveUserData((MapleApplication) this.getApplication(),
					session.getAccessToken());

		}
	}

	private void onClickLogin() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this)
					.setCallback(statusCallback));
		} else {
			Session.openActiveSession(this, true, statusCallback);
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			updateView();
		}
	}

	/**
	 * Retrieves user data from server based on access token
	 * 
	 * @param token
	 *            The Facebook access token
	 */
	private void saveUserData(final MapleApplication mApp, final String token) {
		RequestParams params = new RequestParams();
		params.put("token", token);
		MapleHttpClient.get("users/check_mobile_login", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						// update login button to say loading and disallow more
						// clicks
						Button loginB = (Button) findViewById(R.id.loginB);
						loginB.setEnabled(false);
						loginB.setBackgroundResource(R.drawable.loading);
					}

					@Override
					public void onSuccess(int statusCode, String response) {
						try {
							User appUser = new User(response, token);
							mApp.setUser(appUser);
							Intent i = new Intent(LoginActivity.this,
									PopularAdsActivity.class);
							startActivity(i);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable error, String response) {
						// Something went wrong so let's start up the
						// LoginActivity again
						// No let's not try again. This will throw us into an
						// infinite loop.
						Log.d(TAG,
								"Failed to login. Check heroku logs for error");
						Toast.makeText(LoginActivity.this, "Failed to login",
								Toast.LENGTH_SHORT).show();

					}
					
					@Override
					public void onFinish(){
						// restore the login button in case of failure, or
						// if they return to this page
						Button loginB = (Button) findViewById(R.id.loginB);
						loginB.setEnabled(true);
						loginB.setBackgroundResource(R.layout.button_sign_in);
					}
				});
	}
}