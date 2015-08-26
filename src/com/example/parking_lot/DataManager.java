package com.example.parking_lot;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.xmlpull.v1.XmlPullParser;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;

@SuppressLint("HandlerLeak")
public class DataManager extends Application {
	private int currentFragment;
	private MapActivity mapActivity;
	private Thread timeThread;
	private String CurrentView = null;
	private String name;
	private String userOrVip;// 用来判断现在是哪个界面
	private boolean haveDone = true;
	private boolean isStop = false;
	private boolean isSYN = false;
	private static int index = 0;
	private static Handler carViewHandler;
	private static ArrayList<String> cengData = new ArrayList<String>();
	private static ArrayList<Integer> carState = new ArrayList<Integer>();
	private static ArrayList<Integer> jiaoDu = new ArrayList<Integer>();
	private static ArrayList<Integer> Xdata = new ArrayList<Integer>();
	private static ArrayList<Integer> Ydata = new ArrayList<Integer>();
	private HashMap<String, Marker> parkingHashMap = new HashMap<String, Marker>();
	static ArrayList<ParkingInfo> parkingInfo = new ArrayList<ParkingInfo>();// 停车场数组
	private ArrayList<String> likedParkingLotArrayList = new ArrayList<String>();
	private Handler vIP_SYN_Handler = null;
	private String ParkingName = null;
	

	public String getParkingName() {
		return ParkingName;
	}

	public void setParkingName(String parkingName) {
		ParkingName = parkingName;
	}

	public Handler getvIP_SYN_Handler() {
		return vIP_SYN_Handler;
	}

	public void setvIP_SYN_Handler(Handler vIP_SYN_Handler) {
		this.vIP_SYN_Handler = vIP_SYN_Handler;
	}

	public ArrayList<String> getLikedParkingLotArrayList() {
		return likedParkingLotArrayList;
	}

	public void setLikedParkingLotArrayList(
			ArrayList<String> likedParkingLotArrayList) {
		this.likedParkingLotArrayList = likedParkingLotArrayList;
	}

	public HashMap<String, Marker> getParkingHashMap() {
		return parkingHashMap;
	}

	public boolean isHaveDone() {
		return haveDone;
	}

	public void setHaveDone(boolean haveDone) {
		this.haveDone = haveDone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrentView() {
		return CurrentView;
	}

	public void setCurrentView(String currentView) {
		CurrentView = currentView;
	}

	public boolean isStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public Thread getTimeThread() {
		return timeThread;
	}

	public void setTimeThread(Thread timeThread) {
		this.timeThread = timeThread;
	}

	public MapActivity getMapActivity() {
		return mapActivity;
	}

	public void setMapActivity(MapActivity mapActivity) {
		this.mapActivity = mapActivity;
	}

	public static Handler getCarViewHandler() {
		return carViewHandler;
	}

	public static void setCarViewHandler(Handler carViewHandler) {
		DataManager.carViewHandler = carViewHandler;
	}

	public static int getIndex() {
		return index;
	}

	public static void setIndex(int index) {
		DataManager.index = index;
	}

	public Handler getParkingLot_info() {
		return parkingLot_info;
	}

	public ArrayList<ParkingInfo> getParkingInfo() {
		return parkingInfo;
	}

	public void setParkingInfo(ArrayList<ParkingInfo> parkingInfo) {
		this.parkingInfo = parkingInfo;
	}

	public int getCurrentFragment() {
		return currentFragment;
	}

	public void setCurrentFragment(int currentFragment) {
		this.currentFragment = currentFragment;
	}

	public boolean isSYN() {
		return isSYN;
	}

	public void setSYN(boolean isSYN) {
		this.isSYN = isSYN;
	}

	public String getUserOrVip() {
		return userOrVip;
	}

	public void setUserOrVip(String userOrVip) {
		this.userOrVip = userOrVip;
	}

	Handler parkingLot_info = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				new Thread(new changnetwork()).start();
				if (isSYN() && getUserOrVip().equals("vip")) {
					
					Intent serviceIntent = new Intent();
					serviceIntent.setClass(getApplicationContext(),
							Data_Service.class);
					startService(serviceIntent);
				}

				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SDKInitializer.initialize(this);
		SharedPreferences mySharedPreferences = getApplicationContext()
				.getSharedPreferences("SYNData", Activity.MODE_PRIVATE);// 初始化存储对象
		String SYN = mySharedPreferences.getString("SYN", null);
		if (SYN == null) {
			isSYN = true;
		} else {
			if (SYN == "true") {
				isSYN = true;
			} else if (SYN == "false") {
				isSYN = false;
			}
		}

	}

