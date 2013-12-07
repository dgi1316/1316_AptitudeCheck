package com.hard.targets.aptitudecheck;

import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class SlateActivity extends Activity {
	
	String SUB_TITLE = null, TABLE_NAME = null;
	int index = 0;
	WebView wvQues;
	LinearLayout localLinearLayout;
	AdView avSlate;
	DatabaseHelper myDbHelper;
	String start = "<html><body style='text-align:justify'><b>";
	String end = "</b><hr></body></html>";
	DrawingView dv;
	private Paint mPaint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slate);
		
		Intent t = getIntent();
		SUB_TITLE = t.getStringExtra("sub");
		TABLE_NAME = t.getStringExtra("tb");
		index = t.getIntExtra("id", 0);
		
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

		initialize();
		
		avSlate = (AdView) findViewById(R.id.adViewSlate);
		avSlate.loadAd(new AdRequest());
	}
	
	public class DrawingView extends View {
		
		public int width;
        public  int height;
        private Bitmap  mBitmap;
        private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;
        
        public void erase() {
        	mBitmap.eraseColor(Color.TRANSPARENT);
            mPath.reset();
            invalidate();
        }
        
        public DrawingView(Context c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);  
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);
        }
        
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        	super.onSizeChanged(w, h, oldw, oldh);
        	
        	mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        	mCanvas = new Canvas(mBitmap);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
        	super.onDraw(canvas);
        	canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
        	canvas.drawPath( mPath,  mPaint);
        	canvas.drawPath( circlePath,  circlePaint);
        }
        
        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;
        
        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
        
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;

                circlePath.reset();
                //circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }
        
        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath,  mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
        	float x = event.getX();
        	float y = event.getY();
        	
        	switch (event.getAction()) {
        	case MotionEvent.ACTION_DOWN:
        		touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        	}
        	return true;
        }
	}

	private void setUpActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setSubtitle("SLATE: " + SUB_TITLE);
	}

	private void initialize() {
		wvQues = (WebView) findViewById(R.id.wvQ);
		localLinearLayout = (LinearLayout) findViewById(R.id.drawView);
		dv = new DrawingView(this);
		localLinearLayout.addView(this.dv);
		mPaint = new Paint();
	    mPaint.setAntiAlias(true);
	    mPaint.setDither(true);
	    mPaint.setColor(Color.BLUE);
	    mPaint.setStyle(Paint.Style.STROKE);
	    mPaint.setStrokeJoin(Paint.Join.ROUND);
	    mPaint.setStrokeCap(Paint.Cap.ROUND);
	    mPaint.setStrokeWidth(5);
		wvQues.loadData(start + "" + myDbHelper.getQuestion(TABLE_NAME, index) + "" + end, "text/html", "UTF-8");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.slate, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			myDbHelper.closeDatabase();
			this.finish();
			break;
		case R.id.action_formula:
			Intent formula = new Intent("com.hard.targets.aptitudecheck.FORMULAACTIVITY");
			formula.putExtra("sub", SUB_TITLE);
			formula.putExtra("c", TABLE_NAME);
			startActivity(formula);
			break;
		case R.id.action_erase:
			dv.erase();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			myDbHelper.closeDatabase();
		}
		return super.onKeyDown(keyCode, event);
	}

}