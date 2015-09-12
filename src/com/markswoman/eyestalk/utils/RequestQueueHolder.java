package com.markswoman.eyestalk.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueHolder {

	private RequestQueue requestQueue;
	private static final RequestQueueHolder instance = new RequestQueueHolder();

	private RequestQueueHolder() {
	}

	public static RequestQueueHolder getInstance() {
		return instance;
	}

	public void init(Context context) {
		requestQueue = Volley.newRequestQueue(context);
	}

	public RequestQueue getRequestQueue() {
		return requestQueue;
	}

}
