package com.markswoman.eyestalk.api.request.base;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public abstract class BaseRequest<T> extends Request<T> {

	@Override
	protected VolleyError parseNetworkError(VolleyError volleyError) {

		if (volleyError.networkResponse != null
				&& volleyError.networkResponse.data != null) {
			VolleyError error = new VolleyError(new String(
					volleyError.networkResponse.data));
			volleyError = error;
		}
		return super.parseNetworkError(volleyError);
	}

	private Gson mGson;
	private Class<T> mResponseClass;
	private Listener<T> mSuccessListener;

	public BaseRequest(int method, String url, Class<T> responseClass,
			Listener<T> successListener, ErrorListener errorListener) {
		super(method, url, errorListener);
		mResponseClass = responseClass;
		mSuccessListener = successListener;
		mGson = new Gson();

		DefaultRetryPolicy policy = new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		this.setRetryPolicy(policy);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			T parsedGSON = mGson.fromJson(jsonString, mResponseClass);
			return Response.success(parsedGSON,
					HttpHeaderParser.parseCacheHeaders(response));

		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException je) {
			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected void deliverResponse(T response) {
		if (mSuccessListener != null) {
			mSuccessListener.onResponse(response);
		}
	}

	protected void addParam(Map<String, String> params, String paramKey,
			String paramValue) {
		if (paramValue != null) {
			params.put(paramKey, paramValue);
		}
	}

}