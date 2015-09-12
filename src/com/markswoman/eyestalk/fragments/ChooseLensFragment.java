package com.markswoman.eyestalk.fragments;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.devsmart.android.ui.HorizontalListView;
import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.activities.MainActivity;
import com.markswoman.eyestalk.activities.base.BaseActivity;
import com.markswoman.eyestalk.adapters.SubBrandAdapter;
import com.markswoman.eyestalk.adapters.UpgradeLensAdapter;
import com.markswoman.eyestalk.database.EyesTalkDatabase;
import com.markswoman.eyestalk.fragments.base.BaseFragment;
import com.markswoman.eyestalk.model.CharacterModel;
import com.markswoman.eyestalk.model.CurrentSelection;
import com.markswoman.eyestalk.model.LensBrand;
import com.markswoman.eyestalk.model.ProductSubBrand;
import com.markswoman.eyestalk.model.uiobject.TouchImageView;

import static com.markswoman.eyestalk.utils.Constants.MAX_ZOOM;

/**
 * Fragment class for Choose Lens page
 * 
 */
public class ChooseLensFragment extends BaseFragment implements OnClickListener {

	private View view;
	private AdapterView<ListAdapter> firstSubcategory;
	private AdapterView<ListAdapter> secondSubcategory;

	private MainActivity delegate;

	private EyesTalkDatabase database;

	private int categoryId;
	private ProductSubBrand selectedProduct;

	private CharacterModel selectedModel;
	private Bitmap selectedBitmap;

	// Subbrands Array
	private ArrayList<ProductSubBrand> allProducts;
	private ArrayList<LensBrand> allLens;

	// View elements
	private LinearLayout tabBar;
	private TouchImageView characterImage;

	public static ChooseLensFragment newInstance() {
		ChooseLensFragment fragment = new ChooseLensFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_choose_lens, container,
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

		View vi = this.view;

		View first = vi.findViewById(R.id.listview_first_subcategory);
		if (first instanceof HorizontalListView) {
			firstSubcategory = (HorizontalListView) first;
		} else if (first instanceof ListView) {
			firstSubcategory = (ListView) first;
		}

		View second = vi.findViewById(R.id.listview_second_subcategory);
		if (second instanceof HorizontalListView) {
			secondSubcategory = (HorizontalListView) second;
		} else if (second instanceof ListView) {
			secondSubcategory = (ListView) second;
		}

		characterImage = (TouchImageView) vi
				.findViewById(R.id.character_in_choose_lens);
		characterImage.setMaxZoom(MAX_ZOOM);

		delegate = (MainActivity) baseActivity;
		selectedModel = delegate.SELECTED_MODEL;

		database = new EyesTalkDatabase(delegate);

		// Tab bar buttons and layout
		vi.findViewById(R.id.button_ice_in_choose_lens)
				.setOnClickListener(this);
		vi.findViewById(R.id.button_xtwo_in_choose_lens).setOnClickListener(
				this);
		vi.findViewById(R.id.button_aplus_in_choose_lens).setOnClickListener(
				this);

		tabBar = (LinearLayout) vi
				.findViewById(R.id.linearlayout_tab_bar_in_choose_lens);

		// Map view elements to event handlers
		vi.findViewById(R.id.button_check_item_in_choose_lens)
				.setOnClickListener(this);
		vi.findViewById(R.id.button_compare_item_in_choose_lens)
				.setOnClickListener(this);

		// set category as X two by default
		categoryId = 1;

		allProducts = database.getAllProducts();
		allLens = database.getAllLens();

		// populate list content of first sub category bar
		populateFirstSubcategory();

		populateCharacterImage();
	}

	// populate list content of first sub category bar
	private void populateFirstSubcategory() {
		final ArrayList<ProductSubBrand> items = new ArrayList<ProductSubBrand>();
		String[] categories = { "ice", "x2", "aplus" };
		for (int i = 0; i < allProducts.size(); i++) {
			if (allProducts.get(i).category
					.equalsIgnoreCase(categories[categoryId]))
				items.add(allProducts.get(i));
		}
		final SubBrandAdapter adapter = new SubBrandAdapter(baseActivity, items);
		firstSubcategory.setAdapter(adapter);
		if (items.size() > 0) {
			adapter.setSelectedItem(items.get(0));
			selectedProduct = items.get(0);
			populateSecondSubcategory();
		}
		firstSubcategory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedProduct = items.get(position);
				adapter.setSelectedItem(selectedProduct);
				adapter.notifyDataSetChanged();
				populateSecondSubcategory();
			}
		});
	}

	// populate list content of second sub category bar
	private void populateSecondSubcategory() {
		final ArrayList<LensBrand> items = new ArrayList<LensBrand>();
		for (LensBrand lens : allLens) {
			if (selectedProduct != null
					&& lens.productId == selectedProduct.brandId) {
				items.add(lens);
			}
		}
		final UpgradeLensAdapter adapter = new UpgradeLensAdapter(baseActivity,
				items);
		secondSubcategory.setAdapter(adapter);
		secondSubcategory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.setSelectedItem(items.get(position));
				adapter.notifyDataSetChanged();
				selectedModel.setLens(items.get(position));
				populateCharacterImage();
			}
		});
	}

	// change character image
	private void populateCharacterImage() {
		selectedBitmap = selectedModel.getBitmap();
		characterImage.setImageBitmap(selectedBitmap);
	}

	@Override
	public void onClick(View v) {
		int color = -1;
		int selectedCategoryId = -1;
		if (v.getId() == R.id.button_ice_in_choose_lens) {
			selectedCategoryId = 0;
			color = delegate.getResources().getColor(R.color.green_color_1);
		} else if (v.getId() == R.id.button_xtwo_in_choose_lens) {
			selectedCategoryId = 1;
			color = delegate.getResources().getColor(R.color.pink_color_2);
		} else if (v.getId() == R.id.button_aplus_in_choose_lens) {
			selectedCategoryId = 2;
			color = delegate.getResources().getColor(R.color.yellow_color_1);
		} else if (v.getId() == R.id.button_check_item_in_choose_lens) {
			if (selectedProduct != null) {
				baseActivity.showFragment(BaseActivity.content_frame,
						OurCollectionDetailFragment
								.newInstance(selectedProduct), true);
			}
		} else if (v.getId() == R.id.button_compare_item_in_choose_lens) {
			if (delegate.currentSelection == CurrentSelection.LEFT) {
				delegate.LEFT_MODEL = new CharacterModel(selectedModel);
				delegate.LEFT_BITMAP = selectedBitmap;
				delegate.LEFT_PRODUCT = selectedProduct;
			} else if (delegate.currentSelection == CurrentSelection.RIGHT) {
				delegate.RIGHT_MODEL = new CharacterModel(selectedModel);
				delegate.RIGHT_BITMAP = selectedBitmap;
				delegate.RIGHT_PRODUCT = selectedProduct;
			}
			baseActivity.showFragment(BaseActivity.content_frame,
					CompareFragment.newInstance(), true);
		}
		if (selectedCategoryId != -1 && selectedCategoryId != categoryId) {
			categoryId = selectedCategoryId;
			tabBar.setBackgroundColor(color);
			selectedProduct = null;
			populateFirstSubcategory();
			populateSecondSubcategory();
		}
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		selectedModel.setLens(null);
		// selectedBitmap.recycle();
	}

}
