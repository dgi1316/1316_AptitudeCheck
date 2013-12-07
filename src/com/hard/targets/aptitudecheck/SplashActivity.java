package com.hard.targets.aptitudecheck;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class SplashActivity extends Activity implements Runnable {

	public final String database = "bookmark.db";
	public final static String table = "bookmark";
	public final static String Column_qid = "qid";
	public final static String Column_ques = "question";
	public final static String Column_o1 = "o1";
	public final static String Column_o2 = "o2";
	public final static String Column_o3 = "o3";
	public final static String Column_o4 = "o4";
	public final static String Column_ans = "ans";
	public final static String Column_cat = "cat";
	public final static String Column_title = "title";
	
	static SQLiteDatabase bookmarkdb;
	
	DatabaseHelper myDbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		setupActionBar();
		
		myDbHelper = new DatabaseHelper(this);
		
		myDbHelper.db_delete();
		
		Thread timer = new Thread(this);
		timer.start();
	}

	private void setupActionBar() {
		getActionBar().hide();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1500);
		} catch (Exception e) {
		} finally {
			Intent startingPoint = new Intent("com.hard.targets.aptitudecheck.MAINACTIVITY");
			startActivity(startingPoint);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bookmarkdb = openOrCreateDatabase(database, SQLiteDatabase.CREATE_IF_NECESSARY|SQLiteDatabase.OPEN_READWRITE, null);
		bookmarkdb.execSQL("CREATE TABLE IF NOT EXISTS " + table + " (" +
				Column_qid + " TEXT NOT NULL, " +
				Column_ques + " TEXT NOT NULL, " +
				Column_o1 + " TEXT NOT NULL, " +
				Column_o2 + " TEXT NOT NULL, " +
				Column_o3 + " TEXT NOT NULL, " +
				Column_o4 + " TEXT NOT NULL, " +
				Column_ans + " TEXT NOT NULL, " +
				Column_cat + " TEXT NOT NULL, " +
				Column_title + " TEXT NOT NULL);");
	}

}
