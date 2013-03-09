package com.example.maple_android;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;

public class LoginActivity extends Activity {

  private Button buttonLoginLogout;
  private Session.StatusCallback statusCallback = new SessionStatusCallback();
  private TextView welcomeText;
  private TextView accessTokenText;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    buttonLoginLogout = (Button) findViewById(R.id.enter);
	welcomeText = (TextView) findViewById(R.id.welcome);
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
    
//	mEnterButton.setOnClickListener(new View.OnClickListener() {
//        public void onClick(View v) {
//        	Intent intent = new Intent(LoginActivity.this, MainActivity.class); 
//            startActivity(intent);
//        }
//    });
	
    // start Facebook Login
//    Session.openActiveSession(this, true, new Session.StatusCallback() {
//
//      // callback when session changes state
//      @Override
//      public void call(Session session, SessionState state, Exception exception) {
//        if (session.isOpened()) {
//
//          // make request to the /me API
//          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
//
//            // callback after Graph API response with user object
//            @Override
//            public void onCompleted(GraphUser user, Response response) {
//              if (user != null) {
//                TextView welcome = (TextView) findViewById(R.id.welcome);
//                welcome.setText("Hello " + user.getName() + "!");
//                
//              }
//            }
//          });
//        }
//      }
//    });
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
          buttonLoginLogout.setText(R.string.logout);
          buttonLoginLogout.setOnClickListener(new OnClickListener() {
              public void onClick(View view) { onClickLogout(); }
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
  
}