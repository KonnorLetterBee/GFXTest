package com.kngames.gfxtest;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	
	//	custom layout object
	TouchExampleView mainLayout;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		//	instantiates the custom layout and sets the content view to it
        mainLayout = new TouchExampleView(getApplicationContext());
        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        setContentView(mainLayout, mainParams);
        
		//	creates a Drawable object to insert into the layout, and inserts 4 copies of it at different locations
        Drawable temp = getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher);
        temp.setBounds(0, 0, temp.getIntrinsicWidth(), temp.getIntrinsicHeight());
        mainLayout.addDrawObject(temp, 30, 30, 200, 200);
        mainLayout.addDrawObject(temp, 30, 360, 200, 200);
        mainLayout.addDrawObject(temp, 360, 30, 200, 200);
        mainLayout.addDrawObject(temp, 360, 360, 200, 200);
    }
}
