package com.example.parking_lot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MyParkingLot extends Fragment {
	private View view;
	private DataManager dataManager;
	private ListView myParkinglotListView;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> likedParkingLotArrayList = new ArrayList<String>();
	private int deleteNumber;
	private String deleteString;
	private HashSet<String> interestedParking = new HashSet<String>();
	private SharedPreferences.Editor sEditor;
	private Parking_lot parking_lot;
	private Handler showHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.my_parkinglot, container, false);
		SharedPreferences mySharedPreferences = getActivity()
				.getSharedPreferences("ParkingLotData", Activity.MODE_PRIVATE);// 初始化存储对象
		sEditor = mySharedPreferences.edit(); // 初始化存储对象
		myParkinglotListView = (ListView) view.findViewById(R.id.MyParkingLot);
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, likedParkingLotArrayList);
		dataManager = (DataManager) getActivity().getApplication();
		dataManager.setCurrentFragment(2);

		updata();

		myParkinglotListView.setAdapter(adapter);
		dataManager.setLikedParkingLotArrayList(likedParkingLotArrayList);

		myParkinglotListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						deleteNumber = position;
						deleteString = likedParkingLotArrayList.get(position);
						Toast.makeText(getActivity(), deleteString,
								Toast.LENGTH_SHORT).show();
						return false;
					}
				});

		myParkinglotListView
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						// TODO Auto-generated method stub
						menu.add(0, 0, 0, "导航");// 设置menu的第0个元素是删除按钮
						menu.add(0, 1, 0, "进入");// 设置menu的第0个元素是删除按钮
						menu.add(0, 2, 0, "删除");// 设置menu的第0个元素是删除按钮
					}
				});

		return view;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 0:
			FragmentManager fm0 = getFragmentManager();
			FragmentTransaction ft0 = fm0
					.beginTransaction();
			fm0 = getFragmentManager();
			ft0 = fm0.beginTransaction();
			MapActivity mapActivity = dataManager.getMapActivity();
			ft0.replace(R.id.main_frame, mapActivity);
			ft0.commit();
			showHandler = mapActivity.getShowHandler();
			Message message = new Message();
			message.what = 2;
			message.obj = deleteString;
			showHandler.sendMessage(message);
			break;
		case 1:
			FragmentManager fm1 = getFragmentManager();
			FragmentTransaction ft1 = fm1
					.beginTransaction();
			for (int i = 0; i < dataManager.getParkingInfo().size(); i++) {
				ParkingInfo data = dataManager.getParkingInfo().get(i);
				if (data.getName().equals(deleteString)) {
					new Thread(new cengnetwork()).start();// 下载停车场层的信息
					dataManager.setIndex(i);
					fm1 = getFragmentManager();
					ft1 = fm1.beginTransaction();
					parking_lot = new Parking_lot();
					ft1.replace(R.id.main_frame, parking_lot);
					ft1.commit();
					dataManager.setCurrentFragment(2);
					parking_lot.setParkingInfo(dataManager
							.getParkingInfo().get(i));
					break;
				}
			}
			break;
		case 2:
			for (int j = 0; j < dataManager.getLikedParkingLotArrayList()
					.size(); j++) {
				if (dataManager.getLikedParkingLotArrayList().get(j)
						.equals(deleteString)) {
					dataManager.getLikedParkingLotArrayList().remove(j);
				}
			}
			interestedParking.clear();
			for (int k = 0; k < dataManager.getLikedParkingLotArrayList()
					.size(); k++) {
				interestedParking.add(dataManager.getLikedParkingLotArrayList()
						.get(k));
			}
			sEditor.putStringSet("interestedParkingLot", interestedParking);
			sEditor.commit();
			Toast.makeText(getActivity(), "取消标记成功", Toast.LENGTH_SHORT).show();
			updata();
			break;
		default:
			break;
		}
		myParkinglotListView.setAdapter(adapter);
		return false;
	}

	void updata() {
		likedParkingLotArrayList.clear();
		SharedPreferences mySharedPreferences = getActivity()
				.getSharedPreferences("ParkingLotData", Activity.MODE_PRIVATE);// 初始化存储对象
		HashSet<String> parkingHashSet = new HashSet<String>();
		parkingHashSet = (HashSet<String>) mySharedPreferences.getStringSet(
				"interestedParkingLot", null);
		if (parkingHashSet != null) {
			Iterator it = parkingHashSet.iterator();

			while (it.hasNext()) {
				likedParkingLotArrayList.add(it.next() + "");
			}
		}
	}

}