	static void setCheWeiData(ParkingInfo park) {
		park.getCarportList().clear();
		jiashuju();
		for (int i = 0; i < 20; i++) {
			park.getCarportList().add(
					new Carport(cengData.get(i), jiaoDu.get(i), Xdata.get(i),
							Ydata.get(i), carState.get(i)));
			Log.d("data",
					cengData.get(i) + "," + jiaoDu.get(i) + "," + Xdata.get(i)
							+ "," + Ydata.get(i) + "," + carState.get(i));
		}
		Message message = new Message();
		message.what = 0;
		carViewHandler.sendMessage(message);

	}

	static void setCheWeiDataLinDa(ParkingInfo park) {
		park.getCarportList().clear();

		ArrayList<String> cengDataLD = new ArrayList<String>();
		ArrayList<Integer> carStateLD = new ArrayList<Integer>();
		ArrayList<Integer> jiaoDuLD = new ArrayList<Integer>();
		ArrayList<Integer> XdataLD = new ArrayList<Integer>();
		ArrayList<Integer> YdataLD = new ArrayList<Integer>();
		cengDataLD.clear();
		carStateLD.clear();
		jiaoDuLD.clear();
		XdataLD.clear();
		YdataLD.clear();
		for (int i = 0; i < 20; i++) {
			jiaoDuLD.add(0);
		}
		for (int i = 0; i < 10; i++) {
			YdataLD.add(315);
		}
		for (int i = 0; i < 10; i++) {
			YdataLD.add(565);
		}
		XdataLD.add(470);
		XdataLD.add(558);
		XdataLD.add(645);
		XdataLD.add(733);
		XdataLD.add(820);
		XdataLD.add(908);
		XdataLD.add(995);
		XdataLD.add(1081);
		XdataLD.add(1168);
		XdataLD.add(1257);
		XdataLD.add(470);
		XdataLD.add(558);
		XdataLD.add(645);
		XdataLD.add(733);
		XdataLD.add(820);
		XdataLD.add(908);
		XdataLD.add(995);
		XdataLD.add(1081);
		XdataLD.add(1168);
		XdataLD.add(1257);

		for (int i = 0; i < 20; i++) {
			cengDataLD.add("010" + i);
		}
		carStateLD.add(0);
		carStateLD.add(0);
		carStateLD.add(1);
		carStateLD.add(0);
		carStateLD.add(0);
		carStateLD.add(0);
		carStateLD.add(0);
		carStateLD.add(0);
		carStateLD.add(0);
		carStateLD.add(0);
		carStateLD.add(0);
		carStateLD.add(0);
		carStateLD.add(0);
		carStateLD.add(0);
		carStateLD.add(0);
		carStateLD.add(2);
		carStateLD.add(0);
		carStateLD.add(0);
		carStateLD.add(0);
		carStateLD.add(0);
		for (int i = 0; i < 20; i++) {
			park.getCarportList().add(
					new Carport(cengDataLD.get(i), jiaoDuLD.get(i), XdataLD
							.get(i), YdataLD.get(i), carStateLD.get(i)));
		}
		Message message = new Message();
		message.what = 0;
		carViewHandler.sendMessage(message);
	}

	static void setCheWeiDataXiangFang(ParkingInfo park) {
		ArrayList<String> cengDataXF = new ArrayList<String>();
		ArrayList<Integer> carStateXF = new ArrayList<Integer>();
		ArrayList<Integer> jiaoDuXF = new ArrayList<Integer>();
		ArrayList<Integer> XdataXF = new ArrayList<Integer>();
		ArrayList<Integer> YdataXF = new ArrayList<Integer>();
		park.getCarportList().clear();
		cengDataXF.clear();
		carStateXF.clear();
		jiaoDuXF.clear();
		XdataXF.clear();
		YdataXF.clear();

		for (int i = 0; i < 40; i++) {
			jiaoDuXF.add(0);
		}
		for (int i = 0; i < 10; i++) {
			YdataXF.add(315);
		}
		for (int i = 0; i < 10; i++) {
			YdataXF.add(565);
		}
		for (int i = 0; i < 10; i++) {
			YdataXF.add(315);
		}
		for (int i = 0; i < 10; i++) {
			YdataXF.add(565);
		}
		for (int i = 0; i < 2; i++) {
			XdataXF.add(470);
			XdataXF.add(558);
			XdataXF.add(645);
			XdataXF.add(733);
			XdataXF.add(820);
			XdataXF.add(908);
			XdataXF.add(995);
			XdataXF.add(1081);
			XdataXF.add(1168);
			XdataXF.add(1257);
			XdataXF.add(470);
			XdataXF.add(558);
			XdataXF.add(645);
			XdataXF.add(733);
			XdataXF.add(820);
			XdataXF.add(908);
			XdataXF.add(995);
			XdataXF.add(1081);
			XdataXF.add(1168);
			XdataXF.add(1257);
		}

		for (int i = 0; i < 20; i++) {
			cengDataXF.add("010" + i);
		}
		for (int i = 0; i < 20; i++) {
			cengDataXF.add("020" + i);
		}
		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(0);
		carStateXF.add(0);
		carStateXF.add(0);
		carStateXF.add(0);
		carStateXF.add(0);
		carStateXF.add(2);
		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(1);

		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(0);
		carStateXF.add(0);
		carStateXF.add(1);
		carStateXF.add(0);
		carStateXF.add(0);
		carStateXF.add(0);
		carStateXF.add(1);
		carStateXF.add(1);
		carStateXF.add(0);
		carStateXF.add(0);
		carStateXF.add(1);
		carStateXF.add(0);
		carStateXF.add(0);
		carStateXF.add(0);
		carStateXF.add(0);
		carStateXF.add(1);
		for (int i = 0; i < 40; i++) {
			park.getCarportList().add(
					new Carport(cengDataXF.get(i), jiaoDuXF.get(i), XdataXF
							.get(i), YdataXF.get(i), carStateXF.get(i)));
		}
		Message message = new Message();
		message.what = 0;
		carViewHandler.sendMessage(message);
	}

