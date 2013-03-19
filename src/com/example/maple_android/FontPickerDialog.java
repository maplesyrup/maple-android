package com.example.maple_android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

public class FontPickerDialog extends DialogFragment {
	// Keeps the font file paths and names in separate arrays
	private List<String> m_fontPaths;
	private List<String> m_fontNames;
	private Context mContext;
	private String selectedFont;

	// create callback method on font selected
	public interface FontPickerDialogListener {
		public void onFontSelected(FontPickerDialog dialog);
	}

	// Use this instance of the interface to deliver action events
	FontPickerDialogListener mListener;

	// Override the Fragment.onAttach() method to instantiate the
	// Listener
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

	public static FontPickerDialog newInstance() {
		FontPickerDialog frag = new FontPickerDialog();
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// get context
		mContext = getActivity();

		// Let FontManager find available fonts
		HashMap<String, String> fonts = FontManager.enumerateFonts();
		m_fontPaths = new ArrayList<String>();
		m_fontNames = new ArrayList<String>();

		// add fonts to List
		for (String path : fonts.keySet()) {
			m_fontPaths.add(path);
			m_fontNames.add(fonts.get(path));
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
				selectedFont = m_fontPaths.get(magicNumber);
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
	
	
	// create method to get selected font
	public String getSelectedFont(){
		return selectedFont;
	}

	// Create an adapter to show the fonts in the dialog
	// Each font will be a text view with the text being the
	// font name, written in the style of the font
	private class FontAdapter extends BaseAdapter {
		private Context mContext;

		public FontAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return m_fontNames.size();
		}

		@Override
		public Object getItem(int position) {
			return m_fontNames.get(position);
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
			Typeface tface = Typeface.createFromFile(m_fontPaths.get(position));
			view.setTypeface(tface);
			view.setText(m_fontNames.get(position));

			return view;

		}
	}
}
