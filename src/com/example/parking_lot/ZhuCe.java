package com.example.parking_lot;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ZhuCe extends Fragment{
	View view;
	private EditText zhangHaoEditText;
	private EditText miMaEditText;
	private Button zhuceButton;
	private FragmentManager fm;
	private FragmentTransaction ft;
	private LogIn logIn;
	private Database database ;
	private String name;
	private String password;
	private String vip = "false";
	private CheckBox checkBox;
	TextView backTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.zhuce, container,false);
		zhangHaoEditText = (EditText)view.findViewById(R.id.ZhangHao);
		miMaEditText = (EditText)view.findViewById(R.id.MiMa);
		zhuceButton = (Button)view.findViewById(R.id.Zhuce);
		checkBox = (CheckBox)view.findViewById(R.id.VIP);
		backTextView = (TextView)view.findViewById(R.id.back);
		logIn = new LogIn();
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		
		
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(vip.equals("false")){
					vip = "true";
				}else {
					vip = "false";
				}
			}
		});
		
		zhuceButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isZhuCe = false;
				name = zhangHaoEditText.getText().toString();
				password = miMaEditText.getText().toString();
				database = new Database(getActivity());
				Cursor cursor = database.select();
				while(cursor.moveToNext()){
					if(name.equals(cursor.getString(0))){
						Toast.makeText(getActivity(), "此号码已被注册！", Toast.LENGTH_SHORT).show();
						isZhuCe = true;
						break;
					}
				}
				if(!isZhuCe){
					database.insert(name, password, vip);
					Toast.makeText(getActivity(), "注册成功！", Toast.LENGTH_SHORT).show();
					ft.replace(R.id.content_frame, logIn);
					ft.commit();
				}
			}
		});
		
		backTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ft.replace(R.id.content_frame, logIn);
				ft.commit();
			}
		});
		
		return view;
	}

}
