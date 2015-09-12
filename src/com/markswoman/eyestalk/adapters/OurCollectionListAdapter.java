package com.markswoman.eyestalk.adapters;

import java.util.ArrayList;

import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.model.ProductSubBrand;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter class for our collection items list
 * 
 */
public class OurCollectionListAdapter extends BaseAdapter {

	private ArrayList<ProductSubBrand> items;
	private LayoutInflater inflater;

	public OurCollectionListAdapter(Context context,
			ArrayList<ProductSubBrand> items) {
		this.inflater = LayoutInflater.from(context);
		this.items = items;
		if (items == null) {
			items = new ArrayList<ProductSubBrand>();
		}
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.our_collection_grid_cell, parent,
					false);
		}

		ProductSubBrand item = getItem(position);
		TextView itemTitle = (TextView) vi
				.findViewById(R.id.textview_item_title_in_our_collection_cell);
		ImageView itemImage = (ImageView) vi
				.findViewById(R.id.imageview_item_in_our_collection_cell);
		itemTitle.setText(item.name);
		itemImage.setImageURI(item.imageUri);
		return vi;
	}
}
