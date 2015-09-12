package com.markswoman.eyestalk.api.request;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.markswoman.eyestalk.api.UrlBuilder;
import com.markswoman.eyestalk.api.request.base.BaseRequest;
import com.markswoman.eyestalk.api.response.GeneralPostResponse;

/**
 * Custom Request class for registering device ID to server
 * 
 */
public class PostRegisterDeviceRequest extends BaseRequest<GeneralPostResponse> {

	private String deviceId;

	public PostRegisterDeviceRequest(
			Listener<GeneralPostResponse> successListener,
			ErrorListener errorListener, String deviceId) {
		super(Method.POST, UrlBuilder.getInstance().postRegisterDevice(),
				GeneralPostResponse.class, successListener, errorListener);
		this.deviceId = deviceId;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		Map<String, String> params = new HashMap<String, String>();
		addParam(params, "gcm_id", this.deviceId);
		return params;
	}

}
