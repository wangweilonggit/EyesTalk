package com.markswoman.eyestalk.adapters;

import java.util.ArrayList;

import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.model.ProductSubBrand;
import com.markswoman.eyestalk.utils.CommonUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import static com.markswoman.eyestalk.activities.MainActivity.cachedBitmaps;

/**
 * Adapter class for Sub Category list
 * 
 */
public class SubBrandAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<ProductSubBrand> items;
	private ProductSubBrand selectedItem;

	public SubBrandAdapter(Context context, ArrayList<ProductSubBrand> items) {
		this.inflater = LayoutInflater.from(context);
		this.items = items;
	}

	public void setSelectedItem(ProductSubBrand selectedItem) {
		this.selectedItem = selectedItem;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public ProductSubBrand getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProductSubBrand item = getItem(position);
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.subbrand_item, parent, false);
		}
		ImageView image = (ImageView) vi
				.findViewById(R.id.imageview_content_in_subcategory_item);
		// image.setImageURI(item.logoUri);
		Bitmap bm = null;
		bm = cachedBitmaps.get(item.logoUri);
		if (bm == null) {
			bm = CommonUtils.getInstance().getPreview(item.logoUri);
			cachedBitmaps.put(item.logoUri, bm);
		}
		image.setImageBitmap(bm);
		if (selectedItem == null || selectedItem.brandId != item.brandId) {
			image.setAlpha(0.3f);
		} else {
			image.setAlpha(1.0f);
		}
		return vi;
	}

}
