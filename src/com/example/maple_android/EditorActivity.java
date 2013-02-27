package com.example.maple_android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class EditorActivity extends Activity {
	
	private ImageView photo;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("photoByteArray");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        photo = (ImageView)this.findViewById(R.id.photo);
        photo.setImageBitmap(bitmap);
        
    }
}
