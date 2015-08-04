package com.example.parking_lot;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class Database_SYN extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "SYNData.db";
	private final static int DATABASE_VERSION = 1;
	private final static String NAME = "name";
	private final static String TABLE_NAME = "ZhuCeData_table";
	private DataManager dataManager;

	public Database_SYN(Context context, DataManager dm) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.dataManager = dm;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		for (int i = 0; i < dataManager.getParkingInfo().size(); i++) {

			String sql = "CREATE TABLE "
					+ dataManager.getParkingInfo().get(i).getName() + " ("
					+ "name" + " text  );";
			db.execSQL(sql);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		for (int i = 0; i < dataManager.getParkingInfo().size(); i++) {
			String sql = "DROP TABLE IF EXISTS "
					+ dataManager.getParkingInfo().get(i).getName();
			db.execSQL(sql);
			onCreate(db);
		}

	}

	public Cursor select(String TABLE_NAME) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

	// Ôö¼Ó²Ù×÷
	public long insert(String TABLE_NAME, String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(NAME, name);
		long row = db.insert(TABLE_NAME, null, cv);
		return row;
	}

}
