package com.markswoman.eyestalk.adapters;

import java.util.ArrayList;

import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.model.LensBrand;
import com.markswoman.eyestalk.utils.CommonUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static com.markswoman.eyestalk.activities.MainActivity.cachedBitmaps;

/**
 * Adapter class for Sub Category list
 * 
 */
public class UpgradeLensAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<LensBrand> items;
	private LensBrand selectedLens;

	public UpgradeLensAdapter(Context context, ArrayList<LensBrand> items) {
		this.inflater = LayoutInflater.from(context);
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public LensBrand getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setSelectedItem(LensBrand lens) {
		selectedLens = lens;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.upgrade_lens_item, parent, false);
		}
		TextView title = (TextView) vi.findViewById(R.id.textview_title);
		ImageView image = (ImageView) vi.findViewById(R.id.item_image);
		LensBrand item = getItem(position);
		title.setText(item.name);
		// image.setImageURI(item.lensUri);
		Bitmap bm = cachedBitmaps.get(item.lensUri);
		if (bm == null) {
			bm = CommonUtils.getInstance().getPreview(item.lensUri);
			cachedBitmaps.put(item.lensUri, bm);
		}
		image.setImageBitmap(bm);
		if (selectedLens == null || selectedLens.lensId != item.lensId) {
			image.setAlpha(0.5f);
		} else {
			image.setAlpha(1.0f);
		}
		return vi;
	}

}
