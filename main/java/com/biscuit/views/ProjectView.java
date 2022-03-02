/*
	Author: Hamad Al Marri;
 */

package com.biscuit.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.biscuit.ColorCodes;
import com.biscuit.commands.help.ProjectHelp;
import com.biscuit.commands.planner.ShowPlan;
import com.biscuit.commands.planner.ShowPlanDetails;
import com.biscuit.commands.project.ShowProject;
import com.biscuit.commands.project.ShowTeam;
import com.biscuit.commands.release.AddRelease;
import com.biscuit.commands.release.ListReleases;
import com.biscuit.commands.sprint.AddSprint;
import com.biscuit.commands.sprint.ListSprints;
import com.biscuit.commands.task.ListTasks;
import com.biscuit.commands.userStory.AddUserStoryToBacklog;
import com.biscuit.commands.userStory.ListUserStories;
import com.biscuit.factories.ProjectCompleterFactory;
import com.biscuit.models.Project;
import com.biscuit.models.Release;
import com.biscuit.models.Sprint;
import com.biscuit.models.UserStory;
import com.biscuit.models.services.Finder.Releases;
import com.biscuit.models.services.Finder.Sprints;
import com.biscuit.models.services.Finder.UserStories;

import jline.console.completer.Completer;

public class ProjectView extends View {

	Project project = null;


	public ProjectView(View previousView, Project p) {
		super(previousView, p.name);
		this.project = p;
	}


	@Override
	void addSpecificCompleters(List<Completer> completers) {
		completers.addAll(ProjectCompleterFactory.getProjectCompleters(project));
	}


	@Override
	boolean executeCommand(String[] words) throws IOException {
		if (words.length == 1) {
			return execute1Keyword(words);
		} else if (words.length == 2) {
			return execute2Keywords(words);
		} else if (words.length == 3) {
			return execute3Keywords(words);
		} else if (words.length == 4) {
			return execute4Keywords(words);
		}
		return false;
	}


	private boolean execute4Keywords(String[] words) throws IOException {
		if (words[0].equals("show") || words[0].equals("-s")) {
			if (words[1].equals("backlog")) {
				if (words[2].equals("filter")) {
					(new ListUserStories(project.backlog, "", true, words[3], false, "")).execute();
					return true;
				} else if (words[2].equals("sort") || words[2].equals("-st")) {
					(new ListUserStories(project.backlog, "", false, "", true, words[3])).execute();
					return true;
				}
			}
		} else if (words[0].equals("list") || words[0].equals("-ls")) {
			if (words[1].equals("releases")) {
				if (words[2].equals("filter") || words[2].equals("-f")) {
					(new ListReleases(project, "Releases", true, words[3], false, "")).execute();
					return true;
				} else if (words[2].equals("sort") || words[2].equals("-st")) {
					(new ListReleases(project, "Releases", false, "", true, words[3])).execute();
					return true;
				}
			} else if (words[1].equals("sprints")) {
				if (words[2].equals("filter") || words[2].equals("-f")) {
					(new ListSprints(project, "Sprints", true, words[3], false, "")).execute();
					return true;
				} else if (words[2].equals("sort") || words[2].equals("-st")) {
					(new ListSprints(project, "Sprints", false, "", true, words[3])).execute();
					return true;
				}
			} else if (words[1].equals("user_stories") || words[1].equals("us") || words[1].equals("US")) {
				if (words[2].equals("filter") || words[2].equals("-f")) {
					(new ListUserStories(UserStories.getAll(project), "User Stories (Filtered)", true, words[3], false, "")).execute();
					return true;
				} else if (words[2].equals("sort") || words[2].equals("-st")) {
					(new ListUserStories(UserStories.getAll(project), "All User Stories", false, "", true, words[3])).execute();
					return true;
				}
			}
		}
		return false;
	}


