package com.markswoman.eyestalk.activities.base;

import java.util.UUID;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.fragments.base.BaseFragment;
import com.markswoman.eyestalk.utils.CompatibilityUtil;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class BaseActivity extends SherlockFragmentActivity {

	public final static int content_frame = R.id.container_fragment;
	protected DrawerLayout mDrawerLayout;
	protected BaseFragment rootFragment;
	private BaseFragment activeFragment;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if (CompatibilityUtil.isTablet(this)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	private int custom_animation = 0;

	public static enum CUSTOM_ANIMATIONS {
		FADE_IN, SLIDE_FROM_LEFT, SLIDE_FROM_RIGHT, SLIDE_FROM_TOP, SLIDE_FROM_BOTTOM
	}

	// Status variables to decide whether to show search result or favorites
	public final String SALON_SEARCH_RESULT = "SALON_SEARCH_RESULT";
	public final String SALON_FAVORITE_RESULT = "SALON_FAVORITE_RESULT";

	public void showFragment(int contentFrame, BaseFragment fragment) {
		showFragment(contentFrame, fragment, false);
	}

	public void showFragment(int contentFrame, BaseFragment fragment,
			boolean addToBackStack) {
		this.activeFragment = fragment;
		String tag = UUID.randomUUID().toString();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		switch (CUSTOM_ANIMATIONS.values()[custom_animation]) {
		case FADE_IN:
			transaction.setCustomAnimations(R.anim.abc_fade_in,
					R.anim.abc_fade_out, R.anim.abc_fade_in,
					R.anim.abc_fade_out);
			break;
		case SLIDE_FROM_LEFT:
			transaction.setCustomAnimations(R.anim.abc_slide_in_left,
					R.anim.abc_slide_out_right, R.anim.abc_fade_in,
					R.anim.abc_fade_out);
			break;
		case SLIDE_FROM_RIGHT:
			transaction.setCustomAnimations(R.anim.abc_slide_in_right,
					R.anim.abc_slide_out_left, R.anim.abc_fade_in,
					R.anim.abc_fade_out);
			break;
		case SLIDE_FROM_BOTTOM:
			transaction.setCustomAnimations(R.anim.abc_slide_in_bottom,
					R.anim.abc_slide_out_top, R.anim.abc_fade_in,
					R.anim.abc_fade_out);
			break;
		case SLIDE_FROM_TOP:
			transaction.setCustomAnimations(R.anim.abc_slide_in_top,
					R.anim.abc_slide_out_bottom, R.anim.abc_fade_in,
					R.anim.abc_fade_out);
			break;
		default:
			transaction.setCustomAnimations(R.anim.abc_fade_in,
					R.anim.abc_fade_out, R.anim.abc_fade_in,
					R.anim.abc_fade_out);
			break;
		}

		transaction.replace(contentFrame, fragment, tag);
		if (addToBackStack) {
			transaction.addToBackStack(tag);
		}
		transaction.commit();
		getSupportFragmentManager().executePendingTransactions();
		if (getSupportFragmentManager().getBackStackEntryCount() == 0)
			rootFragment = fragment;
	}

	/** Set custom_animation for fragment transaction **/
	public void setCustomAnimation(int custom_animation) {
		this.custom_animation = custom_animation;
	}

	/**
	 * Enables the cleanup of all stack before adding this fragment. Can be
	 * useful to make the Dash-board or other fragment the base fragment in
	 * terms of order
	 * 
	 * @param contentFrame
	 * @param fragment
	 * @param addToBackStack
	 * @param remove
	 */
	public void showFragment(int contentFrame, BaseFragment fragment,
			boolean addToBackStack, boolean remove) {

		if (remove) {
			this.popToRoot();
		}
		showFragment(contentFrame, fragment, addToBackStack);
	}

	public void popToRoot() {
		int backStackCount = getSupportFragmentManager()
				.getBackStackEntryCount();
		for (int i = 0; i < backStackCount; i++) {
			// Get the back stack fragment id.
			int backStackId = getSupportFragmentManager()
					.getBackStackEntryAt(i).getId();
			getSupportFragmentManager().popBackStack(backStackId,
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
		activeFragment = null;
	}

	@Override
	public void onBackPressed() {
		if (activeFragment != null) {
			if (activeFragment.backButtonPressed()) {
				super.onBackPressed();
				int backStackCount = getSupportFragmentManager()
						.getBackStackEntryCount();
				if (backStackCount > 0) {
					String tag = getSupportFragmentManager()
							.getBackStackEntryAt(backStackCount - 1).getName();
					activeFragment = (BaseFragment) getSupportFragmentManager()
							.findFragmentByTag(tag);
				} else {
					if (activeFragment.equals(rootFragment))
						activeFragment = null;
					else
						activeFragment = rootFragment;
				}
			}
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * Enable/Disable navigation drawer panel
	 * 
	 * @param flag
	 *            If true enable navigation drawer or disable
	 */
	public void setEnableNavigationPane(boolean flag) {
		int mode = flag ? DrawerLayout.LOCK_MODE_UNLOCKED
				: DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
		mDrawerLayout.setDrawerLockMode(mode);
	}

	/**** Hide soft keyboard ****/
	public void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		// check if no view has focus:
		View view = getCurrentFocus();
		if (view == null)
			return;

		inputManager.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
