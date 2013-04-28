package com.example.ad_creation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.maple_android.FontManager;
import com.example.maple_android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/** This class provides a dialog to choose a font.
 * 
 * The dialog was built with inspiration from the 
 * FontPreference class made by George Yunaev under
 * Apache 2.0 and found at <p>
 * http://www.ulduzsoft.com/2012/01/fontpreference-dialog-for-android/ 
 * <p>
 * The font picker is built on the FontManager class 
 * that searches the phone for available fonts, and 
 * returns their names and pathnames in a hashmap.<p>
 * http://www.ulduzsoft.com/2012/01/enumerating-the-fonts-on-android-platform/
 * <p>
 * Use this class by calling<p>
 * FontPickerDialog dlg = new FontPickerDialog();<p>
 * dlg.show(getFragmentManager(), "font_picker");
 * <p>
 * "font_picker" can be any text. It simply names the fragment
 * in case you need to access it later
 * 
 *
 */
public class FontPickerDialog extends DialogFragment {
	// Keeps the font file paths and names in separate arrays
	private List<String> mFontPaths; // list of file paths for the available fonts
	private List<String> mFontNames; // font names of the available fonts. These indices match up with mFontPaths
	private Context mContext; // The calling activities context.
	private String mSelectedFont; // The font that was selected
	// create callback method to bass back the selected font
	public interface FontPickerDialogListener {
		/** This method is called when a font is selected
		 * in the FontPickerDialog
		 * @param dialog The dialog used to pick the font. Use dialog.getSelectedFont() to access the pathname of the chosen font
		 */
		public void onFontSelected(FontPickerDialog dialog);
	}

	// Use this instance of the interface to deliver action events
	FontPickerDialogListener mListener;

	// Override the Fragment.onAttach() method to instantiate the
	// FontPickerDialogListener
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (FontPickerDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement FontPickerDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// get context
		mContext = getActivity();

		// Let FontManager find available fonts
		HashMap<String, String> fonts = FontManager.enumerateFonts();
		mFontPaths = new ArrayList<String>();
		mFontNames = new ArrayList<String>();

		// add fonts to List
		for (String path : fonts.keySet()) {
			mFontPaths.add(path);
			mFontNames.add(fonts.get(path));
		}

		// Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(inflater.inflate(R.layout.font_picker_dialog, null));

		// set adapter to show fonts
		FontAdapter adapter = new FontAdapter(mContext);
		builder.setAdapter(adapter, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				int magicNumber = arg1;
				mSelectedFont = mFontPaths.get(magicNumber);
				mListener.onFontSelected(FontPickerDialog.this);
			}
		});

		builder.setTitle("Select A Font");

		// Add the buttons
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// don't have to do anything on cancel
					}
				});

		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();

		return dialog;
	}
	
	
	/** Callback method that that is called once a font has been
	 * selected and the fontpickerdialog closes.
	 * @return The pathname of the font that was selected
	 */
	public String getSelectedFont(){
		return mSelectedFont;
	}

	/** Create an adapter to show the fonts in the dialog.
	* Each font will be a text view with the text being the
	* font name, written in the style of the font
	*/
	private class FontAdapter extends BaseAdapter {
		private Context mContext;

		public FontAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return mFontNames.size();
		}

		@Override
		public Object getItem(int position) {
			return mFontNames.get(position);
		}

		@Override
		public long getItemId(int position) {
			// We use the position as ID
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = (TextView) convertView;

			// This function may be called in two cases: a new view needs to be
			// created,
			// or an existing view needs to be reused
			if (view == null) {
				view = new TextView(mContext);

			} else {
				view = (TextView) convertView;
			}

			// Set text to be font name and written in font style
			Typeface tface = Typeface.createFromFile(mFontPaths.get(position));
			view.setTypeface(tface);
			view.setText(mFontNames.get(position));

			return view;

		}
	}
}
