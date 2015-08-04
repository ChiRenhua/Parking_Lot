package com.example.qiniufiletool;

import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.qiniu.auth.JSONObjectRet;
import com.qiniu.io.IO;
import com.qiniu.io.PutExtra;
import com.qiniu.utils.QiniuException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class UpFile {
	private Uri uri;
	
	public Uri geturi() {
		return uri;
	}
	public void seturi(Uri uri) {
		this.uri = uri;
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				doUpload(geturi(),"Message.txt");
				break;

			default:
				break;
			}
		}
		
	};
	
	public static final int PICK_PICTURE_RESUMABLE = 0;
	Context mContext;
	// @gist upload_arg
	// ����ţ�󶨵Ķ�Ӧbucket������. Ĭ����bucket.qiniudn.com
	public static String bucketName = "komey";//ban2
	public static String domain = bucketName + ".qiniudn.com";
	// upToken ������Ҫ���л�ȡ. SDK ����ʵ�ֻ�ȡ����. ��token���ں���ٻ�ȡһ��
	boolean uploading = false;
	public String uptoken = null;
	public UpFile(Context context){
		mContext = context;
		doupToken();
	}
	public void doupToken()
	{
		new Thread(new Runnable(){
            @Override
            public void run() {
                updateToken();
            }
        }).start();
	}
	private void updateToken(){
		String httpUrl = "http://qiniu-lmh5257.myalauda.cn/uploadtoken?bucket=tingche&key=Message.txt";//up_ban2.php
		// ����httpRequest����
		HttpGet httpRequest = new HttpGet(httpUrl);
		try {
			// ȡ��HttpClient����
			HttpClient httpclient = new DefaultHttpClient();
			// ����HttpClient��ȡ��HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// ����ɹ�
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// ȡ�÷��ص��ַ���
				uptoken = EntityUtils.toString(httpResponse
						.getEntity());

			} else {

			}
		} catch (Exception e) {

		}
		Log.v("token", uptoken);
		Message message = new Message();
		message.what = 0;
		handler.sendMessage(message);
	}
	/**
	 * ��ͨ�ϴ��ļ�
	 * 
	 * @param uri
	 */
	public void doUpload(Uri uri,String fileName) {
		if(uptoken == null){
			Toast.makeText(mContext, "tokenΪ��", Toast.LENGTH_SHORT).show();
		}
		if (uploading) {
			//hint.setText("�ϴ��У����Ժ�");
			Toast.makeText(mContext, "�ϴ��У����Ժ�", Toast.LENGTH_SHORT).show();
			return;
		}
		uploading = true;
		String key = fileName; // �Զ�����key
		
		//key="test/aa.png";
		PutExtra extra = new PutExtra();
		extra.params = new HashMap<String, String>();
		extra.params.put("x:a", "����������Ϣ");
		//hint.setText("�ϴ���");
		IO.putFile(mContext, uptoken, key, uri, extra, new JSONObjectRet() {
			@Override
			public void onProcess(long current, long total) {
				//Toast.makeText(mContext, current + "/" + total, Toast.LENGTH_SHORT).show();
				//hint.setText(current + "/" + total);
			}

			@Override
			public void onSuccess(JSONObject resp) {
				uploading = false;
				String hash = resp.optString("hash", "");
				String value = resp.optString("x:a", "");
				String redirect = "http://" + domain + "/" + hash;
				Toast.makeText(mContext, "��Ϣ���ͳɹ�! ", Toast.LENGTH_SHORT).show();
				//hint.setText("�ϴ��ɹ�! " + hash);
				//Intent intent = new Intent(Intent.ACTION_VIEW, Uri
				//		.parse(redirect));
				//startActivity(intent);
			}

			@Override
			public void onFailure(QiniuException ex) {
				uploading = false;
				Toast.makeText(mContext, "����: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
				//hint.setText("����: " + ex.getMessage());
			}
		});
	}
	
}
