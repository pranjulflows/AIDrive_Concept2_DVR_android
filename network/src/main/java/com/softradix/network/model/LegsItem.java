package com.softradix.network.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LegsItem{

	@SerializedName("summary")
	private Summary summary;

	@SerializedName("points")
	private List<PointsItem> points;

	public void setSummary(Summary summary){
		this.summary = summary;
	}

	public Summary getSummary(){
		return summary;
	}

	public void setPoints(List<PointsItem> points){
		this.points = points;
	}

	public List<PointsItem> getPoints(){
		return points;
	}
}