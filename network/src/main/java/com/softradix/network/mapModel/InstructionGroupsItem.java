package com.softradix.network.mapModel;

import com.google.gson.annotations.SerializedName;

public class InstructionGroupsItem{

	@SerializedName("lastInstructionIndex")
	private int lastInstructionIndex;

	@SerializedName("groupLengthInMeters")
	private int groupLengthInMeters;

	@SerializedName("firstInstructionIndex")
	private int firstInstructionIndex;

	@SerializedName("groupMessage")
	private String groupMessage;

	public int getLastInstructionIndex(){
		return lastInstructionIndex;
	}

	public int getGroupLengthInMeters(){
		return groupLengthInMeters;
	}

	public int getFirstInstructionIndex(){
		return firstInstructionIndex;
	}

	public String getGroupMessage(){
		return groupMessage;
	}
}