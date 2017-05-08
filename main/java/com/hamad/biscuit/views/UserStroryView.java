package com.hamad.biscuit.views;

import java.io.IOException;
import java.util.List;

import com.hamad.biscuit.commands.task.AddTaskToUserStory;
import com.hamad.biscuit.commands.task.ListTasks;
import com.hamad.biscuit.commands.userStory.ChangeStatusUserStory;
import com.hamad.biscuit.commands.userStory.EditUserStory;
import com.hamad.biscuit.commands.userStory.ShowUserStory;
import com.hamad.biscuit.factories.UserStoryCompleterFactory;
import com.hamad.biscuit.models.Task;
import com.hamad.biscuit.models.UserStory;
import com.hamad.biscuit.models.enums.State;

import jline.console.completer.Completer;

public class UserStroryView extends View {

	UserStory userStory = null;


	public UserStroryView(View previousView, UserStory userStory) {
		super(previousView, userStory.title);
		this.userStory = userStory;
	}


	@Override
	void addSpecificCompleters(List<Completer> completers) {
		completers.addAll(UserStoryCompleterFactory.getUserStoryCompleters(userStory));
	}


	@Override
	boolean executeCommand(String[] words) throws IOException {
		if (words.length == 1) {
			return execute1Keyword(words);
		} else if (words.length == 2) {
			return execute2Keywords(words);
		} else if (words.length == 3) {
			return execute3Keywords(words);
		}
		return false;
	}


	private boolean execute3Keywords(String[] words) {
		if (words[0].equals("go_to")) {
			if (words[1].equals("task")) {
				if (Task.getTasks(userStory).contains(words[2])) {
					Task t = Task.find(userStory, words[2]);
					if (t == null) {
						return false;
					}

					t.project = userStory.project;

					TaskView usv = new TaskView(this, t);
					usv.view();
					return true;
				}
			}
		}
		return false;
	}


	private boolean execute2Keywords(String[] words) throws IOException {
		if (words[0].equals("change_status_to")) {
			if (State.values.contains(words[1])) {
				(new ChangeStatusUserStory(userStory, State.valueOf(words[1].toUpperCase()))).execute();
				return true;
			}
		} else if (words[0].equals("add")) {
			if (words[1].equals("task")) {
				(new AddTaskToUserStory(reader, userStory.project, userStory)).execute();
				return true;
			}
		}

		return false;
	}


	private boolean execute1Keyword(String[] words) throws IOException {
		if (words[0].equals("show")) {
			(new ShowUserStory(userStory)).execute();
			return true;
		} else if (words[0].equals("edit")) {
			(new EditUserStory(reader, userStory)).execute();
			this.name = userStory.title;
			updatePromptViews();

			return true;
		} else if (words[0].equals("tasks")) {
			(new ListTasks(userStory, "")).execute();
			return true;
		}

		return false;
	}

}
