package com.biscuit.commands.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

import com.biscuit.ColorCodes;
import com.biscuit.commands.Command;
import com.biscuit.models.Project;
import com.biscuit.models.Dashboard;

import jline.console.ConsoleReader;

public class AddProject implements Command {

	Dashboard dashboard = Dashboard.getInstance();
	Project project = new Project();
	ConsoleReader reader = null;


	public AddProject(ConsoleReader reader) {
		super();
		this.reader = reader;
	}


	public boolean execute() throws IOException {

		StringBuilder description = new StringBuilder();
		String line;
		String prompt = reader.getPrompt();
		
		project.backlog.project = project;
		
		reader.setPrompt(ColorCodes.BLUE + "project name: " + ColorCodes.RESET);
		project.name = reader.readLine();
		reader.setPrompt(ColorCodes.BLUE + "\ndescription: " + ColorCodes.YELLOW + "\n(\\q to end writing)\n" + ColorCodes.RESET);

		while ((line = reader.readLine()) != null) {
			if (line.equals("\\q")) {
				break;
			}
			description.append(line).append("\n");
			reader.setPrompt("");
		}

		project.description = description.toString();
		
		reader.println();
	    reader.println(ColorCodes.BLUE + "Team Details:" + ColorCodes.RESET);
	    reader.println();
	    
		reader.setPrompt(ColorCodes.BLUE + "No of Members in Team: " + ColorCodes.RESET);
		project.numberOfTeammembers= reader.readLine();
		
	    int number;
	    number=Integer.parseInt(project.numberOfTeammembers);
	    reader.setPrompt(ColorCodes.BLUE + "Names of Team Members: " + ColorCodes.RESET);
	    
	    String str[]=new String[number];
	    int i;
	    for(i=0;i<str.length;i++)
	    {
	    	str[i]=reader.readLine();
	    }
	    
	    project.nameOfTeammembers=str;    
	    reader.println();
	    reader.println(ColorCodes.GREEN + "Team Members has been added successfully!" + ColorCodes.RESET);
		
	    reader.println();
		reader.setPrompt(ColorCodes.BLUE + "github URL of the project: " + ColorCodes.RESET);
		project.githubURL = reader.readLine();

		reader.setPrompt(prompt);

		dashboard.addProject(project);

		dashboard.save();
		project.save();

		reader.println();
		reader.println(ColorCodes.GREEN + "Project \"" + project.name + "\" has been added!" + ColorCodes.RESET);

		return false;
	}

}
