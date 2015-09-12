package com.markswoman.eyestalk.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.activities.MainActivity;
import com.markswoman.eyestalk.activities.base.BaseActivity;
import com.markswoman.eyestalk.fragments.base.BaseFragment;
import com.markswoman.eyestalk.model.CharacterModel;

/**
 * Fragment class for skin select page
 * 
 */
public class SkinSelectFragment extends BaseFragment implements OnClickListener {

	private View view;
	private MainActivity delegate;

	public static SkinSelectFragment newInstance() {
		SkinSelectFragment fragment = new SkinSelectFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_skin_select, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Initialize view and class members
		initViewAndClassMembers();
	}

	// Initialize view and class members
	private void initViewAndClassMembers() {
		delegate = (MainActivity) baseActivity;
		delegate.setEnableSideMenu(true);

		View vi = this.view;
		vi.findViewById(R.id.button_melayu_in_skin_select).setOnClickListener(
				this);
		vi.findViewById(R.id.button_western_in_skin_select).setOnClickListener(
				this);
		vi.findViewById(R.id.button_asia_in_skin_select).setOnClickListener(
				this);
	}

	@Override
	public void onClick(View v) {
		CharacterModel selectedModel = null;
		if (v.getId() == R.id.button_melayu_in_skin_select) {
			selectedModel = new CharacterModel(delegate.CHARACTER_MELAYU);
		} else if (v.getId() == R.id.button_western_in_skin_select) {
			selectedModel = new CharacterModel(delegate.CHARACTER_WESTERN);
		} else if (v.getId() == R.id.button_asia_in_skin_select) {
			selectedModel = new CharacterModel(delegate.CHARACTER_ASIAN);
		}
		if (selectedModel != null) {
			delegate.SELECTED_MODEL = selectedModel;
			baseActivity.showFragment(BaseActivity.content_frame,
					ChooseLensFragment.newInstance(), true);
		}
	}
}
