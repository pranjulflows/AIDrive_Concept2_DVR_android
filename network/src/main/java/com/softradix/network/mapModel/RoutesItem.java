package com.softradix.network.mapModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RoutesItem{

	@SerializedName("summary")
	private Summary summary;

	@SerializedName("guidance")
	private Guidance guidance;

	@SerializedName("legs")
	private List<LegsItem> legs;

	@SerializedName("sections")
	private List<SectionsItem> sections;

	public Summary getSummary(){
		return summary;
	}

	public Guidance getGuidance(){
		return guidance;
	}

	public List<LegsItem> getLegs(){
		return legs;
	}

	public List<SectionsItem> getSections(){
		return sections;
	}
}