/*
	Author: Hamad Al Marri;
 */

package com.biscuit.views;

import com.biscuit.commands.project.AddProject;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.JOptionPane.showMessageDialog;


import com.biscuit.ColorCodes;
import com.biscuit.factories.UniversalCompleterFactory;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;

public abstract class View implements ActionListener{
	
	public static JFrame dashboardFrame = new JFrame();
	public static JFrame addProjectFrame = new JFrame();
    public static JPanel panel = new JPanel();
    public static JPanel panel1 = new JPanel();
    public static JPanel panel2 = new JPanel();
    private static boolean flag = true;
    public static JLabel nameLabel = new JLabel();
    public static JLabel descriptionLabel = new JLabel();
    public static JLabel urlLabel = new JLabel();
    public static JLabel teamMembersLabel = new JLabel();
    public static JTextField nameTextField = new JTextField(20);
    public static JTextField descriptionTextField = new JTextField(20);
    public static JTextField urlTextField = new JTextField(20);

    public static JTextField teamMembersTextField = new JTextField(20);
    
    JButton exit = new JButton("Exit");
	JButton addProjectExit = new JButton("Exit");
    JButton add_project = new JButton("Add Project");
    JButton view_project = new JButton("View project");
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
            panel.add(exit);

			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					dashboardFrame.dispose();
				}
			});

            add_project.addActionListener(this);
            
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
	
}
