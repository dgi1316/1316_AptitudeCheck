package com.hard.targets.aptitudecheck;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class DrawCanvas extends View implements View.OnTouchListener {
	
	public Canvas a;
	public Path b;
	private Paint c;
	private float d;
	private float e;
	
	public DrawCanvas(Context paramContext) {
		super(paramContext);
		setFocusable(true);
		setFocusableInTouchMode(true);
		setOnTouchListener(this);
		this.a = new Canvas();
		this.b = new Path();
		this.c = new Paint();
		this.c.setAntiAlias(true);
	    this.c.setDither(true);
	    this.c.setColor(-16777216);
	    this.c.setStyle(Paint.Style.STROKE);
	    this.c.setStrokeJoin(Paint.Join.ROUND);
	    this.c.setStrokeCap(Paint.Cap.ROUND);
	    this.c.setStrokeWidth(3.0F);
	}
	
	private void a(float paramFloat1, float paramFloat2) {
		this.b.moveTo(paramFloat1, paramFloat2);
	    this.d = paramFloat1;
	    this.e = paramFloat2;
	}
	
	private void b() {
		this.b.lineTo(this.d, this.e);
		this.a.drawPath(this.b, this.c);
	}
	
	private void b(float paramFloat1, float paramFloat2) {
		float f1 = Math.abs(paramFloat1 - this.d);
	    float f2 = Math.abs(paramFloat2 - this.e);
	    if ((f1 >= 4.0F) || (f2 >= 4.0F)) {
	    	this.b.quadTo(this.d, this.e, (paramFloat1 + this.d) / 2.0F, (paramFloat2 + this.e) / 2.0F);
	        this.d = paramFloat1;
	        this.e = paramFloat2;
	    }
	}
	
	public void a() {
		this.b = null;
		this.b = new Path();
		invalidate();
	}
	
	protected void onDraw(Canvas paramCanvas) {
		paramCanvas.drawColor(-1);
		paramCanvas.drawPath(this.b, this.c);
	}
	
	public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
		float f1 = paramMotionEvent.getX();
	    float f2 = paramMotionEvent.getY();
	    switch (paramMotionEvent.getAction()) {
	    default:
	    	return true;
	    case 0:
	    	a(f1, f2);
	    	invalidate();
	    case 2:
	    	b(f1, f2);
	    	invalidate();
	    case 1:
	    	b();
	    	invalidate();
	    }
	    return true;
	}

}