/*
	Author: Hamad Al Marri;
 */

package com.biscuit.views;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.biscuit.ColorCodes;
import com.biscuit.factories.UniversalCompleterFactory;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;

public abstract class View implements ActionListener{
	
	public static JFrame frame = new JFrame();
	public static JFrame frame1 = new JFrame();
	public static JFrame frame2 = new JFrame();
    public static JPanel panel = new JPanel();
    public static JPanel panel1 = new JPanel();
    public static JPanel panel2 = new JPanel();
    public static JPanel panel3 = new JPanel();
    private static boolean flag = true;
    public static JLabel name_p = new JLabel();
    public static JLabel desc_p = new JLabel();
    public static JLabel url_p = new JLabel();
    public static JLabel teamsize_p = new JLabel();
    public static JLabel teammem_p = new JLabel();
    public static JTextField name_t = new JTextField(20);
    public static JTextField desc_t = new JTextField(20);
    public static JTextField url_t = new JTextField(20);

    public static JTextField teamsize_t = new JTextField(20);
    public static JTextField teammem_t = new JTextField(20);
    public static JTextArea text = new JTextArea();
    JButton exit = new JButton("Exit");
    JButton add_project = new JButton("Add Project");
    JButton view_project = new JButton("List Projects");
    JButton s = new JButton("Save");
    

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
			frame.setSize(500,100);
			frame.setTitle("Dashboard");
            panel.add(add_project);
            panel.add(view_project);
            panel.add(exit);
            add_project.addActionListener(this);
            view_project.addActionListener(this);
            frame.setLayout(new GridLayout(0, 1));
            frame.add(panel);
            frame.add(panel1);
            frame.setVisible(true);
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
		if (e.getSource() == add_project) {
        System.out.println("Provide input to Add Project");
        frame1.setSize(320,220);
        frame1.setTitle("Add Project");
        name_p.setText("Name");
        panel2.add(name_p);
        name_t.setBounds(100,20,165,25);
        panel2.add(name_t);
        desc_p.setText("Descrption");
        panel2.add(desc_p);
        desc_t.setBounds(100,20,165,25);
        panel2.add(desc_t);
        url_p.setText("GitHub URL");
        panel2.add(url_p);
        url_t.setBounds(100,20,165,25);
        panel2.add(url_t);
        panel2.add(teamsize_p);
        teamsize_t.setBounds(100,20,165,25);
        teamsize_p.setText("Team Size");
        panel2.add(teamsize_t);
        teammem_p.setText("Team Members");
        panel2.add(teammem_p);
        teammem_t.setBounds(100,20,165,25);
        panel2.add(teammem_t);
        panel2.add(s);
        frame1.add(panel2);
        frame1.setVisible(true);
    }
		if (e.getSource() == view_project){
			System.out.println("Listing Projects in the home directory");
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
				frame2.setSize(50,200);
				panel3.add(text);
				frame2.setLayout(new GridLayout(0, 1));
	            frame2.add(panel3);
	            frame2.setVisible(true);
			}
		}
	}

}
