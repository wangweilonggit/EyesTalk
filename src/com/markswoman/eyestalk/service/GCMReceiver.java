package com.markswoman.eyestalk.service;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

public class GCMReceiver extends GCMBroadcastReceiver {

	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		return "com.markswoman.eyestalk.service.GCMIntentService";
	}
}
