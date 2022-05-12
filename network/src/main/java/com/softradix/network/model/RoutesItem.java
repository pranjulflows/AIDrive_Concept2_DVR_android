package com.softradix.network.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RoutesItem{

	@SerializedName("summary")
	private Summary summary;

	@SerializedName("legs")
	private List<LegsItem> legs;

	@SerializedName("sections")
	private List<SectionsItem> sections;

	public void setSummary(Summary summary){
		this.summary = summary;
	}

	public Summary getSummary(){
		return summary;
	}

	public void setLegs(List<LegsItem> legs){
		this.legs = legs;
	}

	public List<LegsItem> getLegs(){
		return legs;
	}

	public void setSections(List<SectionsItem> sections){
		this.sections = sections;
	}

	public List<SectionsItem> getSections(){
		return sections;
	}
}