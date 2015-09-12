package com.markswoman.eyestalk.service;

import java.util.ArrayList;

import com.markswoman.eyestalk.database.EyesTalkDatabase;
import com.markswoman.eyestalk.model.LensBrand;
import com.markswoman.eyestalk.model.ProductSubBrand;
import com.markswoman.eyestalk.storage.LocalStorage;
import com.markswoman.eyestalk.utils.CommonUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

public class AssetsControllerService extends Service {

	public static ArrayList<ProductSubBrand> allProducts;
	public static ArrayList<LensBrand> allLens;
	public static int downloadIndex;
	public static ArrayList<String> urlArray;

	private ResultReceiver resultReceiver;
	private EyesTalkDatabase database;
	private String currentFile;

	private String timestamp;

	@Override
	public void onCreate() {
		super.onCreate();

		IntentFilter filter = new IntentFilter(
				"com.markswoman.eyestalk.DownloadCompleted");
		registerReceiver(downloadCompleteReceiver, filter);

		urlArray = new ArrayList<String>();
		downloadIndex = 0;

		database = new EyesTalkDatabase(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(downloadCompleteReceiver);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			resultReceiver = (ResultReceiver) intent
					.getParcelableExtra("receiver");
			timestamp = intent.getStringExtra("timestamp");
			for (ProductSubBrand product : allProducts) {
				urlArray.add(product.logoPath);
				urlArray.add(product.imagePath);
				urlArray.add(product.imagePath2);
				urlArray.add(product.imagePath3);
				urlArray.add(product.imagePath4);
			}
			for (LensBrand lens : allLens) {
				urlArray.add(lens.lensPath);
				urlArray.add(lens.eyesAsianPath);
				urlArray.add(lens.eyesWesternPath);
				urlArray.add(lens.eyesMelayuPath);
			}
			downloadAsset();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void downloadAsset() {
		Long tsLong = System.currentTimeMillis();
		String fileName = "eyesTalk_" + tsLong.toString() + ".png";
		currentFile = CommonUtils.getInstance().getStorageUri(
				CommonUtils.BACKUP_FOLDER, fileName);
		Intent downloadIntent = new Intent(this, DownloadService.class);
		downloadIntent.putExtra("receiver", resultReceiver);
		downloadIntent.putExtra("url", urlArray.get(downloadIndex));
		downloadIntent.putExtra("file", currentFile.toString());
		startService(downloadIntent);
	}

	private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			changeUri();
			downloadIndex++;
			if (downloadIndex == urlArray.size()) {
				Bundle resultData = new Bundle();
				resultData.putInt("progress", -1);
				resultData.putString("url", "");
				resultReceiver
						.send(DownloadService.UPDATE_PROGRESS, resultData);
				for (ProductSubBrand product : allProducts) {
					database.insertOrUpdateProduct(product);
				}
				for (LensBrand lens : allLens) {
					database.insertOrUpdateLens(lens);
				}
				LocalStorage.getInstance().setLatestTimestamp(timestamp);
			} else {
				downloadAsset();
			}
		}
	};

	private void changeUri() {
		Uri file = Uri.parse(currentFile);
		if (downloadIndex < allProducts.size() * 5) {
			ProductSubBrand product = allProducts.get(downloadIndex / 5);
			if (downloadIndex % 5 == 0) {
				product.logoUri = file;
			} else if (downloadIndex % 5 == 1) {
				product.imageUri = file;
			} else if (downloadIndex % 5 == 2) {
				product.imageUri2 = file;
			} else if (downloadIndex % 5 == 3) {
				product.imageUri3 = file;
			} else if (downloadIndex % 5 == 4) {
				product.imageUri4 = file;
			}
			allProducts.set(downloadIndex / 5, product);
		} else {
			int lensIndex = (downloadIndex - allProducts.size() * 5);
			LensBrand lens = allLens.get(lensIndex / 4);
			if (lensIndex % 4 == 0) {
				lens.lensUri = file;
			} else if (lensIndex % 4 == 1) {
				lens.eyesAsianUri = file;
			} else if (lensIndex % 4 == 2) {
				lens.eyesWesternUri = file;
			} else if (lensIndex % 4 == 3) {
				lens.eyesMelayuUri = file;
			}
			allLens.set(lensIndex / 4, lens);
		}
	}

}
