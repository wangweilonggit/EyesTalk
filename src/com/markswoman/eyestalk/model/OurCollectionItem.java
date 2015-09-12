package com.markswoman.eyestalk.model;

/**
 * Object class for our collection item
 * 
 */
public class OurCollectionItem {

	private int resId;
	private String name;

	public OurCollectionItem(String name, int resId) {
		this.name = name;
		this.resId = resId;
	}

	public String getName() {
		return this.name;
	}

	public int getResId() {
		return this.resId;
	}

}
