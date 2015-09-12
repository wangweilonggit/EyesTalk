package com.markswoman.eyestalk.fragments.base;

import com.actionbarsherlock.app.SherlockFragment;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.activities.base.BaseActivity;
import com.markswoman.eyestalk.model.uiobject.EyesTalkProgressDialog;
import com.markswoman.eyestalk.utils.CommonUtils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends SherlockFragment {

	protected BaseActivity baseActivity;
	protected View rootView;
	protected EyesTalkProgressDialog progress;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			baseActivity = (BaseActivity) activity;
			progress = new EyesTalkProgressDialog(baseActivity);
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must extend BaseActivity");
		}
	}

	@Override
	public void onDestroyView() {
		baseActivity.hideKeyboard();

		super.onDestroyView();
	}

	protected ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			if (progress != null && progress.isShowing()) {
				progress.dismiss();
			}
			CommonUtils.getInstance().showMessage(R.string.connection_error);
		}
	};

	public boolean backButtonPressed() {
		return true;
	}

	/**
	 * 
	 * @param inflater
	 * @param container
	 * @param resource
	 * @return true if view was inflated
	 */
	protected boolean inflateViewIfNull(LayoutInflater inflater,
			ViewGroup container, int resource) {
		if (rootView == null) {
			rootView = inflater.inflate(resource, container, false);
			return true;
		} else {
			((ViewGroup) rootView.getParent()).removeView(rootView);
			return false;
		}
	}
}
