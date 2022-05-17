package com.softradix.network.mapModel;

import com.google.gson.annotations.SerializedName;

public class PointsItem{

	@SerializedName("latitude")
	private double latitude;

	@SerializedName("longitude")
	private double longitude;

	public double getLatitude(){
		return latitude;
	}

	public double getLongitude(){
		return longitude;
	}
}