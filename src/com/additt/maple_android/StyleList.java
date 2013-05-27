package com.additt.maple_android;

import java.util.ArrayList;
import com.twotoasters.android.horizontalimagescroller.widget.TextStyle;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * This class holds all of the text styles that we provide for the Text
 * Activity. They are defined through the TextStyle class, and can be accessed
 * with StyleList.getStyles()
 * 
 * @author Eli
 * 
 */
public class StyleList {

	public static ArrayList<TextStyle> getStyles() {
		ArrayList<TextStyle> styles = new ArrayList<TextStyle>();
		TextStyle style;
		Paint p;

		// Basic white text
		style = new TextStyle(R.drawable.text_style_white);
		p = new Paint();
		p.setColor(Color.WHITE);
		style.addPaint(p);
		styles.add(style);

//		// Basic black text
//		style = new TextStyle();
//		p = new Paint();
//		p.setColor(Color.BLACK);
//		style.addPaint(p);
//		styles.add(style);
//
//		// white text black outline
//		style = new TextStyle();
//		// create paint for black outline to go
//		// behind main text
//		p = new Paint();
//		p.setStyle(Paint.Style.STROKE);
//		p.setStrokeWidth(4);
//		style.addPaint(p);
//		styles.add(style);
//		// create paint for white text
//		p = new Paint();
//		p.setColor(Color.WHITE);
//		style.addPaint(p);
//
//		// White Text black shadow
//		style = new TextStyle();
//		p = new Paint();
//		p.setColor(Color.WHITE);
//		p.setShadowLayer(2.0f, 1.0f, 1.0f, Color.BLACK);
//		style.addPaint(p);
//		styles.add(style);

		// return list of styles
		return styles;
	}
}
