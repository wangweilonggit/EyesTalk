package com.markswoman.eyestalk.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocalStorage {

	private final String GCM_REGISTER_ID = "GCM_REGISTER_ID";
	private final String IS_REGISTERED_DEVICE = "IS_REGISTERED_DEVICE";
	private final String LATEST_TIMESTAMP = "LATEST_TIMESTAMP";
	private final String IS_EXIST_UNREAD_NEWS = "IS_EXIST_UNREAD_NEWS";

	private final SharedPreferences mSharedPreferences;
	private static LocalStorage instance;

	private LocalStorage(final Context context) {
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
	}

	public static LocalStorage init(final Context context) {
		if (instance == null) {
			instance = new LocalStorage(context);
		}
		return instance;
	}

	public static LocalStorage getInstance() {
		return instance;
	}

	public void saveGCMRegisterID(String value) {
		mSharedPreferences.edit().putString(GCM_REGISTER_ID, value).commit();
	}

	public String getSavedGCMRegisterID() {
		return mSharedPreferences.getString(GCM_REGISTER_ID, null);
	}

	public void setAsRegisteredDevice() {
		mSharedPreferences.edit().putBoolean(IS_REGISTERED_DEVICE, true)
				.commit();
	}

	public boolean isRegisteredDevice() {
		return mSharedPreferences.getBoolean(IS_REGISTERED_DEVICE, false);
	}

	/**
	 * Sets the preference for LATEST_TIMESTAMP in {@link SharedPreferences}
	 * 
	 * @param value
	 *            Latest Update time-stamp
	 */
	public void setLatestTimestamp(String value) {
		mSharedPreferences.edit().putString(LATEST_TIMESTAMP, value).commit();
	}

	/**
	 * Gets the LATEST_TIMESTAMP stored in {@link SharedPreferences}
	 * 
	 * @return Last update time-stamp value or empty string
	 */
	public String getLatestTimestamp() {
		return mSharedPreferences.getString(LATEST_TIMESTAMP, "");
	}

	public void setExistUnreadNews(boolean value) {
		mSharedPreferences.edit().putBoolean(IS_EXIST_UNREAD_NEWS, value)
				.commit();
	}

	public boolean isExistUnreadNews() {
		return mSharedPreferences.getBoolean(IS_EXIST_UNREAD_NEWS, false);
	}
}
