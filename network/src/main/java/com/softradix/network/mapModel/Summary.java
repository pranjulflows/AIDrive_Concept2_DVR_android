package com.softradix.network.mapModel;

import com.google.gson.annotations.SerializedName;

public class Summary{

	@SerializedName("departureTime")
	private String departureTime;

	@SerializedName("trafficLengthInMeters")
	private int trafficLengthInMeters;

	@SerializedName("travelTimeInSeconds")
	private int travelTimeInSeconds;

	@SerializedName("trafficDelayInSeconds")
	private int trafficDelayInSeconds;

	@SerializedName("arrivalTime")
	private String arrivalTime;

	@SerializedName("lengthInMeters")
	private int lengthInMeters;

	public String getDepartureTime(){
		return departureTime;
	}

	public int getTrafficLengthInMeters(){
		return trafficLengthInMeters;
	}

	public int getTravelTimeInSeconds(){
		return travelTimeInSeconds;
	}

	public int getTrafficDelayInSeconds(){
		return trafficDelayInSeconds;
	}

	public String getArrivalTime(){
		return arrivalTime;
	}

	public int getLengthInMeters(){
		return lengthInMeters;
	}
}