package com.markswoman.eyestalk.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

/**
 * Object class for sub category of product
 * 
 */
public class LensBrand {

	@SerializedName("id")
	public int lensId;

	@SerializedName("name")
	public String name;

	@SerializedName("lens_path")
	public String lensPath;

	@SerializedName("eyes_asian_path")
	public String eyesAsianPath;

	@SerializedName("eyes_western_path")
	public String eyesWesternPath;

	@SerializedName("eyes_melayu_path")
	public String eyesMelayuPath;

	@SerializedName("description")
	public String description;

	@SerializedName("product")
	public int productId;

	@SerializedName("deleted")
	public int deleted;

	@SerializedName("created_on")
	public String createdOn;

	@SerializedName("modified_on")
	public String modifiedOn;

	@SerializedName("status")
	public String status;

	public Uri lensUri;
	public Uri eyesAsianUri;
	public Uri eyesWesternUri;
	public Uri eyesMelayuUri;
}
