package com.markswoman.eyestalk.api.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.markswoman.eyestalk.model.LensBrand;

/**
 * Custom Response class for Get All Lens Products Request
 * 
 */
public class GetAllLensResponse {

	@SerializedName("status")
	private int status;

	@SerializedName("result")
	private ArrayList<LensBrand> lens;

	@SerializedName("message")
	private String message;

	public boolean isSuccess() {
		return this.status == 0;
	}

	public int getStatus() {
		return this.status;
	}

	public ArrayList<LensBrand> getAllLens() {
		return this.lens;
	}

	public String getMessage() {
		return this.message;
	}
}
