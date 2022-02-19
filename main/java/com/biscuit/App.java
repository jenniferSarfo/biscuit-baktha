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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


//import java.util.Calendar;
//import java.util.GregorianCalendar;

import com.biscuit.models.Dashboard;
import com.biscuit.views.DashboardView;

public class App {
	public App(){
		JFrame frame = new JFrame();
		JButton button = new JButton("Start the project");
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		panel.setLayout(new GridLayout(0,1));
		panel.add(button);
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GUI");
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
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
