package com.biscuit.views;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.net.*;
import java.io.*;

import java.util.*;

import com.biscuit.ColorCodes;
import com.biscuit.commands.help.BacklogHelp;
import com.biscuit.commands.userStory.AddUserStoryToBacklog;
import com.biscuit.commands.userStory.ListUserStories;
import com.biscuit.factories.BacklogCompleterFactory;
import com.biscuit.models.Backlog;
import com.biscuit.models.UserStory;
import com.biscuit.models.enums.BusinessValue;
import com.biscuit.models.enums.Status;
import com.biscuit.models.services.Finder.UserStories;

import jline.console.completer.Completer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class BacklogView extends View {

	Backlog backlog = null;


	public BacklogView(View previousView, Backlog backlog) {
		super(previousView, "backlog");
		this.backlog = backlog;
	}


	@Override
	void addSpecificCompleters(List<Completer> completers) {
		completers.addAll(BacklogCompleterFactory.getBacklogCompleters(backlog));
	}


	@Override
	boolean executeCommand(String[] words) throws IOException {

		if (words.length == 1) {
			return execute1Keyword(words);
		} else if (words.length == 2) {
			return execute2Keyword(words);
		}
		else if (words.length == 3) {
			return execute3Keyword(words);
		}
		else if (words.length == 4) {
			return execute4Keyword(words);
		}

		return false;
	}


	private boolean execute4Keyword(String[] words) throws IOException {
		if (words[0].equals("list") || words[0].equals("-ls")) {
			if (words[1].equals("user_stories") || words[1].equals("us") || words[1].equals("US")) {
				if (words[2].equals("filter") || words[2].equals("-f")) {
					(new ListUserStories(backlog, "Backlog (User Stories)", true, words[3], false, "")).execute();
					return true;
				} else if (words[2].equals("sort") || words[2].equals("-st")) {
					(new ListUserStories(backlog, "Backlog (User Stories)", false, "", true, words[3])).execute();
					return true;
				}
			}
		}

		return false;
	}


	private boolean execute2Keyword(String[] words) throws IOException {
		if (words[0].equals("add") || words[0].equals("-a")) {
			if (words[1].equals("user_story") || words[1].equals("us") || words[1].equals("US")) {
				(new AddUserStoryToBacklog(reader, this.backlog.project)).execute();
				resetCompleters();

				return true;
			}
		} else if (words[0].equals("list") || words[0].equals("-ls")) {
			if (words[1].equals("user_stories") || words[1].equals("us") || words[1].equals("US")) {
				(new ListUserStories(backlog, "Backlog (User Stories)")).execute();
				return true;
			}
		} else if (words[0].equals("go_to") || words[0].equals(">")) {
			if (UserStories.getAllNames(backlog).contains(words[1])) {
				UserStory us = UserStories.find(backlog, words[1]);
				if (us == null) {
					return false;
				}

				us.project = backlog.project;

				UserStroryView usv = new UserStroryView(this, us);
				usv.view();
				return true;
			}
		}

		return false;
	}


	private boolean execute3Keyword(String[] words) throws IOException {
		String prompt = reader.getPrompt();

		if (words[0].equals("add") || words[0].equals("-a")) {
			if ((words[1].equals("user_story") || words[1].equals("us") || words[1].equals("US"))  && words[2].equals("viaCSV")) {
				String line = "";
				String splitBy = ",";
				try {
					reader.setPrompt(ColorCodes.BLUE + "CSV of userstories file path " + ColorCodes.RESET);
					String filePath = reader.readLine();
					System.out.println(filePath);
					BufferedReader br = new BufferedReader(new FileReader(filePath));

					int count = 0;

					while ((line = br.readLine()) != null)   //returns a Boolean value
					{

						String[] userStoryAttributes = line.split(splitBy);    // use comma as separator
						if (count == 0)  // Skips header of the file
						{
							count = 1;
							continue;
						}
						UserStory userStory = new UserStory();
						userStory.title = userStoryAttributes[2];
						userStory.description = userStoryAttributes[3];
						userStory.state = Status.valueOf("OPEN");
						String[] allowedBusinessValues = {"MUST_HAVE", "GREAT", "GOOD", "AVERAGE", "NICE_TO_HAVE"};
						int r = (int) (Math.random() * 5);
						userStory.businessValue = BusinessValue.valueOf(allowedBusinessValues[r]);
						Date date1;
						try {
							date1 = new SimpleDateFormat("yyyy-MM-dd").parse(userStoryAttributes[22].substring(0, 10));
						} catch (Exception e) {
							date1 = new Date();
						}

						userStory.initiatedDate = date1;
						userStory.plannedDate = new Date();

						try {
							Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(userStoryAttributes[36].substring(0, 10));
							userStory.dueDate = date2;
						} catch (Exception e) {
							userStory.dueDate = new Date();
						}

						userStory.points = Integer.parseInt(userStoryAttributes[18]);
						(new AddUserStoryToBacklog(reader, this.backlog.project)).executeCSV(userStory);
						
						
					}
				} catch (IOException e) {
					System.out.println(e);
				}


				resetCompleters();
				reader.setPrompt(prompt);
				return true;
			}

			if ((words[1].equals("user_story") || words[1].equals("us") || words[1].equals("US")) && words[2].equals("viaTaigaAPI")) {
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
				String projectId="";
				try {

					InputStream content = (InputStream)connection.getInputStream();
					BufferedReader in   =
							new BufferedReader (new InputStreamReader (content));
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
					while ((line = in.readLine()) != null) {
						projectId= line.split(", \"name\": ")[0].split("\"id\": ")[1];
					}

					mile(authToken, projectId);
				}

				catch (Exception e)
				{
					System.out.println(e);

				}
				
				resetCompleters();
				reader.setPrompt(prompt);
				return true;

			}

		}

		return false;
	}

	private void mile(String authToken, String projectId)
	{
		String prompt = reader.getPrompt();
		try {
			URL url1 = new URL("https://api.taiga.io/api/v1/milestones?project="+projectId);
			HttpURLConnection conn = (HttpURLConnection) url1.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "Bearer " + authToken);

			List<String> usDescription = new ArrayList<String>();
			List<String> usStoryPoints = new ArrayList<String>();


			try {

				InputStream content = (InputStream) conn.getInputStream();
				BufferedReader in =
						new BufferedReader(new InputStreamReader(content));

				String json = in.readLine();

				JSONTokener tokener = new JSONTokener(json);

				JSONArray finalResult = new JSONArray(tokener);

				for(int i = 0; i < finalResult.length();i++) {
					JSONObject innerObj = finalResult.getJSONObject(i);
					for(Iterator it = innerObj.keys(); it.hasNext(); ) {
						String key = (String)it.next();

						if(key.equals("user_stories")) {

							Object o = innerObj.get(key);
							String s = o.toString();

							JSONTokener tokener1 = new JSONTokener(s);
							JSONArray result = new JSONArray(tokener1);

							for(int i1 = 0; i1 < result.length();i1++) {
								JSONObject innerObj1 = result.getJSONObject(i1);
								for(Iterator it1 = innerObj1.keys(); it1.hasNext(); )
								{
									String key1 = (String)it1.next();
									try {
										if(key1.equals("subject"))
										{
											usDescription.add(innerObj1.get(key1).toString());
										}
										else if(key1.equals("total_points"))
										{
											usStoryPoints.add(innerObj1.get(key1).toString());
										}
									}
									catch (Exception e)
									{System.out.println(key1);}
								}
							}


							for(int k=0;k<usDescription.size();k++)
							{
								UserStory userStory = new UserStory();
								userStory.title = usDescription.get(k);
								userStory.description = "";
								userStory.state = Status.valueOf("OPEN");
								String[] allowedBusinessValues = {"MUST_HAVE", "GREAT", "GOOD", "AVERAGE", "NICE_TO_HAVE"};
								int r = (int) (Math.random() * 5);
								userStory.businessValue = BusinessValue.valueOf(allowedBusinessValues[r]);
								userStory.initiatedDate = new Date();
								userStory.plannedDate = new Date();
								userStory.dueDate = new Date();
								userStory.points = Integer.parseInt(usStoryPoints.get(k));
								(new AddUserStoryToBacklog(reader, this.backlog.project)).executeCSV(userStory);
							}
						}
					}
				}
			} catch (Exception e) {
				System.out.println(e);

			}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}

	}


	private boolean execute1Keyword(String[] words) throws IOException {
		if (words[0].equals("user_stories") || words[0].equals("us") || words[0].equals("US")) {
			(new ListUserStories(backlog, "Backlog (User Stories)")).execute();
			return true;
		} else if (words[0].equals("help") || words[0].equals("-h")) {
			return (new BacklogHelp()).execute();
		}

		return false;
	}

}
