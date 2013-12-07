package com.hard.targets.aptitudecheck;

import java.io.IOException;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class SolutionActivity extends Activity {

	String SUB_TITLE = null, TABLE_NAME = null;
	int index = 0;
	DatabaseHelper myDbHelper;
	WebView wvQues, wvSol;
	AdView avSolution;
	String start1 = "<html><body style='text-align:justify'><b>";
	String start2 = "<html><body style='text-align:justify'><b>Solution:</b><br><br>";
	String end1 = "</b><hr></body></html>";
	String end2 = "</body></html>";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_solution);
		
		Intent t = getIntent();
		SUB_TITLE = t.getStringExtra("sub");
		TABLE_NAME = t.getStringExtra("tb");
		index = t.getIntExtra("id", 0);
		
		setupActionBar();
		
		myDbHelper = new DatabaseHelper(this);
		
		try {
			myDbHelper.createDatabase();
		} catch(IOException e) {
			throw new Error("Unable to create database.");
		}
		
		try {
			myDbHelper.openDatabase();
		} catch (SQLException sqle) {
			throw sqle;
		}
		
		intialize();
		
		avSolution = (AdView) findViewById(R.id.adViewSolution);
		avSolution.loadAd(new AdRequest());

	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setSubtitle("SOLUTION: " + SUB_TITLE);
	}
	
	private void intialize() {
		wvQues = (WebView) findViewById(R.id.wvQues);
		wvSol = (WebView) findViewById(R.id.wvSol);
		
		wvQues.loadData(start1 + "" + myDbHelper.getQuestion(TABLE_NAME, index) + "" + end1, "text/html", "UTF-8");
		wvSol.loadData(start2 + "" + myDbHelper.getSolution(TABLE_NAME, index) + "" + end2, "text/html", "UTF-8");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.solution, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			myDbHelper.closeDatabase();
			this.finish();
			return true;
		case R.id.action_rate:
			
			return true;
		case R.id.action_share:
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			myDbHelper.closeDatabase();
		}
		return super.onKeyDown(keyCode, event);
	}

}
