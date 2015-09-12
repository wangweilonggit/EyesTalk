package com.markswoman.eyestalk.model.uiobject;

import com.markswoman.eyestalk.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class EyesTalkProgressDialog extends Dialog {

	private ImageView iv;

	public EyesTalkProgressDialog(Context context) {
		super(context, R.style.TransparentProgressDialog);
		WindowManager.LayoutParams wlmp = getWindow().getAttributes();
		wlmp.gravity = Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(wlmp);
		setTitle(null);
		setCanceledOnTouchOutside(false);
		setOnCancelListener(null);
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				context.getResources().getDimensionPixelSize(
						R.dimen.progress_dialog_width), context.getResources()
						.getDimensionPixelSize(R.dimen.progress_dialog_height));
		iv = new ImageView(context);
		iv.setImageResource(R.drawable.progress_icon);
		layout.addView(iv, params);
		addContentView(layout, params);
	}

	@Override
	public void show() {
		super.show();
		RotateAnimation anim = new RotateAnimation(0.0f, 358.0f,
				Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,
				.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(3000);
		iv.setAnimation(anim);
		iv.startAnimation(anim);
	}
}