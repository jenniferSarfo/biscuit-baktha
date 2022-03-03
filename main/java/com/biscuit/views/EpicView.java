package com.biscuit.views;

import java.io.IOException;
import java.util.List;

import com.biscuit.commands.epic.ChangeStatusEpic;
import com.biscuit.commands.help.SprintHelp;
import com.biscuit.commands.sprint.ChangeStatusSprint;
import com.biscuit.commands.sprint.EditSprint;
import com.biscuit.commands.sprint.ShowSprint;
import com.biscuit.commands.userStory.AddUserStoryToEpic;
import com.biscuit.commands.userStory.AddUserStoryToSprint;
import com.biscuit.commands.userStory.ListUserStories;
import com.biscuit.factories.SprintCompleterFactory;
import com.biscuit.models.Epic;
import com.biscuit.models.Release;
import com.biscuit.models.Sprint;
import com.biscuit.models.UserStory;
import com.biscuit.models.enums.Status;
import com.biscuit.models.services.Finder.Epics;
import com.biscuit.models.services.Finder.Releases;
import com.biscuit.models.services.Finder.UserStories;

import jline.console.completer.Completer;

public class EpicView extends View {

	Epic epic = null;


	public EpicView(View previousView, Epic epic) {
		super(previousView, epic.name);
		this.epic = epic;
	}


	@Override
	void addSpecificCompleters(List<Completer> completers) {
//		completers.addAll(SprintCompleterFactory.getSprintCompleters(epic));
	}


	@Override
	boolean executeCommand(String[] words) throws IOException {
		if (words.length == 1) {
			return execute1Keyword(words);
		} else if (words.length == 2) {
			return execute2Keywords(words);
		} else if (words.length == 4) {
			return execute4Keyword(words);
		}

		return false;
	}


	private boolean execute4Keyword(String[] words) throws IOException {
		if (words[0].equals("list") || words[0].equals("-ls")) {
			if (words[1].equals("user_stories") || words[1].equals("us") || words[1].equals("US")) {
				if (words[2].equals("filter") || words[2].equals("-f")) {
					(new ListUserStories(epic, epic.name + " (User Stories)", true, words[3], false, "")).execute();
					return true;
				} else if (words[2].equals("sort") || words[2].equals("-st")) {
					(new ListUserStories(epic, epic.name + " (User Stories)", false, "", true, words[3])).execute();
					return true;
				}
			}
		}

		return false;
	}


	private boolean execute2Keywords(String[] words) throws IOException {
		if (words[0].equals("change_status_to")) {
			if (Status.values.contains(words[1])) {
				(new ChangeStatusEpic(epic, Status.valueOf(words[1].toUpperCase()))).execute();
				return true;
			}
		} else if (words[0].equals("list") || words[0].equals("-ls")) {
			if (words[1].equals("user_stories") || words[1].equals("us") || words[1].equals("US")) {
				(new ListUserStories(epic, epic.name + " (User Stories)")).execute();
				return true;
			}
		} else if (words[0].equals("add") || words[0].equals("-a")) {
			if (words[1].equals("user_story") || words[1].equals("us") || words[1].equals("US")) {
				(new AddUserStoryToEpic(reader, epic)).execute();
				resetCompleters();

				return true;
			}
		} 
		else if (words[0].equals("go_to") || words[0].equals(">")) {
//			if (words[1].equals("user_story") || words[1].equals("us")) {
//				if (UserStories.getAllNames(epic).contains(words[2])) {
//					UserStory r = UserStories.find(epic, words[2]);
//					if (r == null) {
//						return false;
//					}
//
//					// r.project = project;
//
//					EpicView rv = new EpicView(this, r);
//					System.out.println("Viewing epics");
//					rv.view();
//					return true;
//				}
//			}
//			
//			else {
				if (UserStories.getAllNames(epic).contains(words[1])) {
				UserStory us = UserStories.find(epic, words[1]);
				if (us == null) {
					return false;
				}

				UserStroryView usv = new UserStroryView(this, us);
				System.out.println("Viewing User story");
				usv.view();
				return true;
			}
//			}
		}
		return false;
	}
	
//	private boolean execute2Keywords(String[] words) throws IOException {
//		if (words[0].equals("add") || words[0].equals("-a")) {
//			if (words[1].equals("user_story") || words[1].equals("us") || words[1].equals("US")) {
//				(new AddUserStoryToEpic(reader, epic)).execute();
//				resetCompleters();
//
//				return true;
//			}
//		} 
//		else if (words[0].equals("go_to") || words[0].equals(">")) {
//			if (words[1].equals("user_story")) {
//				if (UserStories.getAllNames(epic).contains(words[2])) {
//					UserStory us = UserStories.find(epic, words[2]);
//					if (us == null) {
//						return false;
//					}
//
//					// r.project = project;
//
//					UserStroryView usv = new UserStroryView(this, us);
//					System.out.println("Viewing User story");
//					usv.view();
//					return true;
//				}
//			}
//			
//			else {
//				if (UserStories.getAllNames(epic).contains(words[1])) {
//				UserStory us = UserStories.find(sprint, words[1]);
//				if (us == null) {
//					return false;
//				}
//
//				UserStroryView usv = new UserStroryView(this, us);
//				System.out.println("Viewing User story");
//				usv.view();
//				return true;
//			}
//			}
//		}
//		return false;
//	}


	private boolean execute1Keyword(String[] words) throws IOException {
//		if (words[0].equals("show") || words[0].equals("-s")) {
//			(new ShowSprint(sprint)).execute();
//			return true;
//		} else if (words[0].equals("edit") || words[0].equals("-e")) {
//			(new EditSprint(reader, sprint)).execute();
//			this.name = sprint.name;
//			updatePromptViews();
//
//			return true;
//		} else if (words[0].equals("user_stories") || words[0].equals("us") || words[0].equals("US")) {
//			(new ListUserStories(epic, epic.name + " (User Stories)")).execute();
//			return true;
//		} else if (words[0].equals("help") || words[0].equals("-h")) {
//			return (new SprintHelp()).execute();
//		}
		return false;
	}

}
