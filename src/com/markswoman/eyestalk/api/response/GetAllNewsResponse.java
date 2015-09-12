package com.markswoman.eyestalk.api.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.markswoman.eyestalk.model.TipsAndTricksItem;

/**
 * Custom Response class for Get All News Request
 * 
 */
public class GetAllNewsResponse {

	@SerializedName("status")
	private int status;

	@SerializedName("result")
	private ArrayList<TipsAndTricksItem> items;

	@SerializedName("message")
	private String message;

	public int getStatusCode() {
		return this.status;
	}

	public ArrayList<TipsAndTricksItem> getItems() {
		return this.items;
	}

	public String getMessage() {
		return this.message;
	}
}
