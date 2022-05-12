package com.softradix.network.model;

import com.google.gson.annotations.SerializedName;

public class Summary{

	@SerializedName("departureTime")
	private String departureTime;

	@SerializedName("noTrafficTravelTimeInSeconds")
	private int noTrafficTravelTimeInSeconds;

	@SerializedName("trafficLengthInMeters")
	private int trafficLengthInMeters;

	@SerializedName("travelTimeInSeconds")
	private int travelTimeInSeconds;

	@SerializedName("trafficDelayInSeconds")
	private int trafficDelayInSeconds;

	@SerializedName("arrivalTime")
	private String arrivalTime;

	@SerializedName("historicTrafficTravelTimeInSeconds")
	private int historicTrafficTravelTimeInSeconds;

	@SerializedName("liveTrafficIncidentsTravelTimeInSeconds")
	private int liveTrafficIncidentsTravelTimeInSeconds;

	@SerializedName("lengthInMeters")
	private int lengthInMeters;

	public void setDepartureTime(String departureTime){
		this.departureTime = departureTime;
	}

	public String getDepartureTime(){
		return departureTime;
	}

	public void setNoTrafficTravelTimeInSeconds(int noTrafficTravelTimeInSeconds){
		this.noTrafficTravelTimeInSeconds = noTrafficTravelTimeInSeconds;
	}

	public int getNoTrafficTravelTimeInSeconds(){
		return noTrafficTravelTimeInSeconds;
	}

	public void setTrafficLengthInMeters(int trafficLengthInMeters){
		this.trafficLengthInMeters = trafficLengthInMeters;
	}

	public int getTrafficLengthInMeters(){
		return trafficLengthInMeters;
	}

	public void setTravelTimeInSeconds(int travelTimeInSeconds){
		this.travelTimeInSeconds = travelTimeInSeconds;
	}

	public int getTravelTimeInSeconds(){
		return travelTimeInSeconds;
	}

	public void setTrafficDelayInSeconds(int trafficDelayInSeconds){
		this.trafficDelayInSeconds = trafficDelayInSeconds;
	}

	public int getTrafficDelayInSeconds(){
		return trafficDelayInSeconds;
	}

	public void setArrivalTime(String arrivalTime){
		this.arrivalTime = arrivalTime;
	}

	public String getArrivalTime(){
		return arrivalTime;
	}

	public void setHistoricTrafficTravelTimeInSeconds(int historicTrafficTravelTimeInSeconds){
		this.historicTrafficTravelTimeInSeconds = historicTrafficTravelTimeInSeconds;
	}

	public int getHistoricTrafficTravelTimeInSeconds(){
		return historicTrafficTravelTimeInSeconds;
	}

	public void setLiveTrafficIncidentsTravelTimeInSeconds(int liveTrafficIncidentsTravelTimeInSeconds){
		this.liveTrafficIncidentsTravelTimeInSeconds = liveTrafficIncidentsTravelTimeInSeconds;
	}

	public int getLiveTrafficIncidentsTravelTimeInSeconds(){
		return liveTrafficIncidentsTravelTimeInSeconds;
	}

	public void setLengthInMeters(int lengthInMeters){
		this.lengthInMeters = lengthInMeters;
	}

	public int getLengthInMeters(){
		return lengthInMeters;
	}
}