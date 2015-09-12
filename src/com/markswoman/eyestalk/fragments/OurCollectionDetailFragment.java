package com.markswoman.eyestalk.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.fragments.base.BaseFragment;
import com.markswoman.eyestalk.model.ProductSubBrand;

/**
 * Fragment class for Our Collection detail page
 * 
 */
public class OurCollectionDetailFragment extends BaseFragment implements
		OnClickListener {

	private ProductSubBrand item;

	private View view;
	private TextView textDescription;

	private ImageView mainImage;
	private ImageView productImage;
	private ImageView productImage2;
	private ImageView productImage3;
	private ImageView productImage4;

	public static OurCollectionDetailFragment newInstance(ProductSubBrand item) {
		OurCollectionDetailFragment fragment = new OurCollectionDetailFragment(
				item);
		return fragment;
	}

	private OurCollectionDetailFragment(ProductSubBrand item) {
		this.item = item;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_our_collection_detail,
				container, false);
		return this.view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Initialize view elements and class members
		initViewAndClassMembers();
	}

	// Initialize view elements and class members
	private void initViewAndClassMembers() {
		View vi = this.view;
		// Map View elements to Class Members
		mainImage = (ImageView) vi
				.findViewById(R.id.imageview_our_collection_detail);
		productImage = (ImageView) vi
				.findViewById(R.id.imageview_our_collection_detail_item_1);
		productImage2 = (ImageView) vi
				.findViewById(R.id.imageview_our_collection_detail_item_2);
		productImage3 = (ImageView) vi
				.findViewById(R.id.imageview_our_collection_detail_item_3);
		productImage4 = (ImageView) vi
				.findViewById(R.id.imageview_our_collection_detail_item_4);

		productImage.setOnClickListener(this);
		productImage2.setOnClickListener(this);
		productImage3.setOnClickListener(this);
		productImage4.setOnClickListener(this);

		// Populate View Content
		TextView collectionTitle = (TextView) vi
				.findViewById(R.id.textview_collection_title);
		textDescription = (TextView) vi
				.findViewById(R.id.textview_collection_detail_comment);
		collectionTitle.setText(item.name);
		textDescription.setText(item.description);
		mainImage.setImageURI(item.imageUri);
		productImage.setImageURI(item.imageUri);
		productImage2.setImageURI(item.imageUri2);
		productImage3.setImageURI(item.imageUri3);
		productImage4.setImageURI(item.imageUri4);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.imageview_our_collection_detail_item_1) {
			mainImage.setImageURI(item.imageUri);
		} else if (v.getId() == R.id.imageview_our_collection_detail_item_2) {
			mainImage.setImageURI(item.imageUri2);
		} else if (v.getId() == R.id.imageview_our_collection_detail_item_3) {
			mainImage.setImageURI(item.imageUri3);
		} else if (v.getId() == R.id.imageview_our_collection_detail_item_4) {
			mainImage.setImageURI(item.imageUri4);
		}
	}
}
