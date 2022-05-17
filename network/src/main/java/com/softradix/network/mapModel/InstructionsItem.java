package com.softradix.network.mapModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class InstructionsItem{

	@SerializedName("pointIndex")
	private int pointIndex;

	@SerializedName("drivingSide")
	private String drivingSide;

	@SerializedName("instructionType")
	private String instructionType;

	@SerializedName("roadNumbers")
	private List<String> roadNumbers;

	@SerializedName("travelTimeInSeconds")
	private int travelTimeInSeconds;

	@SerializedName("street")
	private String street;

	@SerializedName("routeOffsetInMeters")
	private int routeOffsetInMeters;

	@SerializedName("possibleCombineWithNext")
	private boolean possibleCombineWithNext;

	@SerializedName("message")
	private String message;

	@SerializedName("point")
	private Point point;

	@SerializedName("maneuver")
	private String maneuver;

	@SerializedName("turnAngleInDecimalDegrees")
	private int turnAngleInDecimalDegrees;

	@SerializedName("junctionType")
	private String junctionType;

	@SerializedName("combinedMessage")
	private String combinedMessage;

	@SerializedName("signpostText")
	private String signpostText;

	@SerializedName("exitNumber")
	private String exitNumber;

	@SerializedName("roundaboutExitNumber")
	private int roundaboutExitNumber;

	public int getPointIndex(){
		return pointIndex;
	}

	public String getDrivingSide(){
		return drivingSide;
	}

	public String getInstructionType(){
		return instructionType;
	}

	public List<String> getRoadNumbers(){
		return roadNumbers;
	}

	public int getTravelTimeInSeconds(){
		return travelTimeInSeconds;
	}

	public String getStreet(){
		return street;
	}

	public int getRouteOffsetInMeters(){
		return routeOffsetInMeters;
	}

	public boolean isPossibleCombineWithNext(){
		return possibleCombineWithNext;
	}

	public String getMessage(){
		return message;
	}

	public Point getPoint(){
		return point;
	}

	public String getManeuver(){
		return maneuver;
	}

	public int getTurnAngleInDecimalDegrees(){
		return turnAngleInDecimalDegrees;
	}

	public String getJunctionType(){
		return junctionType;
	}

	public String getCombinedMessage(){
		return combinedMessage;
	}

	public String getSignpostText(){
		return signpostText;
	}

	public String getExitNumber(){
		return exitNumber;
	}

	public int getRoundaboutExitNumber(){
		return roundaboutExitNumber;
	}
}