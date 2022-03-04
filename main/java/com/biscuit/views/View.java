/*
	Author: Hamad Al Marri;
 */

package com.biscuit.views;

import com.biscuit.commands.project.AddProject;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.biscuit.ColorCodes;
import com.biscuit.factories.UniversalCompleterFactory;
import com.biscuit.models.Project;
import com.biscuit.models.UserStory;
import com.biscuit.models.services.Finder.Projects;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;

public abstract class View implements ActionListener{
	
	public static JFrame dashboardFrame = new JFrame();
	public static JFrame addProjectFrame = new JFrame();
	public static JFrame listProjectFrame = new JFrame();
    public static JPanel panel = new JPanel();
    public static JPanel panel1 = new JPanel();
    public static JPanel panel2 = new JPanel();
    public static JPanel panel3 = new JPanel();
    private static boolean flag = true;
    public static JLabel nameLabel = new JLabel();
    public static JLabel descriptionLabel = new JLabel();
    public static JLabel urlLabel = new JLabel();
    public static JLabel teamMembersLabel = new JLabel();
    public static JTextField nameTextField = new JTextField(20);
    public static JTextField descriptionTextField = new JTextField(20);
    public static JTextField urlTextField = new JTextField(20);
    public static JTextField teamMembersTextField = new JTextField(20);
    public static JTextArea text = new JTextArea();

	public static JFrame backlogFrame = new JFrame();
	public static JPanel panel4 = new JPanel();
	public static JLabel backlogProjectLabel = new JLabel();
	public static JTextField backlogProjectTextField = new JTextField(20);
	JButton backlogView = new JButton("Backlog Stories");
	JButton backlogExit = new JButton("Exit");

    
    
    JButton exit = new JButton("Exit");
	JButton addProjectExit = new JButton("Exit");
    JButton add_project = new JButton("Add Project");
    JButton view_project = new JButton("View Projects");
	JButton view_backlog = new JButton("Project Backlog");
    JButton save = new JButton("Save");
    

	static ConsoleReader reader;
	static List<String> promptViews;
	static List<Completer> universalCompleters;
	static Completer completer;

	String name;
	View previousView = null;
	boolean isViewed = false;

