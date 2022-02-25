/*
	Author: Hamad Al Marri;
 */

package com.biscuit;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

//import java.util.Calendar;
//import java.util.GregorianCalendar;

import com.biscuit.models.Dashboard;
import com.biscuit.views.DashboardView;
import com.biscuit.views.View;

public class App implements ActionListener{
	public static JFrame frame = new JFrame();
	public static JFrame frame1 = new JFrame();
	
	public static JButton button = new JButton();
	public static JButton button1 = new JButton();
	public static JButton button2 = new JButton();
	public static JButton button3 = new JButton();
	public static JButton button4 = new JButton();
	
	public static JPanel panel = new JPanel();

	public static JPanel panel1 = new JPanel();
	public static JLabel lab = new JLabel();
	public static JLabel label = new JLabel();
	public static JLabel label1 = new JLabel();
	public static JLabel label2 = new JLabel();
	public static JLabel label3 = new JLabel();
	
	public static void GUI(){
		//button.setText("Dashboard");
		lab.setText("Dashboard");
		button1.setText("Add Project");
		button2.setText("View Projects");
		button1.addActionListener(new App());
		//button2.addActionListener(new App());
		panel.setBorder(BorderFactory.createEmptyBorder(50,100,50,100));
		panel.setLayout(new GridLayout(0,1));
		//panel.add(button);
		panel.setName("Dashboard");
		/*String title = "Dashboard";
		Border border = BorderFactory.createTitledBorder(title);
		panel.setBorder(border);*/
		panel.add(lab);
		panel.add(button1);
		panel.add(button2);
		frame.add(panel, BorderLayout.CENTER);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Dashboard");
		frame.pack();
		frame.setVisible(true);
	}


	public static void main(String[] args) {
		GUI();
		initialize();
	}


	private static void initialize() {
		Dashboard.setInstance(Dashboard.load());

		if (Dashboard.getInstance() == null) {
			Dashboard.setInstance(new Dashboard());
		}

		Dashboard.getInstance().save();

		DashboardView dbv = new DashboardView();
		dbv.view();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println("Button Clicked");
		
		//success.setText("Button clicked");
		/*button1.setText("Add Project");
		button2.setText("View Projects");
		panel1.setBorder(BorderFactory.createEmptyBorder(50,100,50,100));
		panel1.setLayout(new GridLayout(0,1));
		panel1.add(button1);
		panel1.add(button2);
		frame1.add(panel1, BorderLayout.CENTER);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setTitle("GUI->Dashboard");
		frame1.pack();
		//button1.addActionListener(new View());*/
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		label3.setText("Enter the following details");
		label.setText("Name:");
		label1.setText("Descrption:");
		label2.setText("Number of team members:");
		panel1.setBorder(BorderFactory.createEmptyBorder(50,100,50,100));
		panel1.setLayout(new GridLayout(0,1));
		panel1.add(label3);
		panel1.add(label);
		panel1.add(label1);
		panel1.add(label2);
		//panel1.add(button3);
		//panel1.add(button4);
		frame1.add(panel1, BorderLayout.CENTER);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setTitle("GUI->Dashboard");
		frame1.pack();
		frame1.setVisible(true);
		
		
		
		
	}


//	private static void test() {
//		Calendar cal = new GregorianCalendar();
//		int startingYear = cal.get(Calendar.YEAR) - 2;
//		int endingYear = startingYear + 4;
//		cal.set(startingYear, 0, 1);
//
//		while (cal.get(Calendar.YEAR) <= endingYear) {
//			System.out.println(cal.getTime());
//			cal.add(Calendar.DAY_OF_MONTH, 1);
//		}
//
//	}

}
