package com.markswoman.eyestalk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Object class for Tips and Tricks item
 * 
 */
public class TipsAndTricksItem {

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("short_desc")
	private String shortDescription;

	@SerializedName("image_path")
	private String imageUrl;

	@SerializedName("story")
	private String story;

	@SerializedName("created_on")
	private String createdOn;

	public String getTitle() {
		return this.title;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public String getStory() {
		return this.story;
	}

	public String getCreatedOn() {
		return this.createdOn;
	}

}
