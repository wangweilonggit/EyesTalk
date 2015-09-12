package com.markswoman.eyestalk.model.uiobject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Custom Radio Button class for sub category
 * 
 */
public class CategoryButton extends Button implements OnClickListener {

	private int normalResId;
	private int hoverResId;
	private boolean isChecked;

	public CategoryButton(Context context) {
		super(context);
		sharedConstructor(context);
	}

	public CategoryButton(Context context, int normalResId, int hoverResId) {
		super(context);
		this.normalResId = normalResId;
		this.hoverResId = hoverResId;
		sharedConstructor(context);
	}

	public CategoryButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		sharedConstructor(context);
	}

	public CategoryButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		sharedConstructor(context);
	}

	private void sharedConstructor(Context context) {
		setChecked(false);
	}

	public void setChecked(boolean flag) {
		isChecked = flag;
		if (flag == true) {
			setCompoundDrawables(null, getResources().getDrawable(hoverResId),
					null, null);
		} else {
			setCompoundDrawables(null, getResources().getDrawable(normalResId),
					null, null);
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		isChecked = !isChecked;
		setChecked(isChecked);
	}
}
