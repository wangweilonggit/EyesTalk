package com.markswoman.eyestalk.api.request;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.markswoman.eyestalk.api.UrlBuilder;
import com.markswoman.eyestalk.api.request.base.BaseRequest;
import com.markswoman.eyestalk.api.response.GetAllNewsResponse;

/**
 * Custom Request class which is to fetch all news from the server
 * 
 */
public class GetAllNewsRequest extends BaseRequest<GetAllNewsResponse> {

	public GetAllNewsRequest(Listener<GetAllNewsResponse> successListener,
			ErrorListener errorListener) {
		super(Method.GET, UrlBuilder.getInstance().getAllNews(),
				GetAllNewsResponse.class, successListener, errorListener);
	}

}
