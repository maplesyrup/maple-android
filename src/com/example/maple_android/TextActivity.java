package com.example.maple_android;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.example.maple_android.ColorPickerDialog.OnColorChangedListener;

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
	private final double SCALE_FACTOR = 0.2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text);
		
		// get company name
		Bundle extras = getIntent().getExtras();
		companyTag = extras.getString("companyTag");
		
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
        
        // get TextView to overlap on photo
        photoText = (TextView) findViewById(R.id.photoText);
     	updateTextSize(); // initialize size to default
        
     	// set up text color
     	textColor = photoText.getTextColors().getDefaultColor();
     	((TextView) findViewById(R.id.changeColor)).setTextColor(textColor);
     	
     	// set up font size listener
     	((EditText)findViewById(R.id.fontSize)).addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	updateTextSize();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
        
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
	
	public void changeFontStyle(View view){
		/* Android uses a really dumb sysem where you can't individually specify bold and italic,
		 * if you want both there is a different setting for it. Makes this a little messier.
		 */
		
		CheckBox bold = (CheckBox)findViewById(R.id.bold);
		CheckBox italic = (CheckBox)findViewById(R.id.italic);			
		
		if(bold.isChecked() && italic.isChecked()) photoText.setTypeface(null, Typeface.BOLD_ITALIC);
		else if(bold.isChecked()) photoText.setTypeface(null, Typeface.BOLD);
		else if(italic.isChecked()) photoText.setTypeface(null, Typeface.ITALIC);
		else photoText.setTypeface(Typeface.DEFAULT);

	}
	
	// grabs the user entered value from the font size entry
	// text box and updates the text size with it
	private void updateTextSize() {
		// grab entry as string
		String textSize = ((EditText)findViewById(R.id.fontSize)).getText().toString();
		
		// try to convert to int
		Integer size = null;
		try {
		    size = new Integer(textSize);
		  } catch (NumberFormatException e) {}
		
		// update size if possible
		if(size != null) photoText.setTextSize((float)size);
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
		
		// set focus to text edit
		textEntry.setFocusable(true);
		textEntry.requestFocus();
		
		
		return true;
	}
	
	private void toggleOptions(){
		// switch setting
		showOptions = !showOptions;
		
		// get int value for visibility setting
		int visibility;
		if(showOptions){
			visibility = View.VISIBLE;
			findViewById(R.id.textInstructions).setVisibility(View.GONE);
		}
		else {
			visibility = View.INVISIBLE;
			findViewById(R.id.textInstructions).setVisibility(View.VISIBLE);
		}
		
		// update View visibilities
		findViewById(R.id.changeColor).setVisibility(visibility);
		findViewById(R.id.save).setVisibility(visibility);
		findViewById(R.id.changeFont).setVisibility(visibility);
		findViewById(R.id.fontSize).setVisibility(visibility);
		findViewById(R.id.fontSizeLabel).setVisibility(visibility);
		findViewById(R.id.bold).setVisibility(visibility);
		findViewById(R.id.italic).setVisibility(visibility);
		textEntry.setVisibility(visibility);
		photoText.setVisibility(visibility);		
	}
	
	public void changeFont(View view){
		
	}
	
	public void changeColor(View view){
		 OnColorChangedListener l = new OnColorChangedListener(){

			@Override
			public void colorChanged(int color) {
				textColor = color;
				photoText.setTextColor(color);
				((TextView) findViewById(R.id.photoText)).setTextColor(textColor);
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
