package com.biscuit.commands.epic;

import java.io.IOException;

import com.biscuit.ColorCodes;
import com.biscuit.commands.Command;
import com.biscuit.models.Sprint;
import com.biscuit.models.services.DateService;

public class ShowEpic implements Command {
	Sprint s = null;


	public ShowEpic(Sprint s) {
		super();
		this.s = s;
	}


	@Override
	public boolean execute() throws IOException {

		System.out.println(ColorCodes.BLUE + "name: " + ColorCodes.RESET + s.name);
		System.out.println(ColorCodes.BLUE + "description: ");
		System.out.println(ColorCodes.RESET + s.description);
		System.out.println(ColorCodes.BLUE + "state: " + ColorCodes.RESET + s.state);
		System.out.println(
				ColorCodes.BLUE + "Start date: " + ColorCodes.RESET + DateService.getDateAsString(s.startDate));
		System.out.println(ColorCodes.BLUE + "Due date: " + ColorCodes.RESET + DateService.getDateAsString(s.dueDate));
		System.out.println(ColorCodes.BLUE + "Assigned effort: " + ColorCodes.RESET + s.assignedEffort);
		System.out.println(ColorCodes.BLUE + "Velocity: " + ColorCodes.RESET + s.velocity);
		System.out.println(ColorCodes.BLUE + "Sprint Minutes: " + ColorCodes.RESET + s.sprintretrospectiveminutes);

		System.out.println();

		return true;
	}
}
