package com.markswoman.eyestalk.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.activities.MainActivity;
import com.markswoman.eyestalk.activities.base.BaseActivity;
import com.markswoman.eyestalk.fragments.base.BaseFragment;
import com.markswoman.eyestalk.model.CurrentSelection;
import com.markswoman.eyestalk.model.uiobject.TouchImageView;

import static com.markswoman.eyestalk.utils.Constants.MAX_ZOOM;

/**
 * Fragment class for compare lens page
 * 
 */
public class CompareFragment extends BaseFragment implements OnClickListener {

	private MainActivity delegate;

	// View Elements
	private View view;
	private TouchImageView leftCharacter, rightCharacter;
	private RelativeLayout leftPanelActive, leftPanelDeactive;
	private RelativeLayout rightPanelActive, rightPanelDeactive;

	public static CompareFragment newInstance() {
		CompareFragment fragment = new CompareFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_compare, container,
				false);
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

		delegate = (MainActivity) baseActivity;
		delegate.setEnableSideMenu(true);

		View vi = this.view;
		// Map view elements to class members
		leftCharacter = (TouchImageView) vi
				.findViewById(R.id.imageview_left_character_in_compare_fragment);
		rightCharacter = (TouchImageView) vi
				.findViewById(R.id.imageview_right_character_in_compare_fragment);
		leftPanelActive = (RelativeLayout) vi
				.findViewById(R.id.layout_left_panel_active);
		leftPanelDeactive = (RelativeLayout) vi
				.findViewById(R.id.layout_left_panel_deactive);
		rightPanelActive = (RelativeLayout) vi
				.findViewById(R.id.layout_right_panel_active);
		rightPanelDeactive = (RelativeLayout) vi
				.findViewById(R.id.layout_right_panel_deactive);
		// initialize view objects
		leftCharacter.setMaxZoom(MAX_ZOOM);
		rightCharacter.setMaxZoom(MAX_ZOOM);
		// Map view elements to event handlers
		vi.findViewById(R.id.button_choose_lens_item_on_left_character)
				.setOnClickListener(this);
		vi.findViewById(R.id.button_check_item_on_left_character)
				.setOnClickListener(this);
		vi.findViewById(R.id.button_choose_lens_item_on_right_character)
				.setOnClickListener(this);
		vi.findViewById(R.id.button_check_item_on_right_character)
				.setOnClickListener(this);

		populateCharacterView();
	}

	// Populate characater view
	private void populateCharacterView() {

		changeComparePanel();
		if (delegate.LEFT_MODEL != null) {
			leftCharacter.setImageBitmap(delegate.LEFT_BITMAP);
		}
		if (delegate.RIGHT_MODEL != null) {
			rightCharacter.setImageBitmap(delegate.RIGHT_BITMAP);
		}
	}

	private void changeComparePanel() {
		leftPanelActive
				.setVisibility(delegate.LEFT_MODEL != null ? View.VISIBLE
						: View.INVISIBLE);
		leftPanelDeactive
				.setVisibility(delegate.LEFT_MODEL == null ? View.VISIBLE
						: View.INVISIBLE);
		rightPanelActive
				.setVisibility(delegate.RIGHT_MODEL != null ? View.VISIBLE
						: View.INVISIBLE);
		rightPanelDeactive
				.setVisibility(delegate.RIGHT_MODEL == null ? View.VISIBLE
						: View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_choose_lens_item_on_left_character) {
			delegate.currentSelection = CurrentSelection.LEFT;
			baseActivity.showFragment(BaseActivity.content_frame,
					SkinSelectFragment.newInstance(), true);
		} else if (v.getId() == R.id.button_choose_lens_item_on_right_character) {
			delegate.currentSelection = CurrentSelection.RIGHT;
			baseActivity.showFragment(BaseActivity.content_frame,
					SkinSelectFragment.newInstance(), true);
		} else if (v.getId() == R.id.button_check_item_on_left_character) {
			if (delegate.LEFT_PRODUCT != null) {
				baseActivity.showFragment(BaseActivity.content_frame,
						OurCollectionDetailFragment
								.newInstance(delegate.LEFT_PRODUCT), true);
			}
		} else if (v.getId() == R.id.button_check_item_on_right_character) {
			if (delegate.RIGHT_PRODUCT != null) {
				baseActivity.showFragment(BaseActivity.content_frame,
						OurCollectionDetailFragment
								.newInstance(delegate.RIGHT_PRODUCT), true);
			}
		}
	}
}