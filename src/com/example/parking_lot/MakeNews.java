package com.example.parking_lot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.qiniufiletool.File_op;
import com.example.qiniufiletool.UpFile;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MakeNews extends Fragment{
	private View view;
	private DataManager dataManager;
	private Button sendButton;
	private EditText sendEditText;
	private String time;
	String DEFAULT_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.makenews, container, false);
		dataManager = (DataManager)getActivity().getApplication();
		dataManager.setCurrentFragment(2);
		sendButton = (Button)view.findViewById(R.id.Send);
		sendEditText = (EditText)view.findViewById(R.id.sendNews);
		sendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String message = sendEditText.getText().toString();
				if(message.equals("")){
					Toast.makeText(getActivity(), "消息不能为空！", Toast.LENGTH_LONG).show();
				}else{
					SimpleDateFormat dateFormatter = new SimpleDateFormat(
							DEFAULT_TIME_FORMAT);
					time = dateFormatter.format(Calendar.getInstance().getTime());
					File file = new File(File_op.sd_pathString+"/Parking_Lot/Message.txt");
					File_op.writeFile(file, message+"/"+time+"\n");
					sendEditText.setText("");
					UpFile upFile = new UpFile(getActivity());
					//upFile.doUpload(Uri.fromFile(file), "Message.txt");
					upFile.seturi(Uri.fromFile(file));
					
					
					
				}
			}
		});
		return view;
	}

}
