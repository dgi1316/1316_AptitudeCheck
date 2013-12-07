package com.hard.targets.aptitudecheck;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class BookmarkActivity extends Activity implements OnCheckedChangeListener {
	
	Cursor c;
	MenuItem back, forward;
	String SUB_TITLE = null, TABLE_NAME = null, qno = null, ans = null, where = null;
	String o1, o2, o3, o4;
	int i = 1;
	WebView wvQues;
	TextView tvQno, tvResult;
	AdView adViewBookmark;
	RadioGroup options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark);
		
		initialize();
		
		c = SplashActivity.bookmarkdb.rawQuery("select * from bookmark", null);
		c.moveToFirst();
		
		setQuestion();
		
		adViewBookmark = (AdView) findViewById(R.id.adViewBookmark);
		adViewBookmark.loadAd(new AdRequest());
		
	}

	private void initialize() {
		tvQno = (TextView) findViewById(R.id.tvQno);
		wvQues = (WebView) findViewById(R.id.wvQuestion);
		tvResult = (TextView) findViewById(R.id.tvResult);
		options = (RadioGroup) findViewById(R.id.radioGroup1);
		
		options.setOnCheckedChangeListener(this);
	}

	private void setQuestion() {
		qno = c.getString(0);
		o1 = c.getString(2);
		o2 = c.getString(3);
		o3 = c.getString(4);
		o4 = c.getString(5);
		ans = c.getString(6);
		TABLE_NAME = c.getString(7);
		SUB_TITLE = c.getString(8);
		setupActionBar();
		
		tvQno.setText(i + " / " + c.getCount());
		wvQues.loadData(c.getString(1), "text/html", "UTF-8");
		
		((RadioButton) options.getChildAt(0)).setText(o1);
		((RadioButton) options.getChildAt(1)).setText(o2);
		((RadioButton) options.getChildAt(2)).setText(o3);
		((RadioButton) options.getChildAt(3)).setText(o4);
		
		enableButton();
	}
	
	private void enableButton() {
		options.getChildAt(0).setEnabled(true);
		options.getChildAt(1).setEnabled(true);
		options.getChildAt(2).setEnabled(true);
		options.getChildAt(3).setEnabled(true);
		
	}
	
	private void disableButton() {
		options.getChildAt(0).setEnabled(false);
		options.getChildAt(1).setEnabled(false);
		options.getChildAt(2).setEnabled(false);
		options.getChildAt(3).setEnabled(false);
	}

	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setSubtitle("BOOKMARKS: " + SUB_TITLE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bookmark, menu);
		back = menu.findItem(R.id.action_back);
		forward = menu.findItem(R.id.action_forward);
		back.setEnabled(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			c.close();
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_back:
			adViewBookmark.loadAd(new AdRequest());
			forward.setEnabled(true);
			if(i > 1) {
				i--;
				c.moveToPrevious();
				options.clearCheck();
				tvResult.setVisibility(View.INVISIBLE);
				setQuestion();
				if(i == 1) {
					back.setEnabled(false);
				} else {
					back.setEnabled(true);
				}
			}
			return true;
		case R.id.action_forward:
			adViewBookmark.loadAd(new AdRequest());
			back.setEnabled(true);
			if(i < c.getCount()) {
				i++;
				c.moveToNext();
				options.clearCheck();
				tvResult.setVisibility(View.INVISIBLE);
				setQuestion();
				if(i == c.getCount()) {
					forward.setEnabled(false);
				} else {
					forward.setEnabled(true);
				}
			}
			return true;
		case R.id.action_slate:
			Intent slate = new Intent("com.hard.targets.aptitudecheck.SLATEACTIVITY");
			slate.putExtra("sub", SUB_TITLE);
			slate.putExtra("tb", TABLE_NAME);
			slate.putExtra("id", Integer.parseInt(qno));
			startActivity(slate);
			return true;
		case R.id.action_remove_bookmark:
			long l = 0;
			where = "cat = ? AND qid = ?";
			String[] whereArgs = {TABLE_NAME, qno};
			l = SplashActivity.bookmarkdb.delete(SplashActivity.table, where, whereArgs);
			if (l == 0) {
				Toast.makeText(this, "ERROR: Could not Remove Bookmark.", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Bookmark Removed Successfully.", Toast.LENGTH_SHORT).show();
			}
			return true;
		case R.id.action_formula:
			Intent formula = new Intent("com.hard.targets.aptitudecheck.FORMULAACTIVITY");
			formula.putExtra("sub", SUB_TITLE);
			formula.putExtra("c", TABLE_NAME);
			startActivity(formula);
			return true;
		case R.id.action_explain:
			Intent solution = new Intent("com.hard.targets.aptitudecheck.SOLUTIONACTIVITY");
			solution.putExtra("sub", SUB_TITLE);
			solution.putExtra("tb", TABLE_NAME);
			solution.putExtra("id", Integer.parseInt(qno));
			startActivity(solution);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		String answer = null;
		tvResult.setTextColor(Color.parseColor("#2B6F39"));
		if(ans.equals("A")) {
			answer = o1;
		} else if (ans.equals("B")) {
			answer = o2;
		} else if (ans.equals("C")) {
			answer = o3;
		} else if (ans.equals("D")) {
			answer = o4;
		}
		switch (arg1) {
		case R.id.a:
			disableButton();
			tvResult.setVisibility(View.VISIBLE);
			if(ans.equalsIgnoreCase("A")) {
				tvResult.setText("Correct Answer!");
			} else {
				tvResult.setTextColor(Color.RED);
				tvResult.setText("Wrong Answer!\nThe Correct answer is " + answer);
			}
			break;
		case R.id.b:
			disableButton();
			tvResult.setVisibility(View.VISIBLE);
			if(ans.equalsIgnoreCase("B")) {
				tvResult.setText("Correct Answer!");
			} else {
				tvResult.setTextColor(Color.RED);
				tvResult.setText("Wrong Answer!\nThe Correct answer is " + answer);
			}
			break;
		case R.id.c:
			disableButton();
			tvResult.setVisibility(View.VISIBLE);
			if(ans.equalsIgnoreCase("C")) {
				tvResult.setText("Correct Answer!");
			} else {
				tvResult.setTextColor(Color.RED);
				tvResult.setText("Wrong Answer!\nThe Correct answer is " + answer);
			}
			break;
		case R.id.d:
			disableButton();
			tvResult.setVisibility(View.VISIBLE);
			if(ans.equalsIgnoreCase("D")) {
				tvResult.setText("Correct Answer!");
			} else {
				tvResult.setTextColor(Color.RED);
				tvResult.setText("Wrong Answer!\nThe Correct answer is " + answer);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			c.close();
		}
		return super.onKeyDown(keyCode, event);
	}

}
