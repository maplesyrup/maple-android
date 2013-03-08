package com.example.maple_android;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class LoginActivity extends Activity {

  private Button mEnterButton;
	
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

	mEnterButton = (Button) findViewById(R.id.enter);	
	mEnterButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	Intent intent = new Intent(LoginActivity.this, MainActivity.class); 
            LoginActivity.this.startActivity(intent);
        }
    });
	
	
    // start Facebook Login
    Session.openActiveSession(this, true, new Session.StatusCallback() {

      // callback when session changes state
      @Override
      public void call(Session session, SessionState state, Exception exception) {
        if (session.isOpened()) {

          // make request to the /me API
          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

            // callback after Graph API response with user object
            @Override
            public void onCompleted(GraphUser user, Response response) {
              if (user != null) {
                TextView welcome = (TextView) findViewById(R.id.welcome);
                welcome.setText("Hello " + user.getName() + "!");
                
              }
            }
          });
        }
      }
    });
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
  }

}