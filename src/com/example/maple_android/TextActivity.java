package com.example.maple_android;

import java.io.ByteArrayOutputStream;

import com.example.maple_android.ColorPickerDialog.OnColorChangedListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TextActivity extends Activity {
	private byte[] byteArray;
	private ImageView photo;
	private String companyTag;
	private Bitmap srcBitmap;
	
	// text option
	private float text_x;
	private float text_y;
	private EditText textEntry;
	private TextView photoText;
	private int textColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text);
		
		// set page title
        TextView title = (TextView)this.findViewById(R.id.headerText);
     	title.setText("Add Text To Your " + companyTag + " Ad");
		
		// get picture 
		Bundle extras = getIntent().getExtras();
		byteArray = extras.getByteArray("photoByteArray");
		srcBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		
		
		// get company name
		companyTag = extras.getString("companyTag");
		
		
		// set photo
		photo = (ImageView)this.findViewById(R.id.photo);
        photo.setImageBitmap(srcBitmap);
        
        // initialize photo for clicking
        photo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return placeText(v, event); 	            	
            }			
        });
        
        // TextView to overlap on photo
        photoText = (TextView) findViewById(R.id.photoText);
        textColor = photoText.getTextColors().getDefaultColor();
        
        // set up text listener
        textEntry = (EditText)this.findViewById(R.id.textEntry);
        textEntry.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable e) {
				photoText.setText(e.toString());				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {	}       	
        	
        });
        
	}
	
	private boolean placeText(View v, MotionEvent event) {
		// update options after click
		findViewById(R.id.instructions).setVisibility(View.GONE);
		findViewById(R.id.changeColor).setVisibility(View.VISIBLE);
		findViewById(R.id.save).setVisibility(View.VISIBLE);
		textEntry.setVisibility(View.VISIBLE);
		photoText.setVisibility(View.VISIBLE);
		
		// save click location
		text_x = event.getX();
		text_y = event.getY() + photoText.getHeight() / 2;
		
		// place textView
		photoText.setX(text_x);
		photoText.setY(text_y);	
		
		
		return true;
	}		
	
	public void changeColor(View view){
		 OnColorChangedListener l = new OnColorChangedListener(){

			@Override
			public void colorChanged(int color) {
				textColor = color;
				photoText.setTextColor(color);				
			}			 
		 };
		
		ColorPickerDialog newFragment = new ColorPickerDialog(view.getContext(), l, textColor);
		 newFragment.show();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text, menu);
		return true;
	}
	
	
	public void save(View view){		
		// get text bitmap
		Bitmap textBitmap = loadBitmapFromView(photoText);
		photo.setImageBitmap(textBitmap);
		
		// combine two bitmaps
		Bitmap bmOverlay = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(srcBitmap, new Matrix(), null);
        canvas.drawBitmap(textBitmap, text_x, text_y - photoText.getHeight(), null);
        
		// save picture to byte array and return
		ByteArrayOutputStream stream = new ByteArrayOutputStream();        
		bmOverlay.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();
        
        returnToEditor(view);        
	}
	
	private Bitmap loadBitmapFromView(View v) {		
		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);                
	    Canvas c = new Canvas(b);
	    v.layout(0, 0, v.getWidth(), v.getHeight());
	    v.draw(c);
	    return b;
	}
	
	
	public void returnToEditor(View view){
		Intent i = new Intent(this, EditorActivity.class);
		i.putExtra("photoByteArray", byteArray);
		i.putExtra("companyTag", companyTag);
		startActivity(i);
	}

}
