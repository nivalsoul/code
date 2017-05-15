package com.nivalsoul.code.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class HttpToolTest extends TestCase {
	String url = "http://36kr.com/api/post?column_id=70&b_id=&per_page=2";

	public void testGet() {
		String result = HttpTool.get(url);
		System.out.println(result);
	}

	public void testPost() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "setActivityInfos");
		String result = HttpTool.post(url, params );
		System.out.println(result);
	}

}
