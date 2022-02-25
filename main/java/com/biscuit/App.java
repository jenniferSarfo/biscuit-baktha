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

public class App{
//	public static JFrame frame = new JFrame();
//	public static JFrame frame1 = new JFrame();
//	
//	public static JButton button = new JButton();
//	
	public static void main(String[] args) {
		//GUI();
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
