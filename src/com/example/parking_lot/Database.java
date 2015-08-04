package com.example.parking_lot;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "ZhuCeData.db";
	private final static int DATABASE_VERSION = 1;
	private final static String TABLE_NAME = "ZhuCeData_table";
	public final static String NAME = "name";
	public final static String PASSWORD = "password";
	public final static String VIP = "vip";
	

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + NAME + " text, "
				+ PASSWORD + " text, " + VIP + " text  );";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

	// 增加操作
	public long insert(String name, String password, String vip) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(NAME, name);
		cv.put(PASSWORD, password);
		cv.put(VIP, vip);
		long row = db.insert(TABLE_NAME, null, cv);
		return row;
	}

	// 删除操作
	public void delete(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "delete from ZhuCeData_table where NAME='" +  name + "'";// 删除操作的SQL语句
		db.execSQL(sql);
	}

	// 修改操作
	public void update(String name, String huafei) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = NAME + " = ?";
		String[] whereValue = { name };
		ContentValues cv = new ContentValues();
		cv.put("NAME", name);
		cv.put("HUAFEI", huafei);
		db.update(TABLE_NAME, cv, where, whereValue);
	}

}
