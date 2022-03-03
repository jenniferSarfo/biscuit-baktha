package com.biscuit;
import com.biscuit.models.Epic;
import com.biscuit.models.UserStory;

import junit.framework.Test;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class EpicTest extends TestCase{
	
	/**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( EpicTest.class );
    }
    
    /**
     * Rigourous Test :-)
     */

    public static void testEpicAddUserStories() {
    	Epic epic = new Epic();
    	UserStory us = new UserStory();
    	epic.addUserStory(us);
    	epic.getUserStories();
    	
    	assertTrue(epic.getUserStories().size() != 0);
    }
    
    
    /**
     * Rigourous Test :-)
     */

    public static void testEpicUserStories() {
    	Epic epic = new Epic();
    	epic.getUserStories();
    	
    	assertTrue(epic.getUserStories().size() != 0);
    }

}
