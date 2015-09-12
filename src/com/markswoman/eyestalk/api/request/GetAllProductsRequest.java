package com.markswoman.eyestalk.api.request;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.markswoman.eyestalk.api.UrlBuilder;
import com.markswoman.eyestalk.api.request.base.BaseRequest;
import com.markswoman.eyestalk.api.response.GetAllProductsResponse;
import com.markswoman.eyestalk.storage.LocalStorage;

/**
 * Custom request class for all products
 * 
 */
public class GetAllProductsRequest extends BaseRequest<GetAllProductsResponse> {

	public GetAllProductsRequest(
			Listener<GetAllProductsResponse> successListener,
			ErrorListener errorListener) {
		super(Method.PUT, UrlBuilder.getInstance().getAllProducts(
				LocalStorage.getInstance().getLatestTimestamp()),
				GetAllProductsResponse.class, successListener, errorListener);
	}

}
