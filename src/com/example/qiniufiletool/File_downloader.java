package com.example.qiniufiletool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class File_downloader implements Runnable {

	File tarFile;
	int a = 0;
	InputStream inFileStream;
	File_op fileop;
	int size = 0;
	int File_index = 0;// 文件每缓冲下载点
	int File_Allindex = 0;// 文件下载进度节点
	int index_h = 0;// 新文件源文件百分比
	HttpURLConnection conn;
	Handler handler;
	String fileId;
	String filepathString;

	// 文件下载类
	public File_downloader(Handler activityHandler, String filepathString) {
		handler = activityHandler;
		this.filepathString = filepathString;

	}

	public InputStream creatInputStream() {
		try {

			URL myurl = new URL("http://7xjgv9.dl1.z0.glb.clouddn.com/"+filepathString);
			conn = (HttpURLConnection) myurl.openConnection();
			inFileStream = conn.getInputStream();
			Log.d("test", "creatInputStream");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("test", "Filedownloader.creatInputStream:"
					+ e.getMessage().toString());
		}
		return inFileStream;
	}

	// ?
	public int setbPoint() {
		return 0;
	}

	public void writeFile(Handler h_oneHandler, String targer,
			InputStream input, int inpSize) {

		boolean suc = true;
		OutputStream output = null;
		try {

			output = new FileOutputStream(tarFile);
			// Log.d("test", "信息：3，" + inpSize);
			byte buffer[] = new byte[4 * 1024];
			while (((File_index = input.read(buffer)) != -1) && (inpSize != -1)) {

				output.write(buffer, 0, File_index);
			}
			output.flush();
		} catch (Exception e) {
			Log.d("test", "writeFile:wrong" + e.getMessage().toString());
			handler.sendEmptyMessage(404);
			suc = false;
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(suc){
			handler.sendEmptyMessage(200);
		}

		
	}

	public void init() {

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		fileop = new File_op();
		fileop.creatDir();
		tarFile = fileop.creatFile(filepathString);
		Log.d("test", "adc" + tarFile.getPath());
		// tarFile = filedownData.getDownFile();
		if ((tarFile != null)) {
			Log.d("test", "信息：2");
			Log.d("test", "信息：3" + size);
			writeFile(handler, fileId, creatInputStream(), size);
		}
	}
}
