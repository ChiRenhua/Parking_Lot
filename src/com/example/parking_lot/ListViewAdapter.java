package com.example.parking_lot;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter{
	private LayoutInflater inflater; 
	private ArrayList<String> mData ;
	ListViewAdapter(Context context,ArrayList<String> data){
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData = data;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size()+1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(position==0){
			convertView =  inflater.inflate(R.layout.image, null);
			ImageView image = (ImageView)convertView.findViewById(R.id.imageView1);
			TextView text = (TextView)convertView.findViewById(R.id.textView1);
		}else{ 
			if(holder == null){
				
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.text, null);
			holder.textview = (TextView)convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textview.setText(mData.get(position-1));
		}
		return convertView;
	}
	private class ViewHolder{
		TextView textview;
	}

}
