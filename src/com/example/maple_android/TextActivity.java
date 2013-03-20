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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.commonsware.cwac.colormixer.ColorMixer;
import com.commonsware.cwac.colormixer.ColorMixerDialog;

public class TextActivity extends Activity implements
		FontPickerDialog.FontPickerDialogListener {
	/* Global app */
	MapleApplication mApp;

	private byte[] mByteArray;
	private ImageView mPhoto;
	private Bitmap mSrcBitmap;

	// text option
	private boolean mShowOptions;
	private float mTextXPos;
	private float mTextYPos;
	private EditText mTextEntryField;
	private TextView mPhotoText;
	private int mTextColor;
	private String mFilePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text);

		// Init app
		mApp = (MapleApplication) this.getApplication();

		// get picture
		Bundle extras = getIntent().getExtras();
		mByteArray = extras.getByteArray("photoByteArray");
		mFilePath = extras.getString("filePath");
		mSrcBitmap = BitmapFactory.decodeByteArray(mByteArray, 0,
				mByteArray.length);

		// set photo
		mPhoto = (ImageView) this.findViewById(R.id.photo);
		mPhoto.setImageBitmap(mSrcBitmap);

		// initialize photo for clicking
		mPhoto.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return placeText(v, event);
			}
		});

		// start off not showing edit options
		mShowOptions = false;

		// get TextView to overlap on photo
		mPhotoText = (TextView) findViewById(R.id.photoText);
		updateTextSize(); // initialize size to default

		// set up text color
		mTextColor = mPhotoText.getTextColors().getDefaultColor();
		((TextView) findViewById(R.id.changeColor)).setTextColor(mTextColor);

		// set up font size listener
		((EditText) findViewById(R.id.fontSize))
				.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						updateTextSize();
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
					}
				});

		// set up text listener
		mTextEntryField = (EditText) this.findViewById(R.id.textEntry);
		mTextEntryField.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable e) {
				mPhotoText.setText(e.toString(), BufferType.SPANNABLE);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

		});

	}

	// grabs the user entered value from the font size entry
	// text box and updates the text size with it
	private void updateTextSize() {
		// grab entry as string
		String textSize = ((EditText) findViewById(R.id.fontSize)).getText()
				.toString();

		// try to convert to int
		Integer size = null;
		try {
			size = Integer.valueOf(textSize);
		} catch (NumberFormatException e) {
		}

		// update size if possible
		if (size != null)
			mPhotoText.setTextSize((float) size);
	}

	private boolean placeText(View v, MotionEvent event) {
		// update options after click
		if (!mShowOptions)
			toggleOptions();

		// save click location
		mTextXPos = event.getX();
		mTextYPos = event.getY();

		// place textView
		mPhotoText.setX(mTextXPos + v.getX());
		mPhotoText.setY(mTextYPos + v.getY() - mPhotoText.getBaseline());

		// set focus to text edit
		mTextEntryField.setFocusable(true);
		mTextEntryField.requestFocus();

		return true;
	}

	private void toggleOptions() {
		// switch setting
		mShowOptions = !mShowOptions;

		// get int value for visibility setting
		int visibility;
		if (mShowOptions) {
			visibility = View.VISIBLE;
			findViewById(R.id.textInstructions).setVisibility(View.GONE);
		} else {
			visibility = View.INVISIBLE;
			findViewById(R.id.textInstructions).setVisibility(View.VISIBLE);
		}

		// update View visibilities
		findViewById(R.id.changeColor).setVisibility(visibility);
		findViewById(R.id.save).setVisibility(visibility);
		findViewById(R.id.changeFont).setVisibility(visibility);
		findViewById(R.id.fontSize).setVisibility(visibility);
		findViewById(R.id.fontSizeLabel).setVisibility(visibility);
		mTextEntryField.setVisibility(visibility);
		mPhotoText.setVisibility(visibility);
	}

	// start a dialog that shows all availabe fonts and
	// allows the user to pick which one they want to use
	public void changeFont(View view) {
		FontPickerDialog dlg = new FontPickerDialog();
		dlg.show(getFragmentManager(), "font_picker");
	}

	// call back method when a font has been selected
	@Override
	public void onFontSelected(FontPickerDialog dialog) {
		// get font file path and typeface style
		String fontPath = dialog.getSelectedFont();
		Typeface tface = Typeface.createFromFile(fontPath);

		// update text on ad
		mPhotoText.setTypeface(tface);

		// update font button to show what font we're using
		Button fontButton = (Button) findViewById(R.id.changeFont);
		fontButton.setTypeface(tface);

	}

	public void changeColor(View view) {

		new ColorMixerDialog(this, mTextColor,
				new ColorMixer.OnColorChangedListener() {

					@Override
					public void onColorChange(int color) {
						mTextColor = color;
						mPhotoText.setTextColor(color);
						Button b = (Button) findViewById(R.id.changeColor);
						b.setTextColor(color);

					}

				}).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text, menu);
		return true;
	}

	public void save(View view) {
		// get text bitmap
		Bitmap textBitmap = loadBitmapFromView(mPhotoText);

		// combine two bitmaps
		Bitmap bmOverlay = Bitmap.createBitmap(mSrcBitmap.getWidth(),
				mSrcBitmap.getHeight(), mSrcBitmap.getConfig());
		Canvas canvas = new Canvas(bmOverlay);
		canvas.drawBitmap(mSrcBitmap, new Matrix(), null);
		canvas.drawBitmap(textBitmap, mTextXPos, mTextYPos - mPhotoText.getHeight(),
				null);

		// save picture to byte array and return
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmOverlay.compress(Bitmap.CompressFormat.PNG, 100, stream);
		mByteArray = stream.toByteArray();

		returnToEditor(view);
	}

	private Bitmap loadBitmapFromView(View v) {
		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(0, 0, v.getWidth(), v.getHeight());
		v.draw(c);
		return b;
	}

	public void returnToEditor(View view) {
		Intent i = new Intent(this, EditorActivity.class);
		i.putExtra("photoByteArray", mByteArray);
		i.putExtra("filePath", mFilePath);

		startActivity(i);
	}

}
