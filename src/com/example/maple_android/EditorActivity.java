package com.example.maple_android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.maple_android.Utility;

public class EditorActivity extends Activity {
	
	private ImageView photo;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Bundle extras = getIntent().getExtras();
        
        // Grab photo byte array and decode it
        byte[] byteArray = extras.getByteArray("photoByteArray");
        String photoPath = (String) extras.get("photoPath");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        photo = (ImageView)this.findViewById(R.id.photo);
        photo.setImageBitmap(bitmap);
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("image", photoPath));
        Utility.post("http://localhost:3000", params);
        
    }
}
