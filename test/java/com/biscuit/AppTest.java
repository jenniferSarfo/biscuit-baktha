package com.biscuit;

import com.biscuit.commands.theme.AddTheme;
import com.biscuit.models.Project;
import com.biscuit.models.enums.Status;

import com.biscuit.views.DashboardView;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */

    public static void testListSortedProjects()
    {
        DashboardView dbv = new DashboardView();
        List<String> output = dbv.getSortedListOfProjects();

        if(output == null)
        {
            assertFalse(true);
        }

        else if(output.size()==0)
        {
           assertTrue(true);
        }

        else
        {
            List copy = new ArrayList(output);
            Collections.sort(copy);
            if(copy.equals(output))
            {
                assertTrue(true);
            }
            else
            {
                assertFalse(true);
            }
        }

        assertTrue(true);

    }

    public static void testAddTheme()
    {
        Project p =Project.load("test");
        Date d= null;
        Status s=null;
        AddTheme at= new AddTheme(p,"test","test",s,d,d,true);
        boolean result = at.execute();

        if(result==true)
        {
            assertTrue("Add Theme Test succeeded", true);
        }
        else
        {
            assertTrue("Add Theme Test Failed", false);
        }
    }


    public void testApp()
    {
        testListSortedProjects();
    }
}
