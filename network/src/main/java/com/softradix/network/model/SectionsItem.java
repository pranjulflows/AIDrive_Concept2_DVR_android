package com.softradix.network.model;

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

	public void setStartPointIndex(int startPointIndex){
		this.startPointIndex = startPointIndex;
	}

	public int getStartPointIndex(){
		return startPointIndex;
	}

	public void setEndPointIndex(int endPointIndex){
		this.endPointIndex = endPointIndex;
	}

	public int getEndPointIndex(){
		return endPointIndex;
	}

	public void setSectionType(String sectionType){
		this.sectionType = sectionType;
	}

	public String getSectionType(){
		return sectionType;
	}

	public void setTravelMode(String travelMode){
		this.travelMode = travelMode;
	}

	public String getTravelMode(){
		return travelMode;
	}
}