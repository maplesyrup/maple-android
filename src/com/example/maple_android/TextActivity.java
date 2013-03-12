package com.example.maple_android;

import java.io.ByteArrayOutputStream;

import com.example.maple_android.ColorPickerDialog.OnColorChangedListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class TextActivity extends Activity {
	private byte[] byteArray;
	private ImageView photo;
	private String companyTag;
	private Bitmap srcBitmap;
	
	// text option
	private boolean showOptions;
	private float text_x;
	private float text_y;
	private EditText textEntry;
	private TextView photoText;
	private int textColor;
	private String fontPath;
	private double textSize;
	private final double SCALE_FACTOR = 0.2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text);
		
		// get company name
		Bundle extras = getIntent().getExtras();
		companyTag = extras.getString("companyTag");
		
		// set page title
        TextView title = (TextView)this.findViewById(R.id.headerText);
     	title.setText("Add Text To Your " + companyTag + " Ad");
		
		// get picture 
		byteArray = extras.getByteArray("photoByteArray");
		srcBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		
		
		
		
		
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
        
        // start off not showing edit options
        showOptions = false;
        
        // TextView to overlap on photo
        photoText = (TextView) findViewById(R.id.photoText);
     	textSize = photoText.getTextSize();
        textColor = photoText.getTextColors().getDefaultColor();
        
        // set up text listener
        textEntry = (EditText)this.findViewById(R.id.textEntry);
        textEntry.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable e) {
				photoText.setText(e.toString(), BufferType.SPANNABLE);
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
		if(!showOptions) toggleOptions();
		
		// save click location
		text_x = event.getX();
		text_y = event.getY();
		
		// place textView
		photoText.setX(text_x + v.getX());
		photoText.setY(text_y + v.getY() - photoText.getBaseline());	
		
		
		
		return true;
	}
	
	private void toggleOptions(){
		// switch setting
		showOptions = !showOptions;
		
		// get int value for visibility setting
		int visibility;
		if(showOptions) visibility = View.VISIBLE;
		else visibility = View.GONE;
		
		// update View visibilities
		findViewById(R.id.changeColor).setVisibility(visibility);
		findViewById(R.id.save).setVisibility(visibility);
		findViewById(R.id.changeFont).setVisibility(visibility);
		findViewById(R.id.increaseSize).setVisibility(visibility);
		findViewById(R.id.decreaseSize).setVisibility(visibility);
		textEntry.setVisibility(visibility);
		photoText.setVisibility(visibility);		
	}
	
	public void changeFont(View view){
		
	}
	
	public void changeTextSize(View view){
		// check if we are decreasing or increasing size
		double modifier = SCALE_FACTOR;
		if(view.getId() == R.id.decreaseSize) modifier *= -1;
		
		// change text size
		textSize = textSize * (1 + modifier); 
		
		// update TextView
		photoText.setTextSize((float)textSize);


		
	}
	
	public void changeColor(View view){
		 OnColorChangedListener l = new OnColorChangedListener(){

			@Override
			public void colorChanged(int color) {
				textColor = color;
				photoText.setTextColor(color);				
			}			 
		 };
		
		ColorPickerDialog colorPicker = new ColorPickerDialog(view.getContext(), l, textColor);
		colorPicker.show();		
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
		i.putExtra("accessToken", getIntent().getExtras().getString("accessToken"));
		startActivity(i);
	}

}
