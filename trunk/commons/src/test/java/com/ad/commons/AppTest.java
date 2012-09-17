package com.ad.commons;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ad.commons.model.Person;
import com.ad.commons.util.Utils;

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
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public void testApp() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException {
		assertTrue(true);

		
//		Pattern pattern = Pattern.compile("\\{\\s*[application|session|request]\\s*.\\s*([^}]*)\\s*\\}");
//		Matcher m = pattern.matcher("{session.user.userId}");
//		System.out.println(m.group());
		
		
		
		Map<String ,Object> session = new HashMap<String ,Object>();
		Map<String ,Object> applicatoin = new HashMap<String ,Object>();
		Person p = new Person();
		p.setName("kk");
		session.put("p", p);
		applicatoin.put("y", "mm");
		String s = "{session.p.name}";
		System.out.println(Utils.convertValue(s, applicatoin, session));
		
		 s = "1111{application.y}";
		System.out.println(Utils.convertValue(s, applicatoin, session));
		
		
		s = "{session.p.name}";
		System.out.println(Utils.convertValue(s, applicatoin, session));
	}
	public void testMD5() throws NoSuchAlgorithmException{
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		byte[] btInput = "1".getBytes();
		// 获得MD5摘要算法的 MessageDigest 对象
		MessageDigest mdInst = MessageDigest.getInstance("MD5");
		// 使用指定的字节更新摘要
		mdInst.update(btInput);
		// 获得密文
		byte[] md = mdInst.digest();
		// 把密文转换成十六进制的字符串形式
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		
		System.out.println(new String(str));
	}
}