	private boolean execute3Keywords(String[] words) throws IOException{
		if (words[0].equals("go_to") || words[0].equals(">")) {
			if (words[1].equals("release")) {
				if (Releases.getAllNames(project).contains(words[2])) {
					Release r = Releases.find(project, words[2]);
					if (r == null) {
						return false;
					}

					// r.project = project;

					ReleaseView rv = new ReleaseView(this, r);
					System.out.println("Viewing release");
					rv.view();
					return true;
				}
			} else if (words[1].equals("sprint")) {
				if (Sprints.getAllNames(project).contains(words[2])) {
					Sprint s = Sprints.find(project, words[2]);
					if (s == null) {
						return false;
					}

					// s.project = project;

					SprintView sv = new SprintView(this, s);
					System.out.println("Viewing Sprint");
					sv.view();
					return true;
				}
			} else if (words[1].equals("user_story") || words[1].equals("us") || words[1].equals("US")) {
				if (UserStories.getAllNames(project).contains(words[2])) {
					UserStory us = UserStories.find(project, words[2]);
					if (us == null) {
						return false;
					}

					UserStroryView usv = new UserStroryView(this, us);
					System.out.println("Viewing User story");
					usv.view();
					return true;
				}
			}
		}
		else if(words[0].equals("add") && words[1].equals("team") && words[2].equals("viaTaigaAPI"))
		{
			String prompt = reader.getPrompt();
			URL url = new URL ("https://api.taiga.io/api/v1/auth");
			Map<String,Object> params = new LinkedHashMap<>();
			params.put("type", "normal");
			reader.setPrompt(ColorCodes.BLUE + "Enter taiga username" + ColorCodes.RESET);
			String username = reader.readLine();
			reader.setPrompt(ColorCodes.BLUE + "Enter taiga password" + ColorCodes.RESET);
			String password = reader.readLine();
			params.put("username", username);
			params.put("password", password);

			StringBuilder postData = new StringBuilder();
			for (Map.Entry<String,Object> param : params.entrySet()) {
				if (postData.length() != 0) postData.append('&');
				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			byte[] postDataBytes = postData.toString().getBytes("UTF-8");



			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setDoOutput(true);
			connection.getOutputStream().write(postDataBytes);

			String authToken="";
			try {

				InputStream content = (InputStream)connection.getInputStream();
				BufferedReader in   =
						new BufferedReader (new InputStreamReader(content));
				String line;
				while ((line = in.readLine()) != null) {
					String[] arrOfStr = line.split("auth_token");
					authToken=arrOfStr[1].substring(4,216);
				}

			}
			catch (Exception e)
			{
				System.out.println(e);
			}

			reader.setPrompt(ColorCodes.BLUE + "Enter project slug from Taiga" + ColorCodes.RESET);
			String taigaSlug = reader.readLine();

			URL url1 = new URL ("https://api.taiga.io/api/v1/projects/by_slug?slug="+taigaSlug);
			HttpURLConnection conn = (HttpURLConnection) url1.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "Bearer " + authToken);

			try {
				InputStream content = (InputStream)conn.getInputStream();
				BufferedReader in   =
						new BufferedReader (new InputStreamReader (content));
				String line;
				HashSet<String> names = new HashSet<>();
				while ((line = in.readLine()) != null) {
					String[] lines = line.split("\"full_name_display\": ");

					for(int i=1;i<lines.length;i++)
					{
						String temp = lines[i].split(",")[0];
						names.add(temp.replace("\"", ""));
					}

					String[] teamMembers = names.toArray(new String[names.size()]);
					project.nameOfTeammembers = teamMembers;

					System.out.println("Below Team members have been successfully added to the project:");

					for(String name : teamMembers)
					{
						System.out.println(name);
					}

					project.save();
					resetCompleters();
					reader.setPrompt(prompt);
					return true;
				}
			}

			catch (Exception e)
			{
				System.out.println(e);
			}

		}

		return false;
	}