	static void jiexiDataceng(String xmldata) {
		// TODO Auto-generated method stub
		carState.clear();
		cengData.clear();
		ArrayList<Integer> state = new ArrayList<Integer>();
		state.clear();
		int size = xmldata.toCharArray().length;
		String ceng = xmldata.substring(0, 2);
		int firt = 6;
		int last = 8;
		int temp = 1;

		while (last <= (size + 1)) {
			String chewei = xmldata.substring(firt, last);
			firt = firt + 2;
			last = last + 2;
			if (temp % 2 == 0) {
				state.add(Integer.parseInt(chewei));
			} else {
				cengData.add(ceng + chewei);
			}

			temp++;
		}
		carState.add(state.get(5));
		carState.add(state.get(6));
		carState.add(state.get(7));
		carState.add(state.get(2));
		carState.add(state.get(3));
		carState.add(state.get(4));
		carState.add(state.get(8));
		carState.add(state.get(9));
		carState.add(state.get(10));
		carState.add(state.get(11));
		carState.add(state.get(12));
		carState.add(state.get(13));
		carState.add(state.get(0));
		carState.add(state.get(1));
		carState.add(state.get(14));
		carState.add(state.get(15));
		carState.add(state.get(16));
		carState.add(state.get(17));
		carState.add(state.get(18));
		carState.add(state.get(19));
		setCheWeiData(parkingInfo.get(getIndex()));
		setCheWeiDataLinDa(parkingInfo.get(2));
		setCheWeiDataXiangFang(parkingInfo.get(1));
		for (int i = 0; i < cengData.size(); i++) {
			Log.d("xml", cengData.get(i));
		}

	}

	void jiexiDatachang(String xmldata) {
		// TODO Auto-generated method stub
		int index = Integer.parseInt(xmldata.substring(0, 4));

		parkingInfo.get(index - 1).setTemperature(
				Integer.parseInt(xmldata.substring(5, 7)));
		double jingDu = Double.parseDouble(xmldata.substring(8, 11) + "."
				+ xmldata.substring(11, 16));
		double weiDu = Double.parseDouble(xmldata.substring(17, 20) + "."
				+ xmldata.substring(20, 25));
		LatLng ll = new LatLng(weiDu, jingDu);
		int numAll = Integer.parseInt(xmldata.substring(30, 33));
		int numRemaining = Integer.parseInt(xmldata.substring(26, 29));
		parkingInfo.get(index - 1).setLatLng(ll);
		parkingInfo.get(index - 1).setNumAll(numAll);
		parkingInfo.get(index - 1).setNumRemaining(numRemaining);
		double faZhi = numRemaining / numAll;
		if (faZhi <= 0.2) {
			parkingInfo.get(index - 1).setFaZhi(2);
		} else if (0.2 < faZhi && faZhi < 0.8) {
			parkingInfo.get(index - 1).setFaZhi(1);
		} else if (0.8 <= faZhi && faZhi <= 1.0) {
			parkingInfo.get(index - 1).setFaZhi(0);
		}
		/*
		 * 停车场假数据
		 */
		LatLng ll2 = new LatLng(45.725088, 126.659630);// 假数据，防死机
		parkingInfo.get(index).setLatLng(ll2);// 假数据，防死机
		parkingInfo.get(index).setTemperature(23);
		parkingInfo.get(index).setFloorNumber(2);
		parkingInfo.get(index).setNumAll(40);
		parkingInfo.get(index).setNumRemaining(21);
		parkingInfo.get(index).setFaZhi(1);
		LatLng ll3 = new LatLng(45.724088, 126.649630);// 假数据，防死机
		parkingInfo.get(index + 1).setLatLng(ll3);// 假数据，防死机
		parkingInfo.get(index + 1).setTemperature(32);
		parkingInfo.get(index + 1).setFloorNumber(1);
		parkingInfo.get(index + 1).setNumAll(20);
		parkingInfo.get(index + 1).setNumRemaining(18);
		parkingInfo.get(index + 1).setFaZhi(0);

		/*
		 * 停车场假数据
		 */

		// 向地图界面发起消息通知停车场加载完成。
		Message message = new Message();
		message.what = 1;
		MapActivity mapActivity = getMapActivity();
		Handler showHandler = mapActivity.getShowHandler();
		showHandler.sendMessage(message);
		parkingInfo.get(index - 1).setFloorNumber(
				Integer.parseInt(xmldata.substring(33, 35)));// 停车场层数假数据
		Log.d("chirenhua",
				index + "," + Integer.parseInt(xmldata.substring(5, 7)) + ","
						+ jingDu + "," + weiDu);
	}

