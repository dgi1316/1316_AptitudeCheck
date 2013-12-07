package com.hard.targets.aptitudecheck;

import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class MainActivity extends Activity implements OnItemClickListener {
	
	ListView lv;
	AdView adViewMain;
	String[] items = { "Ages", "Alligation or Mixture", "Area", "Average", "Banker's Discount", "Boats and Streams", "Calendar", "Chain Rule", "Clock", "Compund Interest", "Decimal Fraction", "Partnership", "Permutation and Combination", "Profit and Loss", "Simple Interest", "Time and Distance", "Time and Work", "Trains" };
	LinkedList<String> item = new LinkedList<String>();
	ArrayAdapter<String> aa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		lv = (ListView) findViewById(R.id.lvMenu);
		for(String i:items)
			item.add(i);
		aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item);
		lv.setAdapter(aa);
		lv.setOnItemClickListener(this);
		
		adViewMain = (AdView) findViewById(R.id.adViewMain);
		adViewMain.loadAd(new AdRequest());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
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
		case R.id.action_about:
			Intent about = new Intent("com.hard.targets.aptitudecheck.ABOUTACTIVITY");
			startActivity(about);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
			.setIcon(R.drawable.ic_dialog_alert)
			.setMessage(R.string.dialog_message)
			.setTitle(R.string.dialog_title)
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User clicked OK button
					MainActivity.this.finish();
				}
			})
			.setNegativeButton(R.string.no, null)
			.show();
			
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent test = new Intent("com.hard.targets.aptitudecheck.TESTACTIVITY");
		test.putExtra("s", "" + lv.getItemIdAtPosition(arg2));
		test.putExtra("sub", "" + lv.getItemAtPosition(arg2));
		startActivity(test);
	}

}
