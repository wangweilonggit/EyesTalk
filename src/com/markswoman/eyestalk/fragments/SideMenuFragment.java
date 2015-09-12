package com.markswoman.eyestalk.fragments;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.activities.MainActivity;
import com.markswoman.eyestalk.adapters.SideMenuAdapter;
import com.markswoman.eyestalk.fragments.base.BaseFragment;

/**
 * Fragment class for side menu
 * 
 */
public class SideMenuFragment extends BaseFragment implements OnClickListener {

	private View view;
	private ListView listSideMenu;

	private MainActivity delegate;

	public interface SideMenuItemClickListener {
		public void onNavigationItemSelected(int position);
	}

	private SideMenuItemClickListener mCallback;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_side_menu, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Initialize View and Class Members
		initializeViewAndClassMembers();
	}

	// Initialize View and Class Members
	private void initializeViewAndClassMembers() {

		delegate = (MainActivity) baseActivity;
		mCallback = (SideMenuItemClickListener) delegate;

		View vi = this.view;
		listSideMenu = (ListView) vi.findViewById(R.id.list);
		Context context = getActivity();
		String[] items = context.getResources().getStringArray(
				R.array.side_menu_items);
		ArrayList<String> menuItems = new ArrayList<String>();
		Collections.addAll(menuItems, items);
		listSideMenu.setAdapter(new SideMenuAdapter(getActivity(), menuItems));
		listSideMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				navigateToScreen(position);
			}
		});

		// Map view elements to event handlers
		vi.findViewById(R.id.button_facebook_in_sidemenu).setOnClickListener(
				this);
		vi.findViewById(R.id.button_twitter_in_sidemenu).setOnClickListener(
				this);
	}

	// Navigates to corresponding screen
	private void navigateToScreen(int position) {
		mCallback.onNavigationItemSelected(position);
	}

	@Override
	public void onClick(View v) {
		Uri uri = null;
		if (v.getId() == R.id.button_facebook_in_sidemenu) {
			uri = Uri.parse("https://www.facebook.com/X2ContactLens");
		} else if (v.getId() == R.id.button_twitter_in_sidemenu) {
			uri = Uri.parse("https://twitter.com/X2softlens");
		}
		if (uri != null) {
			Intent launchWeb = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(launchWeb);
		}
	}
}
