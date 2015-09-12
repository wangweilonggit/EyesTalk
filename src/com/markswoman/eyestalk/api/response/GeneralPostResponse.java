package com.markswoman.eyestalk.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Custom Response class for Original Post Request
 * 
 */
public class GeneralPostResponse {

	@SerializedName("status")
	private int statusCode;

	@SerializedName("result")
	private String result;

	@SerializedName("message")
	private String message;

	public int getStatusCode() {
		return this.statusCode;
	}

	public String getResult() {
		return this.result;
	}

	public String getMessage() {
		return this.message;
	}

}
