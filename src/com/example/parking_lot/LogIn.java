package com.example.parking_lot;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class LogIn extends Fragment {
	private View view;
	private EditText zhangHaoEditText;
	private EditText miMaEditText;
	private TextView zhuCeTextView;
	private Button dengLuButton;
	private FragmentManager fm;
	private FragmentTransaction ft;
	private ZhuCe zhuCe;
	private String name;
	private String password;
	private String vip;
	private Database database;
	//private 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.login, container,false);
		zhangHaoEditText = (EditText)view.findViewById(R.id.ZhangHao);
		miMaEditText = (EditText)view.findViewById(R.id.MiMa);
		zhuCeTextView = (TextView)view.findViewById(R.id.ZhuCe);
		dengLuButton = (Button)view.findViewById(R.id.DengLu);
		database = new Database(getActivity());
		zhuCe = new ZhuCe();
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		
		dengLuButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean ishave = false;
				name = zhangHaoEditText.getText().toString();
				password = miMaEditText.getText().toString();
				Cursor cursor = database.select();
				while(cursor.moveToNext()){
					if(name.equals(cursor.getString(0))){
						ishave = true;
						if(password.equals(cursor.getString(1))){
							if(cursor.getString(2).equals("true")){
								Intent intent = new Intent();
								intent.setClass(getActivity(), Main.class);
								intent.putExtra("user", "vip");
								intent.putExtra("name", cursor.getString(0));
								startActivity(intent);
								//Toast.makeText(getActivity(), "跳转到VIP界面！", Toast.LENGTH_SHORT).show();
								getActivity().finish();
								break;
							}else{
								Intent intent = new Intent();
								intent.setClass(getActivity(), Main.class);
								intent.putExtra("user", "commen");
								intent.putExtra("name", cursor.getString(0));
								startActivity(intent);
								//Toast.makeText(getActivity(), "跳转到普通用户界面！", Toast.LENGTH_SHORT).show();
								getActivity().finish();
								break;
							}
							
						}else {
							Toast.makeText(getActivity(), "密码错误！", Toast.LENGTH_SHORT).show();
							break;
						}
					}
				}
				if(!ishave){
					Toast.makeText(getActivity(), "没有此用户！", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		zhuCeTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ft.replace(R.id.content_frame,zhuCe);
				ft.commit();
			}
		});
		return view;
	}

}
