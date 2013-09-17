package com.ad.mq.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);

		String json = "{"
						 +"   filterable:true,"
						 +"   filter : {    type : 'float'    },"
						 +"    flex:1,"
						 +"    summaryType: 'sum',"
						 +"    summaryRenderer: function (value, summaryData, dataIndex) {"
						 +"                    return Ext.String.format('总价合计:{0}', value);"
						 +"                }"
						 +"}";   
		
//        System.out.println(json);  
        Pattern p = Pattern.compile("\\s*summaryType\\s*:\\s*['|\"].*?['|\"]");  
        Matcher m = p.matcher(json);  
        while(m.find()){  
            String tmp = m.group();  
            System.out.println(tmp);  
           // json = json.replaceAll(tmp, tmp.replaceAll("\"","'"));  
        }  
      //  System.out.println(json);  
	}
}
