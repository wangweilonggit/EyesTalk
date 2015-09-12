package com.markswoman.eyestalk;

import com.markswoman.eyestalk.api.UrlBuilder;
import com.markswoman.eyestalk.storage.LocalStorage;
import com.markswoman.eyestalk.utils.CommonUtils;
import com.markswoman.eyestalk.utils.RequestQueueHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;

public class MainApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize the singletons so their instances
		// are bound to the application process.
		initSingletons();
	}

	/** Instantiate the singletons **/
	private void initSingletons() {
		CommonUtils.init(getApplicationContext());
		RequestQueueHolder.getInstance().init(getApplicationContext());
		UrlBuilder.init(getApplicationContext());
		LocalStorage.init(getApplicationContext());

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisk(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(
				defaultOptions).build();
		ImageLoader.getInstance().init(config);
	}
}
