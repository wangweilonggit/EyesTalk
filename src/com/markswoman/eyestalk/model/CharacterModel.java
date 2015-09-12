package com.markswoman.eyestalk.model;

import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.utils.CommonUtils;
import com.markswoman.eyestalk.utils.CompatibilityUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.util.DisplayMetrics;

/**
 * Object class for Character
 * 
 */
public class CharacterModel {

	private Bitmap characterRes;
	private LensBrand lens;
	private SkinType skinType;
	private Context context;

	private final int THRESHOLD_TABLET = DisplayMetrics.DENSITY_MEDIUM;
	private final int THRESHOLD_PHONE = DisplayMetrics.DENSITY_XHIGH;

	public CharacterModel(Context context, SkinType skinType,
			Bitmap characterRes) {
		this.characterRes = characterRes;
		this.context = context;
		this.skinType = skinType;
	}

	public CharacterModel(CharacterModel another) {
		this.context = another.getContext();
		this.skinType = another.getSkinType();
		this.characterRes = another.getCharacterRes();
		this.lens = another.getLens();
	}

	public Context getContext() {
		return this.context;
	}

	public Bitmap getCharacterRes() {
		return this.characterRes;
	}

	public LensBrand getLens() {
		return this.lens;
	}

	public void setLens(LensBrand lens) {
		this.lens = lens;
	}

	public SkinType getSkinType() {
		return this.skinType;
	}

	public Bitmap getBitmap() {
		// Currently, character model images are saved to resource folder
		// in case of tablet, these images are saved to drawable-mdpi
		// in case of phone, these images are saved to drawable-xhdpi
		float density = context.getResources().getDisplayMetrics().densityDpi;

		if (CompatibilityUtil.isTablet(context)) {
			density = density / THRESHOLD_TABLET;
		} else {
			density = density / THRESHOLD_PHONE;
		}
		Bitmap characterBitmap;
		if (lens != null) {
			characterBitmap = Bitmap.createBitmap(characterRes);
			Uri lensUri = null;
			int lensX = 0, lensY = 0;
			if (skinType == SkinType.ASIAN) {
				lensUri = lens.eyesAsianUri;
				lensX = context.getResources().getInteger(
						R.integer.lens_asian_x);
				lensY = context.getResources().getInteger(
						R.integer.lens_asian_y);
			} else if (skinType == SkinType.MELAYU) {
				lensUri = lens.eyesMelayuUri;
				lensX = context.getResources().getInteger(
						R.integer.lens_melayu_x);
				lensY = context.getResources().getInteger(
						R.integer.lens_melayu_y);
			} else if (skinType == SkinType.WESTERN) {
				lensUri = lens.eyesWesternUri;
				lensX = context.getResources().getInteger(
						R.integer.lens_western_x);
				lensY = context.getResources().getInteger(
						R.integer.lens_western_y);
			}
			if (lensUri != null) {
				try {
					Bitmap characterEye = CommonUtils.getInstance().getPreview(
							lensUri);
					characterEye = Bitmap.createScaledBitmap(characterEye,
							(int) (characterEye.getWidth() * density),
							(int) (characterEye.getHeight() * density), false);
					Canvas canvas = new Canvas(characterBitmap);
					Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
					canvas.drawBitmap(characterEye, lensX * density, lensY
							* density, paint);
					characterEye.recycle();
				} catch (Exception e) {
					CommonUtils.getInstance().showMessage(
							"Unable to fetch asset for selection");
				}
			}
		} else {
			characterBitmap = characterRes;
		}
		return characterBitmap;
	}

	public void recycle() {
		if (this.characterRes != null) {
			this.characterRes.recycle();
		}
	}
}
