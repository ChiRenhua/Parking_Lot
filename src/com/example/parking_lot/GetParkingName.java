package com.example.parking_lot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/*
 * 获取所有停车场的名字
 */
public class GetParkingName implements Runnable {
	DataManager dataManager = new DataManager();
	String url;
	Context me;
	Handler handler;
	ArrayList<ParkingInfo> datas;

	public GetParkingName(Context context, String url,
			ArrayList<ParkingInfo> parkingData, Handler handler) {
		this.url = url;
		me = context;
		datas = parkingData;
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String result = getJsonMsg(url);
		parseJsonMsg(result);
	}

	private String getJsonMsg(String strUrl) {
		// HttpGet对象
		HttpGet httpRequest = new HttpGet(strUrl);
		String strResult = "";
		try {
			// HttpClient对象
			HttpClient httpClient = new DefaultHttpClient();
			// 获得HttpResponse对象
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的数据
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return strResult;
	}

	@SuppressWarnings("resource")
	private void parseJsonMsg(String str) {
		ParkingInfo parking = null;
		try {
			JSONArray jsonObjs = new JSONObject(str).getJSONArray("items");
			for (int i = 0; i < jsonObjs.length(); i++) {

				String name = ((JSONObject) jsonObjs.opt(i)).getString("key");
				String value = name.substring(5, name.length() - 4);
				String parking_property = value.substring(0, 1);
				if (parking_property.equals("F")) {
					String parkingName = value.substring(1);
					parking = new ParkingInfo(me, parkingName,"Free");// 截取停车场的名字
				}
				if (parking_property.equals("B")) {
					String parkingName = value.substring(3);
					int Price = Integer.parseInt(value.substring(1, 3));
					parking = new ParkingInfo(me, parkingName,"Business",Price);// 截取停车场的名字
				}

				InputStream inFileStream;
				try {
					String nameUrl = URLEncoder.encode(name, "UTF-8"); // 转码
					URL myurl = new URL("http://7xjgv9.dl1.z0.glb.clouddn.com"
							+ "/" + nameUrl);// 测试地址
					HttpURLConnection conn = (HttpURLConnection) myurl
							.openConnection();
					inFileStream = myurl.openStream();
					Scanner isScanner = new Scanner(inFileStream);
					StringBuffer buf = new StringBuffer();
					while (isScanner.hasNextLine()) {
						buf.append(isScanner.nextLine() + "\n");
					}
					String responseContent = buf.toString();

					parking.setAddress(responseContent);
					datas.add(parking);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// Log.d("test", "e:"+e.getMessage());
				}
			}
			Message parkingMessage = new Message();
			parkingMessage.what = 0;
			handler.sendMessage(parkingMessage);
			if (dataManager.getvIP_SYN_Handler() != null) {
				dataManager.getvIP_SYN_Handler().sendMessage(parkingMessage);
			}
		} catch (JSONException e) {
			System.out.println("Jsons parse error !");
			e.printStackTrace();
		}
	}
}
