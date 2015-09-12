package com.markswoman.eyestalk.adapters;

import java.util.ArrayList;

import com.markswoman.eyestalk.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter class for Side Menu
 * 
 */
public class SideMenuAdapter extends BaseAdapter {

	private ArrayList<String> items;
	private LayoutInflater inflater;

	public SideMenuAdapter(Context context, ArrayList<String> items) {
		this.inflater = LayoutInflater.from(context);
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public String getItem(int position) {
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
			vi = inflater.inflate(R.layout.side_menu_list_item, parent, false);
		}
		TextView title = (TextView) vi
				.findViewById(R.id.textview_sidemenu_item_title);
		title.setText(getItem(position));
		return vi;
	}

}
