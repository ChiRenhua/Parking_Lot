package com.example.parking_lot;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;

public class VIP_Synchronization extends Fragment {
	private View view;
	private DataManager dataManager;
	private ListView parkingLotListView;
	private ListView carListView;
	private ArrayList<String> parking_lotArrayList = new ArrayList<String>();
	private ArrayList<String> car_List = new ArrayList<String>();
	MyAdapter parkingAdapter;
	private ArrayAdapter<String> carAdapter;
	private Switch synSwitch;
	private SharedPreferences.Editor sEditor;
	private Database_SYN database_SYN;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				for (int i = 0; i < dataManager.getParkingInfo().size(); i++) {
					parking_lotArrayList.add(dataManager.getParkingInfo()
							.get(i).getName());
				}
				parkingLotListView.setAdapter(parkingAdapter);
				break;

			default:
				break;
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.vip_synchronization, container, false);
		dataManager = (DataManager) getActivity().getApplication();
		dataManager.setvIP_SYN_Handler(handler);
		database_SYN = new Database_SYN(getActivity(), dataManager);
		parkingLotListView = (ListView) view
				.findViewById(R.id.parking_lot_list);
		carListView = (ListView) view.findViewById(R.id.car_list);
		synSwitch = (Switch) view.findViewById(R.id.syn_switch);

		dataManager.setCurrentFragment(2);// 设置状态，选择退出后进入哪个页面
		parkingAdapter = new MyAdapter(getActivity(), parking_lotArrayList);
		parkingLotListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		carAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, car_List);

		for (int i = 0; i < dataManager.getParkingInfo().size(); i++) {
			parking_lotArrayList.add(dataManager.getParkingInfo().get(i)
					.getName());
		}
		parkingLotListView.setAdapter(parkingAdapter);
		synSwitch.setChecked(dataManager.isSYN());

		parkingLotListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				parkingAdapter.setSelectedItem(position);
				parkingAdapter.notifyDataSetChanged();
				String name = parkingAdapter.gettext(position);
				updata(name);
			}
		});

		synSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				dataManager.setSYN(isChecked);
				SharedPreferences mySharedPreferences = getActivity()
						.getSharedPreferences("SYNData", Activity.MODE_PRIVATE);// 初始化存储对象
				sEditor = mySharedPreferences.edit(); // 初始化存储对象
				sEditor.putString("SYN", isChecked + "");
				sEditor.commit();
				if (isChecked) {
					Intent serviceIntent = new Intent();
					serviceIntent.setClass(getActivity(), Data_Service.class);
					getActivity().startService(serviceIntent);
				} else {
					Intent serviceIntent = new Intent();
					serviceIntent.setClass(getActivity(), Data_Service.class);
					getActivity().stopService(serviceIntent);
				}

			}
		});

		return view;
	}

	void updata(String name) {
		car_List.clear();
		Cursor cursor = database_SYN.select(name);
		while (cursor.moveToNext()) {
			if (cursor.getString(0) != null) {
				car_List.add(cursor.getString(0));
			}
		}
		carListView.setAdapter(carAdapter);
	}

}

class MyAdapter extends BaseAdapter {

	Context context;
	SparseBooleanArray selected;
	boolean isSingle = true;
	int old = -1;
	private ArrayList<String> parking_lotArrayList = new ArrayList<String>();

	public MyAdapter(Context context, ArrayList<String> parking_lotArrayList) {
		// TODO Auto-generated constructor stub
		super();
		this.context = context;
		selected = new SparseBooleanArray();
		this.parking_lotArrayList = parking_lotArrayList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return parking_lotArrayList.size();
	}

	public String gettext(int position) {
		// TODO Auto-generated method stub
		return parking_lotArrayList.get(position);
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return parking_lotArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.tv_test);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (selected.get(position)) {
			convertView.setBackgroundResource(R.color.darkgray);
		} else {
			convertView.setBackgroundResource(R.color.gray);
		}
		viewHolder.textView.setText(parking_lotArrayList.get(position));
		return convertView;
	}

	public void setSelectedItem(int selected) {
		if (isSingle = true && old != -1) {
			this.selected.put(old, false);
		}
		this.selected.put(selected, true);
		old = selected;
	}

	class ViewHolder {
		public TextView textView;
	}

}
