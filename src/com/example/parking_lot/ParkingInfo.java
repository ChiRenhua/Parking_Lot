package com.example.parking_lot;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;

public class ParkingInfo {
	private int index;// 索引

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	private boolean like = false;

	private String name;
	private int numAll;
	private int numRemaining;
	private LatLng ll;// 坐标
	private Marker marker;// 标记
	private Bitmap image;
	private Context context;
	private int temperature;
	private int floorNumber;
	private int faZhi;

	public int getFaZhi() {
		return faZhi;
	}

	public void setFaZhi(int faZhi) {
		this.faZhi = faZhi;
	}

	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}

	public void setFloorNumber(int floorNumber) {
		this.floorNumber = floorNumber;
	}

	private String address;
	private ArrayList<Carport> CarportList = new ArrayList<Carport>();

	public ArrayList<Carport> getCarportList() {
		return CarportList;
	}

	ParkingInfo(Context context, String parkingName, LatLng arg3,
			int temperature, int floorNumber) {
		this.context = context;
		this.name = parkingName;
		this.ll = arg3;
		this.temperature = temperature;
		this.floorNumber = floorNumber;
	}

	public int getNumAll() {
		return numAll;
	}

	public void setNumAll(int numAll) {
		this.numAll = numAll;
	}

	public int getNumRemaining() {
		return numRemaining;
	}

	public void setNumRemaining(int numRemaining) {
		this.numRemaining = numRemaining;
	}

	public LatLng getLatLng() {
		return ll;
	}

	public void setLatLng(LatLng ll) {
		this.ll = ll;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public String getParkingName() {

		String text = name + " " + numRemaining + "/" + numAll;

		return text;
	}

	public void setBitmap(Bitmap image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTemperature() {
		return temperature;
	}

	public int getFloorNumber() {
		return floorNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ParkingInfo(Context content, String name) {
		this.context = content;
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
