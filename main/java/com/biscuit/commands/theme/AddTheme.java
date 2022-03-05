package com.biscuit.commands.theme;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.biscuit.ColorCodes;
import com.biscuit.commands.Command;
import com.biscuit.factories.DateCompleter;
import com.biscuit.models.Project;
import com.biscuit.models.Theme;
import com.biscuit.models.enums.Status;
import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;

public class AddTheme implements Command {

	Theme theme = new Theme();
	Project p = new Project();
	String name;
	String description;
	Status state;
	Date startDate;
	Date dueDate;
	boolean themeCompleted;

	public AddTheme(Project p, String name, String description, Status state, Date startDate, Date dueDate, boolean themeCompleted) {
		super();
		this.p=p;
		this.name=name;
		this.description=description;
		this.state=state;
		this.startDate=startDate;
		this.dueDate=dueDate;
		this.themeCompleted=themeCompleted;
	}

	public boolean execute(){
		try
		{
		theme.name=name;
		theme.description=description;
		theme.state=state;
		theme.startDate=startDate;
		theme.dueDate=dueDate;
		theme.themeCompleted=themeCompleted;
		p.addTheme(theme);
		p.save();
		return true;
		}

		catch(Exception e)
		{
		System.out.println(e);
		return false;
		}
	}
}
