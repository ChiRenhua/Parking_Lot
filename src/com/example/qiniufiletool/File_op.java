package com.example.qiniufiletool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.R.bool;
import android.os.Environment;
import android.util.Log;

public class File_op {
	/**
	 * 
	 * ʹ��FileOutputStreamд�ļ�ʾ��
	 */
	private String sd_dirpath;
	private File dir_init;
	private File file_init;

	@SuppressWarnings("static-access")
	public static String sd_pathString = new Environment()
			.getExternalStorageDirectory().getPath();// �����ļ��洢��ʼ��Ŀ¼
	private String sd_dfpathString;// ��ʼ·��SD���� rollcall

	/**
	 * ���캯���жϲ�����sd_setpath·�������pathΪ����Ĭ�ϴ���·��/rollcall
	 * 
	 * @param sd_setpath
	 *            Ŀ¼·��
	 * 
	 */
	public File creatDir() {
		sd_dfpathString = "Parking_Lot";
		creatDir(sd_dfpathString);
		return dir_init;

	}

	// ��ȡ����·��
	public String getSDPATH() {
		return sd_dfpathString;

	}

	// ���ľ���·��
	public void setSDPATH(String sd_pathString) {
		this.sd_dfpathString = sd_pathString;
	}

	/**
	 * ����·��mnt/shell/emulated/0/��һ��·��������Ϊ��·��dirpath
	 * 
	 * @param dirpath
	 *            Ŀ¼·��
	 * @return
	 */
	public void creatDir(String dirpath) {
		sd_dirpath = sd_pathString + "/" + dirpath;
		dir_init = new File(sd_dirpath);
		if (!dir_init.exists()) {
			dir_init.mkdirs();
			Log.d("test", "·��:/mnt/sdcard/" + sd_dirpath + "�����ɹ���");
		} else {
			Log.d("test", "·��:/mnt/sdcard/" + sd_dirpath + "�Ѵ��ڣ�");
		}
		setSDPATH(sd_dirpath);
	}

	/**
	 * �����ļ���Ϊ�����ļ���Ӧ�������ļ�·����
	 * 
	 * @param filepath
	 *            �ļ�·��+��
	 * @return File
	 */
	public File creatFile(String filepath) {
		file_init = new File(sd_dfpathString + "/" + filepath);
		try {
			file_init.createNewFile();
			Log.d("test", "�ļ�" + filepath + "�����ɹ���");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("test", "�ļ�" + filepath + "��������" + e.getMessage().toString());
		}
		return file_init;
	}

	/**
	 * �ж��ļ��Ƿ����
	 * 
	 * @param filename
	 *            �ļ���
	 * @return �Ƿ���ڣ�
	 */
	public boolean existFile(String filename) {
		File file = new File(sd_pathString + "/" + sd_dfpathString + "/"
				+ filename);
		return file.exists();
	}

	/**
	 * ��ȡtxt�ļ�
	 * 
	 * @param path
	 *            ·��
	 * @return �ļ�����
	 */
	@SuppressWarnings("unused")
	public String readTXT(String path) {
		String str = "";
		File readFile = new File(path);
		if (readFile != null) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						new FileInputStream(readFile), "GB2312"));
				String strLine;
				while ((strLine = in.readLine()) != null) {
					str = str + strLine;
					str = str + "\n";
				}
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Log.d("test", "��ѡ��һ���ļ�");
		}

		return str;
	}

	/**
	 * ��ȡ�ļ����е�����txt��xls��ʽ�ļ�������
	 * 
	 * @param path
	 *            File��
	 * @param filetype
	 *            �ļ�����
	 * @return �����ļ����͵�list��
	 */
	public ArrayList<String> getFileNameListTxt(File path, String filetype) {

		ArrayList<String> FileNameList = new ArrayList<String>();

		if (path.isDirectory()) {

			if (path.exists()) {
				path.mkdir(); // path����ΪĿ¼
			}
			MyFilter myfilter = new MyFilter(filetype);// ����txt������������ļ���ʽ
			File[] fileNum = path.listFiles(myfilter);// ��ȡĿ¼�µ�����txt��ʽ�ļ�

			for (int i = 0; i < fileNum.length; i++) {

				String fileName = fileNum[i].getName();
				// ���
				FileNameList.add(fileName);
			}
		}
		return FileNameList;
	}

	/**
	 * �ļ�������
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyFilter implements FilenameFilter {
		// typeΪ��Ҫ���˵��������������type=".jpg"����ֻ�ܷ��غ�׺Ϊjpg���ļ�
		private String type;

		public MyFilter(String type) {
			this.type = type;
		}

		public boolean accept(File dir, String name) {
			return name.endsWith(type); // ����true���ļ���ϸ�
		}
	}

	public static boolean writeFile(File file, String message) {
		OutputStreamWriter outputStreamWriter;
		try {
			outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file,true));
			outputStreamWriter.write(message);
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
