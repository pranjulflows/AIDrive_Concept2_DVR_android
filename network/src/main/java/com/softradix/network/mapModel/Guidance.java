package com.softradix.network.mapModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Guidance{

	@SerializedName("instructions")
	private List<InstructionsItem> instructions;

	@SerializedName("instructionGroups")
	private List<InstructionGroupsItem> instructionGroups;

	public List<InstructionsItem> getInstructions(){
		return instructions;
	}

	public List<InstructionGroupsItem> getInstructionGroups(){
		return instructionGroups;
	}
}