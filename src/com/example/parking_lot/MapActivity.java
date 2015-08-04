package com.example.parking_lot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

@SuppressLint("HandlerLeak")
public class MapActivity extends Fragment implements
		BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener {
	// 当停车场信息加载完成后会通知界面显示
	public Handler showHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				InitOverlatParking();
				break;
			case 2:
				DaoHang(msg.obj + "");
				break;
			default:
				break;
			}
		}

	};

	public Handler getShowHandler() {
		return showHandler;
	}

	private View view;
	private MapView mMapView;
	static BaiduMap mBaiduMap;
	private LocationClient locatinClient;
	private boolean isFirstLoc = true;// 是否首次定位
	private FragmentManager fm;
	private FragmentTransaction ft;
	private Parking_lot parking_lot;
	static DataManager dataManager;
	private RelativeLayout relativeLayout;
	private Button daohangButton;
	private Button jinruButton;
	private Button jieshuButton;
	private Button biaojiButton;
	private Marker temp;
	private LatLng myLocation;
	private RoutePlanSearch mSearch = null;
	private RouteLine route = null;
	private OverlayManager routeOverlay = null;
	private boolean useDefaultIcon = false;
	private ArrayList<Marker> markers = new ArrayList<Marker>();
	private SharedPreferences.Editor sEditor;
	private HashSet<String> interestedParking = new HashSet<String>();
	private ArrayList<String> tempArrayList = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.map, container, false);

		SharedPreferences mySharedPreferences = getActivity()
				.getSharedPreferences("ParkingLotData", Activity.MODE_PRIVATE);// 初始化存储对象
		sEditor = mySharedPreferences.edit(); // 初始化存储对象

		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
		relativeLayout = (RelativeLayout) view.findViewById(R.id.MapLayout);

		daohangButton = new Button(getActivity().getApplicationContext());
		daohangButton.setText("到这去");
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(550, 950, 10, 10);// 150-1650/50-950
		relativeLayout.addView(daohangButton, layoutParams);

		jinruButton = new Button(getActivity().getApplicationContext());
		jinruButton.setText("进入停车场");
		RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams2.setMargins(1183, 950, 10, 10);// 150-1650/50-950
		relativeLayout.addView(jinruButton, layoutParams2);

		jieshuButton = new Button(getActivity().getApplicationContext());
		jieshuButton.setText("结束导航");
		RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams3.setMargins(867, 950, 10, 10);// 150-1650/50-950
		relativeLayout.addView(jieshuButton, layoutParams3);

		biaojiButton = new Button(getActivity().getApplicationContext());
		biaojiButton.setText("标记为感兴趣");
		RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams4.setMargins(867, 950, 10, 10);// 150-1650/50-950
		relativeLayout.addView(biaojiButton, layoutParams4);

		daohangButton.setVisibility(View.INVISIBLE);
		jinruButton.setVisibility(View.INVISIBLE);
		jieshuButton.setVisibility(View.INVISIBLE);
		biaojiButton.setVisibility(View.INVISIBLE);

		jieshuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dataManager.getMapActivity().getActivity().recreate();
			}
		});

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		dataManager = Main.dataManager;
		if (dataManager.isHaveDone()) {
			dataManager.setHaveDone(false);
			new Thread(new GetParkingName(getActivity(),
					"http://qiniuphp.sinaapp.com/tc.php?prefix=name",
					DataManager.parkingInfo, dataManager.getParkingLot_info()))
					.start();
		}

		dataManager.setMapActivity(this);
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);// 设置地图缩放比例
		mBaiduMap.setMapStatus(msu);
		mBaiduMap.setMyLocationEnabled(true);
		locatinClient = new LocationClient(getActivity());
		locatinClient.registerLocationListener(new MyLocationListener());
		// 设置定位参数包括：定位模式（高精度定位模式，低功耗定位模式和仅用设备定位模式），返回坐标类型，是否打开GPS等等。
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);// 定位间隔，每隔10秒定位一次
		locatinClient.setLocOption(option);
		locatinClient.start();
		InitOverlatParking();
		mBaiduMap.setOnMarkerClickListener(new MyOnMarkerClickListener());
		dataManager.setCurrentView("MapActivity");
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			if (mMapView == null || arg0 == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(arg0.getRadius())// 设置定位数据的精确度，单位米
					.direction(100)// 此处设置开发者获取到的方向信息，顺时针0-360
					.latitude(arg0.getLatitude())// 定位数据的纬度
					.longitude(arg0.getLongitude())// 定位数据的精度
					.build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				myLocation = new LatLng(arg0.getLatitude(), arg0.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory
						.newLatLng(myLocation);
				mBaiduMap.animateMapStatus(u);
			}
		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isFirstLoc = true;
	}

	private class MyOnMarkerClickListener implements OnMarkerClickListener {

		@Override
		public boolean onMarkerClick(Marker arg0) {
			temp = arg0;
			updata();
			// TODO Auto-generated method stub

			for (int i = 0; i < markers.size(); i++) {
				if (markers.get(i) == arg0) {
					daohangButton.setVisibility(View.VISIBLE);
					jinruButton.setVisibility(View.VISIBLE);
					if (dataManager.getUserOrVip().equals("user")) {
						if (dataManager.getLikedParkingLotArrayList().size() == 0) {
							biaojiButton.setText("标记为感兴趣");
							biaojiButton.setVisibility(View.VISIBLE);
						} else {
							for (int j = 0; j < dataManager
									.getLikedParkingLotArrayList().size(); j++) {
								Marker data = dataManager.getParkingHashMap().get(
										dataManager.getLikedParkingLotArrayList()
												.get(j));
								if (data.equals(temp)) {
									biaojiButton.setText("取消感兴趣");
									biaojiButton.setVisibility(View.VISIBLE);
									break;
								} else {
									biaojiButton.setText("标记为感兴趣");
									biaojiButton.setVisibility(View.VISIBLE);
								}

							}

							break;
						}
					}
					
				}
			}

			mBaiduMap.setOnMapTouchListener(new OnMapTouchListener() {

				@Override
				public void onTouch(MotionEvent arg0) {
					// TODO Auto-generated method stub
					daohangButton.setVisibility(View.INVISIBLE);
					jinruButton.setVisibility(View.INVISIBLE);
					biaojiButton.setVisibility(View.INVISIBLE);

				}
			});

			daohangButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for (int i = 0; i < dataManager.getParkingInfo().size(); i++) {
						ParkingInfo data = dataManager.getParkingInfo().get(i);
						if (data.getMarker().equals(temp)) {
							PlanNode stNode = PlanNode.withLocation(myLocation);
							PlanNode enNode = PlanNode.withLocation(data
									.getLatLng());
							mSearch.drivingSearch((new DrivingRoutePlanOption())
									.from(stNode).to(enNode));
							daohangButton.setVisibility(View.INVISIBLE);
							jinruButton.setVisibility(View.INVISIBLE);
							biaojiButton.setVisibility(View.INVISIBLE);
							jieshuButton.setVisibility(View.VISIBLE);
							break;
						}
					}

				}
			});

			jinruButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for (int i = 0; i < dataManager.getParkingInfo().size(); i++) {
						ParkingInfo data = dataManager.getParkingInfo().get(i);
						if (data.getMarker().equals(temp)) {
							new Thread(new cengnetwork()).start();// 下载停车场层的信息
							dataManager.setIndex(i);
							fm = getFragmentManager();
							ft = fm.beginTransaction();
							parking_lot = new Parking_lot();
							ft.replace(R.id.main_frame, parking_lot);
							ft.commit();
							dataManager.setCurrentFragment(2);
							parking_lot.setParkingInfo(dataManager
									.getParkingInfo().get(i));
							break;
						}
					}
				}
			});

			biaojiButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for (int i = 0; i < dataManager.getParkingInfo().size(); i++) {
						ParkingInfo data = dataManager.getParkingInfo().get(i);
						if (data.getMarker().equals(temp)) {
							if (biaojiButton.getText().equals("取消感兴趣")) {
								for (int j = 0; j < dataManager
										.getLikedParkingLotArrayList().size(); j++) {
									if (dataManager
											.getLikedParkingLotArrayList()
											.get(j).equals(data.getName())) {
										dataManager
												.getLikedParkingLotArrayList()
												.remove(j);
									}
								}
								interestedParking.clear();
								for (int k = 0; k < dataManager
										.getLikedParkingLotArrayList().size(); k++) {
									interestedParking.add(dataManager
											.getLikedParkingLotArrayList().get(
													k));
									Log.d("mark",
											"删除："
													+ dataManager
															.getLikedParkingLotArrayList()
															.get(k));
								}
								sEditor.putStringSet("interestedParkingLot",
										interestedParking);
								sEditor.commit();
								Toast.makeText(getActivity(), "取消标记成功",
										Toast.LENGTH_SHORT).show();
								updata();
								break;

							} else {

								dataManager.getLikedParkingLotArrayList().add(
										data.getName());
								interestedParking.clear();
								for (int k = 0; k < dataManager
										.getLikedParkingLotArrayList().size(); k++) {
									interestedParking.add(dataManager
											.getLikedParkingLotArrayList().get(
													k));
									Log.d("mark",
											"添加："
													+ dataManager
															.getLikedParkingLotArrayList()
															.get(k));
								}
								sEditor.putStringSet("interestedParkingLot",
										interestedParking);
								sEditor.commit();
								Toast.makeText(getActivity(), "标记成功",
										Toast.LENGTH_SHORT).show();
								updata();
								break;
							}

						}
					}
					daohangButton.setVisibility(View.INVISIBLE);
					jinruButton.setVisibility(View.INVISIBLE);
					biaojiButton.setVisibility(View.INVISIBLE);
				}

			});

			return false;
		}

	}

	public void InitOverlatParking() {
		markers.clear();
		ParkingInfo data;
		Marker marker;
		dataManager.getParkingHashMap().clear();
		for (int i = 0; i < dataManager.getParkingInfo().size(); i++) {
			data = dataManager.getParkingInfo().get(i);
			if (data.getLatLng() != null) {
				OverlayOptions ooText;

				switch (data.getFaZhi()) {
				case 0:
					marker = (Marker) mBaiduMap
							.addOverlay(new MarkerOptions()
									.position(data.getLatLng())
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.icon_green_marka)));
					// 添加文字
					ooText = new TextOptions().bgColor(0xAAFFFF00).fontSize(24)
							.fontColor(0xFF008B00).text(data.getParkingName())
							.position(data.getLatLng());
					mBaiduMap.addOverlay(ooText);
					break;
				case 1:
					marker = (Marker) mBaiduMap
							.addOverlay(new MarkerOptions()
									.position(data.getLatLng())
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.icon_yellow_marka)));
					// 添加文字
					ooText = new TextOptions().bgColor(0xAAFFFF00).fontSize(24)
							.fontColor(0xFFCFB53B).text(data.getParkingName())
							.position(data.getLatLng());
					mBaiduMap.addOverlay(ooText);
					break;
				case 2:
					marker = (Marker) mBaiduMap.addOverlay(new MarkerOptions()
							.position(data.getLatLng())
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.icon_red_marka)));
					// 添加文字
					ooText = new TextOptions().bgColor(0xAAFFFF00).fontSize(24)
							.fontColor(0xFFFF00FF).text(data.getParkingName())
							.position(data.getLatLng());
					mBaiduMap.addOverlay(ooText);
					break;
				default:
					marker = null;
					break;
				}

				data.setMarker(marker);
				dataManager.getParkingInfo().set(i, data);
				markers.add(marker);
				dataManager.getParkingHashMap().put(data.getName(),
						data.getMarker());
			}

		}
	}

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			route = result.getRouteLines().get(0);
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
			routeOverlay = overlay;
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	void updata() {
		tempArrayList.clear();
		SharedPreferences mySharedPreferences = getActivity()
				.getSharedPreferences("ParkingLotData", Activity.MODE_PRIVATE);// 初始化存储对象
		HashSet<String> parkingHashSet = new HashSet<String>();
		parkingHashSet = (HashSet<String>) mySharedPreferences.getStringSet(
				"interestedParkingLot", null);
		if (parkingHashSet != null) {
			Iterator it = parkingHashSet.iterator();

			while (it.hasNext()) {
				tempArrayList.add(it.next() + "");
			}
		}

		dataManager.setLikedParkingLotArrayList(tempArrayList);
	}

	/*
	 * 在我的停车场界面导航的回掉函数
	 */
	private void DaoHang(String name) {
		// TODO Auto-generated method stub
		for (int i = 0; i < dataManager.getParkingInfo().size(); i++) {
			ParkingInfo data = dataManager.getParkingInfo().get(i);
			if (data.getName().equals(name)) {
				PlanNode stNode = PlanNode.withLocation(myLocation);
				PlanNode enNode = PlanNode.withLocation(data.getLatLng());
				mSearch.drivingSearch((new DrivingRoutePlanOption()).from(
						stNode).to(enNode));
				jieshuButton.setVisibility(View.VISIBLE);
				break;
			}
		}
	}

}
