package com.example.parking_lot;


import java.util.ArrayList;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Window;

public class Parking_MainActivity extends Activity {
	private FragmentManager fm;
	private FragmentTransaction ft;
	private LogIn logIn;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parking__main);
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		logIn = new LogIn();
		ft.replace(R.id.content_frame, logIn);
		ft.commit();

	}

	
}
