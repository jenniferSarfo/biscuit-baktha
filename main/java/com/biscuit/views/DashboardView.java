/*
	Author: Hamad Al Marri;
 */

package com.biscuit.views;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.biscuit.ColorCodes;
import com.biscuit.commands.help.DashboardHelp;
import com.biscuit.commands.project.AddProject;
import com.biscuit.commands.project.EditProject;
import com.biscuit.commands.project.RemoveProject;
import com.biscuit.factories.DashboardCompleterFactory;
import com.biscuit.models.Project;
import com.biscuit.models.services.Finder.Projects;

import jline.console.completer.Completer;

public class DashboardView extends View {

	public DashboardView() {
		super(null, "Dashboard");
	}


	@Override
	void addSpecificCompleters(List<Completer> completers) {
		completers.addAll(DashboardCompleterFactory.getDashboardCompleters());
	}


	@Override
	boolean executeCommand(String[] words) throws IOException {

		if (words.length == 1) {
			return execute1Keyword(words);
		} else if (words.length == 2) {
			return execute2Keyword(words);
		} else if (words.length == 3) {
			return execute3Keyword(words);
		}

		return false;
	}


	private boolean execute3Keyword(String[] words) throws IOException {
		if (words[0].equals("go_to") || words[0].equals(">")) {
			// "project#1", "users", "contacts", "groups"

			if (words[1].equals("project") || words[1].equals("p")) {
				// check if project name
				Project p = Projects.getProject(words[2]);
				if (p != null) {
					ProjectView pv = new ProjectView(this, p);
					System.out.println("Viewing Project");
					pv.view();
					return true;
				}
				return false;
			}
		} else if (words[1].equals("project") || words[1].equals("p")) {
			if (words[0].equals("edit") || words[0].equals("-e")) {
				Project p = Projects.getProject(words[2]);
				if (p != null) {
					(new EditProject(reader, p)).execute();
					resetCompleters();
					return true;
				}
				return false;
			} else if (words[0].equals("remove") || words[0].equals("-r")) {
				Project p = Projects.getProject(words[2]);
				if (p != null) {
					(new RemoveProject(reader, p)).execute();
					resetCompleters();
					return true;
				}
				return false;
			}
		}
		else if (words[0].equals("list") || words[0].equals("-ls")) {
			if(words[1].equals("projects") && words[2].equals("-sort")){
				List<String> sortedFiles = getSortedListOfProjects();

				for(String s: sortedFiles)
				{
					System.out.println(s);
				}

				resetCompleters();
				return true;
			}
		}

		return false;
	}

	public List<String> getSortedListOfProjects()
	{
		String userHome = System.getProperty("user.home");

		File folder = new File(userHome+"/"+"biscuit");
		File[] listOfFiles = folder.listFiles();

		if(listOfFiles == null) {
			return null;
		}
		else {
			List<String> projectFiles = new ArrayList<String>();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					projectFiles.add(listOfFiles[i].getName().toString());
				} else if (listOfFiles[i].isDirectory()) {
					continue;
				}
			}

			Collections.sort(projectFiles);

			return projectFiles;
		}
	}


	private boolean execute2Keyword(String[] words) throws IOException {
		if (words[0].equals("go_to") || words[0].equals(">")) {
			// "project#1", "users", "contacts", "groups"

			// check if project name
			Project p = Projects.getProject(words[1]);
			if (p != null) {
				ProjectView pv = new ProjectView(this, p);
				System.out.println("In project");
				pv.view();
				return true;
			}
			return false;

		} else if (words[0].equals("list") || words[0].equals("-ls")) {
			if(words[1].equals("projects")){
				String userHome = System.getProperty("user.home");

				File folder = new File(userHome+"/"+"biscuit");
				File[] listOfFiles = folder.listFiles();

				if (listOfFiles == null)
					System.out.println("You don't have biscuit folder in your home directory to list projects");
				else {
					for (int i = 0; i < listOfFiles.length; i++) {
						if (listOfFiles[i].isFile()) {
							System.out.println(listOfFiles[i].getName());
						} else if (listOfFiles[i].isDirectory()) {
							continue;
						}
					}
				}
				resetCompleters();
				return true;
			}
		} else if (words[1].equals("project") || words[1].equals("p")) {
			if (words[0].equals("add") || words[0].equals("-a")) {
				(new AddProject(reader)).execute();
				resetCompleters();
				return true;
			}
		}

		return false;
	}


	private boolean execute1Keyword(String[] words) throws IOException {
		if (words[0].equals("summary")) {
		} else if (words[0].equals("projects") || words[0].equals("p")) {
		} else if (words[0].equals("alerts")) {
		} else if (words[0].equals("check_alert")) {
		} else if (words[0].equals("search")) {
		} else if (words[0].equals("help") || words[0].equals("-h")) {
			return (new DashboardHelp().execute());
		}

		return false;
	}

}
