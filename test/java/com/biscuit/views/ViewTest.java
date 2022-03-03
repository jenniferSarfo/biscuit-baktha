package com.biscuit.views;

import java.awt.Button;
import java.io.File;

import org.junit.Test;

import junit.framework.TestCase;

public class ViewTest extends TestCase {
	@Test
	public void test() {
		Button btn = new Button();
		assertNotNull(btn);
			
	}
	@Test
	public void test1() {
		String userHome = System.getProperty("user.home");

		File folder = new File(userHome+"/"+"biscuit");
		File[] listOfFiles = folder.listFiles();
		
		if (listOfFiles == null)
			assertFalse(false);
		else
			assertTrue(true);
	}

}
