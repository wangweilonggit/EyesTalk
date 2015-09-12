package com.markswoman.eyestalk.api;

import com.markswoman.eyestalk.R;

import android.content.Context;

/**
 * Singleton Class for API building
 * 
 */
public class UrlBuilder {

	private static UrlBuilder instance = null;
	private Context context;

	private String POST_REGISTER_DEVICE = "/api/adddevice";
	private String GET_ALL_NEWS = "/api/news";
	private String GET_ALL_PRODUCTS = "/api/all_products?timestamp=%s";

	public static synchronized void init(Context context) {
		if (instance == null) {
			instance = new UrlBuilder(context);
		}
	}

	public static UrlBuilder getInstance() {
		return instance;
	}

	private UrlBuilder(Context context) {
		this.context = context;
	}

	private String getRestHOST() {
		return context.getResources().getString(R.string.rest_host);
	}

	public String postRegisterDevice() {
		return getRestHOST() + POST_REGISTER_DEVICE;
	}

	public String getAllNews() {
		return getRestHOST() + GET_ALL_NEWS;
	}

	public String getAllProducts(String timeStamp) {
		return getRestHOST() + String.format(GET_ALL_PRODUCTS, timeStamp);
	}

}
