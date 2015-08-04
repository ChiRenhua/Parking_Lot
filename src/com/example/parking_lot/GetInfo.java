package com.example.parking_lot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.os.Environment;
import android.util.Log;


public class GetInfo implements Runnable{

	String wenURL;
	String name;
	int num;
	String picFilePath;
	public GetInfo(String parkingName,int number){
		picFilePath = getSDPath() + "/parking" + "/" + parkingName;
		name = parkingName;
		num = number;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		//ͣ��λ��Ϣ�ļ�
//			String url = "http://7xj647.com1.z0.glb.clouddn.com/һ��";//����URL
//			//String url = Config.URL+"/"+name+"/"+mark+num;
//			String result = connServerForResult(url);//��ȡURL
//			parseJsonResult(result);//��������
		//��ȡ����ͼƬ
		getBackground();
		//ͣ��λ��Ϣ����ַ
			
		
	}
	
	private String connServerForResult(String strUrl) {
		// HttpGet����
		HttpGet httpRequest = new HttpGet(strUrl);
		String strResult = "";
		try {
			// HttpClient����
			HttpClient httpClient = new DefaultHttpClient();
			// ���HttpResponse����
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// ȡ�÷��ص�����
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return strResult;
	}
	private void parseJsonResult(String result){
		
	}
	private void getBackground(){
		String picName =  "1" + num + ".png";
		File picFile = new File(picFilePath + "/" + picName);
		Log.d("test","picFile" + picFile.getPath());
		if(!picFile.exists()){//ͼƬ�����ڣ������ȡ
			File newFile = new File(picFilePath);
			if(!newFile.exists()){//�ļ��в�����
				Log.d("test","newFile:"+newFile.mkdirs());;//�����༶Ŀ¼�ļ���
				Log.d("test", "newFile:"+newFile.getPath());
			}
			URL imgUrl = null;

			try {
				//String picURL = Config.URL +"/" +name + "/" +picName;
				String picURL = "http://7xjg1t.com1.z0.glb.clouddn.com/imagetouxiang.png";
				imgUrl = new URL(picURL);
				HttpURLConnection conn = (HttpURLConnection) imgUrl
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				newFile = new File(picFilePath+ "/" + picName);
				Log.d("test","creat:"+newFile.createNewFile());
				FileOutputStream fos = new FileOutputStream(newFile);
				Log.d("test",":"+newFile.getPath() );
				byte buffer[] = new byte[1024];
				while (((is.read(buffer)) != -1)) {
					fos.write(buffer);
				}
				is.close();
				fos.flush();
				fos.close();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("test","e1:"+e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				Log.d("test","e2:"+e.getMessage());
			}
		}else {
			Log.d("test","�ļ�����");
		}
	}
	public String getSDPath(){ 
	       File sdDir = null; 
	       boolean sdCardExist = Environment.getExternalStorageState()   
	                           .equals(Environment.MEDIA_MOUNTED);   //�ж�sd���Ƿ���� 
	       if   (sdCardExist)   
	       {                               
	         sdDir = Environment.getExternalStorageDirectory();//��ȡ��Ŀ¼ 
	      }   
	       return sdDir.toString(); 
	       
	} 
	private void getAddress(){
		try {
			String nameUrl = URLEncoder.encode(name, "UTF-8"); //ת�� 
//			URL myurl = new URL(Config.URL+"/"+nameUrl+"/"+"2"+num+".txt");
			URL myurl = new URL("http://7xjgv9.dl1.z0.glb.clouddn.com"+"/"+nameUrl);//���Ե�ַ
			 HttpURLConnection	conn = (HttpURLConnection) myurl.openConnection();
			InputStream inFileStream = myurl.openStream();
			Scanner isScanner = new Scanner(inFileStream);
            StringBuffer buf = new StringBuffer();
            while (isScanner.hasNextLine()) {
                buf.append(isScanner.nextLine() + "\n");
            }
            String responseContent = buf.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("test", "e:"+e.getMessage());
		}
	}
}
