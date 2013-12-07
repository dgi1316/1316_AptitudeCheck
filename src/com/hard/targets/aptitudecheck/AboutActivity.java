package com.hard.targets.aptitudecheck;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.support.v4.app.NavUtils;

public class AboutActivity extends Activity {
	
	WebView wv;
	AdView av;
	String url = "file:///android_asset/about.html";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		setupActionBar();
		
		wv = (WebView) findViewById(R.id.wvAbout);
		wv.loadUrl(url);
		
		av = (AdView) findViewById(R.id.adViewAbout);
		av.loadAd(new AdRequest());
	}

	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setSubtitle("ABOUT");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_rate:
			
			return true;
		case R.id.action_share:
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
