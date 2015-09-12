package com.markswoman.eyestalk.fragments;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.activities.MainActivity;
import com.markswoman.eyestalk.api.request.GetAllProductsRequest;
import com.markswoman.eyestalk.api.response.GetAllProductsResponse;
import com.markswoman.eyestalk.fragments.base.BaseFragment;
import com.markswoman.eyestalk.model.LensBrand;
import com.markswoman.eyestalk.model.ProductSubBrand;
import com.markswoman.eyestalk.service.AssetsControllerService;
import com.markswoman.eyestalk.service.DownloadService;
import com.markswoman.eyestalk.storage.LocalStorage;
import com.markswoman.eyestalk.utils.CommonUtils;
import com.markswoman.eyestalk.utils.RequestQueueHolder;

/**
 * Fragment class for menu page
 * 
 */
public class StartMenuFragment extends BaseFragment implements OnClickListener {

	private MainActivity delegate;
	private ArrayList<ProductSubBrand> allProducts;
	private ArrayList<LensBrand> allLens;

	private View view;
	private ProgressDialog downloadProgress;

	private boolean isDownloaded;

	public static StartMenuFragment newInstance() {
		StartMenuFragment fragment = new StartMenuFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isDownloaded = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_menu, container, false);
		return this.view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) baseActivity;
		delegate.setEnableSideMenu(false);

		// Initialize view elements and class members
		initViewAndClassMembers();

		if (!isDownloaded) {
			downloadAssets();
			isDownloaded = true;
		}
	}

	// Initialize view elements and class members
	private void initViewAndClassMembers() {
		View vi = this.view;
		// Map view elements to Event Handlers
		vi.findViewById(R.id.menu_button_product_matching).setOnClickListener(
				this);
		vi.findViewById(R.id.menu_button_tips_and_tricks).setOnClickListener(
				this);
		vi.findViewById(R.id.menu_button_lens_comparison).setOnClickListener(
				this);
		vi.findViewById(R.id.menu_button_our_collection).setOnClickListener(
				this);

		// initialize download progress dialog
		downloadProgress = new ProgressDialog(getActivity());
		downloadProgress.setMessage("Downloading");
		downloadProgress.setIndeterminate(false);
		downloadProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		downloadProgress.setCancelable(false);
		downloadProgress.setCanceledOnTouchOutside(false);

		allProducts = new ArrayList<ProductSubBrand>();
		allLens = new ArrayList<LensBrand>();
	}

	@Override
	public void onClick(View v) {
		int position = -1;
		if (v.getId() == R.id.menu_button_product_matching) {
			position = 1;
		} else if (v.getId() == R.id.menu_button_tips_and_tricks) {
			position = 4;
		} else if (v.getId() == R.id.menu_button_lens_comparison) {
			position = 2;
		} else if (v.getId() == R.id.menu_button_our_collection) {
			position = 3;
		}
		if (position != -1) {
			delegate.onNavigationItemSelected(position);
		}
	}

	private void downloadAssets() {
		progress.show();
		GetAllProductsRequest request = new GetAllProductsRequest(
				successListener, errorListener);
		request.setShouldCache(false);
		RequestQueue queue = RequestQueueHolder.getInstance().getRequestQueue();
		queue.add(request);
	}

	private Listener<GetAllProductsResponse> successListener = new Listener<GetAllProductsResponse>() {
		@Override
		public void onResponse(GetAllProductsResponse response) {
			progress.dismiss();
			if (response.isSuccess()) {
				allProducts = response.getProducts();
				allLens = response.getAllLens();
				startDownloadingAssets();
			} else {
				onErrorCallback(false);
			}
		}
	};

	private ErrorListener errorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			progress.dismiss();
			onErrorCallback(false);
		}
	};

	private void onErrorCallback(boolean noAssets) {
		String timestamp = LocalStorage.getInstance().getLatestTimestamp();
		if (TextUtils.isEmpty(timestamp)) {
			getActivity().finish();
		}
		int notification = noAssets ? R.string.no_update
				: R.string.assets_fetch_error;
		CommonUtils.getInstance().showMessage(notification);
	}

	private void startDownloadingAssets() {
		if (allProducts == null || allLens == null || allProducts.size() == 0
				|| allLens.size() == 0) {
			onErrorCallback(true);
			return;
		}
		downloadProgress.show();
		AssetsControllerService.allProducts = allProducts;
		AssetsControllerService.allLens = allLens;

		Long tsLong = System.currentTimeMillis() / 1000;
		String ts = tsLong.toString();

		// If it's first time run app, then remove all assets from SDCard
		String lastTimestamp = LocalStorage.getInstance().getLatestTimestamp();
		if (TextUtils.isEmpty(lastTimestamp)) {
			CommonUtils.getInstance().cleanDirectory(CommonUtils.BACKUP_FOLDER);
		}

		Intent intent = new Intent(baseActivity, AssetsControllerService.class);
		intent.putExtra("receiver", new DownloadReceiver(new Handler()));
		intent.putExtra("timestamp", ts);
		baseActivity.startService(intent);
	}

	private class DownloadReceiver extends ResultReceiver {
		public DownloadReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			super.onReceiveResult(resultCode, resultData);
			if (resultCode == DownloadService.UPDATE_PROGRESS) {
				int progress = resultData.getInt("progress");
				String url = resultData.getString("url");
				if (progress == -1) {
					CommonUtils.getInstance().showMessage("Download completed");
					downloadProgress.dismiss();
					return;
				}
				downloadProgress.setMessage(String.format("%d of %d files\n%s",
						AssetsControllerService.downloadIndex + 1,
						AssetsControllerService.urlArray.size(), url));
				downloadProgress.setProgress(progress);
			}
		}
	}

}
