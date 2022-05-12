package com.softradix.network.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RoutePointsResponse{

	@SerializedName("routes")
	private List<RoutesItem> routes;

	@SerializedName("formatVersion")
	private String formatVersion;

	public void setRoutes(List<RoutesItem> routes){
		this.routes = routes;
	}

	public List<RoutesItem> getRoutes(){
		return routes;
	}

	public void setFormatVersion(String formatVersion){
		this.formatVersion = formatVersion;
	}

	public String getFormatVersion(){
		return formatVersion;
	}
}