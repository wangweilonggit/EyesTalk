package com.markswoman.eyestalk.api.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.markswoman.eyestalk.model.LensBrand;
import com.markswoman.eyestalk.model.ProductSubBrand;

public class GetAllProductsResponse {

	@SerializedName("status")
	private int status;

	@SerializedName("products")
	private ArrayList<ProductSubBrand> products;

	@SerializedName("lens")
	private ArrayList<LensBrand> allLens;

	@SerializedName("message")
	private String message;

	public boolean isSuccess() {
		return this.status == 0;
	}

	public int getStatus() {
		return this.status;
	}

	public ArrayList<ProductSubBrand> getProducts() {
		return this.products;
	}

	public ArrayList<LensBrand> getAllLens() {
		return this.allLens;
	}

	public String getMessage() {
		return this.message;
	}
}
