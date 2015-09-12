package com.markswoman.eyestalk.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.fragments.base.BaseFragment;
import com.markswoman.eyestalk.model.TipsAndTricksItem;
import com.markswoman.eyestalk.utils.CommonUtils;

/**
 * Fragment Class for Tips and Tricks detail page
 * 
 */
public class TipsAndTricksDetailFragment extends BaseFragment {

	private View view;

	private TipsAndTricksItem item;

	private TextView textViewTitle;
	private TextView textViewContent1;
	private TextView textViewContent2;
	private ImageView imageViewThumbnail1;

	public static TipsAndTricksDetailFragment newInstance(TipsAndTricksItem item) {
		TipsAndTricksDetailFragment fragment = new TipsAndTricksDetailFragment(
				item);
		return fragment;
	}

	private TipsAndTricksDetailFragment(TipsAndTricksItem item) {
		this.item = item;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_tips_and_tricks_detail,
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
		// Map view elements to class members
		textViewTitle = (TextView) vi
				.findViewById(R.id.textview_tips_and_tricks_title_in_detail);
		textViewContent1 = (TextView) vi
				.findViewById(R.id.textview_tips_and_tricks_detail_content_1);
		textViewContent2 = (TextView) vi
				.findViewById(R.id.textview_tips_and_tricks_detail_content_2);
		imageViewThumbnail1 = (ImageView) vi
				.findViewById(R.id.imageview_tips_and_tricks_detail_1);

		// Populate view content
		textViewTitle.setText(item.getTitle());
		textViewContent1.setText(item.getStory());
		if (textViewContent2 != null) {
			textViewContent2.setText(item.getStory());
		}
		CommonUtils.getInstance().loadImage(imageViewThumbnail1,
				item.getImageUrl());
	}

}