	private boolean execute2Keywords(String[] words) throws IOException {
		if (words[0].equals("add") || words[0].equals("-a")) {
			if (words[1].equals("user_story") || words[1].equals("us") || words[1].equals("US")) {
				(new AddUserStoryToBacklog(reader, project)).execute();
				return true;
			} else if (words[1].equals("release")) {
				(new AddRelease(reader, project)).execute();
				resetCompleters();

				return true;
			} else if (words[1].equals("sprint")) {
				(new AddSprint(reader, project)).execute();
				resetCompleters();

				return true;
			}

		} else if (words[0].equals("go_to") || words[0].equals(">")) {
			if (words[1].equals("backlog")) {
				this.project.backlog.project = this.project;
				BacklogView bv = new BacklogView(this, this.project.backlog);
				System.out.println("Navigating to Backlog");
				bv.view();
				
				return true;
			} else if (words[1].equals("releases")) {

				ReleasesView rsv = new ReleasesView(this, project);
				System.out.println("Navigating to Release");
				rsv.view();

				return true;
			} else if (words[1].equals("sprints")) {

				SprintsView ssv = new SprintsView(this, project);
				System.out.println("Navigating to Sprint");
				ssv.view();

				return true;
			} else if (words[1].equals("planner")) {

				PlannerView pv = new PlannerView(this, project);
				System.out.println("Navigating to Planner");
				pv.view();

				return true;
			}
		} else if (words[0].equals("show") || words[0].equals("-s")) {
			if (words[1].equals("backlog")) {
				(new ListUserStories(project.backlog, "Backlog (User Stories)")).execute();
				return true;
			}
		} else if (words[0].equals("list") || words[0].equals("-ls")) {
			if (words[1].equals("releases")) {
				(new ListReleases(project, "Releases")).execute();
				return true;
			} else if (words[1].equals("sprints")) {
				(new ListSprints(project, "Sprints")).execute();
				return true;
			} else if (words[1].equals("user_stories") || words[1].equals("us") || words[1].equals("US")) {
				(new ListUserStories(UserStories.getAll(project), "All User Stories")).execute();
				return true;
			}
		} else if (words[0].equals("plan")) {
			if (words[1].equals("details")) {
				(new ShowPlanDetails(project)).execute();
				return true;
			}
		}
		return false;
	}


	private boolean execute1Keyword(String[] words) throws IOException {
		if (words[0].equals("info") || words[0].equals("-i")) {
			reader.println(project.toString());
			return true;
		} else if (words[0].equals("backlog")) {
			(new ListUserStories(project.backlog, "Backlog (User Stories)")).execute();
			return true;
		} else if (words[0].equals("releases")) {
			(new ListReleases(project, "Releases")).execute();
			return true;
		} else if (words[0].equals("sprints")) {

			for (Release r : project.releases) {
				if (!r.sprints.isEmpty()) {
					(new ListSprints(r, "Release: " + r.name + " -> Sprints")).execute();
				}
			}

			(new ListSprints(project, "Unplanned Sprints")).execute();
			return true;
		} else if (words[0].equals("user_stories") || words[0].equals("us") || words[0].equals("US")) {
			(new ListUserStories(UserStories.getAll(project), "All User Stories")).execute();
			return true;
		} else if (words[0].equals("plan")) {
			(new ShowPlan(project)).execute();
			return true;
		} else if (words[0].equals("tasks")) {
			List<UserStory> userStories = UserStories.getAll(project);
			for (UserStory us : userStories) {
				if (!us.tasks.isEmpty()) {
					(new ListTasks(us, "User Story: " + us.title)).execute();
				}
			}
			return true;
		} else if (words[0].equals("show") || words[0].equals("-s")) {
			return (new ShowProject(project).execute());
		} else if (words[0].equals("help") || words[0].equals("-h")) {
			return (new ProjectHelp().execute());
		}
		else if (words[0].equals("listteam") || words[0].equals("-s"))
		{
			return (new ShowTeam(project).execute());
	    }
		return false;
	}

}
