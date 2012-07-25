package com.kngames.gfxtest;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchExampleView extends View {
	
	//	small inner class to hold a Drawable, and location data of an object to be drawn
	private class DrawObject {
		public int x;
		public int y;
		public Drawable drawable;
		
		public DrawObject(int x, int y, Drawable draw) {
			this.x = x;
			this.y = y;
			this.drawable = draw;
		}
		
		//	calculates the center point in absolute space of this DrawObject
		public Point calculateCenter() {
			return new Point(
					(drawable.getIntrinsicWidth() / 2) + x,
					(drawable.getIntrinsicHeight() / 2) + y);
		}
		
		//	calculates the center point in relative space (from this object's origin) of this DrawObject
		public Point calculateRelativeCenter() {
			return new Point(
					(drawable.getIntrinsicWidth() / 2),
					(drawable.getIntrinsicHeight() / 2));
		}
	}
	
    @SuppressWarnings("unused")
	private Context context;
    private float mLastTouchX;
    private float mLastTouchY;
    
    private ArrayList<DrawObject> drawables;
    private DrawObject selected;
    
	///
	///	Constructors
	///
	
    public TouchExampleView(Context context) {
        this(context, null, 0);
    }
    
    public TouchExampleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public TouchExampleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        
        drawables = new ArrayList<DrawObject>();
    }
    
	///
	///	Object Management Methods
	///
	
	//	creates a new DrawObject from a Drawable object and a coordinate pair and adds it to the list of DrawObjects
	public void addDrawObject(Drawable d, int x, int y) {
    	drawables.add(new DrawObject(x, y, d));
    	invalidate();
    }
    
	//	creates a new DrawObject from a Drawable object, a coordinate pair and width and height measurements, and adds 
	//	it to the list of DrawObjects
    public void addDrawObject(Drawable d, int x, int y, int width, int height) {
    	d.setBounds(0, 0, width, height);
    	drawables.add(new DrawObject(x, y, d));
    	invalidate();
    }
    
    //	takes a set of coordinates and returns the DrawObject touched at that location
    //	if point is within two or more object's bounding boxes, returns the one whose center is closest to touch location
    private DrawObject detectTouchedObject(int x, int y) {
    	ArrayList<DrawObject> touched = new ArrayList<TouchExampleView.DrawObject>();
    	//	brute-forces checks with all objects (to be replaced with more efficient code at a later time)
    	for (DrawObject d : drawables) {
    		if (d.drawable.getBounds().contains(x - d.x, y - d.y))
    			touched.add(d);
    	}
    	
    	if (touched.size() == 1) return touched.get(0);
    	else if (touched.size() == 0) return null;
    	else {
			//	*** this code is known to be buggy ***
    		Point touch = new Point(x, y);
    		DrawObject temp = touched.get(0);
    		double smallestDist = distBetweenPoints(temp.calculateCenter(), touch);
    		
    		//	test every touched object to find the object that has the closest center to touch location
    		for (DrawObject t : touched) {
    			double tempDist = distBetweenPoints(temp.calculateCenter(), touch);
    			if (tempDist < smallestDist) {
    				smallestDist = tempDist;
    				temp = t;
    			}
    		}
    		
    		return temp;
    	}
    }
    
	//	utility method to find the distance between two points in space
    private static double distBetweenPoints(Point a, Point b) {
    	return Math.sqrt(Math.exp(a.x - b.x) + Math.exp(a.y - b.y));
    }

    @Override
	//	draws the canvas to the screen
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        for (DrawObject d : drawables) {
	        canvas.save();
	        canvas.translate(d.x, d.y);
	        d.drawable.draw(canvas);
	        canvas.restore();
        }
    }

    @Override
	//	detects touch events and handles them accordingly
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
		//	initial touch event, selects the DrawObject from the screen to move
        case MotionEvent.ACTION_DOWN: {
            final float x = ev.getX();
            final float y = ev.getY();
            
            // remember original location
            mLastTouchX = x;
            mLastTouchY = y;
            
            selected = detectTouchedObject((int)x, (int)y);
            
            break;
        }
        //	movement (drag) event, moves the selected DrawObject with the finger
        case MotionEvent.ACTION_MOVE: {
        	if (selected != null) {
	            final float x = ev.getX();
	            final float y = ev.getY();
	            
	            // calculate the distance moved
	            final float dx = x - mLastTouchX;
	            final float dy = y - mLastTouchY;
	            
	            // move the object
	            selected.x += dx;
	            selected.y += dy;
	            
	            // remember this new position for the next move event
	            mLastTouchX = x;
	            mLastTouchY = y;
	            
	            invalidate();
	            break;
	        }
        }
        }
        
        return true;
    }
}
