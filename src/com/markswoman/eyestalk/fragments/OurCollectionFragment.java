package com.markswoman.eyestalk.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.activities.MainActivity;
import com.markswoman.eyestalk.activities.base.BaseActivity;
import com.markswoman.eyestalk.adapters.OurCollectionListAdapter;
import com.markswoman.eyestalk.database.EyesTalkDatabase;
import com.markswoman.eyestalk.fragments.base.BaseFragment;
import com.markswoman.eyestalk.model.EyesTalkCategory;
import com.markswoman.eyestalk.model.ProductSubBrand;

/**
 * Fragment class for our collection page
 * 
 */
public class OurCollectionFragment extends BaseFragment implements
		OnClickListener {

	private View view;
	private GridView listCollections;
	private MainActivity delegate;

	private ArrayList<ProductSubBrand> items;
	private OurCollectionListAdapter adapter;
	private String category;

	private EyesTalkDatabase database;
	private ArrayList<ProductSubBrand> allProducts;

	// View elements
	private LinearLayout tabBar;

	public static OurCollectionFragment newInstance() {
		OurCollectionFragment fragment = new OurCollectionFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_our_collection,
				container, false);
		return this.view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Initialize view elements and Class members
		initViewAndClassMembers();
	}

	// Initialize view elements and Class members
	private void initViewAndClassMembers() {

		delegate = (MainActivity) baseActivity;
		delegate.setEnableSideMenu(true);

		database = new EyesTalkDatabase(delegate);
		allProducts = database.getAllProducts();
		if (allProducts == null) {
			allProducts = new ArrayList<ProductSubBrand>();
		}
		items = new ArrayList<ProductSubBrand>();
		category = EyesTalkCategory.XTWO;

		View vi = this.view;
		listCollections = (GridView) vi
				.findViewById(R.id.gridview_our_collections_in_our_collection);

		adapter = new OurCollectionListAdapter(delegate, items);
		listCollections.setAdapter(adapter);

		vi.findViewById(R.id.button_ice_in_our_collection_fragment)
				.setOnClickListener(this);
		vi.findViewById(R.id.button_xtwo_in_our_collection_fragment)
				.setOnClickListener(this);
		vi.findViewById(R.id.button_aplus_in_our_collection_fragment)
				.setOnClickListener(this);

		tabBar = (LinearLayout) vi
				.findViewById(R.id.linearlayout_tab_bar_in_our_collection);

		listCollections.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ProductSubBrand item = items.get(position);
				baseActivity.showFragment(BaseActivity.content_frame,
						OurCollectionDetailFragment.newInstance(item), true);
			}
		});

		populateCollectionsList();
	}

	@Override
	public void onClick(View v) {
		int color = -1;
		if (v.getId() == R.id.button_ice_in_our_collection_fragment) {
			category = EyesTalkCategory.ICE;
			color = delegate.getResources().getColor(R.color.green_color_1);
		} else if (v.getId() == R.id.button_xtwo_in_our_collection_fragment) {
			category = EyesTalkCategory.XTWO;
			color = delegate.getResources().getColor(R.color.pink_color_2);
		} else if (v.getId() == R.id.button_aplus_in_our_collection_fragment) {
			category = EyesTalkCategory.APLUS;
			color = delegate.getResources().getColor(R.color.yellow_color_1);
		}
		if (color != -1) {
			tabBar.setBackgroundColor(color);
			populateCollectionsList();
		}
	}

	@SuppressLint("NewApi")
	private void populateCollectionsList() {
		items.clear();
		for (ProductSubBrand product : allProducts) {
			if (product.category.equalsIgnoreCase(category)) {
				items.add(product);
			}
		}

		adapter.notifyDataSetChanged();

		listCollections.post(new Runnable() {
			@Override
			public void run() {
				ViewGroup.LayoutParams layoutParams = listCollections
						.getLayoutParams();
				int colCount = listCollections.getNumColumns();
				double rowCount = (double) items.size() / (double) colCount;
				layoutParams.height = (delegate.getResources()
						.getDimensionPixelSize(
								R.dimen.our_collection_listitem_height) + delegate
						.getResources().getDimensionPixelSize(
								R.dimen.our_collection_list_vertical_spacing))
						* ((int) rowCount + 1) + 100;
				listCollections.setLayoutParams(layoutParams);
			}
		});
	}
}
