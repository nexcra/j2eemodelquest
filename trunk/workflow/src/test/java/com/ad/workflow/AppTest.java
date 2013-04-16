package com.ad.workflow;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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
     * @throws ScriptException 
     * @throws NoSuchMethodException 
     */
    public void testApp() throws ScriptException, NoSuchMethodException
    {
        assertTrue( true );
        ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		// 执行指定脚本
		engine.eval("function get() {return 'com.ad.mq...';} function merge(a, b) {c = a * b;return c;}");
		if(engine instanceof Invocable) {
			Invocable invoke = (Invocable)engine;
			// 调用merge方法，并传入两个参数
			// c = merge(2, 3);
//			Double c = (Double)invoke.invokeFunction("merge", 2, 3);
			System.out.println("c = " + invoke.invokeFunction("merge", 2, 3));
//			c = (Double)invoke.invokeFunction("add", 2, 3);
			System.out.println("c = " +invoke.invokeFunction("get", 2, 3));
		}
        
    }
}
