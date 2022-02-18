/*
	Author: Hamad Al Marri;
 */

package com.biscuit;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//import java.util.Calendar;
//import java.util.GregorianCalendar;

import com.biscuit.models.Dashboard;
import com.biscuit.views.DashboardView;

public class App implements ActionListener{
	public static void GUI(){
		JFrame frame = new JFrame();
		JButton button = new JButton("Dashboard");
		button.addActionListener(new App());
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(50,100,50,100));
		panel.setLayout(new GridLayout(0,1));
		panel.add(button);
		frame.add(panel, BorderLayout.CENTER);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GUI");
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

//		test();

		DashboardView dbv = new DashboardView();
		dbv.view();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println("Button Clicked");
		
		//success.setText("Button clicked");
		JFrame frame = new JFrame();
		JButton button = new JButton("View Projects");
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(50,100,50,100));
		panel.setLayout(new GridLayout(0,1));
		panel.add(button);
		frame.add(panel, BorderLayout.CENTER);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GUI->Dashboard");
		frame.pack();
		frame.setVisible(true);
		/*JLabel success = new JLabel("");
		success.setBounds(10,110,300,25);
		panel.add(success);*/
		
		
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
