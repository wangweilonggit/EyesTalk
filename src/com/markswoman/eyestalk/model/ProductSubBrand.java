package com.markswoman.eyestalk.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

/**
 * Object class for sub category of product
 * 
 */
public class ProductSubBrand {

	@SerializedName("id")
	public int brandId;

	@SerializedName("name")
	public String name;

	@SerializedName("category")
	public String category;

	@SerializedName("logo_path")
	public String logoPath;

	@SerializedName("image_path")
	public String imagePath;

	@SerializedName("image_2_path")
	public String imagePath2;

	@SerializedName("image_3_path")
	public String imagePath3;

	@SerializedName("image_4_path")
	public String imagePath4;

	@SerializedName("description")
	public String description;

	@SerializedName("deleted")
	public int deleted;

	@SerializedName("created_on")
	public String createdOn;

	@SerializedName("modified_on")
	public String modifiedOn;

	@SerializedName("status")
	public String status;

	public Uri logoUri;
	public Uri imageUri;
	public Uri imageUri2;
	public Uri imageUri3;
	public Uri imageUri4;
}
