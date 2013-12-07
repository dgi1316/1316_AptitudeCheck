package com.hard.targets.aptitudecheck;

import java.util.LinkedList;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.ads.InterstitialAd;
import com.google.ads.AdRequest.ErrorCode;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class MenuActivity extends Activity implements AdListener, OnItemClickListener {

	ListView lv;
	AdView adViewMain;
	String[] category0 = { "Ages", "Alligation or Mixture", "Area", "Average", "Banker's Discount", "Boats and Streams", "Calendar", "Chain Rule", "Clock", "Compund Interest", "Decimal Fraction", "HCF and LCM", "Logarithm", "Numbers", "Odd Man Out and Series", "Partnership", "Percentage", "Permutation and Combination", "Pipes and Cisterns", "Probability", "Problems on Numbers", "Profit and Loss", "Races and Games", "Ratio and Proportion", "Simple Interest", "Simplification", "Square Root and Cube Root", "Stocks and Shares", "Surds and Indices", "Time and Distance", "Time and Work", "Trains", "True Discount", "Volume and Surface Area" };
	String[] category1 = { "Analogies", "Analyzing Arguments", "Artificial Language", "Cause and Effect", "Course of Action", "Essential Part", "Letter and Symbol Series", "Logical Deduction", "Logical Games", "Logical Problems", "Making Judgments", "Matching Definitions", "Number Series", "Statement and Argument", "Statement and Assumption", "Statement and Conclusion", "Theme Detection", "Verbal Classification", "Verbal Reasoning" };
	String[] category2 = { "Antonyms", "Change of Speech", "Change of Voice", "Closet Test", "Completing Statements", "Comprehension", "Idioms and Phrases", "One Word Substitutes", "Ordering of Sentences", "Ordering of Words", "Paragraph Formation", "Selecting Words", "Sentence Correction", "Sentence Formation", "Sentence Improvement", "Spellings", "Spotting Errors", "Synonyms", "Verbal Analogies" };
	String[] category3 = { "Basic General Knowledge", "Biology", "Books and Authors", "Chemistry", "Days and Years", "Famous Personalities", "Famous Places in India", "General Science", "Honours and Awards", "Indian Culture", "Indian Economy", "Indian Geography", "Indian History", "Indian Politics", "Inventions", "Physics", "Sports", "Technology", "World Geography", "World Organisations" };
	String SUB_TITLE = null;
	int type = 0;
	private InterstitialAd interstitial;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		interstitial = new InterstitialAd(this, "a1526261af57771");
		AdRequest adRequest = new AdRequest();
		interstitial.loadAd(adRequest);
		interstitial.setAdListener(this);
		
		Intent t = getIntent();
		type = t.getIntExtra("type", 0);
		
		switch (type) {
		case 0:
			SUB_TITLE = "General Aptitude";
			break;
		case 1:
			SUB_TITLE = "Logical Reasoning";
			break;
		case 2:
			SUB_TITLE = "Verbal Activity";
			break;
		case 3:
			SUB_TITLE = "General Knowledge";
			break;
		default:
			break;
		}

		setupActionBar();
		
		initialize();
		
		adViewMain = (AdView) findViewById(R.id.adViewMenu);
		adViewMain.loadAd(new AdRequest());
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setSubtitle(SUB_TITLE);

	}
	
	private void initialize() {
		lv = (ListView) findViewById(R.id.listView1);
		lv.setOnItemClickListener(this);
		
		setList();
	}

	private void setList() {
		LinkedList<String> item = new LinkedList<String>();
		ArrayAdapter<String> aa;
		switch (type) {
		case 0:
			for(String i:category0) {
				item.add(i);
			}
			break;
		case 1:
			for(String i:category1) {
				item.add(i);
			}
			break;
		case 2:
			for(String i:category2) {
				item.add(i);
			}
			break;
		case 3:
			for(String i:category3) {
				item.add(i);
			}
			break;
		default:
			break;
		}
		
		aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item);
		lv.setAdapter(aa);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_bookmark:
			int n = 0;
			try {
				Cursor c = SplashActivity.bookmarkdb.rawQuery("SELECT * FROM bookmark", null);
				c.moveToFirst();
				n = c.getCount();
				if(n == 0) {
					Toast.makeText(this, "No Bookmarks", Toast.LENGTH_SHORT).show();
				} else {
					Intent bookmark = new Intent("com.hard.targets.aptitudecheck.BOOKMARKACTIVITY");
					bookmark.putExtra("type", "" + type);
					startActivity(bookmark);
				}
			} catch (Exception e) {
				Toast.makeText(this, "EXCEPTION: " + e, Toast.LENGTH_SHORT).show();
			}
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent test = new Intent("com.hard.targets.aptitudecheck.TESTACTIVITY");
		test.putExtra("s", "" + lv.getItemIdAtPosition(arg2));
		test.putExtra("sub", "" + lv.getItemAtPosition(arg2));
		test.putExtra("type", "" + type);
		startActivity(test);
	}
	
	@Override
	protected void onDestroy() {
		if (adViewMain != null) {
			adViewMain.destroy();
		}
		interstitial.stopLoading();
		super.onDestroy();
	}

	@Override
	public void onDismissScreen(Ad arg0) {
	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
	}

	@Override
	public void onLeaveApplication(Ad arg0) {
	}

	@Override
	public void onPresentScreen(Ad arg0) {
	}

	@Override
	public void onReceiveAd(Ad ad) {
		if (ad == interstitial) {
			interstitial.show();
		}
	}

}
