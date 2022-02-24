/*
	Author: Hamad Al Marri;
 */

package com.biscuit.views;

import java.awt.Color;
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

import com.biscuit.ColorCodes;
import com.biscuit.factories.UniversalCompleterFactory;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;

public abstract class View implements ActionListener{
	
	public static JFrame frame = new JFrame();
    public static JPanel panel = new JPanel();
    public static JPanel panel1 = new JPanel();
    private static boolean flag = true;

    JButton go_to = new JButton("go_to Dashboard");
    JButton clear = new JButton("clear");
    JButton back1 = new JButton("back");
    JButton exit = new JButton("exit");
    JButton back = new JButton("add project");
    JButton go_to_pjct = new JButton("go_to project");
    JButton help = new JButton("help");

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
            //console.setBackground(Color.LIGHT_GRAY);
            panel.add(go_to);
            panel.add(back);
            panel.add(clear);
            panel.add(back1);
            panel.add(exit);
            panel.add(go_to_pjct);
            panel.add(help);
            go_to.addActionListener(this);
            back.addActionListener(this);
            clear.addActionListener(this);
            back1.addActionListener(this);
            exit.addActionListener(this);
            go_to_pjct.addActionListener(this);
            help.addActionListener(this);
            frame.setLayout(new GridLayout(0, 1));
            //frame.setBounds(0, 0, 0, 0);
            frame.add(panel);
            frame.add(panel1);
            frame.pack();
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

		//read();
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

	private void read(String line) {
		line = line.trim();
        String words[] = line.split("\\s+");
        for (String iterator : words)
            panel1.removeAll();
        frame.repaint();
        frame.setVisible(true);
		try {
			

				if (checkIfUnivesalCommand(words)) {
					executeCommand(words);
				}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean checkIfUnivesalCommand(String[] words) throws IOException {

		if (words.length == 1) {
			if (words[0].equals("clear")) {
				panel1.add(new JLabel("Command selected: clear"));
				frame.repaint();
				frame.pack();
				frame.setVisible(true);
                return true;
			} else if (words[0].equals("exit")) {
				panel1.add(new JLabel("Command selected: exit"));
				frame.repaint();
				frame.pack();
				frame.setVisible(true);
                System.exit(0);
			} else if (words[0].equals("back")) {
				panel1.add(new JLabel("Command selected: back..Going to previous view"));
				frame.repaint();
				frame.pack();
				frame.setVisible(true);
                this.close();
                return true;
			}
		} else if (words.length == 2) {
			if ((words[0].equals("go_to")) && words[1].equals("dashboard")) {
				gotoDashboard();
				return true;
			}
		}

		return false;
	}

	private void gotoDashboard() throws IOException {
		if (this.name.equals("Dashboard")) {
			panel1.add(new JLabel("Selected"));
            JLabel label = new JLabel("DashBoard");
            panel1.add(label);
            panel1.repaint();
            panel1.setVisible(true);
            frame.pack();
            frame.repaint();
            frame.setVisible(true);
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
	        System.out.println(e.getActionCommand() + "is pressed");
	        if (e.getSource().getClass().toString().equals("class javax.swing.JButton"))
	            read(e.getActionCommand());
	    }

}
