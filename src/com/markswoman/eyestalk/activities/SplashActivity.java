package com.markswoman.eyestalk.activities;

import java.io.IOException;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.activities.base.BaseActivity;
import com.markswoman.eyestalk.api.request.PostRegisterDeviceRequest;
import com.markswoman.eyestalk.api.response.GeneralPostResponse;
import com.markswoman.eyestalk.storage.LocalStorage;
import com.markswoman.eyestalk.utils.CommonUtils;
import com.markswoman.eyestalk.utils.RequestQueueHolder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

/**
 * Splash activity which shows logo
 * 
 */
public class SplashActivity extends BaseActivity {

	private final int SPLASH_DISPLAY_LENGTH = 1500;
	private GoogleCloudMessaging gcm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		if (!LocalStorage.getInstance().isRegisteredDevice()) {
			if (LocalStorage.getInstance().getSavedGCMRegisterID() == null) {
				registerInBackground();
			} else {
				callPostRegisterDeviceRequest(LocalStorage.getInstance()
						.getSavedGCMRegisterID());
			}
		} else {
			// New Handler to start Main Activity
			// also close Splash Screen after certain time
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					/** Create an Intent that will start Main Activity **/
					Intent intent = new Intent(SplashActivity.this,
							MainActivity.class);
					SplashActivity.this.startActivity(intent);
					SplashActivity.this.finish();
				}
			}, SPLASH_DISPLAY_LENGTH);
		}
	}

	/** Registers the application with GCM servers asynchronously **/
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected void onPreExecute() {
			}

			@Override
			protected String doInBackground(Void... params) {
				String message = null;
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(SplashActivity.this);
					}
					String reg_id = gcm.register(CommonUtils.SENDER_ID);
					message = reg_id;
				} catch (IOException e) {
					message = null;
				}
				return message;
			}

			@Override
			protected void onPostExecute(String result) {
				if (result == null) {
					CommonUtils.getInstance().showMessage(
							R.string.gcm_register_failed);
					startMainApplication();
					return;
				}
				LocalStorage.getInstance().saveGCMRegisterID(result);
				callPostRegisterDeviceRequest(result);
			};
		}.execute(null, null, null);
	}

	private void callPostRegisterDeviceRequest(String deviceId) {
		PostRegisterDeviceRequest request = new PostRegisterDeviceRequest(
				successListener, errorListener, deviceId);
		request.setShouldCache(false);
		RequestQueue queue = RequestQueueHolder.getInstance().getRequestQueue();
		queue.add(request);
	}

	private Listener<GeneralPostResponse> successListener = new Listener<GeneralPostResponse>() {

		@Override
		public void onResponse(GeneralPostResponse response) {
			if (response.getStatusCode() != 0) {
				CommonUtils.getInstance().showMessage(
						R.string.gcm_register_failed);
			} else {
				LocalStorage.getInstance().setAsRegisteredDevice();
			}
			startMainApplication();
		}
	};

	private ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError response) {
			CommonUtils.getInstance().showMessage(R.string.gcm_register_failed);
			startMainApplication();
		}
	};

	private void startMainApplication() {
		/** Create an Intent that will start Main Activity **/
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

}
