package com.biscuit.commands.project;

import java.io.IOException;

import com.biscuit.ColorCodes;
import com.biscuit.commands.Command;
import com.biscuit.models.Project;

public class ShowTeam implements Command {

	Project p = null;


	public ShowTeam(Project p) {
		super();
		this.p = p;
	}


	@Override
	public boolean execute() throws IOException {

		System.out.println(ColorCodes.BLUE + "Team Members: " + ColorCodes.RESET);
		
		String[] teamMembers = p.nameOfTeammembers;
		
		for(int i=0; i<teamMembers.length;i++)
		{
			System.out.println(ColorCodes.RESET + teamMembers[i]);
		}
		System.out.println();

		return true;
	}


}
