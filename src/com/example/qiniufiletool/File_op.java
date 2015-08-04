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
	 * 使用FileOutputStream写文件示例
	 */
	private String sd_dirpath;
	private File dir_init;
	private File file_init;

	@SuppressWarnings("static-access")
	public static String sd_pathString = new Environment()
			.getExternalStorageDirectory().getPath();// 设置文件存储初始根目录
	private String sd_dfpathString;// 初始路径SD卡： rollcall

	/**
	 * 构造函数判断并创建sd_setpath路径，如果path为空则默认创建路径/rollcall
	 * 
	 * @param sd_setpath
	 *            目录路径
	 * 
	 */
	public File creatDir() {
		sd_dfpathString = "Parking_Lot";
		creatDir(sd_dfpathString);
		return dir_init;

	}

	// 获取绝对路径
	public String getSDPATH() {
		return sd_dfpathString;

	}

	// 更改绝对路径
	public void setSDPATH(String sd_pathString) {
		this.sd_dfpathString = sd_pathString;
	}

	/**
	 * 创建路径mnt/shell/emulated/0/下一层路径，参数为子路径dirpath
	 * 
	 * @param dirpath
	 *            目录路径
	 * @return
	 */
	public void creatDir(String dirpath) {
		sd_dirpath = sd_pathString + "/" + dirpath;
		dir_init = new File(sd_dirpath);
		if (!dir_init.exists()) {
			dir_init.mkdirs();
			Log.d("test", "路径:/mnt/sdcard/" + sd_dirpath + "创建成功！");
		} else {
			Log.d("test", "路径:/mnt/sdcard/" + sd_dirpath + "已存在！");
		}
		setSDPATH(sd_dirpath);
	}

	/**
	 * 创建文件名为？的文件到应用设置文件路径下
	 * 
	 * @param filepath
	 *            文件路径+名
	 * @return File
	 */
	public File creatFile(String filepath) {
		file_init = new File(sd_dfpathString + "/" + filepath);
		try {
			file_init.createNewFile();
			Log.d("test", "文件" + filepath + "创建成功！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("test", "文件" + filepath + "创建出错：" + e.getMessage().toString());
		}
		return file_init;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param filename
	 *            文件名
	 * @return 是否存在？
	 */
	public boolean existFile(String filename) {
		File file = new File(sd_pathString + "/" + sd_dfpathString + "/"
				+ filename);
		return file.exists();
	}

	/**
	 * 读取txt文件
	 * 
	 * @param path
	 *            路径
	 * @return 文件数据
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
			Log.d("test", "请选择一个文件");
		}

		return str;
	}

	/**
	 * 获取文件夹中的所有txt或xls格式文件的名字
	 * 
	 * @param path
	 *            File类
	 * @param filetype
	 *            文件类型
	 * @return 包含文件类型的list表
	 */
	public ArrayList<String> getFileNameListTxt(File path, String filetype) {

		ArrayList<String> FileNameList = new ArrayList<String>();

		if (path.isDirectory()) {

			if (path.exists()) {
				path.mkdir(); // path定义为目录
			}
			MyFilter myfilter = new MyFilter(filetype);// 更改txt，换成所需的文件格式
			File[] fileNum = path.listFiles(myfilter);// 获取目录下的所有txt格式文件

			for (int i = 0; i < fileNum.length; i++) {

				String fileName = fileNum[i].getName();
				// 添加
				FileNameList.add(fileName);
			}
		}
		return FileNameList;
	}

	/**
	 * 文件过滤器
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyFilter implements FilenameFilter {
		// type为需要过滤的条件，比如如果type=".jpg"，则只能返回后缀为jpg的文件
		private String type;

		public MyFilter(String type) {
			this.type = type;
		}

		public boolean accept(File dir, String name) {
			return name.endsWith(type); // 返回true的文件则合格
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