	static {
		promptViews = new ArrayList<String>();
		universalCompleters = new ArrayList<Completer>();
		completer = null;

		try {
			reader = new ConsoleReader();
			addUniversalCompleters();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public View(View previousView, String name) {
		this.previousView = previousView;
		this.name = name;
		if (flag) {
			dashboardFrame.setSize(500,100);
			dashboardFrame.setTitle("Dashboard");
//			panel.add(dashboard);
            panel.add(add_project);
            panel.add(view_project);
			panel.add(view_backlog);
            panel.add(exit);

			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					dashboardFrame.dispose();
				}
			});

            add_project.addActionListener(this);
            view_project.addActionListener(this);
			view_backlog.addActionListener(this);
            dashboardFrame.setLayout(new GridLayout(0, 1));
            dashboardFrame.add(panel);
            dashboardFrame.add(panel1);
            dashboardFrame.setVisible(true);
            flag = false;
        }
	}

	public void view() {
		if (!isViewed) {
			addPromptViews();
			isViewed = true;
		}

		setPrompt();

		clearCompleters();

		addCompleters();

		read();
	}

	protected void clearCompleters() {
		if (completer != null)
			reader.removeCompleter(completer);
	}

	private static void addUniversalCompleters() {
		universalCompleters.addAll(UniversalCompleterFactory.getUniversalCompleters());
		completer = new AggregateCompleter(universalCompleters);
		reader.addCompleter(completer);
	}

	protected void addCompleters() {
		List<Completer> completers = new ArrayList<Completer>();

		completers.addAll(universalCompleters);
		addSpecificCompleters(completers);

		completer = new AggregateCompleter(completers);
		reader.addCompleter(completer);
	}

	abstract void addSpecificCompleters(List<Completer> completers);

	protected void resetCompleters() {
		clearCompleters();
		addCompleters();
	}

	private void read() {
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				if (line.isEmpty()) {
					continue;
				}

				String words[] = line.split("\\s+");

				if (checkIfUnivesalCommand(words)) {
					continue;
				}

				if (!executeCommand(words)) {
					System.out.println("invalid command!");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean checkIfUnivesalCommand(String[] words) throws IOException {

		if (words.length == 1) {
			if (words[0].equals("clear") || words[0].equals("clr")) {
				reader.clearScreen();
				return true;
			} else if (words[0].equals("exit")) {
				System.out.println(ColorCodes.BLUE + "See ya!\n" + ColorCodes.RESET);
				reader.shutdown();
				System.exit(0);
			} else if (words[0].equals("dashboard")) {
				gotoDashboard();
				return true;
			} else if (words[0].equals("back") || words[0].equals("<")) {
				this.close();
				return true;
			}
		} else if (words.length == 2) {
			if ((words[0].equals("go_to") || words[0].equals(">")) && words[1].equals("dashboard")) {
				gotoDashboard();
				return true;
			}
		}

		return false;
	}

	private void gotoDashboard() throws IOException {
		if (this.name.equals("Dashboard")) {
			reader.println("you are in the dashboard already!");
		} else {
			promptViews.remove(name);
			View v = this.previousView;
			while (!v.name.equals("Dashboard")) {
				promptViews.remove(v.name);
				v = v.previousView;
			}
			v.view();
		}
	}

	abstract boolean executeCommand(String[] words) throws IOException;

	void addPromptViews() {
		promptViews.add(name);
	}

	void updatePromptViews() {
		promptViews.remove(promptViews.size() - 1);
		promptViews.add(name);
		setPrompt();
	}

	static void setPrompt() {
		StringBuilder prompt = new StringBuilder();

		for (int i = 0; i < promptViews.size(); i++) {
			String pv = promptViews.get(i);
			prompt.append(ColorCodes.PURPLE + pv);

			if (i < promptViews.size() - 1)
				prompt.append(ColorCodes.CYAN + ">");
		}

		prompt.append(ColorCodes.YELLOW + "~" + ColorCodes.GREEN + "$ " + ColorCodes.RESET);

		reader.setPrompt(prompt.toString());
	}

	public void close() {
		if (previousView != null) {
			promptViews.remove(name);
			previousView.view();
		}
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == view_project) {
			String userHome = System.getProperty("user.home");

			File folder = new File(userHome+"/"+"biscuit");
			File[] listOfFiles = folder.listFiles();
			String fileName = "";

			if (listOfFiles == null)
				System.out.println("You don't have biscuit folder in your home directory to list projects");
			else {
				for (int i = 0; i < listOfFiles.length; i++) {
					fileName += "\n"+listOfFiles[i].getName();
				}
				text.setText(fileName);
				listProjectFrame.setSize(200,500);
				panel3.add(text);
				listProjectFrame.setLayout(new GridLayout(0, 1));
				listProjectFrame.add(panel3);
				listProjectFrame.setVisible(true);
			}
			
		}
		else if (e.getSource()==add_project){
        addProjectFrame.setSize(333,260);
        addProjectFrame.setTitle("Add Project");
        nameLabel.setText("Name");
        panel2.add(nameLabel);
        nameTextField.setBounds(100,20,165,25);
        panel2.add(nameTextField);
        descriptionLabel.setText("Descrption");
        panel2.add(descriptionLabel);
        descriptionTextField.setBounds(100,20,165,25);
        panel2.add(descriptionTextField);
        urlLabel.setText("GitHub URL");
        panel2.add(urlLabel);
        urlTextField.setBounds(100,20,165,25);
        panel2.add(urlTextField);
        teamMembersLabel.setText("Team Members names seperated by ','");
        panel2.add(teamMembersLabel);
        teamMembersTextField.setBounds(100,20,165,25);
        panel2.add(teamMembersTextField);
		save.setBounds(160,20,165,25);
        panel2.add(save);
		panel2.add(addProjectExit);
        addProjectFrame.add(panel2);
        addProjectFrame.setVisible(true);
		addProjectFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		addProjectExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				addProjectFrame.dispose();
			}
		});

		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){

				String name = nameTextField.getText();
				String description = descriptionTextField.getText();
				String url = urlTextField.getText();
				String[] teamMembers = teamMembersTextField.getText().split(",");
				int teamSize = teamMembers.length;
								
				if(teamSize==0 || name.length()==0 || description.length()==0 || url.length()==0)
				{
					showMessageDialog(null, "Please fill all the details");
				}
				else
				{
					try {
						(new AddProject(reader)).executeFromGUI(name, description, url, teamMembers, teamSize);
						showMessageDialog(null, "You have successfully created the project");
						addProjectFrame.dispose();
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		 });
    }
	else if(e.getSource()==view_backlog)
	{
		backlogFrame.setSize(333,260);
        backlogFrame.setTitle("Backlog View");
        backlogProjectLabel.setText("Project Name");
        panel4.add(backlogProjectLabel);
		backlogProjectTextField.setBounds(100,20,165,25);
        panel4.add(backlogProjectTextField);
		backlogView.setBounds(160,20,165,25);
        panel4.add(backlogView);
		panel4.add(backlogExit);
        backlogFrame.add(panel4);
        backlogFrame.setVisible(true);
		backlogFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		backlogExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				backlogFrame.dispose();
			}
		});

		backlogView.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String projectName = backlogProjectTextField.getText();
								
				if(projectName.length()==0 )
				{
					showMessageDialog(null, "Please provide project name");
				}
				else
				{
					try {
						String userHome = System.getProperty("user.home");
						File folder = new File(userHome+"/"+"biscuit");
						File[] listOfFiles = folder.listFiles();

						if (listOfFiles == null)
						{
						showMessageDialog(null, "You don't have biscuit folder in your home directory to list projects");
						}
						else {
							int found=0;
							for (int i = 0; i < listOfFiles.length; i++) {
								if(listOfFiles[i].getName().toString().equals(projectName+".json"))
								{
									found=1;
								}
							}
							if(found==0)
							{
								showMessageDialog(null, "The project "+ projectName +" is not present in your computer");
							}
							else
							{
								Project p = Projects.getProject(projectName);
								ProjectView pv = new ProjectView(null, p);

								List<UserStory> result = pv.GUIBacklog();

								if(result==null)
								{
									showMessageDialog(null, "The project "+ projectName +" doesn't have any userstories");
								}

								else
								{
								JFrame userStories = new JFrame();

								// Frame Title
								userStories.setTitle(projectName + " UserStories");

						
								// Data to be displayed in the JTable
								String[][] data = new String[result.size()][10];

								// Column Names
								String[] columnNames = { "Title", "Description", "State", "BusinessValue", "InitiatedDate", "PlannedDate", "DueDate", "StoryPoints", "Happiness", "comments"};
								
								for(int u=0; u<result.size(); u++)
								{
									String row[]= new String[10];
									row[0] = result.get(u).title;
									row[1] = result.get(u).description;
									row[2] = result.get(u).state.toString();
									row[3] = result.get(u).businessValue.toString();
									row[4] = result.get(u).initiatedDate.toString();
									row[5] = result.get(u).plannedDate.toString();
									row[6] = result.get(u).dueDate.toString();
									row[7] = Integer.toString(result.get(u).points);
									row[8] = Integer.toString(result.get(u).Happiness);
									row[9] = result.get(u).comments;
									data[u] = row;
								}	
								// Initializing the JTable
								JTable table = new JTable(data, columnNames);
								table.setBounds(30, 40, 1200, 1200);
						
								// adding it to JScrollPane
								JScrollPane sp = new JScrollPane(table);
								userStories.add(sp);
								// Frame Size
								userStories.setSize(500, 200);
								// Frame Visible = true
								userStories.setVisible(true);
							}
						}
						}
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		 });

	}
}
}
