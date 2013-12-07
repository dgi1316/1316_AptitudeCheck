package com.hard.targets.aptitudecheck;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressLint("SdCardPath")
public class DatabaseHelper extends SQLiteOpenHelper {
	
	private SQLiteDatabase myDatabase;
	private final Context myContext;
	private static final String DATABASE_NAME = "ques.db";
	public final static String DATABASE_PATH = "/data/data/com.hard.targets.aptitudecheck/databases/";
	public static final int DATABASE_VERSION = 2;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
	}
	
	public void createDatabase() throws IOException {
		boolean dbExist = checkDatabase();
		
		if(dbExist) {
			Log.v("DB Exists", "db exists");
		}
		
		boolean dbExist1 = checkDatabase();
		
		if(!dbExist1) {
			this.getReadableDatabase();
			try {
				this.close();
				copyDatabase();
			} catch (IOException e) {
				throw new Error("Error copying Database");
			}
		}
	}
	
	private boolean checkDatabase() {
		boolean checkDB = false;
		
		try {
			String myPath = DATABASE_PATH + DATABASE_NAME;
			File dbFile = new File(myPath);
			checkDB = dbFile.exists();
		} catch (SQLiteException e) {
		}
		
		return checkDB;
	}
	
	private void copyDatabase() throws IOException {
		String outFileName = DATABASE_PATH + DATABASE_NAME;
		
		OutputStream myOutput = new FileOutputStream(outFileName);
		InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
		
		byte[] buffer = new byte[1024];
		int length;
		
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		
		myInput.close();
		myOutput.flush();
		myOutput.close();
	}
	
	public void db_delete() {
		File file = new File(DATABASE_PATH + DATABASE_NAME);
		if (file.exists()) {
			file.delete();
			System.out.println("Delete Database File");
		}
	}
	
	public void openDatabase() throws SQLException {
		String myPath = DATABASE_PATH + DATABASE_NAME;
		myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	public synchronized void closeDatabase() throws SQLException {
		if (myDatabase != null)
			myDatabase.close();
		super.close();
	}
	
	public int getQuestionCount(String TABLE_NAME) {
		int num = 0;
		String query = "SELECT * FROM " + TABLE_NAME;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		num = c.getCount();
		return num;
	}
	
	public String getQuestion(String TABLE_NAME, int id) {
		String q = null;
		String query = "SELECT question FROM " + TABLE_NAME + " WHERE _id = " + id;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			q = c.getString(0);
		}
		return q;
	}
	
	public String getOption1(String TABLE_NAME, int id) {
		String o1 = null;
		String query = "SELECT o1 FROM " + TABLE_NAME + " WHERE _id = " + id;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			o1 = c.getString(0);
		}
		return o1;
	}
	
	public String getOption2(String TABLE_NAME, int id) {
		String o2 = null;
		String query = "SELECT o2 FROM " + TABLE_NAME + " WHERE _id = " + id;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			o2 = c.getString(0);
		}
		return o2;
	}
	
	public String getOption3(String TABLE_NAME, int id) {
		String o3 = null;
		String query = "SELECT o3 FROM " + TABLE_NAME + " WHERE _id = " + id;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			o3 = c.getString(0);
		}
		return o3;
	}
	
	public String getOption4(String TABLE_NAME, int id) {
		String o4 = null;
		String query = "SELECT o4 FROM " + TABLE_NAME + " WHERE _id = " + id;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			o4 = c.getString(0);
		}
		return o4;
	}
	
	public String getAnswer(String TABLE_NAME, int id) {
		String ans = null;
		String query = "SELECT ans FROM " + TABLE_NAME + " WHERE _id = " + id;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			ans = c.getString(0);
		}
		return ans;
	}
	
	public String getSolution(String TABLE_NAME, int id) {
		String sol = null;
		String query = "SELECT sol FROM " + TABLE_NAME + " WHERE _id = " + id;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			sol = c.getString(0);
		}
		return sol;
	}
	
	public String getFormula(String cat) {
		String form = null;
		String query = "SELECT formula FROM formulas WHERE category = ?";
		Cursor c = myDatabase.rawQuery(query, new String[] { cat });
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			form = c.getString(0);
		}
		return form;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			Log.v("Database Uppgrade", "Database version higher than old.");
			db_delete();
		}
	}

}