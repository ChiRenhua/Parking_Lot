package com.example.parking_lot;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

public class Data_Service extends Service {
	private Database_SYN database_SYN;
	private String xmldata;
	private String xmltime;
	private DataManager dataManager;
	private SharedPreferences.Editor sEditor;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		dataManager = (DataManager) getApplication();
		dataManager.setSYN(true);
		SharedPreferences mySharedPreferences = getApplicationContext()
				.getSharedPreferences("SYNData", Activity.MODE_PRIVATE);// 初始化存储对象
		sEditor = mySharedPreferences.edit(); // 初始化存储对象
		sEditor.putString("SYN", "true");
		sEditor.commit();
		database_SYN = new Database_SYN(getApplicationContext(), dataManager);
		Toast.makeText(getApplicationContext(), "同步已开启",
				Toast.LENGTH_LONG).show();
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		database_SYN = new Database_SYN(getApplicationContext(), dataManager);
		new Thread(new getCarData()).start();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(getApplicationContext(), "同步已关闭",
				Toast.LENGTH_LONG).show();
	}

	class getCarData implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {
					URL url = new URL(
							"https://remotemanager.digi.com/ws/DataPoint/dia/channel/00000000-00000000-00409DFF-FF5C4A2E/ceng/write?order=desc&size=1");
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					// can change this to use a different base64 encoder
					String encodedAuthorization = Base64.encodeBytes(
							"hurbustzntcc:!QAZ2wsx".getBytes()).trim();
					conn.setRequestProperty("Authorization", "Basic "
							+ encodedAuthorization);
					InputStream is = conn.getInputStream();
					xmldata = xmljiexi(is);
					SharedPreferences mySharedPreferences = getApplicationContext()
							.getSharedPreferences("SYN", Activity.MODE_PRIVATE);// 初始化存储对象
					String SYN = mySharedPreferences.getString("SYN", null);
					if (xmldata != null && !xmldata.equals(SYN)) {
						database_SYN = new Database_SYN(
								getApplicationContext(), dataManager);
						database_SYN.insert("东区停车场", xmldata);
						mySharedPreferences = getApplicationContext()
								.getSharedPreferences("SYN", Activity.MODE_PRIVATE);// 初始化存储对象
						sEditor = mySharedPreferences.edit(); // 初始化存储对象
						sEditor.putString("SYN", xmldata);
						sEditor.commit();
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	String xmljiexi(InputStream inputStream) throws Exception {
		String dataString = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inputStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("data")) {
					eventType = parser.next();
					dataString = parser.getText();
				} else if (parser.getName().equals("timestampISO")) {
					eventType = parser.next();
					xmltime = parser.getText();
				}
			}

			eventType = parser.next();

		}
		Log.d("chirenhua", dataString+"/"+xmltime);
		return dataString+"/"+xmltime;

	}

}
