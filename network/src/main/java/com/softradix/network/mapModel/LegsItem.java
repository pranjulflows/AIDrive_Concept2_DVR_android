package com.softradix.network.mapModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LegsItem{

	@SerializedName("summary")
	private Summary summary;

	@SerializedName("points")
	private List<PointsItem> points;

	public Summary getSummary(){
		return summary;
	}

	public List<PointsItem> getPoints(){
		return points;
	}
}