package com.example.maple_android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	private static final int CAMERA_REQUEST = 1888;
    private Uri fileUri;
    private ImageView imageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
    	Log.d("MapleSyrup", "Receiving image");
    	
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
        	
        	// Load bitmap into byteArray so that we can pass the data to the new Activity
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            
            photo.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            byte[] photoByteArray = stream.toByteArray();
            
            OutputStream photoOS;
            
            try {
            	photoOS = getContentResolver().openOutputStream(fileUri);
            	photoOS.write(photoByteArray);
            	photoOS.flush();
            	photoOS.close();
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            
            Intent intent = new Intent(this, EditorActivity.class);
            intent.putExtra("photoPath", fileUri.getPath());
            intent.putExtra("photoByteArray", photoByteArray);
            startActivity(intent);
            
        }
    } 
    
    public void openCamera(View view) {
    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri.getPath()); // set the image file name

        // start the image capture Intent
        // create Intent to take a picture and return control to the calling application

        startActivityForResult(intent, CAMERA_REQUEST);
    	
    }
    
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }
    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES), "MapleSyrup");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MapleSyrup", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
