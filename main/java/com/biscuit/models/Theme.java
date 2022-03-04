package com.biscuit.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biscuit.models.enums.Status;

public class Theme {

	public transient Project project;
	public String name;
	public String description;
	public Status state;
	public Date startDate;
	public Date dueDate;
	public boolean themeCompleted;

	public List<UserStory> userStories = new ArrayList<>();
	public List<Bug> bugs;
	public List<Test> tests;

	public static String[] fields;
	public static String[] fieldsAsHeader;

	static {
		fields = new String[] { "name", "description", "state", "start_date", "due_date",  "theme_completed"};
		fieldsAsHeader = new String[] { "Name", "Description", "State", "Start Date", "Due Date", "Theme Completed"};
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
