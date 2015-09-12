package com.markswoman.eyestalk.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.activities.MainActivity;
import com.markswoman.eyestalk.activities.base.BaseActivity;
import com.markswoman.eyestalk.api.request.GetAllNewsRequest;
import com.markswoman.eyestalk.api.response.GetAllNewsResponse;
import com.markswoman.eyestalk.fragments.base.BaseFragment;
import com.markswoman.eyestalk.model.TipsAndTricksItem;
import com.markswoman.eyestalk.utils.CommonUtils;
import com.markswoman.eyestalk.utils.RequestQueueHolder;

/**
 * Fragment class for Tips And Tricks Page
 * 
 */
public class TipsAndTricksFragment extends BaseFragment {

	private View view;
	private LayoutInflater inflater;
	private MainActivity delegate = null;
	private ArrayList<TipsAndTricksItem> items;

	private LinearLayout tipsTricksList;

	public static TipsAndTricksFragment newInstance() {
		TipsAndTricksFragment fragment = new TipsAndTricksFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.view = inflater.inflate(R.layout.fragment_tips_and_tricks,
				container, false);
		this.inflater = inflater;
		return this.view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Initialize view elements and Class Members
		initViewAndClassMembers();
	}

	// Initialize view elements and Class Members
	private void initViewAndClassMembers() {

		delegate = (MainActivity) baseActivity;
		delegate.setEnableSideMenu(true);

		// map view elements to Class members
		View vi = this.view;
		tipsTricksList = (LinearLayout) vi
				.findViewById(R.id.tips_and_tricks_list);

		if (delegate.getCachedNews() == null) {
			// Send get all news request to server
			callGetAllNewsRequest();
		} else {
			items = delegate.getCachedNews();
			populateListContent();
		}
	}

	// Send get all news request to server
	private void callGetAllNewsRequest() {
		progress.show();
		GetAllNewsRequest request = new GetAllNewsRequest(successListener,
				errorListener);
		request.setShouldCache(false);
		RequestQueue queue = RequestQueueHolder.getInstance().getRequestQueue();
		queue.add(request);
	}

	private Listener<GetAllNewsResponse> successListener = new Listener<GetAllNewsResponse>() {
		@Override
		public void onResponse(GetAllNewsResponse response) {
			progress.hide();
			if (response.getStatusCode() == 0) {
				items = response.getItems();
				delegate.cacheNews(items);
				// Fetch list content
				populateListContent();
			}
		}
	};

	// Populate list contents from web-service
	private void populateListContent() {

		// Dynamically add elements to view
		for (int i = 0; i < items.size(); i++) {
			View itemView = inflater.inflate(
					R.layout.tips_and_tricks_list_item, (ViewGroup) getView(),
					false);

			final TipsAndTricksItem item = items.get(i);
			TextView tipsTricksTitle = (TextView) itemView
					.findViewById(R.id.textview_tips_and_tricks_title);
			ImageView tipsTricksThumbnail = (ImageView) itemView
					.findViewById(R.id.imageview_tips_and_tricks_thumbnail);
			TextView tipsTricksComment = (TextView) itemView
					.findViewById(R.id.textview_tips_and_tricks_comment);
			tipsTricksTitle.setText(item.getTitle());
			CommonUtils.getInstance().loadImage(tipsTricksThumbnail,
					item.getImageUrl());
			tipsTricksComment.setText(item.getStory());
			tipsTricksTitle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					baseActivity
							.showFragment(BaseActivity.content_frame,
									TipsAndTricksDetailFragment
											.newInstance(item), true);
				}
			});

			tipsTricksList.addView(itemView);
		}
	}

}
