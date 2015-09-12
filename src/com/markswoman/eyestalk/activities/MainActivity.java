package com.markswoman.eyestalk.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.actionbarsherlock.view.MenuItem;
import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.activities.base.BaseActivity;
import com.markswoman.eyestalk.fragments.CompareFragment;
import com.markswoman.eyestalk.fragments.OurCollectionFragment;
import com.markswoman.eyestalk.fragments.SkinSelectFragment;
import com.markswoman.eyestalk.fragments.StartMenuFragment;
import com.markswoman.eyestalk.fragments.SideMenuFragment.SideMenuItemClickListener;
import com.markswoman.eyestalk.fragments.TipsAndTricksFragment;
import com.markswoman.eyestalk.fragments.base.BaseFragment;
import com.markswoman.eyestalk.model.CharacterModel;
import com.markswoman.eyestalk.model.CurrentSelection;
import com.markswoman.eyestalk.model.ProductSubBrand;
import com.markswoman.eyestalk.model.SkinType;
import com.markswoman.eyestalk.model.TipsAndTricksItem;
import com.markswoman.eyestalk.utils.CommonUtils;
import com.slidingmenu.lib.SlidingMenu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

/** Main Activity class, navigation drawer will positioned **/
public class MainActivity extends BaseActivity implements OnClickListener,
		SideMenuItemClickListener {

	public static MainActivity instance = null;

	private SlidingMenu sideMenu;

	public CharacterModel CHARACTER_MELAYU, CHARACTER_WESTERN, CHARACTER_ASIAN;
	public CharacterModel LEFT_MODEL, RIGHT_MODEL, SELECTED_MODEL;
	public Bitmap LEFT_BITMAP, RIGHT_BITMAP;
	public ProductSubBrand LEFT_PRODUCT, RIGHT_PRODUCT;

	public static Bitmap BITMAP_ASIAN, BITMAP_MELAYU, BITMAP_WESTERN;

	private ArrayList<TipsAndTricksItem> cachedNews;

	public CurrentSelection currentSelection;

	public static Map<Uri, Bitmap> cachedBitmaps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// Customize Side Menu and Action Bar
		customizeSlideMenuAndActionbar();

		// Starts application by displaying the menu fragment
		startApplicationMenu();

		BITMAP_ASIAN = BitmapFactory.decodeResource(getResources(),
				R.drawable.character_asian).copy(Config.ARGB_8888, true);
		BITMAP_MELAYU = BitmapFactory.decodeResource(getResources(),
				R.drawable.character_melayu).copy(Config.ARGB_8888, true);
		BITMAP_WESTERN = BitmapFactory.decodeResource(getResources(),
				R.drawable.character_western).copy(Config.ARGB_8888, true);

		CHARACTER_MELAYU = new CharacterModel(this, SkinType.MELAYU,
				BITMAP_MELAYU);
		CHARACTER_WESTERN = new CharacterModel(this, SkinType.WESTERN,
				BITMAP_WESTERN);
		CHARACTER_ASIAN = new CharacterModel(this, SkinType.ASIAN, BITMAP_ASIAN);

		cachedBitmaps = new HashMap<Uri, Bitmap>();
	}

	@Override
	protected void onStart() {
		super.onStart();

		instance = this;
	}

	@Override
	protected void onStop() {
		super.onStop();

		instance = null;
	}

	// Customize side menu and action bar
	private void customizeSlideMenuAndActionbar() {
		findViewById(R.id.actionbar_button_back).setOnClickListener(this);
		findViewById(R.id.actionbar_button_toggle_navigation)
				.setOnClickListener(this);

		sideMenu = new SlidingMenu(this);
		sideMenu.setMode(SlidingMenu.RIGHT);
		sideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sideMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sideMenu.setFadeDegree(0.35f);
		sideMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		sideMenu.setMenu(R.layout.side_menu_container);
		sideMenu.setSlidingEnabled(true);
	}

	// Starts application by displaying main home screen
	private void startApplicationMenu() {

		if (!CommonUtils.getInstance().createDirIfNotExists(
				CommonUtils.BACKUP_FOLDER)) {
			CommonUtils.getInstance().showMessage(
					"Unable to create directory for downloading");
			finish();
			return;
		}

		showFragment(BaseActivity.content_frame,
				StartMenuFragment.newInstance(), false, true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		sideMenu.toggle();
		return super.onOptionsItemSelected(item);
	}

	// Enable/Disable Side Menu
	public void setEnableSideMenu(boolean flag) {
		int visibility = flag ? View.VISIBLE : View.INVISIBLE;
		findViewById(R.id.actionbar_button_back).setVisibility(visibility);
		findViewById(R.id.actionbar_button_toggle_navigation).setVisibility(
				visibility);
		// sideMenu.setSlidingEnabled(flag);
		sideMenu.setSlidingEnabled(false);
		sideMenu.showContent(flag);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.actionbar_button_back) {
			super.onBackPressed();
		} else if (v.getId() == R.id.actionbar_button_toggle_navigation) {
			sideMenu.toggle();
		}
	}

	@Override
	public void onNavigationItemSelected(int position) {
		BaseFragment fragment = null;
		switch (position) {
		case 0:
			popToRoot();
			break;
		case 1:
			currentSelection = CurrentSelection.LEFT;
			LEFT_MODEL = null;
			RIGHT_MODEL = null;
			LEFT_PRODUCT = null;
			RIGHT_PRODUCT = null;
			fragment = SkinSelectFragment.newInstance();
			break;
		case 2:
			currentSelection = CurrentSelection.LEFT;
			LEFT_MODEL = null;
			RIGHT_MODEL = null;
			LEFT_PRODUCT = null;
			RIGHT_PRODUCT = null;
			fragment = CompareFragment.newInstance();
			break;
		case 3:
			fragment = OurCollectionFragment.newInstance();
			break;
		case 4:
			fragment = TipsAndTricksFragment.newInstance();
			break;
		}
		if (fragment != null) {
			showFragment(BaseActivity.content_frame, fragment, true, true);
		}
	}

	public void cacheNews(ArrayList<TipsAndTricksItem> items) {
		this.cachedNews = items;
	}

	public ArrayList<TipsAndTricksItem> getCachedNews() {
		return this.cachedNews;
	}
}