	/*
	 * 假数据
	 */
	static void jiashuju() {
		for (int i = 0; i < 20; i++) {
			jiaoDu.add(0);
		}
		for (int i = 0; i < 10; i++) {
			Ydata.add(315);
		}
		for (int i = 0; i < 10; i++) {
			Ydata.add(565);
		}
		Xdata.add(470);
		Xdata.add(558);
		Xdata.add(645);
		Xdata.add(733);
		Xdata.add(820);
		Xdata.add(908);
		Xdata.add(995);
		Xdata.add(1081);
		Xdata.add(1168);
		Xdata.add(1257);
		Xdata.add(470);
		Xdata.add(558);
		Xdata.add(645);
		Xdata.add(733);
		Xdata.add(820);
		Xdata.add(908);
		Xdata.add(995);
		Xdata.add(1081);
		Xdata.add(1168);
		Xdata.add(1257);
	}

}

class cengnetwork implements Runnable {
	private String CENGURL = "https://remotemanager.digi.com/ws/DataPoint/dia/channel/00000000-00000000-00409DFF-FF5C4A2E/ceng/write?order=desc&size=1";

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			GetData getData = new GetData();
			getData.getDataFromDigi(CENGURL, 1);
		} catch (Exception e) {
			// TODO: handle exception
			//Log.d("222", e.getMessage());
		}
	}
}

/*
 * 实现Runnable接口启动一个线程，用来实现数据的获取和解析
 */
class changnetwork implements Runnable {
	private String CHANGURL = "https://remotemanager.digi.com/ws/DataPoint/dia/channel/00000000-00000000-00409DFF-FF5C4A2E/chang/write?order=desc&size=1";

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			// 调用getData类的获取数据方法
			GetData getData = new GetData();
			getData.getDataFromDigi(CHANGURL, 0);

		} catch (Exception e) {
			// TODO: handle exception
			Log.d("222", e.getMessage());
		}
	}
}

/*
 * 获取数据的类
 */
class GetData {
	DataManager dataManager = Main.dataManager;
	public String xmldata;
	public String userpassword = "hurbustzntcc:!QAZ2wsx";// 连接服务器的用户名和密码

	/*
	 * XML解析方法，通过传入的inputStream参数来获取到网络的输入流，对输入流的每一行数据进行解析，来判断是否是需要的数据
	 */

	String xmljiexi(InputStream inputStream) throws Exception {
		String xmlString = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inputStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("data")) {
					eventType = parser.next();
					xmlString = parser.getText();
				} else if (parser.getName().equals("serverTimestampISO")) {
					eventType = parser.next();
				}
			}

			eventType = parser.next();
		}

		return xmlString;

	}

	/*
	 * 获取数据方法，利用http协议从digi云上获取停车场数据，并调用xmljiexi方法进行数据解析
	 */
	public void getDataFromDigi(String urlString, int temp) throws Exception {
		// TODO Auto-generated method stub
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		// can change this to use a different base64 encoder
		String encodedAuthorization = Base64.encodeBytes(
				userpassword.getBytes()).trim();
		conn.setRequestProperty("Authorization", "Basic "
				+ encodedAuthorization);
		InputStream is = conn.getInputStream();
		xmldata = xmljiexi(is);
		Log.d("xml", xmldata);
		switch (temp) {
		case 0:

			dataManager.jiexiDatachang(xmldata);
			break;
		case 1:
			dataManager.jiexiDataceng(xmldata);
			break;

		default:
			break;
		}

	}

}
