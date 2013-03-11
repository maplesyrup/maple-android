package com.example.maple_android;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.*;

public class EditorActivity extends Activity implements OnItemSelectedListener {
	public enum Filters {
	    GAUSSIAN("Gaussian"),
	    POSTERIZE("Posterize"),
	    NONE("None")
	    ;
	    
	    private Filters (final String text) {
	        this.text = text;
	    }

	    private final String text;

	   
	    @Override
	    public String toString() {
	        return text;
	    }
	}
	
	private Uri fileUri;
	private ImageView photo;
	private Spinner filterSpinner;
	private Bitmap srcBitmap;
	private Bitmap currBitmap;
	private byte[] byteArray;
	/*for tagging a company */
	private String companyTag;
	private AutoCompleteTextView companySuggest;
	private String[] companySuggestions = {"Apple", "Nike", "Vibram"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Bundle extras = getIntent().getExtras();

        filterSpinner = (Spinner) findViewById(R.id.filters);
        filterSpinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filters_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        filterSpinner.setAdapter(adapter);

        // Grab photo byte array and decode it
        byteArray = extras.getByteArray("photoByteArray");
        
        srcBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        currBitmap = srcBitmap;
        
        photo = (ImageView)this.findViewById(R.id.photo);
        photo.setImageBitmap(srcBitmap);
        
        /*for tagging a company */
        companySuggest = (AutoCompleteTextView)findViewById(R.id.companySuggest);
       // companySuggest.addTextChangedListener(this);
        companySuggest.setAdapter(new ArrayAdapter<String>(this,
        							android.R.layout.simple_dropdown_item_1line, companySuggestions));
        
        // check if a company tag has already been made
        String tag = extras.getString("companyTag");
        if(tag != null){
        	companySuggest.setText(tag);
        	tagPicture(companySuggest);
        }

    }
	
	public void returnToMain(View view){
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}
	
	public void tagPicture(View view){
		// save company tag
		companyTag = companySuggest.getText().toString();
		
		// show further editing options
		filterSpinner.setVisibility(View.VISIBLE);
		findViewById(R.id.post).setVisibility(View.VISIBLE);
		findViewById(R.id.returnToMain).setVisibility(View.VISIBLE);
		findViewById(R.id.logo).setVisibility(View.VISIBLE);
		
	}
	
	public void addLogo(View view){
		Intent i = new Intent(this, LogoActivity.class);
		i.putExtra("photoByteArray", byteArray);
		i.putExtra("companyTag", companyTag);
		startActivity(i);
	}
	
	
	public void postAd(View view) {
		fileUri = Utility.getOutputMediaFileUri(Utility.MEDIA_TYPE_IMAGE); // create a file to save the image
		
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        
        currBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
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

		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("image", fileUri.getPath()));
        params.add(new BasicNameValuePair("user_id", "1"));
        Utility.post("http://10.0.2.2:3000/users/1/posts", params);
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		MapleFilter mapleFilter = null;
		
		String strFilter = filterSpinner.getSelectedItem().toString();
		
		if (strFilter.equals(Filters.GAUSSIAN.toString())) {
			mapleFilter = new MapleGaussianFilter();
		} else if (strFilter.equals(Filters.POSTERIZE.toString())) {
			mapleFilter = new MaplePosterizeFilter();
		} else if (strFilter.equals(Filters.NONE.toString())) {
			currBitmap = srcBitmap;
			return;
		} else {
			return;
		}
		
		if (mapleFilter != null) {
			currBitmap = mapleFilter.filterBitmap(srcBitmap);
		}
		photo.setImageBitmap(currBitmap);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
