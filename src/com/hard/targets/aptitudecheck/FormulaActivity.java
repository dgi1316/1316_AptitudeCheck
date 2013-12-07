package com.hard.targets.aptitudecheck;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class FormulaActivity extends Activity {
	
	String SUB_TITLE = null, category = null;
	WebView wvFormula;
	AdView avFormula;
	String start = "<html><body style='text-align:justify'><b>Formulas:</b><br/>";
	String end = "</body></html>";
	DatabaseHelper myDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formula);
		
		Intent t = getIntent();
		SUB_TITLE = t.getStringExtra("sub");
		category = t.getStringExtra("c");
		
		setUpActionBar();
		
		myDbHelper = new DatabaseHelper(this);
		
		try {
			myDbHelper.createDatabase();
		} catch (IOException e) {
			throw new Error("Unable to create database.");
		}
		
		try {
			myDbHelper.openDatabase();
		} catch (SQLException sqle) {
			throw sqle;
		}
		
		intialize();
		
		avFormula.loadAd(new AdRequest());
	}

	private void setUpActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setSubtitle("FORMULAS: " + SUB_TITLE);
	}

	private void intialize() {
		wvFormula = (WebView) findViewById(R.id.wvFormula);
		avFormula = (AdView) findViewById(R.id.adViewFormula);
		wvFormula.loadData(start + "" + myDbHelper.getFormula(category) + "" + end, "text/html", "UTF-8");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.formula, menu);
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
