package com.example.parking_lot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qiniufiletool.File_downloader;
import com.example.qiniufiletool.File_op;

public class GetNews extends Fragment {
	private View view;
	DataManager dataManager;
	private ListView newsListView;
	private Button updaButton;
	private ArrayList<String> arrayList = new ArrayList<String>();
	private ArrayAdapter<String> adapter;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 200:
//				Toast.makeText(getActivity(), "下载成功！", Toast.LENGTH_LONG)
//						.show();
				break;
			case 404:
//				Toast.makeText(getActivity(), "下载失败！", Toast.LENGTH_LONG)
//						.show();
				break;
			default:
				break;
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		view = inflater.inflate(R.layout.getnews, container, false);
		newsListView = (ListView) view.findViewById(R.id.news);
		updaButton = (Button) view.findViewById(R.id.updata);
		dataManager = (DataManager) getActivity().getApplication();
		dataManager.setCurrentFragment(2);

		updata();
		readNews();
		
		updaButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updata();
				readNews();
			}
		});

		return view;
	}

	private void readNews() {
		// TODO Auto-generated method stub
		arrayList.clear();
		File file = new File(File_op.sd_pathString + "/Parking_Lot/Message.txt");
		try {
			InputStream inputStream = new FileInputStream(file);
			InputStreamReader inputreader = new InputStreamReader(inputStream);
			BufferedReader buffreader = new BufferedReader(inputreader);
			String line;
			// 分行读取
			while ((line = buffreader.readLine()) != null) {
				arrayList.add(line);
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newsListView.setAdapter(adapter);
	}

	void updata() {
		File_downloader file_downloader = new File_downloader(handler,
				"Message.txt");
		new Thread(file_downloader).start();
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, arrayList);
	}

}
