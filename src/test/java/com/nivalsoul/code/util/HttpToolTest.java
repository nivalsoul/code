package com.nivalsoul.code.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class HttpToolTest extends TestCase {
	String url = "http://114.215.239.66:8080/sstcd/mainServlet";

	public void testGet() {
		String result = HttpTool.get(url+"?opt=setActivityInfos");
		System.out.println(result);
	}

	public void testPost() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "setActivityInfos");
		String result = HttpTool.post(url, params );
		System.out.println(result);
	}

}
