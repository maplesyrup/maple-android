package com.example.maple_android;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String success = getIntent().getExtras().getString("successMessage");
        if (success != null) {
        	Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
        }
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
           // make request to the /me API
           Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
	           // callback after Graph API response with user object
	           @Override
	           public void onCompleted(GraphUser user, Response response) {
	             if (user != null) {
	               TextView greeting = (TextView) findViewById(R.id.greeting);
	               greeting.setText("Welcome " + user.getName() + "!"); 
	             }
	           }
           });
         }
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
    	Log.d(TAG, "Receiving image");
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
        	// Load bitmap into byteArray so that we can pass the data to the new Activity
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            
            photo.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            byte[] photoByteArray = stream.toByteArray();
  
            Intent intent = new Intent(this, EditorActivity.class);

            intent.putExtra("photoByteArray", photoByteArray);
            intent.putExtra("accessToken", getIntent().getExtras().getString("accessToken"));
            startActivity(intent);
        }
    } 
    
    public void openCamera(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    startActivityForResult(intent, CAMERA_REQUEST);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
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

    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
      Intent i = new Intent(this, LoginActivity.class);
      startActivity(i);
    }
    
}
