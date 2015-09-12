package com.markswoman.eyestalk.database;

import java.util.ArrayList;
import java.util.Locale;

import com.markswoman.eyestalk.model.EyesTalkProductStatus;
import com.markswoman.eyestalk.model.LensBrand;
import com.markswoman.eyestalk.model.ProductSubBrand;
import com.markswoman.eyestalk.utils.CommonUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

/**
 * Custom helper class for SQLite Database
 * 
 */
public class EyesTalkDatabase extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "eyes_talk_db";
	private final static int DATABASE_VERSION = 1;

	private final String TABLE_PRODUCTS = "products_table";
	private final String TABLE_LENS = "lens_table";

	private final String ID = "id";
	private final String NAME = "name";
	private final String DELETED = "deleted";
	private final String CREATED_ON = "created_on";
	private final String MODIFIED_ON = "modified_on";
	private final String DESCRIPTION = "description";

	private final String CATEGORY = "category";
	private final String LOGO_PATH = "logo_path";
	private final String IMAGE_PATH = "image_path";
	private final String IMAGE_PATH_2 = "image_2_path";
	private final String IMAGE_PATH_3 = "image_3_path";
	private final String IMAGE_PATH_4 = "image_4_path";
	private final String LOGO_URI = "logo_uri";
	private final String IMAGE_URI = "image_uri";
	private final String IMAGE_URI_2 = "image_uri_2";
	private final String IMAGE_URI_3 = "image_uri_3";
	private final String IMAGE_URI_4 = "image_uri_4";

	private final String PRODUCT = "product";
	private final String LENS_PATH = "lens_path";
	private final String ASIAN_PATH = "eyes_asian_path";
	private final String WESTERN_PATH = "eyes_western_path";
	private final String MELAYU_PATH = "eyes_melayu_path";
	private final String LENS_URI = "lens_uri";
	private final String ASIAN_URI = "asian_uri";
	private final String WESTERN_URI = "western_uri";
	private final String MELAYU_URI = "melayu_uri";

	public EyesTalkDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
				+ ID + " INTEGER," + NAME + " TEXT," + LOGO_PATH + " TEXT,"
				+ IMAGE_PATH + " TEXT," + IMAGE_PATH_2 + " TEXT,"
				+ IMAGE_PATH_3 + " TEXT," + IMAGE_PATH_4 + " TEXT,"
				+ DESCRIPTION + " TEXT," + CATEGORY + " TEXT," + DELETED
				+ " INTEGER," + CREATED_ON + " TEXT," + MODIFIED_ON + " TEXT,"
				+ LOGO_URI + " TEXT," + IMAGE_URI + " TEXT," + IMAGE_URI_2
				+ " TEXT," + IMAGE_URI_3 + " TEXT," + IMAGE_URI_4 + " TEXT"
				+ ")";
		db.execSQL(CREATE_PRODUCTS_TABLE);

		String CREATE_LENS_TABLE = "CREATE TABLE " + TABLE_LENS + "(" + ID
				+ " INTEGER," + NAME + " TEXT," + LENS_PATH + " TEXT,"
				+ ASIAN_PATH + " TEXT," + WESTERN_PATH + " TEXT," + MELAYU_PATH
				+ " TEXT," + DESCRIPTION + " TEXT," + PRODUCT + " INTEGER,"
				+ DELETED + " INTEGER," + CREATED_ON + " TEXT," + MODIFIED_ON
				+ " TEXT," + LENS_URI + " TEXT," + ASIAN_URI + " TEXT,"
				+ WESTERN_URI + " TEXT," + MELAYU_URI + " TEXT" + ")";
		db.execSQL(CREATE_LENS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LENS);

		// Create tables again
		onCreate(db);
	}

	public void clearTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_PRODUCTS);
		db.execSQL("DELETE FROM " + TABLE_LENS);
		db.close();
	}

	public void insertOrUpdateProduct(ProductSubBrand product) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = valuesFromProduct(product);
		deleteOldProductFromDatabase(product);
		if (!product.status.equalsIgnoreCase(EyesTalkProductStatus.DELETE)) {
			db.insert(TABLE_PRODUCTS, null, values);
		}
		db.close();
	}

	private void deleteOldProductFromDatabase(ProductSubBrand product) {
		SQLiteDatabase db = this.getWritableDatabase();
		String query = String.format(Locale.US, "SELECT * FROM "
				+ TABLE_PRODUCTS + " WHERE %s=%d", ID, product.brandId);
		Cursor cursor = db.rawQuery(query, null);
		ProductSubBrand oldProduct = null;
		if (cursor.moveToFirst()) {
			oldProduct = productFromCursor(cursor);
		}
		cursor.close();
		if (oldProduct != null) {
			db.delete(TABLE_PRODUCTS,
					String.format("%s=%d", ID, oldProduct.brandId), null);
			CommonUtils.getInstance().deleteFileFromUri(oldProduct.imageUri);
			CommonUtils.getInstance().deleteFileFromUri(oldProduct.imageUri2);
			CommonUtils.getInstance().deleteFileFromUri(oldProduct.imageUri3);
			CommonUtils.getInstance().deleteFileFromUri(oldProduct.imageUri4);
		}
	}

	public void insertOrUpdateLens(LensBrand lens) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = valuesFromLens(lens);
		deleteOldLensFromDatabase(lens);
		if (!lens.status.equalsIgnoreCase(EyesTalkProductStatus.DELETE)) {
			db.insert(TABLE_LENS, null, values);
		}
		db.close();
	}

	private void deleteOldLensFromDatabase(LensBrand lens) {
		SQLiteDatabase db = this.getWritableDatabase();
		String query = String.format(Locale.US, "SELECT * FROM " + TABLE_LENS
				+ " WHERE %s=%d", ID, lens.lensId);
		Cursor cursor = db.rawQuery(query, null);
		LensBrand oldLens = null;
		if (cursor.moveToFirst()) {
			oldLens = lensFromCursor(cursor);
		}
		cursor.close();
		if (oldLens != null) {
			db.delete(TABLE_LENS, String.format("%s=%d", ID, oldLens.lensId),
					null);
			CommonUtils.getInstance().deleteFileFromUri(oldLens.lensUri);
			CommonUtils.getInstance().deleteFileFromUri(oldLens.eyesAsianUri);
			CommonUtils.getInstance().deleteFileFromUri(oldLens.eyesMelayuUri);
			CommonUtils.getInstance().deleteFileFromUri(oldLens.eyesWesternUri);
		}
	}

	public ArrayList<ProductSubBrand> getAllProducts() {
		ArrayList<ProductSubBrand> products = new ArrayList<ProductSubBrand>();
		String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				products.add(productFromCursor(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		return products;
	}

	public ArrayList<LensBrand> getAllLens() {
		ArrayList<LensBrand> lensArray = new ArrayList<LensBrand>();
		String query = "SELECT * FROM " + TABLE_LENS + " WHERE 1";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				lensArray.add(lensFromCursor(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		return lensArray;
	}

	private ContentValues valuesFromProduct(ProductSubBrand product) {
		ContentValues values = new ContentValues();
		values.put(ID, product.brandId);
		values.put(NAME, product.name);
		values.put(LOGO_PATH, product.logoPath);
		values.put(IMAGE_PATH, product.imagePath);
		values.put(IMAGE_PATH_2, product.imagePath2);
		values.put(IMAGE_PATH_3, product.imagePath3);
		values.put(IMAGE_PATH_4, product.imagePath4);
		values.put(DESCRIPTION, product.description);
		values.put(CATEGORY, product.category);
		values.put(DELETED, product.deleted);
		values.put(CREATED_ON, product.createdOn);
		values.put(MODIFIED_ON, product.modifiedOn);
		values.put(LOGO_URI, product.logoUri.toString());
		values.put(IMAGE_URI,
				product.imageUri == null ? "" : product.imageUri.toString());
		values.put(IMAGE_URI_2, product.imageUri == null ? ""
				: product.imageUri.toString());
		values.put(IMAGE_URI_3, product.imageUri2 == null ? ""
				: product.imageUri.toString());
		values.put(IMAGE_URI_4, product.imageUri3 == null ? ""
				: product.imageUri4.toString());
		return values;
	}

	private ProductSubBrand productFromCursor(Cursor cursor) {
		ProductSubBrand product = new ProductSubBrand();
		product.brandId = cursor.getInt(0);
		product.name = cursor.getString(1);
		product.logoPath = cursor.getString(2);
		product.imagePath = cursor.getString(3);
		product.imagePath2 = cursor.getString(4);
		product.imagePath3 = cursor.getString(5);
		product.imagePath4 = cursor.getString(6);
		product.description = cursor.getString(7);
		product.category = cursor.getString(8);
		product.deleted = cursor.getInt(9);
		product.createdOn = cursor.getString(10);
		product.modifiedOn = cursor.getString(11);
		product.logoUri = Uri.parse(cursor.getString(12));
		product.imageUri = Uri.parse(cursor.getString(13));
		product.imageUri2 = Uri.parse(cursor.getString(14));
		product.imageUri3 = Uri.parse(cursor.getString(15));
		product.imageUri4 = Uri.parse(cursor.getString(16));
		return product;
	}

	private ContentValues valuesFromLens(LensBrand lens) {
		ContentValues values = new ContentValues();
		values.put(ID, lens.lensId);
		values.put(NAME, lens.name);
		values.put(LENS_PATH, lens.lensPath);
		values.put(ASIAN_PATH, lens.eyesAsianPath);
		values.put(WESTERN_PATH, lens.eyesWesternPath);
		values.put(MELAYU_PATH, lens.eyesMelayuPath);
		values.put(DESCRIPTION, lens.description);
		values.put(PRODUCT, lens.productId);
		values.put(DELETED, lens.deleted);
		values.put(CREATED_ON, lens.createdOn);
		values.put(MODIFIED_ON, lens.modifiedOn);
		values.put(LENS_URI, lens.lensUri.toString());
		values.put(ASIAN_URI, lens.eyesAsianUri.toString());
		values.put(WESTERN_URI, lens.eyesWesternUri.toString());
		values.put(MELAYU_URI, lens.eyesMelayuUri.toString());
		return values;
	}

	private LensBrand lensFromCursor(Cursor cursor) {
		LensBrand lens = new LensBrand();
		lens.lensId = cursor.getInt(0);
		lens.name = cursor.getString(1);
		lens.lensPath = cursor.getString(2);
		lens.eyesAsianPath = cursor.getString(3);
		lens.eyesWesternPath = cursor.getString(4);
		lens.eyesMelayuPath = cursor.getString(5);
		lens.description = cursor.getString(6);
		lens.productId = cursor.getInt(7);
		lens.deleted = cursor.getInt(8);
		lens.createdOn = cursor.getString(9);
		lens.modifiedOn = cursor.getString(10);
		lens.lensUri = Uri.parse(cursor.getString(11));
		lens.eyesAsianUri = Uri.parse(cursor.getString(12));
		lens.eyesWesternUri = Uri.parse(cursor.getString(13));
		lens.eyesMelayuUri = Uri.parse(cursor.getString(14));
		return lens;
	}

	// Temp code for sqlite manager
	public ArrayList<Cursor> getData(String Query) {
		// get writable database
		SQLiteDatabase sqlDB = this.getWritableDatabase();
		String[] columns = new String[] { "mesage" };
		// an array list of cursor to save two cursors one has results from the
		// query
		// other cursor stores error message if any errors are triggered
		ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
		MatrixCursor Cursor2 = new MatrixCursor(columns);
		alc.add(null);
		alc.add(null);

		try {
			String maxQuery = Query;
			// execute the query results will be save in Cursor c
			Cursor c = sqlDB.rawQuery(maxQuery, null);

			// add value to cursor2
			Cursor2.addRow(new Object[] { "Success" });

			alc.set(1, Cursor2);
			if (null != c && c.getCount() > 0) {

				alc.set(0, c);
				c.moveToFirst();

				return alc;
			}
			return alc;
		} catch (SQLException sqlEx) {
			Log.d("printing exception", sqlEx.getMessage());
			// if any exceptions are triggered save the error message to cursor
			// an return the arraylist
			Cursor2.addRow(new Object[] { "" + sqlEx.getMessage() });
			alc.set(1, Cursor2);
			return alc;
		} catch (Exception ex) {

			Log.d("printing exception", ex.getMessage());

			// if any exceptions are triggered save the error message to cursor
			// an return the arraylist
			Cursor2.addRow(new Object[] { "" + ex.getMessage() });
			alc.set(1, Cursor2);
			return alc;
		}

	}

}
