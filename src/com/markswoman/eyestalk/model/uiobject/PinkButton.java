package com.markswoman.eyestalk.model.uiobject;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class PinkButton extends Button {

	public PinkButton(Context context) {
		super(context);
		sharedConstructor(context);
	}

	public PinkButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		sharedConstructor(context);
	}

	public PinkButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		sharedConstructor(context);
	}

	private void sharedConstructor(Context context) {
		Typeface nevisFont = Typeface.createFromAsset(context.getAssets(),
				"nevis.ttf");
		setTypeface(nevisFont);
	}

}
