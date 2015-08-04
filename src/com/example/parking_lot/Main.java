package com.example.parking_lot;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Main extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private MapActivity mapActivity;
	private FragmentManager fm;
	private FragmentTransaction ft;
	static DataManager dataManager;
	private VIP_Synchronization vip_Synchronization;
	private ArrayList<String> listData = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		dataManager = (DataManager) getApplication();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// dataManager.setName(extras.getString("name"));
			if (extras.getString("user").equals("vip")) {
				listData.add("发送消息");
				listData.add("同步数据");
				dataManager.setUserOrVip("vip");
			} else if (extras.getString("user").equals("commen")) {
				listData.add("消息");
				listData.add("我的停车场");
				dataManager.setUserOrVip("user");
			}
		}
		ListViewAdapter listViewAdapter = new ListViewAdapter(this, listData);
		
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		// marrayList = getResources().getStringArray(R.array.menu);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList.setAdapter(listViewAdapter);
		mDrawerList.setOnItemClickListener(new ItemClickListener());
		mapActivity = new MapActivity();
		ft.replace(R.id.main_frame, mapActivity);
		ft.commit();
		dataManager.setCurrentFragment(1);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		switch (dataManager.getCurrentFragment()) {
		case 1:
			Main.this.finish();
			break;
		case 2:
			ft.replace(R.id.main_frame, mapActivity);
			dataManager.setCurrentFragment(1);
			break;
		default:
			break;
		}
		if (dataManager.getTimeThread() != null) {
			dataManager.setStop(true);
			dataManager.getTimeThread().interrupt();
		}

		ft.commit();
	}

	public class ItemClickListener implements OnItemClickListener {

		GetNews getNews;
		MakeNews makeNews;
		MyParkingLot myParkingLot;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Bundle extras = getIntent().getExtras();
			switch (position) {
			case 1:
				
				if (extras != null) {
					// dataManager.setName(extras.getString("name"));
					if (extras.getString("user").equals("vip")) {
						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						makeNews = new MakeNews();
						fragmentTransaction.replace(R.id.main_frame, makeNews);
						fragmentTransaction.commit();
					} else if (extras.getString("user").equals("commen")) {
						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						getNews = new GetNews();
						fragmentTransaction.replace(R.id.main_frame, getNews);
						fragmentTransaction.commit();
					}
				}
				break;
			case 2:
				if (extras != null) {
					// dataManager.setName(extras.getString("name"));
					if (extras.getString("user").equals("vip")) {
						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						vip_Synchronization = new VIP_Synchronization();
						fragmentTransaction.replace(R.id.main_frame, vip_Synchronization);
						fragmentTransaction.commit();
					} else if (extras.getString("user").equals("commen")) {
						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						myParkingLot = new MyParkingLot();
						fragmentTransaction.replace(R.id.main_frame, myParkingLot);
						fragmentTransaction.commit();
					}
				}
				break;
			default:
				break;
			}

			mDrawerLayout.closeDrawer(mDrawerList);
		}

	}

}
