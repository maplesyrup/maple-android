package com.example.maple_android;

import com.example.browsing.BrowseActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class AdCreationDialog {
	
	private BrowseActivity mActivity;
	private Uri mFileUri;
	public static final int CAMERA_REQUEST = 1;
	public static final int PICK_IMAGE = 2;

	public AdCreationDialog(BrowseActivity activity) {
		mActivity = activity;
	}
	
    /**
	 * Creates a new dialog that allows the user to pick an image
	 * from the gallery or take a new photo.
	 * @param v
	 */
	public void show() {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage("Pick a photo to use in your ad!")
		       .setTitle("Create New Ad");
		
		builder.setPositiveButton("Take New Photo", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               openCamera();
	           }
	       });
		builder.setNegativeButton("Pick From Gallery", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               openGallery();
	           }
	       });
		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	/**
	 * This will open the camera for taking a picture.
	 * 
	 * @param view
	 */
	public void openCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		mActivity.setFileUri(Utility.getOutputMediaFileUri(Utility.MEDIA_TYPE_IMAGE)); 
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mActivity.getFileUri());
		
		mActivity.startActivityForResult(intent, CAMERA_REQUEST);
	}
	
	/**
	 * Opens the gallery for taking a picture
	 */
	public void openGallery() {
		Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
	     mActivity.startActivityForResult(i, PICK_IMAGE); 
	}
	
	
}
