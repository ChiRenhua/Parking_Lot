package com.example.parking_lot;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

@SuppressLint({ "NewApi", "HandlerLeak" })
public class Parking_lot extends Fragment {
	Handler showHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				if (dataManager.getCurrentView().equals("Parking_lot")) {
					updata();
					progressBar.setVisibility(View.GONE);
				}
				break;
			case 1:
				updataButton.setText(msg.obj + "s后刷新");
				break;
			case 2:
				progressBar.setVisibility(View.VISIBLE);
				for (int i = 0; i < carList.size(); i++) {
					layout.removeView(carList.get(i));
				}
				carList.clear();
				new Thread(new cengnetwork()).start();// 下载停车场层的信息
				break;
			default:
				break;
			}
		}

	};
	private ParkingInfo parkingInfo;
	private ArrayList<ImageView> carList = new ArrayList<ImageView>();
	private RelativeLayout layout;
	private int floor_number;
	private int num = 1;
	private Button lastButton;
	private Button nextButton;
	private TextView floor_numbertTextView;// 停车场层数
	private TextView temperature;// 温度
	private TextView car_number;// 可用车位
	private TextView parkingName;// 停车场名称
	private Button updataButton;// 刷新
	private View view;
	private ProgressBar progressBar;
	private DataManager dataManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.parking_lot, container, false);
		layout = (RelativeLayout) view.findViewById(R.id.parking_lotLayout);
		floor_numbertTextView = (TextView) view.findViewById(R.id.floor);
		temperature = (TextView) view.findViewById(R.id.tempereture);
		car_number = (TextView) view.findViewById(R.id.car_number);
		parkingName = (TextView) view.findViewById(R.id.parking_lot_name);
		lastButton = (Button) view.findViewById(R.id.last);
		nextButton = (Button) view.findViewById(R.id.next);
		floor_number = parkingInfo.getFloorNumber();
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		updataButton = (Button) view.findViewById(R.id.updata);
		DataManager.setCarViewHandler(showHandler);
		dataManager = (DataManager) getActivity().getApplication();
		setTimeUpdata updata = new setTimeUpdata();
		dataManager.setTimeThread(updata);
		dataManager.setStop(false);
		updata.start();
		dataManager.setCurrentView("Parking_lot");
		parkingName.setText(dataManager.getParkingName());
		return view;
	}

	public ParkingInfo getParkingInfo() {
		return parkingInfo;
	}

	public void setParkingInfo(ParkingInfo parkingInfo) {
		this.parkingInfo = parkingInfo;
	}

	public void updata() {
		jiazaiData(1);
		lastButton.setEnabled(false);
		if (floor_number == 1) {
			nextButton.setEnabled(false);
		}
		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < carList.size(); i++) {
					layout.removeView(carList.get(i));
				}
				carList.clear();
				num--;
				if (num == 1) {
					lastButton.setEnabled(false);
				}
				nextButton.setEnabled(true);
				jiazaiData(num);

			}
		});
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < carList.size(); i++) {
					layout.removeView(carList.get(i));
				}
				carList.clear();
				num++;
				if (num == floor_number) {
					nextButton.setEnabled(false);
				}
				lastButton.setEnabled(true);
				jiazaiData(num);
			}
		});

		updataButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.VISIBLE);
				for (int i = 0; i < carList.size(); i++) {
					layout.removeView(carList.get(i));
				}
				carList.clear();
				new Thread(new cengnetwork()).start();// 下载停车场层的信息
			}
		});

	}

	/*
	 * 计时刷新
	 */
	class setTimeUpdata extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int time = 60;
			while (time > 0 && !dataManager.isStop()) {

				Message message = new Message();
				message.what = 1;
				message.obj = time;
				showHandler.sendMessage(message);
				time--;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (time == 0) {
					time = 60;
					Message message2 = new Message();
					message2.what = 2;
					showHandler.sendMessage(message2);
				}
			}
		}

	}

	public void jiazaiData(int number) {
		int temp = 0;
		int total_number = 0;
		int enable_number = 0;
		for (int i = 0; i < parkingInfo.getCarportList().size()
				&& temp < number + 1; i++) {
			temp = Integer.parseInt(parkingInfo.getCarportList().get(i).getID()
					.substring(0, 2));
			if (temp == number) {
				total_number++;
				if (parkingInfo.getCarportList().get(i).getState() == 0) {
					enable_number++;
				}
				add_car(parkingInfo.getCarportList().get(i).getPoint_x(),
						parkingInfo.getCarportList().get(i).getPoint_y(),
						parkingInfo.getCarportList().get(i).getRotation(),
						parkingInfo.getCarportList().get(i).getState());
			}
		}

		floor_numbertTextView.setText("第" + number + "层:");
		floor_numbertTextView.setTextColor(Color.WHITE);
		temperature.setText(parkingInfo.getTemperature() + "");
		temperature.setTextColor(Color.WHITE);
		car_number.setText(enable_number + "/" + total_number);
	}

	public void add_car(int x, int y, int rotation, int state) {

		ImageView car = new ImageView(getActivity());

		carList.add(car);
		switch (state) {
		case 0:
			car.setBackground(getResources().getDrawable(
					R.drawable.ic_launcher0));
			break;
		case 1:
			car.setBackground(getResources().getDrawable(
					R.drawable.ic_launcher1));
			break;
		case 2:
			car.setBackground(getResources().getDrawable(
					R.drawable.ic_launcher2));
			break;
		default:
			break;
		}

		RelativeLayout.LayoutParams layoutParams = new LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(x, y, 10, 10);// 150-1650/50-950
		car.setRotation(rotation);
		layout.addView(car, layoutParams);

	}

	// private void addTime(int x, int y, String text, int color) {
	// // TODO Auto-generated method stub
	// TextView textView = new TextView(getActivity());
	// RelativeLayout.LayoutParams layoutParams2 = new LayoutParams(
	// RelativeLayout.LayoutParams.WRAP_CONTENT,
	// RelativeLayout.LayoutParams.WRAP_CONTENT);
	// layoutParams2.setMargins(x, y, 10, 10);// 150-1650/50-950
	// textView.setText(text);
	// switch (color) {
	// case 0:
	//
	// break;
	// case 1:
	// textView.setTextColor(Color.RED);
	// break;
	// case 2:
	// textView.setTextColor(Color.YELLOW);
	// break;
	//
	// default:
	// break;
	// }
	//
	// layout.addView(textView, layoutParams2);
	// }

}
