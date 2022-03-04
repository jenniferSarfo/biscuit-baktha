/*
	Author: Hamad Al Marri;
 */

package com.biscuit.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biscuit.models.enums.Status;

public class Sprint {

	public transient Project project;

	// info
	public String name;
	public String description;
	public Status state;
	public String sprintretrospectiveminutes="";
	public Date startDate;
	public Date dueDate;
	public int assignedEffort;
	public int velocity;
	public boolean isSprintPlanningCompleted;
	public boolean isSprintGoalAchieved;
	public boolean isSprintReviewCompleted;

	public List<UserStory> userStories = new ArrayList<>();
	public List<Epic> epics = new ArrayList<>();
	public List<Bug> bugs;
	public List<Test> tests;
//	public String retrospectiveMinutes;

	// Completed 0pt 0% ToDo 8pt

	public static String[] fields;
	public static String[] fieldsAsHeader;

	static 
	{
		fields = new String[] { "name", "description", "state", "start_date", "due_date", "assigned_effort", "velocity", "sprint_planning_completed", "sprint_goal_achieved", "sprint_review_completed","retrospective_minutes" };
		fieldsAsHeader = new String[] { "Name", "Description", "State", "Start Date", "Due Date", "Assigned Effort", "Velocity", "Sprint Planning Completed", "Sprint Goal Achieved","Sprint Review Completed", "Sprint Minutes"};
	}

	public void addUserStory(UserStory userStory) {
		this.userStories.add(userStory);
	}
	
	public void addEpics(Epic epic) {
		this.epics.add(epic);
	}

	public void save() {
		project.save();
	}

}
