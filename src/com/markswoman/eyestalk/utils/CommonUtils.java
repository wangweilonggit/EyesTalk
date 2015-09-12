package com.markswoman.eyestalk.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.model.CharacterModel;
import com.markswoman.eyestalk.model.LensBrand;
import com.markswoman.eyestalk.model.SkinType;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import org.apache.commons.io.FileUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

public class CommonUtils {

	private Context context;
	private static CommonUtils instance = null;

	public static final String SENDER_ID = "637353289693";
	public static final String BACKUP_FOLDER = "/com.markswoman.eyestalk.assets";

	public final int THRESHOLD_TABLET = DisplayMetrics.DENSITY_MEDIUM;
	public final int THRESHOLD_PHONE = DisplayMetrics.DENSITY_XHIGH;

	public static synchronized CommonUtils init(Context context) {
		if (instance == null) {
			instance = new CommonUtils(context);
		}
		return instance;
	}

	public static CommonUtils getInstance() {
		return instance;
	}

	private CommonUtils(Context context) {
		this.context = context;
	}

	public void showMessage(int resId) {
		showMessage(context.getString(resId));
	}

	public void showMessage(String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		toast.getView().setBackgroundResource(R.drawable.custom_toast);
		toast.show();
	}

	// Get bitmap object for character model
	public Bitmap convertCharacterToBitmap(CharacterModel model) {
		// Currently, character model images are saved to resource folder
		// in case of tablet, these images are saved to drawable-mdpi
		// in case of phone, these images are saved to drawable-xhdpi
		float density = context.getResources().getDisplayMetrics().densityDpi;

		if (CompatibilityUtil.isTablet(context)) {
			density = density / THRESHOLD_TABLET;
		} else {
			density = density / THRESHOLD_PHONE;
		}

		Bitmap character = Bitmap.createBitmap(model.getCharacterRes());
		LensBrand lens = model.getLens();
		if (model.getLens() != null) {
			Uri lensUri = null;
			int lensX = 0, lensY = 0;
			if (model.getSkinType() == SkinType.MELAYU) {
				lensUri = lens.eyesMelayuUri;
				lensX = getIntegerFromResource(R.integer.lens_melayu_x);
				lensY = getIntegerFromResource(R.integer.lens_melayu_y);
			} else if (model.getSkinType() == SkinType.WESTERN) {
				lensUri = lens.eyesWesternUri;
				lensX = getIntegerFromResource(R.integer.lens_western_x);
				lensY = getIntegerFromResource(R.integer.lens_western_y);
			} else if (model.getSkinType() == SkinType.ASIAN) {
				lensUri = lens.eyesAsianUri;
				lensX = getIntegerFromResource(R.integer.lens_asian_x);
				lensY = getIntegerFromResource(R.integer.lens_asian_y);
			}
			if (lensUri != null) {
				try {
					Bitmap characterEye = MediaStore.Images.Media.getBitmap(
							context.getContentResolver(),
							Uri.fromFile(new File(lensUri.toString())));
					characterEye = Bitmap.createScaledBitmap(characterEye,
							(int) (characterEye.getWidth() * density),
							(int) (characterEye.getHeight() * density), false);
					Canvas canvas = new Canvas(character);
					Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
					canvas.drawBitmap(characterEye, lensX * density, lensY
							* density, paint);
					characterEye.recycle();
				} catch (Exception e) {
					CommonUtils.getInstance().showMessage(
							"Unable to fetch asset for selection");
				}
			}
		}
		return character;
	}

	private int getIntegerFromResource(int resId) {
		return context.getResources().getInteger(resId);
	}

	// Get mutable bitmap object from original resource
	public Bitmap copyMutableBitmap(Bitmap bitmap) {
		try {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/eyestalk/temp.txt");
			file.getParentFile().mkdirs();
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();

			// Copy the byte to the file
			// Assume source bitmap loaded using options.inPreferredConfig =
			// Config.ARGB_8888;
			FileChannel channel = randomAccessFile.getChannel();
			MappedByteBuffer map = channel.map(MapMode.READ_WRITE, 0, width
					* height * 4);
			bitmap.copyPixelsToBuffer(map);
			// recycle the source bitmap, this will be no longer used.
			bitmap.recycle();
			// Create a new bitmap to load the bitmap again.
			bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			map.position(0);
			// load it back from temporary
			bitmap.copyPixelsFromBuffer(map);
			// close the temporary file and channel , then delete that also
			channel.close();
			randomAccessFile.close();
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}

	public void loadImage(ImageView imageView, String imageUrl) {
		loadImage(imageView, imageUrl, 0, 0);
	}

	public void loadImage(ImageView imageView, String imageUrl,
			int cornerRadius, int blankResId) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheOnDisk(true)
				.displayer(new RoundedBitmapDisplayer(cornerRadius))
				.showImageForEmptyUri(blankResId).showImageOnFail(blankResId)
				.showImageForEmptyUri(blankResId)
				.showImageOnLoading(blankResId).build();
		ImageLoader.getInstance().displayImage(imageUrl, imageView, options,
				null);
	}

	/**
	 * Create directory in ExternalStorage with the param path
	 * 
	 * @param path
	 *            The path of directory
	 * @return true if successful otherwise false
	 */
	public boolean createDirIfNotExists(String path) {
		boolean ret = true;

		File file = new File(Environment.getExternalStorageDirectory(), path);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				ret = false;
			}
		}
		return ret;
	}

	/**
	 * Create directory in ExternalStorage with the param path
	 * 
	 * @param path
	 *            The path of directory
	 * @return true if successful otherwise false
	 */
	public boolean cleanDirectory(String path) {
		boolean ret = true;

		File file = new File(Environment.getExternalStorageDirectory(), path);
		if (file.exists()) {
			try {
				FileUtils.cleanDirectory(file);
			} catch (IOException e) {
				ret = false;
			}
		}
		return ret;
	}

	/**
	 * Populate actual path of download file
	 * 
	 * @param fileName
	 *            Downloading file name
	 * @return
	 */
	public String getStorageUri(String path, String fileName) {
		return Environment.getExternalStorageDirectory() + path
				+ File.separator + fileName;
	}

	public Bitmap getPreview(Uri uri) {
		File image = new File(uri.toString());

		BitmapFactory.Options bounds = new BitmapFactory.Options();
		bounds.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(image.getPath(), bounds);
		if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
			return null;

		// int originalSize = (bounds.outHeight > bounds.outWidth) ?
		// bounds.outHeight
		// : bounds.outWidth;

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 1;
		return BitmapFactory.decodeFile(image.getPath(), opts);
	}

	public void deleteFileFromUri(Uri uri) {
		if (uri != null) {
			File file = new File(uri.toString());
			file.delete();
		}
	}
}
