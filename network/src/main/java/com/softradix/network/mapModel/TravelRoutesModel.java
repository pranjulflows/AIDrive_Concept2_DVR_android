package com.softradix.network.mapModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TravelRoutesModel{

	@SerializedName("routes")
	private List<RoutesItem> routes;

	@SerializedName("formatVersion")
	private String formatVersion;

	public List<RoutesItem> getRoutes(){
		return routes;
	}

	public String getFormatVersion(){
		return formatVersion;
	}
}