package com.softradix.network.mapModel;

import com.google.gson.annotations.SerializedName;

public class SectionsItem{

	@SerializedName("startPointIndex")
	private int startPointIndex;

	@SerializedName("endPointIndex")
	private int endPointIndex;

	@SerializedName("sectionType")
	private String sectionType;

	@SerializedName("travelMode")
	private String travelMode;

	public int getStartPointIndex(){
		return startPointIndex;
	}

	public int getEndPointIndex(){
		return endPointIndex;
	}

	public String getSectionType(){
		return sectionType;
	}

	public String getTravelMode(){
		return travelMode;
	}
}