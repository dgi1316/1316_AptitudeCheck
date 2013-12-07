package com.hard.targets.aptitudecheck;

import java.io.IOException;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
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

public class TestActivity extends Activity implements OnCheckedChangeListener {
	
	String SUB_TITLE = null, TABLE_NAME = null, ans = null, select = null;
	String o1, o2, o3, o4;
	int i = 1, qno = 0;
	AdView adViewTest;
	TextView tvQNo, tvResult;
	WebView wvQuestion;
	RadioGroup options;
	DatabaseHelper myDbHelper;
	MenuItem back, forward;
	String start = "<html><body style='text-align:justify'><b>";
	String end = "</b><hr></body></html>";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		Intent t = getIntent();
		SUB_TITLE = t.getStringExtra("sub");
		select = t.getStringExtra("s");
		TABLE_NAME = "cat" + "" + select;
		
		setupActionBar();
		
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
		
		qno = myDbHelper.getQuestionCount(TABLE_NAME);
		
		initialize();
		
		setQuestion();
		
		adViewTest = (AdView) findViewById(R.id.adViewTest);
		adViewTest.loadAd(new AdRequest());
	}

	private void initialize() {
		tvQNo = (TextView) findViewById(R.id.tvQno);
		wvQuestion = (WebView) findViewById(R.id.wvQuestion);
		options = (RadioGroup) findViewById(R.id.radioGroup1);
		tvResult = (TextView) findViewById(R.id.tvResult);
		
		options.setOnCheckedChangeListener(this);
	}

	private void setQuestion() {
		tvQNo.setText(i + " / " + qno);
		wvQuestion.loadData(start + "" + myDbHelper.getQuestion(TABLE_NAME, i) + "" + end, "text/html", "UTF-8");
		o1 = myDbHelper.getOption1(TABLE_NAME, i);
		o2 = myDbHelper.getOption2(TABLE_NAME, i);
		o3 = myDbHelper.getOption3(TABLE_NAME, i);
		o4 = myDbHelper.getOption4(TABLE_NAME, i);
		((RadioButton) options.getChildAt(0)).setText(o1);
		((RadioButton) options.getChildAt(1)).setText(o2);
		((RadioButton) options.getChildAt(2)).setText(o3);
		((RadioButton) options.getChildAt(3)).setText(o4);
		ans = myDbHelper.getAnswer(TABLE_NAME, i);
		
		enableButton();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setSubtitle(SUB_TITLE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.test, menu);
		back = menu.findItem(R.id.action_back);
		forward = menu.findItem(R.id.action_forward);
		back.setEnabled(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			myDbHelper.closeDatabase();
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_back:
			adViewTest.loadAd(new AdRequest());
			forward.setEnabled(true);
			if(i > 1) {
				i--;
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
			adViewTest.loadAd(new AdRequest());
			back.setEnabled(true);
			if(i < qno) {
				i++;
				options.clearCheck();
				tvResult.setVisibility(View.INVISIBLE);
				setQuestion();
				if(i == qno) {
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
			slate.putExtra("id", i);
			startActivity(slate);
			return true;
		case R.id.action_bookmark:
			Cursor c = SplashActivity.bookmarkdb.rawQuery("select * from bookmark where cat = ? and qid = ? and title = ?", new String[] { TABLE_NAME, "" + i, SUB_TITLE });
			c.moveToFirst();
			if(c.getCount() == 0) {
				ContentValues values = new ContentValues();
				values.put(SplashActivity.Column_qid, i);
				values.put(SplashActivity.Column_ques, start + "" + myDbHelper.getQuestion(TABLE_NAME, i) + "" + end);
				values.put(SplashActivity.Column_o1, o1);
				values.put(SplashActivity.Column_o2, o2);
				values.put(SplashActivity.Column_o3, o3);
				values.put(SplashActivity.Column_o4, o4);
				values.put(SplashActivity.Column_ans, ans);
				values.put(SplashActivity.Column_cat, TABLE_NAME);
				values.put(SplashActivity.Column_title, SUB_TITLE);
				SplashActivity.bookmarkdb.insert(SplashActivity.table, null, values);
				Toast.makeText(this, "Bookmark Added Successfully", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Bookmark Already Exists", Toast.LENGTH_SHORT).show();
			}
			return true;
		case R.id.action_formula:
			Intent formula = new Intent("com.hard.targets.aptitudecheck.FORMULAACTIVITY");
			formula.putExtra("sub", SUB_TITLE);
			formula.putExtra("c", TABLE_NAME);
			startActivity(formula);
			break;
		case R.id.action_explain:
			Intent solution = new Intent("com.hard.targets.aptitudecheck.SOLUTIONACTIVITY");
			solution.putExtra("sub", SUB_TITLE);
			solution.putExtra("tb", TABLE_NAME);
			solution.putExtra("id", i);
			startActivity(solution);
			break;
		}
		return super.onOptionsItemSelected(item);
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

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
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
		switch (checkedId) {
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
			myDbHelper.closeDatabase();
		}
		return super.onKeyDown(keyCode, event);
	}

}
