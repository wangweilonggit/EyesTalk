package com.markswoman.eyestalk.service;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

/**
 * Custom Background service for downloading assets in the background
 * 
 */
public class DownloadService extends IntentService {

	public static final int UPDATE_PROGRESS = 1200;

	public DownloadService() {
		super("DownloadService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String urlToDownload = intent.getStringExtra("url");
		String actualUrl = urlToDownload.replaceAll(" ", "%20");
		ResultReceiver receiver = (ResultReceiver) intent
				.getParcelableExtra("receiver");
		try {
			URL url = new URL(actualUrl);
			URLConnection connection = url.openConnection();
			connection.connect();
			// this will be useful so that you can show a typical 0-100%
			// progress bar
			int fileLength = connection.getContentLength();
			if (fileLength < 0) {
				fileLength = 4000;
			} else {
				fileLength = fileLength - 1 + 1;
			}

			String file = intent.getStringExtra("file");

			// download the file
			InputStream input = new BufferedInputStream(
					connection.getInputStream());
			OutputStream output = new FileOutputStream(file);

			byte data[] = new byte[1024];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {
				total += count;
				// publishing the progress....
				Bundle resultData = new Bundle();
				resultData.putInt("progress", (int) (total * 100 / fileLength));
				resultData.putString("url", urlToDownload);
				receiver.send(UPDATE_PROGRESS, resultData);
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Bundle resultData = new Bundle();
		resultData.putInt("progress", 100);
		resultData.putString("url", urlToDownload);
		receiver.send(UPDATE_PROGRESS, resultData);

		Intent completed = new Intent(
				"com.markswoman.eyestalk.DownloadCompleted");
		sendBroadcast(completed);
	}

}
