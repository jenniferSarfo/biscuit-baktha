/*
	Author: Hamad Al Marri;
 */

package com.biscuit.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biscuit.models.enums.Status;

public class Epic {

	public transient Project project;

	// info
	public String name;
	public String description;
	public Status state;
	//public String sprintretrospectiveminutes="";
	public Date startDate;
	public Date dueDate;
	//public int assignedEffort;
	//public int velocity;
	public boolean epicCompleted;
	//public boolean isSprintGoalAchieved;
	//public boolean isSprintReviewCompleted;

	public List<UserStory> userStories = new ArrayList<>();
	public List<Bug> bugs;
	public List<Test> tests;
//	public String retrospectiveMinutes;

	// Completed 0pt 0% ToDo 8pt

	public static String[] fields;
	public static String[] fieldsAsHeader;

	static 
	{
		fields = new String[] { "name", "description", "state", "start_date", "due_date",  "epic_completed"};
		fieldsAsHeader = new String[] { "Name", "Description", "State", "Start Date", "Due Date", "Epic Completed"};
	}

	public void addUserStory(UserStory userStory) {
		this.userStories.add(userStory);
	}
	
	public List<UserStory> getUserStories() {
		return this.userStories;
	}

	public void save() {
		project.save();
	}

}
