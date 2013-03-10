package com.example.maple_android;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;

public class LoginActivity extends Activity {

  private Button buttonLoginLogout;
  private Session.StatusCallback statusCallback = new SessionStatusCallback();
  private TextView accessTokenText;
  private String authToken = "";
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    System.out.println("hey there");
	super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    /*** Skip Login For Testing ***/
   Intent i = new Intent(this, MainActivity.class);
   startActivity(i);

    
    buttonLoginLogout = (Button) findViewById(R.id.enter);
	accessTokenText = (TextView) findViewById(R.id.access_token);
	
	Session session = Session.getActiveSession();
    if (session == null) {
        if (savedInstanceState != null) {
            session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
        }
        if (session == null) {
            session = new Session(this);
        }
        Session.setActiveSession(session);
        if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        }
    }

    updateView();
  }

  public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.login, menu);
      return true;
  }
  
  public boolean onOptionsItemSelected(MenuItem item) {
      //respond to menu item selection
	  switch (item.getItemId()) {
	    case R.id.logout:
	    	onClickLogout();
	    return true;
	    default:
	    return super.onOptionsItemSelected(item);
	}
}
  
  @Override
  public void onStart() {
      super.onStart();
      Session.getActiveSession().addCallback(statusCallback);
  }

  @Override
  public void onStop() {
      super.onStop();
      Session.getActiveSession().removeCallback(statusCallback);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
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
    	  accessTokenText.setText("Access token: " + session.getAccessToken());
          AsyncTask<String, Void, String> authTokenRetrieve = new RetrieveAuth().execute(session.getAccessToken());
		try {
			authToken = authTokenRetrieve.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          Log.d("Maple Syrup", "authToken is: " + authToken);
          buttonLoginLogout.setText(R.string.enter);
          buttonLoginLogout.setOnClickListener(new OnClickListener() {
//              public void onClick(View view) { onClickLogout(); }
        	  public void onClick(View view) { 
        		  Intent i = new Intent(LoginActivity.this, MainActivity.class);
        		  i.putExtra("authToken", authToken);
        		  startActivity(i);
        	  }
          });
      } else {
    	  accessTokenText.setText(R.string.access_string);
          buttonLoginLogout.setText(R.string.login);
          buttonLoginLogout.setOnClickListener(new OnClickListener() {
              public void onClick(View view) { onClickLogin(); }
          });
      }
  }

private void onClickLogin() {
      Session session = Session.getActiveSession();
      if (!session.isOpened() && !session.isClosed()) {
          session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
      } else {
          Session.openActiveSession(this, true, statusCallback);
      }
  }

  private void onClickLogout() {
      Session session = Session.getActiveSession();
      if (!session.isClosed()) {
          session.closeAndClearTokenInformation();
      } 
  }
  
  private class SessionStatusCallback implements Session.StatusCallback {
      @Override
      public void call(Session session, SessionState state, Exception exception) {
          updateView();
      }
  }
  
  private class RetrieveAuth extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		String accessToken = params[0];
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpGet httpget = new HttpGet("http://maplesyrup.herokuapp.com/users/check_mobile_login?token=" + accessToken);
		try {
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity ht = response.getEntity();
			BufferedHttpEntity buf = new BufferedHttpEntity(ht);
			InputStream is = buf.getContent();

	        BufferedReader r = new BufferedReader(new InputStreamReader(is));

	        String total = "";
	        String line;
	        while ((line = r.readLine()) != null) {
	            total += line;
	        }
	        return total;
		} catch (ClientProtocolException e) {
	        Log.d("MapleSyrup", "ClientProtocolException with web server");
	    } catch (IOException e) {
	        Log.d("MapleSyrup", "IOException with web server");
	        e.printStackTrace();
	    }
		return null;
	}
	
	@Override
	// Result is not JSON, just the plain auth token, so no need to do anything here
	protected void onPostExecute(String result) {
		Toast.makeText(getApplicationContext(), "Auth token from web server is: " + result, Toast.LENGTH_LONG).show();
	}
  }
  
}