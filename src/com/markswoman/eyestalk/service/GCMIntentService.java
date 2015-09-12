package com.markswoman.eyestalk.service;

import com.google.android.gcm.GCMBaseIntentService;
import com.markswoman.eyestalk.R;
import com.markswoman.eyestalk.activities.SplashActivity;
import com.markswoman.eyestalk.storage.LocalStorage;
import com.markswoman.eyestalk.utils.CommonUtils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

public class GCMIntentService extends GCMBaseIntentService {
	Looper mServiceLooper;
	Context maincontext;
	String mainmessage = "";

	public GCMIntentService() {
		super(CommonUtils.SENDER_ID);
		HandlerThread thread = new HandlerThread(TAG + "HandlerThread",
				Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		mServiceLooper = thread.getLooper();
	}

	@Override
	protected void onError(Context arg0, String arg1) {
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		maincontext = context;
		mainmessage = intent.getExtras().getString("message");
		Handler h = new Handler(mServiceLooper);
		h.post(new Runnable() {
			@Override
			public void run() {
				generateNotification(maincontext, mainmessage);
			}
		});

	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	@SuppressLint("NewApi")
	private static void generateNotification(Context context, String message) {

		LocalStorage.getInstance().setExistUnreadNews(true);

		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context, SplashActivity.class);
		notificationIntent.putExtra("question_key", message);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification.Builder(context)
				.setContentTitle(title).setContentText(message)
				.setSmallIcon(icon).setContentIntent(intent).setWhen(when)
				.setAutoCancel(true).build();

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		int time = (int) (System.currentTimeMillis());
		notificationManager.notify(time, notification);
	}
}
