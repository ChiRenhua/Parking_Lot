package com.example.parking_lot;


public class Carport {
	private String ID;// ��λID
	private int point_x;// x������
	private int point_y;// y������
	private int state;//��λ״̬
	private int rotation;// ��λ�Ƕ�

	public int getState() {
		return state;
	}

	public int getPoint_x() {
		return point_x;
	}

	public int getPoint_y() {
		return point_y;
	}


	public String getID() {
		return ID;
	}

	public int getRotation() {
		return rotation;
	}

	Carport(String ID, int rotation, int point_x, int point_y,int state) {
		this.ID = ID;
		this.rotation = rotation;
		this.point_x = point_x;
		this.point_y = point_y;
		this.state = state;
	}

}
